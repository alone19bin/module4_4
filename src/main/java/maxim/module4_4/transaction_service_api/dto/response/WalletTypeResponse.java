package maxim.module4_4.transaction_service_api.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WalletTypeResponse {
    private UUID uid;
    private String name;
    private String currencyCode;
    private String status;
    private String userType;
    private String creator;
    private String modifier;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
} 