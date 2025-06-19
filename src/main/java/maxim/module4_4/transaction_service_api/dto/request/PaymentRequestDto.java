package maxim.module4_4.transaction_service_api.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequestDto {
    @NotNull
    private UUID userUid;
    @NotNull
    private UUID walletUid;
    @NotNull
    @Positive
    private BigDecimal amount;
    private String status;
    private String comment;
    private Long paymentMethodId;
} 