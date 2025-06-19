package maxim.module4_4.transaction_service_api.converter;

import java.math.BigDecimal;



//конвертации валют
public interface CurrencyConverter {
    BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency);
}
