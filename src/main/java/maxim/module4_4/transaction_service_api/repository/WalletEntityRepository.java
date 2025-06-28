package maxim.module4_4.transaction_service_api.repository;

import maxim.module4_4.transaction_service_api.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с кошельками.
 */
public interface WalletEntityRepository extends JpaRepository<WalletEntity, UUID> {
    /**
     * Получить все кошельки пользователя по userUuid.
     */
    List<WalletEntity> findAllByUserUuid(UUID userUuid);

    /**
     * Получить кошелек пользователя по userUuid и коду валюты.
     */
    @Query("SELECT w FROM WalletEntity w WHERE w.userUuid = :userUuid AND w.walletType.currencyCode = :currencyCode")
    Optional<WalletEntity> findByUserUuidAndCurrencyCode(@Param("userUuid") UUID userUuid, @Param("currencyCode") String currencyCode);
} 