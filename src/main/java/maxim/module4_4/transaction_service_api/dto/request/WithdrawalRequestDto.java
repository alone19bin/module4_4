package maxim.module4_4.transaction_service_api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WithdrawalRequestDto {
    @NotNull(message = "ID кошелька не может быть пустым")
    private UUID walletId;

    @NotNull(message = "Сумма не может быть пустой")
    @Positive(message = "Сумма должна быть положительной")
    private BigDecimal amount;

    private String description;
} 