package maxim.module4_4.transaction_service_api.dto.request;

import lombok.Data;

import java.util.UUID;

/**
 * DTO для создания/обновления кошелька.
 * 
 * Основные цели:
 * 1. Передача данных для создания/обновления кошелька
 * 2. Валидация входных данных
 * 3. Стандартизация формата запроса
 * 
 * Ключевые особенности:
 * - Содержит все необходимые поля для создания/обновления кошелька
 * - Поддерживает разные статусы кошелька
 * - Включает информацию о типе кошелька
 */
@Data
public class WalletRequest {
    private String name;
    private UUID walletTypeUid;
    private String status;
    private UUID userUid;
}
