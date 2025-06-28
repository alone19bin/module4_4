package maxim.module4_4.transaction_service_api.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO для ответа по статусу платёжного запроса.
 */
public class PaymentRequestStatusModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String uid;
    private String status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedAt;

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
} 