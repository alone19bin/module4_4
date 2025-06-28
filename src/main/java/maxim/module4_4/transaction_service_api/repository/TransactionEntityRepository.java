package maxim.module4_4.transaction_service_api.repository;

import maxim.module4_4.transaction_service_api.entity.TransactionEntity;
import maxim.module4_4.transaction_service_api.enums.TransactionStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с транзакциями.
 */
public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, UUID> {
    Optional<TransactionEntity> findTransactionByPaymentRequestUid_IdAndState(UUID paymentRequestId, TransactionStateEnum state);
} 