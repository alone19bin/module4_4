package maxim.module4_4.transaction_service_api.repository;

import maxim.module4_4.transaction_service_api.entity.PaymentRequestEntity;
import maxim.module4_4.transaction_service_api.enums.PaymentStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с платёжными запросами.
 */
public interface PaymentRequestEntityRepository extends JpaRepository<PaymentRequestEntity, UUID> {
    @Modifying
    @Query("update PaymentRequestEntity p set p.status = :status where p.id = :id")
    void updateStatus(@Param("status") PaymentStatusEnum status, @Param("id") UUID id);

    Optional<PaymentRequestEntity> findPaymentRequestByIdAndStatus(UUID id, PaymentStatusEnum paymentStatus);
} 