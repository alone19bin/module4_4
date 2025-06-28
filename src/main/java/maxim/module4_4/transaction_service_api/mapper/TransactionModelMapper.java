package maxim.module4_4.transaction_service_api.mapper;

import maxim.module4_4.transaction_service_api.dto.TransactionResponseModel;
import maxim.module4_4.transaction_service_api.dto.TransactionStatusModel;
import maxim.module4_4.transaction_service_api.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct-маппер для преобразования сущности TransactionEntity в DTO.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface TransactionModelMapper {
    @Mappings({
        @Mapping(target = "uid", expression = "java(transaction.getId() != null ? transaction.getId().toString() : null)"),
        @Mapping(target = "userUuid", expression = "java(transaction.getUserUid() != null ? transaction.getUserUid().toString() : null)"),
        @Mapping(target = "walletUuid", expression = "java(transaction.getWallet() != null ? transaction.getWallet().toString() : null)"),
        @Mapping(target = "amount", source = "amount"),
        @Mapping(target = "type", expression = "java(transaction.getType() != null ? transaction.getType().name() : null)"),
        @Mapping(target = "state", expression = "java(transaction.getState() != null ? transaction.getState().name() : null)"),
        @Mapping(target = "createdAt", source = "createdAt")
    })
    TransactionResponseModel toDto(TransactionEntity transaction);

    @Mappings({
        @Mapping(target = "uid", expression = "java(transaction.getId() != null ? transaction.getId().toString() : null)"),
        @Mapping(target = "state", expression = "java(transaction.getState() != null ? transaction.getState().name() : null)"),
        @Mapping(target = "modifiedAt", source = "modifiedAt")
    })
    TransactionStatusModel toStatusDto(TransactionEntity transaction);
} 