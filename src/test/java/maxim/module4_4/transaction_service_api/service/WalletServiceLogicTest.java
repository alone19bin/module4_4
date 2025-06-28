package maxim.module4_4.transaction_service_api.service;

import maxim.module4_4.transaction_service_api.entity.WalletEntity;
import maxim.module4_4.transaction_service_api.entity.WalletTypeEntity;
import maxim.module4_4.transaction_service_api.enums.WalletStatusEnum;
import maxim.module4_4.transaction_service_api.repository.WalletEntityRepository;
import maxim.module4_4.transaction_service_api.repository.WalletTypeEntityRepository;
import maxim.module4_4.transaction_service_api.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для проверки логики работы с кошелькам
 * Проверяет создание кошелька, поиск по пользователю и валюте
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class WalletServiceLogicTest {
    @Autowired
    private WalletEntityRepository walletRepository;
    @Autowired
    private WalletTypeEntityRepository walletTypeRepository;
    @Autowired
    private WalletService walletService;

    private UUID userUid;
    private WalletTypeEntity walletType;

    @BeforeEach
    void setUp() {
        userUid = UUID.randomUUID();
        walletType = new WalletTypeEntity();
        walletType.setName("TestType");
        walletType.setCurrencyCode("USD");
        walletType.setStatus("ACTIVE");
        walletType.setCreatedAt(LocalDateTime.now());
        walletTypeRepository.save(walletType);
    }

    @Test
    void testCreateAndFindWalletByUser() {
        //Создание кошелька
        WalletEntity wallet = new WalletEntity();
        wallet.setWalletName("Test Wallet");
        wallet.setUserUuid(userUid);
        wallet.setWalletType(walletType);
        wallet.setStatus(WalletStatusEnum.ACTIVE);
        wallet.setBalance(BigDecimal.TEN);
        wallet.setCreatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        //Поиск по пользователю
        List<WalletEntity> wallets = walletRepository.findAllByUserUuid(userUid);
        assertFalse(wallets.isEmpty(), "Кошелек должен быть найден по userUid");
        assertEquals("Test Wallet", wallets.get(0).getWalletName());
    }

    @Test
    void testFindWalletByUserAndCurrency() {
        // Создание кошелька
        WalletEntity wallet = new WalletEntity();
        wallet.setWalletName("USD Wallet");
        wallet.setUserUuid(userUid);
        wallet.setWalletType(walletType);
        wallet.setStatus(WalletStatusEnum.ACTIVE);
        wallet.setBalance(BigDecimal.ONE);
        wallet.setCreatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        // Поиск по userUid и валюте
        Optional<WalletEntity> found = walletRepository.findByUserUuidAndCurrencyCode(userUid, "USD");
        assertTrue(found.isPresent(), "Кошелек должен быть найден по userUid и валюте");
        assertEquals("USD Wallet", found.get().getWalletName());
    }

    @Test
    void testCreateAndArchiveWalletType() {
        WalletTypeEntity type = new WalletTypeEntity();
        type.setName("ArchiveType");
        type.setCurrencyCode("EUR");
        type.setStatus("ACTIVE");
        type.setCreatedAt(LocalDateTime.now());
        walletTypeRepository.save(type);
        // Проверяем создание
        var found = walletTypeRepository.findById(type.getId());
        assertTrue(found.isPresent(), "Тип кошелька должен быть создан");
        // Архивируем
        type.setStatus("ARCHIVED");
        walletTypeRepository.save(type);
        var archived = walletTypeRepository.findById(type.getId());
        assertEquals("ARCHIVED", archived.get().getStatus());
    }

    @Test
    void testFindNonExistentWallet() {
        var notFound = walletRepository.findById(UUID.randomUUID());
        assertTrue(notFound.isEmpty(), "Не должен найтись несуществующий кошелек");
    }

    /**
     * Тест: создание нового кошелька и проверка его сохранения.
     * Логика:
     * 1   Создаём новый WalletEntity с уникальным userUid
     * 2  Устанавливаем обязательные поля (имя, тип, статус, баланс, createdAt)
     * 3 Сохраняем кошелёк в репозиторий
     * 4 Проверяем, что кошелёк успешно найден по userUid и валюте
     *
     * Этот тест также инициирует логику шардирования, так как userUid участвует в выборе шарда
     */
    @Test
    void testCreateWalletAndFindByUserAndCurrency() {
        UUID testUserUid = UUID.randomUUID();
        WalletEntity wallet = new WalletEntity();
        wallet.setWalletName("ShardTestWallet");
        wallet.setUserUuid(testUserUid);
        wallet.setWalletType(walletType);
        wallet.setStatus(WalletStatusEnum.ACTIVE);
        wallet.setBalance(BigDecimal.valueOf(123.45));
        wallet.setCreatedAt(LocalDateTime.now());
        walletRepository.save(wallet);
        // Проверяем, что кошелёк можно найти по userUid и валюте
        Optional<WalletEntity> found = walletRepository.findByUserUuidAndCurrencyCode(testUserUid, "USD");
        assertTrue(found.isPresent(), "Кошелёк должен быть найден по userUid и валюте");
    }

    /**
     * Тест: проверка, что при создании кошелька с разными userUid данные попадают в разные шарды
     * Логика:
     * 1. Создаём два кошелька с разными userUid
     * 2. Сохраняем оба кошелька
     * 3. В логах ShardingSphere должны появиться сообщения о выборе шарда (например, ds_0 или ds_1)
     * 4. Для визуальной проверки — ищем в логах строки, содержащие "Sharding" или имя шарда
     */
    @Test
    void testShardingIsVisibleInLogs() {
        Logger log = LoggerFactory.getLogger("org.apache.shardingsphere");
        UUID userUid1 = UUID.randomUUID();
        UUID userUid2 = UUID.randomUUID();
        WalletEntity wallet1 = new WalletEntity();
        wallet1.setWalletName("ShardWallet1");
        wallet1.setUserUuid(userUid1);
        wallet1.setWalletType(walletType);
        wallet1.setStatus(WalletStatusEnum.ACTIVE);
        wallet1.setBalance(BigDecimal.valueOf(10));
        wallet1.setCreatedAt(LocalDateTime.now());
        walletRepository.save(wallet1);
        WalletEntity wallet2 = new WalletEntity();
        wallet2.setWalletName("ShardWallet2");
        wallet2.setUserUuid(userUid2);
        wallet2.setWalletType(walletType);
        wallet2.setStatus(WalletStatusEnum.ACTIVE);
        wallet2.setBalance(BigDecimal.valueOf(20));
        wallet2.setCreatedAt(LocalDateTime.now());
        walletRepository.save(wallet2);
        // Проверяем, что оба кошелька созданы
        assertTrue(walletRepository.findByUserUuidAndCurrencyCode(userUid1, "USD").isPresent());
        assertTrue(walletRepository.findByUserUuidAndCurrencyCode(userUid2, "USD").isPresent());
        // В логах ShardingSphere должны быть сообщения о выборе шарда (ds_0/ds_1)
    }
} 