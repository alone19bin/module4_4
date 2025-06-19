package maxim.module4_4.transaction_service_api.exception;

/**
 * Исключение, возникающее при отсутствии кошелька.
 * 
 * Основные цели:
 * 1. Информирование о том, что запрашиваемый кошелек не найден
 * 2. Предоставление понятного сообщения об ошибке
 * 3. Стандартизация обработки ошибок
 * 
 * Ключевые особенности:
 * - Наследуется от RuntimeException
 * - Содержит информативное сообщение об ошибке
 * - Поддерживает передачу дополнительной информации
 */
public class WalletNotFoundException extends RuntimeException {
    
    public WalletNotFoundException(String message) {
        super(message);
    }
    
    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
