package maxim.module4_4.transaction_service_api.testsupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Тестовый DTO для ответа по кошельку в тестах.
 * Используется только в тестах для имитации ответа сервиса.
 */
public class WalletResponse {
    private UUID uid;
    private UUID userUid;
    private String name;
    private String status;
    private BigDecimal balance;
    private String currencyCode;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Геттеры и сеттеры
    public UUID getUid() { return uid; }
    public void setUid(UUID uid) { this.uid = uid; }
    public UUID getUserUid() { return userUid; }
    public void setUserUid(UUID userUid) { this.userUid = userUid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    // equals и hashCode для корректной проверки в тестах
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletResponse that = (WalletResponse) o;
        return java.util.Objects.equals(uid, that.uid) &&
                java.util.Objects.equals(userUid, that.userUid) &&
                java.util.Objects.equals(name, that.name) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(balance, that.balance) &&
                java.util.Objects.equals(currencyCode, that.currencyCode) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                java.util.Objects.equals(modifiedAt, that.modifiedAt);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(uid, userUid, name, status, balance, currencyCode, createdAt, modifiedAt);
    }
} 