package maxim.module4_4.transaction_service_api.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для ответа с информацией о платежном запросе.
 * 
 * Основные цели:
 * 1. Предоставление информации о платежном запросе клиенту
 * 2. Стандартизация формата ответа
 * 
 * Ключевые особенности:
 * - Содержит все необходимые поля платежного запроса
 * - Использует Lombok для автоматической генерации геттеров/сеттеров
 * - Поддерживает сериализацию в JSON
 */
@Data
public class PaymentRequestResponse {
    private UUID uid;
    private UUID senderUid;
    private UUID recipientUid;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 