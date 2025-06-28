package maxim.module4_4.transaction_service_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для ответа по кошельку пользователя.
 */
public class WalletResponseModel {
    private String uid;
    private String name;
    private String walletTypeUid;
    private String userUid;
    private String status;
    private BigDecimal balance;
    private String currencyCode;
    private LocalDateTime createdAt;

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getWalletTypeUid() { return walletTypeUid; }
    public void setWalletTypeUid(String walletTypeUid) { this.walletTypeUid = walletTypeUid; }
    public String getUserUid() { return userUid; }
    public void setUserUid(String userUid) { this.userUid = userUid; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 