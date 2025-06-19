package maxim.module4_4.transaction_service_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность транзакции.
 * 
 * Основные цели:
 * 1. Хранение информации о транзакциях
 * 2. Отслеживание статуса транзакций
 * 3. Связь с кошельками и пользователями
 * 
 * Ключевые особенности:
 * - Содержит все необходимые поля транзакции
 * - Использует JPA для маппинга на БД
 * - Поддерживает аудит (createdAt, modifiedAt)
 */
@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @Column(name = "uid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uid;

    @Column(name = "user_uid", nullable = false)
    private UUID userUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_uid", nullable = false)
    private Wallet wallet;

    @Column(name = "wallet_name", nullable = false)
    private String walletName;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_request_uid", nullable = false)
    private PaymentRequest paymentRequest;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}
