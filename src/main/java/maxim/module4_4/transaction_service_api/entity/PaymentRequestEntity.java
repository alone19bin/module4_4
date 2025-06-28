package maxim.module4_4.transaction_service_api.entity;

import maxim.module4_4.transaction_service_api.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность для хранения информации о платёжном запросе.
 * Используется для пополнения, перевода и вывода средств.
 */
@Entity
@Table(name = "payment_requests")
public class PaymentRequestEntity implements EntityWithStatus<PaymentStatusEnum> {
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
    private UUID walletUid;

    @jakarta.persistence.Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @jakarta.persistence.Column(name = "status", length = 32)
    private PaymentStatusEnum status;

    @jakarta.persistence.Column(name = "comment", length = 256, updatable = false)
    private String comment;

    @jakarta.persistence.Column(name = "payment_method_id", updatable = false)
    private Long paymentMethodId;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
    public UUID getUserUid() { return userUid; }
    public void setUserUid(UUID userUid) { this.userUid = userUid; }
    public UUID getWalletUid() { return walletUid; }
    public void setWalletUid(UUID walletUid) { this.walletUid = walletUid; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentStatusEnum getStatus() { return status; }
    public void setStatus(PaymentStatusEnum status) { this.status = status; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Long getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(Long paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    @PrePersist
    public void onInsert() {
        createdAt = LocalDateTime.now();
        modifiedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }
} 