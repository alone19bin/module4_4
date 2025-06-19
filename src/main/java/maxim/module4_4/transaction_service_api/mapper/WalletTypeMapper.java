package maxim.module4_4.transaction_service_api.mapper;

import maxim.module4_4.transaction_service_api.dto.request.WalletTypeRequest;
import maxim.module4_4.transaction_service_api.dto.response.WalletTypeResponse;
import maxim.module4_4.transaction_service_api.entity.WalletType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WalletTypeMapper {
    
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "modifier", ignore = true)
    WalletType toEntity(WalletTypeRequest request);

    WalletTypeResponse toResponse(WalletType walletType);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    void updateEntity(WalletTypeRequest request, @MappingTarget WalletType walletType);
} 