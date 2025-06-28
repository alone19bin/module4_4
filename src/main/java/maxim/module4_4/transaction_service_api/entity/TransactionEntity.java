package maxim.module4_4.transaction_service_api.entity;

import maxim.module4_4.transaction_service_api.enums.TransactionStateEnum;
import maxim.module4_4.transaction_service_api.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность для хранения информации о транзакции (пополнение, перевод, вывод).
 * Связана сплатёжным запросом и кошельком.
 */
@Entity
@Table(name = "transactions")
public class TransactionEntity implements EntityWithStatus<TransactionStateEnum> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @jakarta.persistence.Column(name = "uid", nullable = false)
    private UUID id;

    @jakarta.persistence.Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @jakarta.persistence.Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @jakarta.persistence.Column(name = "user_uid", nullable = false, updatable = false)
    private UUID userUid;

    @jakarta.persistence.Column(name = "wallet_uid", nullable = false)
    private UUID wallet;

    @jakarta.persistence.Column(name = "wallet_name", nullable = false, length = 32)
    private String walletName;

    @jakarta.persistence.Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @jakarta.persistence.Column(name = "type", nullable = false, length = 32, updatable = false)
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;

    @jakarta.persistence.Column(name = "state", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private TransactionStateEnum state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_request_uid", nullable = false)
    private PaymentRequestEntity paymentRequestUid;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
    public UUID getUserUid() { return userUid; }
    public void setUserUid(UUID userUid) { this.userUid = userUid; }
    public UUID getWallet() { return wallet; }
    public void setWallet(UUID wallet) { this.wallet = wallet; }
    public String getWalletName() { return walletName; }
    public void setWalletName(String walletName) { this.walletName = walletName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionTypeEnum getType() { return type; }
    public void setType(TransactionTypeEnum type) { this.type = type; }
    public TransactionStateEnum getState() { return state; }
    public void setState(TransactionStateEnum state) { this.state = state; }
    public PaymentRequestEntity getPaymentRequestUid() { return paymentRequestUid; }
    public void setPaymentRequestUid(PaymentRequestEntity paymentRequestUid) { this.paymentRequestUid = paymentRequestUid; }

    @PrePersist
    public void onInsert() {
        createdAt = LocalDateTime.now();
        modifiedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }

    @Override
    public void setStatus(TransactionStateEnum status) {
        this.state = status;
    }
} 