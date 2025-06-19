package maxim.module4_4.transaction_service_api.repository;

import maxim.module4_4.transaction_service_api.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с кошельками.
 * 
 * Основные цели:
 * 1. Доступ к данным кошельков
 * 2. Поиск кошельков по различным критериям
 * 3. Управление жизненным циклом кошельков
 * 
 * Ключевые особенности:
 * - Использование JPA для работы с БД
 * - Поддержка стандартных операций CRUD
 * - Специализированные методы поиска
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    
    /**
     * Находит кошелек по его идентификатору
     *
     * @param id идентификатор кошелька
     * @return Optional с кошельком, если найден
     */
    Optional<Wallet> findById(UUID id);
    
    /**
     * Находит кошельки пользователя.
     *
     * @param userUid идентификатор пользователя
     * @return список кошельков
     */
    List<Wallet> findByUserUid(UUID userUid);
    
    /**
     * Находит кошелек пользователя по валюте
     *
     * @param userUid идентификатор пользователя
     * @param currencyCode код валюты
     * @return список кошельков
     */
    List<Wallet> findByUserUidAndWalletTypeCurrencyCode(UUID userUid, String currencyCode);
    
    /**
     * Находит кошелек пользователя по идентификатору
     *
     * @param userUid идентификатор пользователя
     * @param id идентификатор кошелька
     * @return Optional с кошельком, если найден
     */
    Optional<Wallet> findByUserUidAndId(UUID userUid, UUID id);
    
    /**
     * Находит кошельки по статусу
     *
     * @param status статус кошелька
     * @return список кошельков
     */
    List<Wallet> findByStatus(String status);
}
