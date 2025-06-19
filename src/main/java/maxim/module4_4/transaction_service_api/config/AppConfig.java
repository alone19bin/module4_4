package maxim.module4_4.transaction_service_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

/**
 Цели:

 * 1. Настройка бинов Spring
 * 2. Конфигурация внешних зависимостей
 * 3. Предоставление общих компонентов для всего приложения
  Особенности:

 * - RestTemplate для работы с внешними API
 * - Централизованная конфигурация HTTP-клиента
 * - Возможность добавления дополнительных настроек
 * Логика работы:
 *
 * 1. RestTemplate используется для:
 *    - Конвертации валют через внешний API
 *    - Проверки статуса транзакций
 *    - Взаимодействия с другими сервисами
 * 2. Бин создается один раз при старте приложения
 * 3. Может быть переиспользован в разных частях приложения
 */
@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        // Настраиваем фабрику с таймаутами
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // 5секунд на подключение
        factory.setReadTimeout(5000);     //5 секунд на чтение
        
        // конвертер
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_OCTET_STREAM,
            new MediaType("application", "javascript", java.nio.charset.StandardCharsets.UTF_8)
        ));
        
        //Создаем и настраиваем RestTemplate
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setMessageConverters(Collections.singletonList(converter));
        
        return restTemplate;
    }
}