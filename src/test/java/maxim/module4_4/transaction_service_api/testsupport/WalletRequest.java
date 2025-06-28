package maxim.module4_4.transaction_service_api.testsupport;

import java.util.UUID;

/**
 * Тестовый DTO для создания или обновления кошелька в тестах.
 * Используется только в тестах для имитации пользовательского запроса.
 */
public class WalletRequest {
    private UUID userUid;
    private String name;
    private UUID walletTypeUid;

    // Геттеры и сеттеры
    public UUID getUserUid() { return userUid; }
    public void setUserUid(UUID userUid) { this.userUid = userUid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UUID getWalletTypeUid() { return walletTypeUid; }
    public void setWalletTypeUid(UUID walletTypeUid) { this.walletTypeUid = walletTypeUid; }
} 