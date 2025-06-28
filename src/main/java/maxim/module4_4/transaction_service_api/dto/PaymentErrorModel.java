package maxim.module4_4.transaction_service_api.dto;

/**
 * DTO для передачи информации об ошибке платёжного запроса.
 */
public class PaymentErrorModel {
    private String message;
    private int code;

    public PaymentErrorModel(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
} 