package maxim.module4_4.transaction_service_api.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Конвертер валют,  использующий API ЦБ РФ.
 * Загружает курсы валют при старте приложения и хранит их в памяти.
 * 
 * Принцип работы:
 * 1. При старте  приложения загружаются все курсы валют с API ЦБ РФ
 * 2.  Курсы сохраняются в формате "FROM_TO" (например, "USD_RUB")
 * 3.  Для каждой пары валют рассчитываются прямые и обратные курсы
 * 4. Кросс-курсы рассчитываются через RUB как промежуточную валюту
 * 5. В случае ошибки загрузки используются резервные курсы
 */
@Component
public class CbrCurrencyConverter implements CurrencyConverter {
    //Логгер для отслеживания работы конвертера
    private static final Logger logger = LoggerFactory.getLogger(CbrCurrencyConverter.class);
    //URL API ЦБ РФ для получения курсов валют
    private static final String CBR_API_URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    //Точность для хранения курсов (6 знаков после запятой)
    private static final int SCALE = 6;
    // Режим округления для математических операций
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    // HTTP клиент для запросов к API
    private final RestTemplate restTemplate;


                //!!!! Хранилище курсов валют в формате "FROM_TO" -> rate
    private final Map<String, BigDecimal> rates = new HashMap<>();
    private final ObjectMapper objectMapper;

    @Autowired
    public CbrCurrencyConverter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Инициализация курсов валют при старте приложения.
     * Загружает курсы с API ЦБ РФ и сохраняет их в памяти.
     * 
     * Процесс :
     * 1. Получение JSON с курсами от API ЦБ РФ
     * 2. Парсинг JSON и извлечение курсов валют
     * 3. Расчет курсов относительно RUB
     * 4. Расчет курсов между всеми валютами
     * 5. В случае ошибки - установка резервных курсов
     */
    @PostConstruct
    public void init() {
        try {
            logger.info("Начало загрузки курсов валют с API ЦБ РФ");
            
              //Создаем заголовки запроса
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json, application/javascript, */*");
            headers.set("User-Agent", "Mozilla/5.0");
            
            logger.debug("Отправка запроса к API ЦБ РФ: {}", CBR_API_URL);
            
            // Выполняем запрос и получаем ответ как Map
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                CBR_API_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            Map<String, Object> jsonResponse = response.getBody();
            if (jsonResponse == null) {
                throw new RuntimeException("Пустой ответ от API ЦБ РФ");
            }
            
            logger.debug("Получен ответ от API ЦБ РФ");
            
            // Проверяем корректность ответа
            if (!jsonResponse.containsKey("Valute")) {
                throw new RuntimeException("Неверный формат ответа от API ЦБ РФ: отсутствует секция 'Valute'");
            }

               //извлекаем секцию с курсами валют
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Object>> valutes = (Map<String, Map<String, Object>>) jsonResponse.get("Valute");
            logger.debug("Найдено {} валют", valutes.size());
            
            // Устанавливаем базовый курс RUB к RUB
            rates.put("RUB_RUB", BigDecimal.ONE);
            logger.debug("Добавлен курс RUB_RUB: 1.0");

                       // Обрабатываем каждую  валюту
            for (Map.Entry<String, Map<String, Object>> entry : valutes.entrySet()) {
                String currencyCode = entry.getKey();
                Map<String, Object> details = entry.getValue();
                
                try {
                    //Извлекаем значение курса и номинал
                    Double value = ((Number) details.get("Value")).doubleValue();
                    Integer nominal = ((Number) details.get("Nominal")).intValue();
                    
                      //проверяем корректность данных
                    if (value == null || nominal == null || nominal == 0) {
                        logger.warn("Пропущена валюта {} из-за некорректных данных", currencyCode);
                        continue;
                    }

                               // Рассчитываем курс за 1 единицу валюты
                    BigDecimal rate = BigDecimal.valueOf(value)
                            .divide(BigDecimal.valueOf(nominal), SCALE, ROUNDING_MODE);

                           // Сохраняем прямые курсы (например, USD_RUB = 90.00)
                    rates.put(currencyCode + "_RUB", rate);
                       // Сохраняем обратные курсы (например, RUB_USD = 0.0111)
                    rates.put("RUB_" + currencyCode, BigDecimal.ONE.divide(rate, SCALE, ROUNDING_MODE));
                    
                    logger.debug("Добавлены курсы для {}: {}_RUB={}, RUB_{}={}", 
                        currencyCode, currencyCode, rate, currencyCode, rates.get("RUB_" + currencyCode));
                } catch (Exception e) {
                    logger.error("Ошибка при обработке валюты {}: {}", currencyCode, e.getMessage());
                }
            }

            // Рассчитываем кросс-курсы между всеми валютами
            calculateCrossRates(valutes.keySet());
            
            logger.info("Загрузка курсов валют успешно завершена, всего курсов: {}", rates.size());
        } catch (Exception e) {
            logger.error("Ошибка при загрузке курсов валют: {}", e.getMessage(), e);
            setDefaultRates();
        }
    }

    /**
     * Рассчитывает кросс-курсы между всеми валютами через RUB.
     * 
     * Принцип расчета:
     * 1. Для каждой пары валют (FROM, TO) рассчитываем курс через RUB
     * 2. FROM_RUB * RUB_TO = FROM_TO
     * 
     * Пример:
     * USD_RUB = 90.00
     * RUB_EUR = 0.0111
     * USD_EUR = 90.00 * 0.0111 = 1.00
     */
    private void calculateCrossRates(java.util.Set<String> currencies) {
        for (String from : currencies) {
            for (String to : currencies) {
                if (!from.equals(to)) {
                       // Получаем курсы через RUB
                    BigDecimal fromToRub = rates.get(from + "_RUB");
                    BigDecimal rubToTo = rates.get("RUB_" + to);
                    
                      // Проверяем наличие необходимых курсов
                    if (fromToRub != null && rubToTo != null) {
                        // Рассчитываем кросс-курс
                        BigDecimal crossRate = fromToRub.multiply(rubToTo)
                                .setScale(SCALE, ROUNDING_MODE);
                        rates.put(from + "_" + to, crossRate);
                        logger.debug("Добавлен кросс-курс {}_{}: {}", from, to, crossRate);
                    }
                }
            }
        }
    }

    /**
     *Устанавливает резервные курсы в случае ошибки загрузки.
     *  Используются фиксированные курсы для основных валют.
     */
    private void setDefaultRates() {
        logger.warn("Установка  резервных курсов валют");
        rates.clear();
        rates.put("USD_RUB", new BigDecimal("90.00"));
        rates.put("RUB_USD", new BigDecimal("0.0111"));
        rates.put("USD_USD", BigDecimal.ONE);
        rates.put("RUB_RUB", BigDecimal.ONE);
    }

    /**
     * Конвертирует сумму из одной валюты в другую.
     * 
     * @param amount Сумма для конвертации
     * @param fromCurrency Исходная валюта
     * @param toCurrency Целевая валюта
     * @return Сконвертированная сумма
     * @throws IllegalArgumentException если сумма отрицательная или курс не найден
     */
    @Override
    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        //проверяем входные данные
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительным числом");
        }
        if (fromCurrency == null || toCurrency == null) {
            throw new IllegalArgumentException("Коды валют не могут быть null");
        }

           // Формируем ключ для поиска курса
        String key = fromCurrency + "_" + toCurrency;
        BigDecimal rate = rates.get(key);
        
        // Проверяем наличие курса
        if (rate == null) {
            logger.error("Курс не найден для пары валют: {}", key);
            throw new IllegalArgumentException("Курс не найден для пары валют: " + key);
        }

        // Конвертируем сумму и округляем до 2 знаков после запятой
        return amount.multiply(rate).setScale(2, ROUNDING_MODE);
    }
}



