package maxim.module4_4.transaction_service_api.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentRequest {
    private UUID userUid;
    private UUID walletUid;
    private BigDecimal amount;
    private String status;
    private String comment;
    private Long paymentMethodId;
} 