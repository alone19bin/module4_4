package maxim.module4_4.transaction_service_api.exception;

import maxim.module4_4.transaction_service_api.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик исключений.
 * 
 * Основные задачи:
 * 1. Обработка всех исключений в приложении
 * 2. Форматирование ответов об ошибках
 * 3. Логирование ошибок
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse("Internal Server Error", e.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse response = new ErrorResponse("Bad Request", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse response = new ErrorResponse("Not Found", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperationException(InvalidOperationException e) {
        ErrorResponse response = new ErrorResponse("Invalid Operation", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}