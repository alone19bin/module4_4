package maxim.module4_4.transaction_service_api.mapper;

import maxim.module4_4.transaction_service_api.dto.response.WalletResponseModel;
import maxim.module4_4.transaction_service_api.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct-маппер для преобразования сущности WalletEntity в DTO.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface WalletModelMapper {
    @Mappings({
        @Mapping(target = "uid", expression = "java(wallet.getWalletId() != null ? wallet.getWalletId().toString() : null)"),
        @Mapping(target = "name", source = "walletName"),
        @Mapping(target = "walletTypeUid", expression = "java(wallet.getWalletType() != null && wallet.getWalletType().getId() != null ? wallet.getWalletType().getId().toString() : null)"),
        @Mapping(target = "userUid", expression = "java(wallet.getUserUuid() != null ? wallet.getUserUuid().toString() : null)"),
        @Mapping(target = "status", expression = "java(wallet.getStatus() != null ? wallet.getStatus().name() : null)"),
        @Mapping(target = "balance", source = "balance"),
        @Mapping(target = "currencyCode", expression = "java(wallet.getWalletType() != null ? wallet.getWalletType().getCurrencyCode() : null)"),
        @Mapping(target = "createdAt", source = "createdAt")
    })
    WalletResponseModel toDto(WalletEntity wallet);
} 