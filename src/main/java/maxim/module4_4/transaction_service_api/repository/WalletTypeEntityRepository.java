package maxim.module4_4.transaction_service_api.repository;

import maxim.module4_4.transaction_service_api.entity.WalletTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с типами кошельков.
 */
public interface WalletTypeEntityRepository extends JpaRepository<WalletTypeEntity, UUID> {
    Optional<WalletTypeEntity> findByNameAndCurrencyCode(String name, String currencyCode);
} 