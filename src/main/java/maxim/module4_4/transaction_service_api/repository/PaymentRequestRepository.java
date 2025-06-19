package maxim.module4_4.transaction_service_api.repository;

import maxim.module4_4.transaction_service_api.entity.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с платежными запросами в базе данных.
 * 
 * Основные цели:
 * 1. Предоставление методов для работы с таблицей платежных запросов
 * 2. Выполнение запросов к БД для операций с платежными запросами
 * 3. Управление персистентностью сущностей PaymentRequest
 * 
 * Ключевые особенности:
 * - Наследует JpaRepository для базовых операций CRUD
 * - Поддерживает кастомные запросы через методы
 * - Использует Spring Data JPA для работы с БД
 * 
 * Методы:
 * - findByUserUid: поиск платежных запросов по идентификатору пользователя
 * - findByWalletUid: поиск платежных запросов по идентификатору кошелька
 * - findByUid: поиск платежного запроса по его идентификатору
 */
@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, UUID> {
}