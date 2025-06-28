package maxim.module4_4.transaction_service_api.testsupport;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Тестовая сущность типа кошелька для тестов
 * Используется только в тестах для имитации типа кошельк
 */
public class WalletTypeTestEntity {
    private UUID id;
    private String currencyCode;
    private LocalDateTime createdAt;

    // Геттеры и сеттеры
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 