package maxim.module4_4.transaction_service_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WalletTypeRequest {
    @NotBlank(message = "Название типа кошелька не может быть пустым")
    @Size(min = 1, max = 32, message = "Название должно быть от 1 до 32 символов")
    private String name;

    @NotBlank(message = "Код валюты не может быть пустым")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Код валюты должен состоять из 3 заглавных букв")
    private String currencyCode;

    @NotBlank(message = "Статус не может быть пустым")
    private String status;

    @Size(max = 15, message = "Тип пользователя не может быть длиннее 15 символов")
    private String userType;
} 