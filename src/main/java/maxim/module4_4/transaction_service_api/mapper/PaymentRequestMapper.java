package maxim.module4_4.transaction_service_api.mapper;

import maxim.module4_4.transaction_service_api.dto.request.PaymentRequestDto;
import maxim.module4_4.transaction_service_api.dto.response.PaymentRequestResponseDto;
import maxim.module4_4.transaction_service_api.entity.PaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Маппер для преобразования между сущностью PaymentRequest и DTO.
 * 
 * Основные задачи:
 * 1. Преобразование сущности PaymentRequest в DTO для ответа
 * 2. Обеспечение единообразного представления данных в API
 * 3. Скрытие внутренней структуры сущности от клиентов API
 * 
 * Логика работы:
 * - Преобразование всех полей сущности в соответствующие поля DTO
 * - Обработка null-значений
 * - Преобразование статуса из enum в строку
 * - Сохранение временных меток создания и обновления
 */
@Mapper(componentModel = "spring")
public interface PaymentRequestMapper {
    
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "userUid", source = "userUid")
    @Mapping(target = "walletUid", source = "walletUid")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "paymentMethodId", source = "paymentMethodId")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    PaymentRequestResponseDto toResponseDto(PaymentRequest request);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    PaymentRequest toEntity(PaymentRequestDto dto);
} 