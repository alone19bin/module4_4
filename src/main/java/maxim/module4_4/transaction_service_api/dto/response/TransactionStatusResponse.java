package maxim.module4_4.transaction_service_api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionStatusResponse {
    private UUID uid;
    private String state;
    private LocalDateTime updatedAt;
} 