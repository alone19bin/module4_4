package maxim.module4_4.transaction_service_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxim.module4_4.transaction_service_api.dto.request.WalletRequest;
import maxim.module4_4.transaction_service_api.dto.response.WalletResponse;
import maxim.module4_4.transaction_service_api.entity.Wallet;
import maxim.module4_4.transaction_service_api.entity.WalletType;
import maxim.module4_4.transaction_service_api.exception.BusinessException;
import maxim.module4_4.transaction_service_api.mapper.WalletMapper;
import maxim.module4_4.transaction_service_api.repository.WalletRepository;
import maxim.module4_4.transaction_service_api.repository.WalletTypeRepository;
import maxim.module4_4.transaction_service_api.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления кошельками и транзакциями.
 * 
 * Основные задачи класса:
 * 1. Управление жизненным циклом кошельков (создание, блокировка, обновление)
 * 2. Обработка транзакций (создание, валидация, поиск)
 * 3. Работа с платежными запросами
 * 4. Проверка бизнес-ограничений (статус кошелька, уникальность валюты и т.д.)
 * 
 * ВАЖНО: Все методы содержат подробные комментарии для понимания логики даже новичками.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletTypeRepository walletTypeRepository;
    private final WalletMapper walletMapper;

    /**
     * Создание нового кошелька для пользователя.
     * 
     * Логика:
     * 1. Проверяем, существует ли тип кошелька с нужной валютой и названием.
     * 2. Проверяем, что у пользователя нет кошелька с такой валютой (уникальность).
     * 3. Создаем и сохраняем новый кошелек со статусом ACTIVE и балансом 0.
     * 4. Возвращаем DTO с данными нового кошелька.
     * 
     * @param request Запрос на создание кошелька
     * @return WalletResponse DTO с данными созданного кошелька
     * @throws BusinessException если имя кошелька пустое или уже существует кошелек с такой валютой
     * @throws IllegalArgumentException если тип кошелька не найден
     */
    @Override
    @Transactional
    public WalletResponse createWallet(WalletRequest request) {
        log.debug("Creating wallet for user: {}", request.getUserUid());
        
        // Проверка на пустое имя
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BusinessException("Имя кошелька не может быть пустым");
        }

        // Проверка существования типа кошелька
        WalletType walletType = walletTypeRepository.findByUid(request.getWalletTypeUid())
                .orElseThrow(() -> new IllegalArgumentException("Wallet type not found"));

        // Проверка на дублирование валюты
        List<Wallet> existingWallets = walletRepository.findByUserUid(request.getUserUid());
        boolean hasWalletWithSameCurrency = existingWallets.stream()
                .anyMatch(w -> w.getWalletType().getCurrencyCode().equals(walletType.getCurrencyCode()));
        if (hasWalletWithSameCurrency) {
            throw new BusinessException("У пользователя уже есть кошелек с валютой " + walletType.getCurrencyCode());
        }

        // Создание нового кошелька
        Wallet wallet = walletMapper.toEntity(request);
        wallet.setWalletType(walletType);
        wallet.setBalance(java.math.BigDecimal.ZERO);
        wallet.setStatus("ACTIVE");

        return walletMapper.toResponse(walletRepository.save(wallet));
    }

    /**
     * Получает список кошельков пользователя.
     * 
     * Логика работы:
     * 1. Находит все кошельки пользователя по userUid
     * 2. Преобразует каждую сущность в DTO
     * 3. Возвращает список DTO
     * 
     * @param userUid идентификатор пользователя
     * @return список кошельков пользователя
     */
    @Override
    public List<WalletResponse> getWalletsByUserUid(UUID userUid) {
        return walletRepository.findByUserUid(userUid)
                .stream()
                .map(walletMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WalletResponse> getWalletsByUserUidAndCurrencyCode(UUID userUid, String currencyCode) {
        return walletRepository.findByUserUidAndWalletTypeCurrencyCode(userUid, currencyCode)
                .stream()
                .map(walletMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Обновляет существующий кошелек.
     * 
     * Логика работы:
     * 1. Находит кошелек по его идентификатору
     * 2. Обновляет его данные из запроса
     * 3. Сохраняет изменения
     * 
     * @param walletId идентификатор кошелька
     * @param request запрос на обновление кошелька
     * @return обновленная информация о кошельке
     * @throws RuntimeException если кошелек не найден
     */
    @Override
    public WalletResponse updateWallet(UUID walletId, WalletRequest request) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found: " + walletId));
        
        // Обновляем поля кошелька
        if (request.getName() != null) {
            wallet.setName(request.getName());
        }
        if (request.getStatus() != null) {
            wallet.setStatus(request.getStatus());
        }
        
        var saved = walletRepository.save(wallet);
        return walletMapper.toResponse(saved);
    }

    @Override
    public WalletResponse getWalletByUserUidAndCurrency(UUID userUid, String currencyCode) {
        var wallets = walletRepository.findByUserUidAndWalletTypeCurrencyCode(userUid, currencyCode);
        if (wallets.isEmpty()) throw new RuntimeException("Кошелек не найден");
        return walletMapper.toResponse(wallets.get(0));
    }
}