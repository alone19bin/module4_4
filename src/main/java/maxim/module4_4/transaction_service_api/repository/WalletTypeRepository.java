package maxim.module4_4.transaction_service_api.repository;

import maxim.module4_4.transaction_service_api.entity.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с типами кошельков.
 * 
 * Основные цели:
 * 1. Доступ к данным типов кошельков
 * 2. Поиск типов кошельков по различным критериям
 * 3. Управление жизненным циклом типов кошельков
 * 
 * Ключевые особенности:
 * - Использование JPA для работы с БД
 * - Поддержка стандартных операций CRUD
 * - Специализированные методы поиска
 */
@Repository
public interface WalletTypeRepository extends JpaRepository<WalletType, UUID> {
    
    /**
     * Находит тип кошелька по его идентификатору.
     *
     * @param uid идентификатор типа кошелька
     * @return Optional с типом кошелька, если найден
     */
    Optional<WalletType> findByUid(UUID uid);
    
    /**
     * Находит тип кошелька по коду валюты и названию.
     *
     * @param currencyCode код валюты
     * @param name название типа кошелька
     * @return Optional с типом кошелька, если найден
     */
    Optional<WalletType> findByCurrencyCodeAndName(String currencyCode, String name);
    
    /**
     * Находит типы кошельков по названию.
     *
     * @param name название типа кошелька
     * @return список типов кошельков
     */
    List<WalletType> findByName(String name);

    /**
     * Находит тип кошелька по названию и коду валюты.
     *
     * @param name название типа кошелька
     * @param currencyCode код валюты
     * @return Optional с типом кошелька, если найден
     */
    Optional<WalletType> findByNameAndCurrencyCode(String name, String currencyCode);
}
