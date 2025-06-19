package maxim.module4_4.transaction_service_api.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для ответа с информацией о транзакции.
 * 
 * Основные цели:
 * 1. Предоставление информации о транзакции клиенту
 * 2. Стандартизация формата ответа
 * 
 * Ключевые особенности:
 * - Содержит все необходимые поля транзакции
 * - Использует Lombok для автоматической генерации геттеров/сеттеров
 * - Поддерживает сериализацию в JSON
 */
@Data
public class TransactionResponse {
    private UUID uid;
    private UUID userUid;
    private UUID walletUid;
    private String walletName;
    private BigDecimal amount;
    private String type;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
