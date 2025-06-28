package maxim.module4_4.transaction_service_api.entity;

import maxim.module4_4.transaction_service_api.enums.WalletStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность для хранения информации о кошельке пользователя.
 * Связана с типом кошелька и пользователем.
 */
@Builder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "wallets")
@AllArgsConstructor
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @jakarta.persistence.Column(name = "uid", nullable = false)
    private UUID walletId;

    @jakarta.persistence.Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @jakarta.persistence.Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @jakarta.persistence.Column(name = "name", nullable = false, length = 32, updatable = false)
    private String walletName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_type_uid", nullable = false)
    private WalletTypeEntity walletType;

    @jakarta.persistence.Column(name = "user_uid", nullable = false, updatable = false)
    private UUID userUuid;

    @jakarta.persistence.Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private WalletStatusEnum status;

    @jakarta.persistence.Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @jakarta.persistence.Column(name = "archived_at")
    private LocalDateTime archivedAt;

    public UUID getWalletId() { return walletId; }
    public void setWalletId(UUID walletId) { this.walletId = walletId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
    public String getWalletName() { return walletName; }
    public void setWalletName(String walletName) { this.walletName = walletName; }
    public WalletTypeEntity getWalletType() { return walletType; }
    public void setWalletType(WalletTypeEntity walletType) { this.walletType = walletType; }
    public UUID getUserUuid() { return userUuid; }
    public void setUserUuid(UUID userUuid) { this.userUuid = userUuid; }
    public WalletStatusEnum getStatus() { return status; }
    public void setStatus(WalletStatusEnum status) { this.status = status; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public LocalDateTime getArchivedAt() { return archivedAt; }
    public void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }
} 