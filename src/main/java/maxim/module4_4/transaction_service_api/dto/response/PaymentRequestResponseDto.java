package maxim.module4_4.transaction_service_api.dto.response;

import lombok.Data;
import maxim.module4_4.transaction_service_api.entity.PaymentRequestStatus;
import maxim.module4_4.transaction_service_api.entity.PaymentRequestType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentRequestResponseDto {
    private UUID uid;
    private UUID userUid;
    private UUID walletUid;
    private BigDecimal amount;
    private String status;
    private String comment;
    private Long paymentMethodId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
} 