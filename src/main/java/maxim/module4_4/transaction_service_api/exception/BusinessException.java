package maxim.module4_4.transaction_service_api.exception;

/**
 * Исключение, возникающее при нарушении бизнес-правил.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 