package maxim.module4_4.transaction_service_api.mapper;

import maxim.module4_4.transaction_service_api.dto.response.TransactionResponse;
import maxim.module4_4.transaction_service_api.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "userUid", source = "userUid")
    @Mapping(target = "walletUid", source = "wallet.id")
    @Mapping(target = "walletName", source = "walletName")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "state", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    TransactionResponse toResponse(Transaction transaction);
} 