package maxim.module4_4.transaction_service_api.testsupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Тестовая сущность кошелька для тестов
 * Используется только в тестах для имитации кошелька
 */
public class WalletTestEntity {
    private UUID id;
    private UUID userUid;
    private String name;
    private WalletTypeTestEntity walletType;
    private String status;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Геттеры и сеттеры
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserUid() { return userUid; }
    public void setUserUid(UUID userUid) { this.userUid = userUid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public WalletTypeTestEntity getWalletType() { return walletType; }
    public void setWalletType(WalletTypeTestEntity walletType) { this.walletType = walletType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 