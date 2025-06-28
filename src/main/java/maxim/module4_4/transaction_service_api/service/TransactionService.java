package maxim.module4_4.transaction_service_api.service;

import maxim.module4_4.transaction_service_api.dto.TransactionResponseModel;
import maxim.module4_4.transaction_service_api.dto.TransactionStatusModel;
import maxim.module4_4.transaction_service_api.entity.TransactionEntity;
import maxim.module4_4.transaction_service_api.enums.TransactionStateEnum;
import maxim.module4_4.transaction_service_api.enums.TransactionTypeEnum;
import maxim.module4_4.transaction_service_api.mapper.TransactionModelMapper;
import maxim.module4_4.transaction_service_api.repository.TransactionEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с транзакциями.
 */
@Service
public class TransactionService {
    private final TransactionEntityRepository transactionRepository;
    private final TransactionModelMapper transactionMapper;

    public TransactionService(TransactionEntityRepository transactionRepository, TransactionModelMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Поиск транзакций по фильтрам.
     */
    public List<TransactionResponseModel> searchTransactions(UUID userUid, UUID walletUid, String type, String state, LocalDate dateFrom, LocalDate dateTo) {
        // Пример фильтрации через stream
        List<TransactionEntity> all = transactionRepository.findAll();
        return all.stream()
                .filter(t -> userUid == null || userUid.equals(t.getUserUid()))
                .filter(t -> walletUid == null || walletUid.equals(t.getWallet()))
                .filter(t -> !StringUtils.hasText(type) || t.getType().name().equalsIgnoreCase(type))
                .filter(t -> !StringUtils.hasText(state) || t.getState().name().equalsIgnoreCase(state))
                .filter(t -> dateFrom == null || !t.getCreatedAt().toLocalDate().isBefore(dateFrom))
                .filter(t -> dateTo == null || !t.getCreatedAt().toLocalDate().isAfter(dateTo))
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить статус транзакции по uid.
     */
    public Optional<TransactionStatusModel> getTransactionStatus(UUID uid) {
        return transactionRepository.findById(uid).map(transactionMapper::toStatusDto);
    }
} 