package maxim.module4_4.transaction_service_api.exception;

/**
 * Исключение для случая, когда транзакция не найдена.
 */
public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
} 