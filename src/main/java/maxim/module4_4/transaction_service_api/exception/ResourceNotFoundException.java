package maxim.module4_4.transaction_service_api.exception;

/**
 * Исключение, возникающее при попытке получить доступ к несуществующему ресурсу.
 * Например:
 * - Попытка получить информацию о несуществующем кошельке
 * - Попытка получить статус несуществующей транзакции
 * - Попытка получить информацию о несуществующем платежном запросе
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s не найден с %s : '%s'", resourceName, fieldName, fieldValue));
    }
} 