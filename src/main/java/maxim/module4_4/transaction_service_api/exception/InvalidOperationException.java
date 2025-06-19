package maxim.module4_4.transaction_service_api.exception;

/**
 * Исключение, возникающее при попытке выполнить недопустимую операцию.
 * Например:
 * - Попытка создать транзакцию с отрицательной суммой
 * - Попытка принять уже принятый платежный запрос
 * - Попытка обновить кошелек с недопустимыми параметрами
 */
public class InvalidOperationException extends RuntimeException {
    
    public InvalidOperationException(String message) {
        super(message);
    }
    
    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 