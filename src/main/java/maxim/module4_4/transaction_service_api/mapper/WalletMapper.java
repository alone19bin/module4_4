package maxim.module4_4.transaction_service_api.mapper;

import maxim.module4_4.transaction_service_api.dto.request.WalletRequest;
import maxim.module4_4.transaction_service_api.dto.response.WalletResponse;
import maxim.module4_4.transaction_service_api.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования между сущностями и DTO кошельков.
 * 
 * Основные цели:
 * 1. Преобразование сущности Wallet в WalletResponse
 * 2. Преобразование WalletRequest в сущность Wallet
 * 
 * Ключевые особенности:
 * - Использует MapStruct для автоматической генерации кода
 * - Поддерживает сложные маппинги с вложенными объектами
 * - Игнорирует поля, которые не должны быть замаплены
 */
@Mapper(componentModel = "spring")
public interface WalletMapper {

    /**
     * Преобразует сущность Wallet в WalletResponse.
     * 
     * @param wallet сущность кошелька
     * @return DTO с информацией о кошельке
     */
    @Mapping(target = "uid", source = "id")
    @Mapping(target = "userUid", source = "userUid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "balance", source = "balance")
    @Mapping(target = "currencyCode", source = "walletType.currencyCode")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    WalletResponse toResponse(Wallet wallet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userUid", source = "userUid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "walletType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Wallet toEntity(WalletRequest request);
}