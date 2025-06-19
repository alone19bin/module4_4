package maxim.module4_4.transaction_service_api.repository;

import maxim.module4_4.transaction_service_api.entity.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

/**
 * Репозиторий для работы с транзакциями в бд
 * 
 * Основные цели:
 * 1. Предоставление методов для работы с таблицей транзакций
 * 2. Выполнение запросов к БД для операций с транзакциями
 * 3. Управление персистентностью сущностей Transaction
 * 
 * Ключевые особенности:
 * - Наследует JpaRepository для базовых операций CRUD
 * - Поддерживает кастомные запросы через методы
 * - Использует Spring Data JPA для работы с БД
 * 
 * Методы:
 * - findByUserUid: поиск транзакций по идентификатору пользователя
 * - findByUserUidAndCreatedAtBetween: поиск транзакций по идентификатору пользователя и периоду времени
 * - findByWalletUid: поиск транзакций по идентификатору кошелька
 * - findByUid: поиск транзакции по ее идентификатору
 * 
 * Примеры использования:
 * 1. Найти все транзакции пользователя:
 *    findByUserUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
 * 
 * 2. Найти транзакции по кошельку:
 *    findBySourceWalletUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"))
 * 
 * 3. Найти транзакции по фильтрам:
 *    findByCriteria(userUid, walletUid, type, state, dateFrom, dateTo)
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    /**
     * Находит все транзакции пользователя.
     * 
     * @param userUid идентификатор пользователя
     * @return список транзакций пользователя
     */
    @Query("SELECT t FROM Transaction t WHERE t.userUid = :userUid")
    List<Transaction> findByUserUid(@Param("userUid") UUID userUid);
    
    /**
     * Находит транзакции пользователя за указанный период.
     */
    @Query("SELECT t FROM Transaction t WHERE t.userUid = :userUid AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByUserUidAndCreatedAtBetween(
            @Param("userUid") UUID userUid,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    /**
     * Находит все транзакции по кошельку.
     */
    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId")
    List<Transaction> findByWalletUid(@Param("walletId") UUID walletId);

    /**
     * Находит транзакции, где указанный кошелёк является источником
     */
    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.type = :type")
    List<Transaction> findByWalletUidAndType(@Param("walletId") UUID walletId, @Param("type") String type);

    /**
     * Поиск транзакций по различным критериям.
     * 
     * @param userUid UUID пользователя ( опционально)
     * @param walletUid UUID кошелька (опционально)
     * @param type тип транзакции  (опционально)
     * @param state состояние транзакции (опционально)
     * @param startDate начальная дата (опционально)
     * @param endDate конечная дата (опционально )
     * @return список найденных транзакций
     */
    default List<Transaction> findByCriteria(
            UUID userUid,
            UUID walletUid,
            String type,
            String state,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        
        return findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            
            if (userUid != null) {
                predicates.add(cb.equal(root.get("userUid"), userUid));
            }
            
            if (walletUid != null) {
                predicates.add(cb.equal(root.get("wallet").get("id"), walletUid));
            }
            
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            
            if (state != null) {
                predicates.add(cb.equal(root.get("status"), state));
            }
            
            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("createdAt"), startDate, endDate));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        }, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Находит транзакцию по ее идентификатору.
     * 
     * @param uid идентификатор транзакции
     * @return Optional с найденной транзакцией или пустой Optional
     */
    Optional<Transaction> findByUid(UUID uid);

    /**
     * Находит транзакции по типу.
     *
     * @param type тип транзакции
     * @return список транзакций
     */
    @Query("SELECT t FROM Transaction t WHERE t.type = :type")
    List<Transaction> findByType(@Param("type") String type);
    
    /**
     * Находит транзакции по статусу.
     *
     * @param state статус транзакции
     * @return список транзакций
     */
    @Query("SELECT t FROM Transaction t WHERE t.status = :state")
    List<Transaction> findByState(@Param("state") String state);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByWalletUidAndCreatedAtBetween(
            @Param("walletId") UUID walletId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}