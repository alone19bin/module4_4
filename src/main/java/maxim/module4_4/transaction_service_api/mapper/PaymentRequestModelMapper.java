package maxim.module4_4.transaction_service_api.mapper;

import maxim.module4_4.transaction_service_api.dto.PaymentRequestStatusModel;
import maxim.module4_4.transaction_service_api.entity.PaymentRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct-маппер для преобразования сущности PaymentRequestEntity в DTO.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface PaymentRequestModelMapper {
    @Mappings({
        @Mapping(target = "uid", expression = "java(paymentRequest.getId() != null ? paymentRequest.getId().toString() : null)"),
        @Mapping(target = "status", expression = "java(paymentRequest.getStatus() != null ? paymentRequest.getStatus().name() : null)"),
        @Mapping(target = "modifiedAt", source = "modifiedAt")
    })
    PaymentRequestStatusModel toStatusDto(PaymentRequestEntity paymentRequest);
} 