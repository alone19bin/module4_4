package maxim.module4_4.transaction_service_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность кошелька (Wallet)  для хранения денежных средств пользователя.
 * 
 * Основные характеристики:
 * - Каждый кошелёк привязан к конкретному пользователю (userUid)
 * - Имеет определённый тип (walletType), который определяет валюту и другие параметры
 * - Хранит текущий баланс (balance)
 * - Имеет статус (status), определяющий его активность
 * 
 * Связи с другими сущностями:
 * - @ManyToOne с WalletType: каждый кошелёк имеет один тип, но один тип может быть у многих кошельков
 * - Связь с Transaction: кошелёк может участвовать во многих транзакциях
 * 
 *  поля:
 * - uid: уникальный идентификатор кошелька (генерируется автоматически)
 * - name: название кошелька (например, "Основной кошелёк")
 * - balance: текущий баланс (хранится как BigDecimal для точных вычислений)
 * - status: статус кошелька (ACTIVE, INACTIVE, BLOCKED)
 * - createdAt: дата и время создания кошелька
 * - modifiedAt: дата и время последнего изменения
 */
@Data
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @Column(name = "uid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "user_uid", nullable = false)
    private UUID userUid;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_type_uid", nullable = false)
    private WalletType walletType;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    /**
     * Устанавливает тип кошелька.
     * Этот метод используется при создании или обновлении кошелька.
     * 
     * @param walletType тип кошелька, который будет установлен
     */
    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    /**
     * Получает тип кошелька.
     * Этот метод используется для получения информации о валюте и других параметрах кошелька.
     * 
     * @return тип кошелька
     */
    public WalletType getWalletType() {
        return walletType;
    }
}
