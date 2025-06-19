package maxim.module4_4.transaction_service_api.controller;

import lombok.RequiredArgsConstructor;
import maxim.module4_4.transaction_service_api.dto.request.CreateTransactionRequest;
import maxim.module4_4.transaction_service_api.dto.response.TransactionResponse;
import maxim.module4_4.transaction_service_api.dto.response.TransactionStatusResponse;
import maxim.module4_4.transaction_service_api.entity.Transaction;
import maxim.module4_4.transaction_service_api.mapper.TransactionMapper;
import maxim.module4_4.transaction_service_api.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с транзакциями.
 * 
 * Примеры запросов
 * 1. Поиск всех транзакций пользователя:
 *    GET /api/v1/transactions?user_uid=123e4567-e89b-12d3-a456-426614174000
 * 
 * 2. Поиск транзакций по кошельку:
 *    GET /api/v1/transactions?wallet_uid=123e4567-e89b-12d3-a456-426614174001
 * 
 * 3. Поиск транзакций по типу:
 *    GET /api/v1/transactions?type=TRANSFER
 * 
 * 4. Поиск транзакций по состоянию:
 *    GET /api/v1/transactions?state=COMPLETED
 * 
 * 5.Поиск транзакций за период:
 *    GET /api/v1/transactions?date_from=2025-06-01T00:00:00&date_to=2025-06-01T23:59:59
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<List<TransactionResponse>> createTransaction(@RequestBody CreateTransactionRequest request) {
        List<TransactionResponse> transactions = transactionService.createTransaction(request);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> searchTransactions(
            @RequestParam(required = false) UUID userUid,
            @RequestParam(required = false) UUID walletUid,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo
    ) {
        List<Transaction> transactions = transactionService.searchTransactions(userUid, walletUid, type, state, dateFrom, dateTo);
        List<TransactionResponse> responses = transactions.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{uid}/status")
    public ResponseEntity<TransactionStatusResponse> getTransactionStatus(@PathVariable UUID uid) {
        Transaction transaction = transactionService.getTransactionStatus(uid);
        TransactionStatusResponse response = new TransactionStatusResponse();
        response.setUid(transaction.getUid());
        response.setState(transaction.getStatus().name());
        response.setUpdatedAt(transaction.getModifiedAt());
        return ResponseEntity.ok(response);
    }

    /**
     * Получает список транзакций для кошелька.
     */
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionResponse>> getWalletTransactions(@PathVariable UUID walletId) {
        return ResponseEntity.ok(transactionService.getWalletTransactions(walletId));
    }

    @GetMapping("/wallet/{walletId}/period")
    public ResponseEntity<List<TransactionResponse>> getWalletTransactionsByPeriod(
            @PathVariable UUID walletId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.getWalletTransactionsByPeriod(walletId, startDate, endDate));
    }

    /**
     * Получает транзакцию по её идентификатору.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }
}