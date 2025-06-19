package maxim.module4_4.transaction_service_api.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для ответа с информацией о кошельке.
 * 
 * Основные цели:
 * 1 Предоставление информации о кошельке клиенту
 *  2  Стандартизация формата ответа
 * 
 * Ключевые особенности:
 * - Содержит все необходимые поля кошелька
 * - Использует Lombok для автоматической генерации геттеров/сеттеров
 * - Поддерживает сериализацию в JSON
 */
@Data
public class WalletResponse {
    private UUID uid;
    private String name;
    private UUID userUid;
    private String status;
    private BigDecimal balance;
    private String currencyCode;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
} 