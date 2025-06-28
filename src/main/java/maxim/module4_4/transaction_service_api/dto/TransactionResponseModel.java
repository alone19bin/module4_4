package maxim.module4_4.transaction_service_api.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для ответа по транзакции.
 */
public class TransactionResponseModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String uid;
    private String userUuid;
    private String walletUuid;
    private BigDecimal amount;
    private String type;
    private String state;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getUserUuid() { return userUuid; }
    public void setUserUuid(String userUuid) { this.userUuid = userUuid; }
    public String getWalletUuid() { return walletUuid; }
    public void setWalletUuid(String walletUuid) { this.walletUuid = walletUuid; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 