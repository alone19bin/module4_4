package maxim.module4_4.transaction_service_api.testsupport;

/**
 * Маппер для преобразования между тестовыми сущностями и DTO кошелька
 * Используется только в тестах для имитации маппинга
 */
public class WalletTestMapper {
    /**
     * Преобразует WalletRequest в WalletTestEntity.
     */
    public WalletTestEntity toEntity(WalletRequest request) {
        WalletTestEntity entity = new WalletTestEntity();
        entity.setUserUid(request.getUserUid());
        entity.setName(request.getName());
        // Тип кошелька устанавливается отдельно в тестах
        return entity;
    }

    /**
     * Преобразует WalletTestEntity в WalletResponse.
     */
    public WalletResponse toResponse(WalletTestEntity entity) {
        WalletResponse response = new WalletResponse();
        response.setUid(entity.getId());
        response.setUserUid(entity.getUserUid());
        response.setName(entity.getName());
        response.setStatus(entity.getStatus());
        response.setBalance(entity.getBalance());
        response.setCurrencyCode(entity.getWalletType() != null ? entity.getWalletType().getCurrencyCode() : null);
        response.setCreatedAt(entity.getCreatedAt());
        response.setModifiedAt(entity.getUpdatedAt());
        return response;
    }
} 