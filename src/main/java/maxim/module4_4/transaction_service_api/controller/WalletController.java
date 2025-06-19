package maxim.module4_4.transaction_service_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxim.module4_4.transaction_service_api.dto.request.WalletRequest;
import maxim.module4_4.transaction_service_api.dto.response.WalletResponse;
import maxim.module4_4.transaction_service_api.entity.Wallet;
import maxim.module4_4.transaction_service_api.mapper.WalletMapper;
import maxim.module4_4.transaction_service_api.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST-контроллер для управления кошельками и транзакциями.
 * 
 * Основные цели:
 * 1. Предоставление REST API для работы с кошельками
 * 2. Обработка HTTP-запросов для операций с кошельками
 * 3. Управление транзакциями и платежными запросами
 * 
 * Ключевые особенности:
 * - Использует REST-принципы для API
 * - Поддерживает основные HTTP-методы (GET, POST, PUT)
 * - Возвращает стандартные HTTP-статусы
 * - Валидирует входные данные
 * 
 * Endpoints:
 * - POST /api/v1/wallets: создание кошелька
 * - GET   /api/v1/wallets: получение списка кошельков
 * - GET  /api/v1/wallets/currency: получение кошелька по валюте
 * - PUT /api/v1/wallets/{walletUid}: обновление информации о кошельке
 * - POST   /api/v1/wallets/{walletUid}/transactions: создание транзакции
 * - POST  /api/v1/wallets/{walletUid}/payment-requests: создание платежного запроса
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final WalletMapper walletMapper;

    /**
     * Создает новый кошелек.
     */
    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody WalletRequest request) {
        log.debug("Received request to create wallet: {}", request);
        WalletResponse wallet = walletService.createWallet(request);
        return ResponseEntity.ok(wallet);
    }

    /**
     * Получает список кошельков пользователя.
     * 
     * @param userUid идентификатор пользователя
     * @param currencyCode код валюты (опционально)
     */
    @GetMapping
    public ResponseEntity<List<WalletResponse>> getWalletsByUserUid(
            @RequestParam UUID userUid,
            @RequestParam(required = false) String currencyCode) {
        log.debug("Received request to get wallets for user: {} with currency: {}", userUid, currencyCode);
        List<WalletResponse> wallets;
        if (currencyCode != null) {
            wallets = walletService.getWalletsByUserUidAndCurrencyCode(userUid, currencyCode);
        } else {
            wallets = walletService.getWalletsByUserUid(userUid);
        }
        return ResponseEntity.ok(wallets);
    }

    /**
     * Обновляет информацию о кошельке.
     * 
     * @param walletUid идентификатор кошелька
     * @param request запрос на обновление информации о кошельке
     */
    @PutMapping("/{walletUid}")
    public ResponseEntity<WalletResponse> updateWallet(
            @PathVariable UUID walletUid,
            @RequestBody WalletRequest request) {
        WalletResponse wallet = walletService.updateWallet(walletUid, request);
        return ResponseEntity.ok(wallet);
    }
}