package maxim.module4_4.transaction_service_api.dto.topup;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

/**
 * DTO для общего запроса платёжной операции (топап, вывод и т.д.).
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CommonPaymentRequestModel {
    @NotNull
    private UUID requestId;
    @NotNull
    private String paymentStatus;
} 