package maxim.module4_4.transaction_service_api.controller;

import maxim.module4_4.transaction_service_api.dto.TransactionResponseModel;
import maxim.module4_4.transaction_service_api.dto.TransactionStatusModel;
import maxim.module4_4.transaction_service_api.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Контроллер для работы с транзакциями.
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Поиск транзакций по фильтрам.
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponseModel>> searchTransactions(
            @RequestParam(value = "user_uid", required = false) UUID userUid,
            @RequestParam(value = "wallet_uid", required = false) UUID walletUid,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "date_from", required = false) LocalDate dateFrom,
            @RequestParam(value = "date_to", required = false) LocalDate dateTo
    ) {
        List<TransactionResponseModel> transactions = transactionService.searchTransactions(userUid, walletUid, type, state, dateFrom, dateTo);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Получить статус транзакции по uid.
     */
    @GetMapping("/{uid}/status")
    public ResponseEntity<TransactionStatusModel> getTransactionStatus(@PathVariable("uid") UUID uid) {
        Optional<TransactionStatusModel> status = transactionService.getTransactionStatus(uid);
        return status.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
} 