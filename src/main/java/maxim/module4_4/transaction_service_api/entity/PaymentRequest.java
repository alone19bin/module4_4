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
 * Сущность платежного запроса.
 * 
 * Основные цели:
 * 1. Хранение информации о платежных запросах
 * 2. Отслеживание статусов платежей
 * 3. Связь с кошельками и пользователями
 * 
 * Ключевые особенности:
 * - Использование JPA для маппинга в БД
 * - Поддержка аудита (createdAt, modifiedAt)
 * - Связи с другими сущностями
 */
@Data
@Entity
@Table(name = "payment_requests")
public class PaymentRequest {
    @Id
    @Column(name = "uid", nullable = false, updatable = false)
    private UUID uid;

    @Column(name = "user_uid", nullable = false)
    private UUID userUid;

    @Column(name = "wallet_uid", nullable = false)
    private UUID walletUid;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private String status;

    @Column
    private String comment;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}