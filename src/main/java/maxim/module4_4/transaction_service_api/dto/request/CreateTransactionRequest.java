package maxim.module4_4.transaction_service_api.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO для создания транзакции.
 * 
 * Основные цели:
 * 1. Передача данных для создания транзакции
 * 2. Валидация входных данных
 * 3. Стандартизация формата запроса
 * 
 * Ключевые особенности:
 * - Содержит все необходимые поля для создания транзакции
 * - Поддерживает разные типы транзакций
 * - Включает информацию о целевом кошельке для переводов
 */
@Data
public class CreateTransactionRequest {
    @NotNull
    private String userUid;
    
    @NotNull
    private String walletUid;
    
    @NotNull
    private String paymentRequestUid;
    
    @NotNull
    @Positive
    private BigDecimal amount;
    
    @NotNull
    private String type;
}
