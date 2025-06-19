package maxim.module4_4.transaction_service_api.exception;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Стандартизированный ответ об ошибке API.
 */
@Getter
@Setter
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApiError(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
} 