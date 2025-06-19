package maxim.module4_4.transaction_service_api.service;

import lombok.RequiredArgsConstructor;
import maxim.module4_4.transaction_service_api.entity.Transaction;
import maxim.module4_4.transaction_service_api.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import maxim.module4_4.transaction_service_api.dto.request.CreateTransactionRequest;
import maxim.module4_4.transaction_service_api.dto.response.TransactionResponse;
import maxim.module4_4.transaction_service_api.entity.TransactionType;
import maxim.module4_4.transaction_service_api.entity.TransactionStatus;
import maxim.module4_4.transaction_service_api.mapper.TransactionMapper;
import maxim.module4_4.transaction_service_api.entity.Wallet;
import maxim.module4_4.transaction_service_api.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import maxim.module4_4.transaction_service_api.repository.PaymentRequestRepository;
import maxim.module4_4.transaction_service_api.entity.PaymentRequest;
import java.util.stream.Collectors;
import maxim.module4_4.transaction_service_api.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final PaymentRequestRepository paymentRequestRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<Transaction> searchTransactions(
            UUID userUid,
            UUID walletUid,
            String type,
            String state,
            LocalDateTime dateFrom,
            LocalDateTime dateTo
    ) {
        return transactionRepository.findByCriteria(userUid, walletUid, type, state, dateFrom, dateTo);
    }

    @Transactional(readOnly = true)
    public Transaction getTransactionStatus(UUID uid) {
        return transactionRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + uid));
    }

    @Transactional
    public List<TransactionResponse> createTransaction(CreateTransactionRequest request) {
        List<TransactionResponse> responses = new ArrayList<>();
        
        Wallet wallet = walletRepository.findById(UUID.fromString(request.getWalletUid()))
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
                
        PaymentRequest paymentRequest = paymentRequestRepository.findById(UUID.fromString(request.getPaymentRequestUid()))
                .orElseThrow(() -> new RuntimeException("Payment request not found"));

        Transaction transaction = new Transaction();
        transaction.setUserUid(UUID.fromString(request.getUserUid()));
        transaction.setWallet(wallet);
        transaction.setWalletName(wallet.getName());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.valueOf(request.getType()));
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setPaymentRequest(paymentRequest);

        Transaction saved = transactionRepository.save(transaction);
        responses.add(transactionMapper.toResponse(saved));
        return responses;
    }

    /**
     * Получает список транзакций для кошелька.
     */
    public List<TransactionResponse> getWalletTransactions(UUID walletId) {
        return transactionRepository.findByWalletUid(walletId)
                .stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получает транзакцию по её идентификатору.
     */
    public TransactionResponse getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Транзакция не найдена"));
    }

    public List<TransactionResponse> getWalletTransactionsByPeriod(UUID walletId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByWalletUidAndCreatedAtBetween(walletId, startDate, endDate).stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }
} 