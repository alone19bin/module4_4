package maxim.module4_4.transaction_service_api.service;

import maxim.module4_4.transaction_service_api.dto.request.WalletRequest;
import maxim.module4_4.transaction_service_api.dto.response.WalletResponse;
import maxim.module4_4.transaction_service_api.entity.Wallet;
import maxim.module4_4.transaction_service_api.entity.WalletType;
import maxim.module4_4.transaction_service_api.exception.BusinessException;
import maxim.module4_4.transaction_service_api.mapper.WalletMapper;
import maxim.module4_4.transaction_service_api.repository.WalletRepository;
import maxim.module4_4.transaction_service_api.repository.WalletTypeRepository;
import maxim.module4_4.transaction_service_api.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тесты для сервиса управления кошельками.
 * 
 * Основные цели тестирования:
 * 1. Проверка корректности создания кошельков
 * 2. Проверка получения информации о кошельках
 * 3. Проверка обновления данных кошельков
 * 4. Проверка обработки ошибок и граничных случаев
 * 
 * Структура тестов:
 * - Использует вложенные классы для группировки связанных тестов
 * - Каждый тест содержит подробное описание проверяемого сценария
 * - Использует Mockito для мокирования зависимостей
 * - Следует паттерну AAA (Arrange-Act-Assert)
 */
class WalletServiceTest {
    // Моки для зависимостей сервиса
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletTypeRepository walletTypeRepository;
    @Mock
    private WalletMapper walletMapper;

    // Тестируемый сервис
    private WalletServiceImpl walletService;

    /**
     * Инициализация тестового окружения перед каждым тестом.
     * Создает моки всех зависимостей и экземпляр тестируемого сервиса.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletService = new WalletServiceImpl(
                walletRepository,
                walletTypeRepository,
                walletMapper
        );
    }

    /**
     * Тесты для функциональности создания кошельков.
     * Проверяет различные сценарии создания кошелька и обработку ошибок.
     */
    @Nested
    class WalletCreationTests {
        private UUID userUid;
        private UUID walletTypeUid;
        private WalletType walletType;
        private WalletRequest request;
        private Wallet wallet;
        private WalletResponse walletResponse;

        /**
         * Подготовка тестовых данных перед каждым тестом.
         * Создает необходимые объекты и устанавливает их начальное состояние.
         */
        @BeforeEach
        void setUp() {
            // Создание тестовых идентификаторов
            userUid = UUID.randomUUID();
            walletTypeUid = UUID.randomUUID();
            
            // Создание типа кошелька
            walletType = new WalletType();
            walletType.setId(walletTypeUid);
            walletType.setCurrencyCode("USD");

            // Создание запроса на создание кошелька
            request = new WalletRequest();
            request.setUserUid(userUid);
            request.setName("Test Wallet");
            request.setWalletTypeUid(walletTypeUid);

            // Создание тестового кошелька
            wallet = new Wallet();
            wallet.setId(UUID.randomUUID());
            wallet.setUserUid(userUid);
            wallet.setName(request.getName());
            wallet.setWalletType(walletType);
            wallet.setStatus("ACTIVE");
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setCreatedAt(LocalDateTime.now());
            wallet.setUpdatedAt(LocalDateTime.now());

            // Создание ожидаемого ответа
            walletResponse = new WalletResponse();
            walletResponse.setUid(wallet.getId());
            walletResponse.setUserUid(wallet.getUserUid());
            walletResponse.setName(wallet.getName());
            walletResponse.setStatus(wallet.getStatus());
            walletResponse.setBalance(wallet.getBalance());
            walletResponse.setCurrencyCode(wallet.getWalletType().getCurrencyCode());
            walletResponse.setCreatedAt(wallet.getCreatedAt());
            walletResponse.setModifiedAt(wallet.getUpdatedAt());
        }

        /**
         * Тест успешного создания кошелька.
         * 
         * Проверяет:
         * 1. Корректность создания кошелька с валидными данными
         * 2. Правильность маппинга всех полей
         * 3. Установку начального баланса и статуса
         * 4. Создание временных меток
         */
        @Test
        void createWallet_Success() {
            // Arrange: подготовка тестовых данных и моков
            when(walletTypeRepository.findById(walletTypeUid)).thenReturn(Optional.of(walletType));
            when(walletRepository.findByUserUid(userUid)).thenReturn(List.of());
            when(walletMapper.toEntity(request)).thenReturn(wallet);
            when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
            when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

            // Act: вызов тестируемого метода
            WalletResponse response = walletService.createWallet(request);

            // Assert: проверка результатов
            assertNotNull(response, "Ответ не должен быть null");
            assertEquals(wallet.getId(), response.getUid(), "UID кошелька должен совпадать");
            assertEquals(wallet.getUserUid(), response.getUserUid(), "UID пользователя должен совпадать");
            assertEquals(wallet.getName(), response.getName(), "Имя кошелька должно совпадать");
            assertEquals(wallet.getStatus(), response.getStatus(), "Статус должен быть ACTIVE");
            assertEquals(wallet.getBalance(), response.getBalance(), "Баланс должен быть 0");
            assertEquals(wallet.getWalletType().getCurrencyCode(), response.getCurrencyCode(), "Валюта должна совпадать");
            assertNotNull(response.getCreatedAt(), "createdAt должен быть заполнен");
            assertNotNull(response.getModifiedAt(), "modifiedAt должен быть заполнен");
        }

        /**
         * Тест создания кошелька с несуществующим типом валюты.
         * 
         * Проверяет:
         * 1. Корректную обработку случая с невалидным типом валюты
         * 2. Выброс правильного исключения
         */
        @Test
        void createWallet_InvalidCurrencyCode() {
            // Arrange: настройка мока для возврата пустого результата
            when(walletTypeRepository.findById(walletTypeUid)).thenReturn(Optional.empty());

            // Act & Assert: проверка выброса исключения
            assertThrows(IllegalArgumentException.class, () -> walletService.createWallet(request),
                    "Должно быть выброшено IllegalArgumentException при невалидном типе валюты");
        }

        /**
         * Тест создания кошелька с дублирующейся валютой.
         * 
         * Проверяет:
         * 1. Невозможность создания двух кошельков с одной валютой
         * 2. Выброс бизнес-исключения
         */
        @Test
        void createWallet_DuplicateCurrency() {
            // Arrange: создание существующего кошелька с той же валютой
            Wallet existingWallet = new Wallet();
            existingWallet.setId(UUID.randomUUID());
            existingWallet.setUserUid(userUid);
            existingWallet.setWalletType(walletType);

            when(walletTypeRepository.findById(walletTypeUid)).thenReturn(Optional.of(walletType));
            when(walletRepository.findByUserUid(userUid)).thenReturn(List.of(existingWallet));

            // Act & Assert: проверка выброса исключения
            assertThrows(BusinessException.class, () -> walletService.createWallet(request),
                    "Должно быть выброшено BusinessException при попытке создать дублирующий кошелек");
        }

        /**
         * Тест создания кошелька с пустым именем.
         * 
         * Проверяет:
         * 1. Валидацию обязательных полей
         * 2. Выброс бизнес-исключения при невалидных данных
         */
        @Test
        void createWallet_EmptyName() {
            // Arrange: установка пустого имени
            request.setName("");
            when(walletTypeRepository.findById(walletTypeUid)).thenReturn(Optional.of(walletType));

            // Act & Assert: проверка выброса исключения
            assertThrows(BusinessException.class, () -> walletService.createWallet(request),
                    "Должно быть выброшено BusinessException при пустом имени");
        }
    }

    /**
     * Тесты для функциональности получения информации о кошельках.
     * Проверяет различные сценарии получения данных о кошельках.
     */
    @Nested
    class WalletRetrievalTests {
        private UUID userUid;
        private Wallet wallet;
        private WalletResponse walletResponse;

        /**
         * Подготовка тестовых данных перед каждым тестом.
         * Создает тестовый кошелек и ожидаемый ответ.
         */
        @BeforeEach
        void setUp() {
            // Создание тестовых данных
            userUid = UUID.randomUUID();
            
            // Создание типа кошелька
            WalletType walletType = new WalletType();
            walletType.setId(UUID.randomUUID());
            walletType.setCurrencyCode("USD");

            // Создание тестового кошелька
            wallet = new Wallet();
            wallet.setId(UUID.randomUUID());
            wallet.setUserUid(userUid);
            wallet.setName("Test Wallet");
            wallet.setWalletType(walletType);
            wallet.setStatus("ACTIVE");
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setCreatedAt(LocalDateTime.now());
            wallet.setUpdatedAt(LocalDateTime.now());

            // Создание ожидаемого ответа
            walletResponse = new WalletResponse();
            walletResponse.setUid(wallet.getId());
            walletResponse.setUserUid(wallet.getUserUid());
            walletResponse.setName(wallet.getName());
            walletResponse.setStatus(wallet.getStatus());
            walletResponse.setBalance(wallet.getBalance());
            walletResponse.setCurrencyCode(wallet.getWalletType().getCurrencyCode());
            walletResponse.setCreatedAt(wallet.getCreatedAt());
            walletResponse.setModifiedAt(wallet.getUpdatedAt());
        }

        /**
         * Тест получения всех кошельков пользователя.
         * 
         * Проверяет:
         * 1. Корректность получения списка кошельков
         * 2. Правильность маппинга данных
         */
        @Test
        void getWalletsByUserUid_Success() {
            // Arrange: настройка моков
            when(walletRepository.findByUserUid(userUid)).thenReturn(List.of(wallet));
            when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

            // Act: вызов тестируемого метода
            List<WalletResponse> response = walletService.getWalletsByUserUid(userUid);

            // Assert: проверка результатов
            assertNotNull(response, "Ответ не должен быть null");
            assertFalse(response.isEmpty(), "Список не должен быть пустым");
            assertEquals(1, response.size(), "Должен быть один кошелек");
            assertEquals(walletResponse, response.get(0), "Данные кошелька должны совпадать");
        }

        /**
         * Тест получения кошельков пользователя по валюте.
         * 
         * Проверяет:
         * 1. Корректность фильтрации по валюте
         * 2. Правильность маппинга данных
         */
        @Test
        void getWalletsByUserUidAndCurrencyCode_Success() {
            // Arrange: настройка тестовых данных
            String currencyCode = "USD";
            when(walletRepository.findByUserUidAndWalletTypeCurrencyCode(userUid, currencyCode))
                    .thenReturn(List.of(wallet));
            when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

            // Act: вызов тестируемого метода
            List<WalletResponse> response = walletService.getWalletsByUserUidAndCurrencyCode(userUid, currencyCode);

            // Assert: проверка результатов
            assertNotNull(response, "Ответ не должен быть null");
            assertFalse(response.isEmpty(), "Список не должен быть пустым");
            assertEquals(1, response.size(), "Должен быть один кошелек");
            assertEquals(walletResponse, response.get(0), "Данные кошелька должны совпадать");
        }

        /**
         * Тест получения конкретного кошелька пользователя по валюте.
         * 
         * Проверяет:
         * 1. Корректность получения конкретного кошелька
         * 2. Правильность маппинга данных
         */
        @Test
        void getWalletByUserUidAndCurrency_Success() {
            // Arrange: настройка тестовых данных
            String currencyCode = "USD";
            when(walletRepository.findByUserUidAndWalletTypeCurrencyCode(userUid, currencyCode))
                    .thenReturn(List.of(wallet));
            when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

            // Act: вызов тестируемого метода
            WalletResponse response = walletService.getWalletByUserUidAndCurrency(userUid, currencyCode);

            // Assert: проверка результатов
            assertNotNull(response, "Ответ не должен быть null");
            assertEquals(walletResponse, response, "Данные кошелька должны совпадать");
        }

        /**
         * Тест получения несуществующего кошелька.
         * 
         * Проверяет:
         * 1. Корректную обработку случая отсутствия кошелька
         * 2. Выброс правильного исключения
         */
        @Test
        void getWalletByUserUidAndCurrency_NotFound() {
            // Arrange: настройка мока для возврата пустого списка
            String currencyCode = "USD";
            when(walletRepository.findByUserUidAndWalletTypeCurrencyCode(userUid, currencyCode))
                    .thenReturn(List.of());

            // Act & Assert: проверка выброса исключения
            assertThrows(RuntimeException.class, () -> 
                walletService.getWalletByUserUidAndCurrency(userUid, currencyCode),
                "Должно быть выброшено исключение при отсутствии кошелька");
        }
    }

    /**
     * Тесты для функциональности обновления кошельков.
     * Проверяет различные сценарии обновления данных кошелька.
     */
    @Nested
    class WalletUpdateTests {
        private UUID walletUid;
        private WalletRequest request;
        private Wallet wallet;
        private WalletResponse walletResponse;

        /**
         * Подготовка тестовых данных перед каждым тестом.
         * Создает тестовый кошелек и данные для обновления.
         */
        @BeforeEach
        void setUp() {
            // Создание тестовых данных
            walletUid = UUID.randomUUID();
            
            // Создание запроса на обновление
            request = new WalletRequest();
            request.setUserUid(UUID.randomUUID());
            request.setName("Updated Wallet");
            request.setWalletTypeUid(UUID.randomUUID());

            // Создание тестового кошелька
            wallet = new Wallet();
            wallet.setId(walletUid);
            wallet.setUserUid(request.getUserUid());
            wallet.setName(request.getName());
            wallet.setStatus("ACTIVE");
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setCreatedAt(LocalDateTime.now());
            wallet.setUpdatedAt(LocalDateTime.now());

            // Создание ожидаемого ответа
            walletResponse = new WalletResponse();
            walletResponse.setUid(wallet.getId());
            walletResponse.setUserUid(wallet.getUserUid());
            walletResponse.setName(wallet.getName());
            walletResponse.setStatus(wallet.getStatus());
            walletResponse.setBalance(wallet.getBalance());
            walletResponse.setCreatedAt(wallet.getCreatedAt());
            walletResponse.setModifiedAt(wallet.getUpdatedAt());
        }

        /**
         * Тест успешного обновления кошелька.
         * 
         * Проверяет:
         * 1. Корректность обновления данных
         * 2. Правильность маппинга обновленных данных
         */
        @Test
        void updateWallet_Success() {
            // Arrange: настройка моков
            when(walletRepository.findById(walletUid)).thenReturn(Optional.of(wallet));
            when(walletRepository.save(wallet)).thenReturn(wallet);
            when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

            // Act: вызов тестируемого метода
            WalletResponse response = walletService.updateWallet(walletUid, request);

            // Assert: проверка результатов
            assertNotNull(response, "Ответ не должен быть null");
            assertEquals(walletResponse, response, "Данные обновленного кошелька должны совпадать");
        }

        /**
         * Тест обновления несуществующего кошелька.
         * 
         * Проверяет:
         * 1. Корректную обработку случая отсутствия кошелька
         * 2. Выброс правильного исключения
         */
        @Test
        void updateWallet_NotFound() {
            // Arrange: настройка мока для возврата пустого результата
            when(walletRepository.findById(walletUid)).thenReturn(Optional.empty());

            // Act & Assert: проверка выброса исключения
            assertThrows(RuntimeException.class, () -> walletService.updateWallet(walletUid, request),
                    "Должно быть выброшено исключение при попытке обновить несуществующий кошелек");
        }
    }
} 