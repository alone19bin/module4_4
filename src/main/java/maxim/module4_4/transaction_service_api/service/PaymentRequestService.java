package maxim.module4_4.transaction_service_api.service;

import lombok.RequiredArgsConstructor;
import maxim.module4_4.transaction_service_api.dto.request.CreatePaymentRequest;
import maxim.module4_4.transaction_service_api.dto.request.WithdrawalRequestDto;
import maxim.module4_4.transaction_service_api.dto.response.PaymentRequestResponseDto;
import maxim.module4_4.transaction_service_api.entity.PaymentRequest;
import maxim.module4_4.transaction_service_api.entity.PaymentRequestStatus;
import maxim.module4_4.transaction_service_api.entity.PaymentRequestType;
import maxim.module4_4.transaction_service_api.entity.Transaction;
import maxim.module4_4.transaction_service_api.entity.TransactionStatus;
import maxim.module4_4.transaction_service_api.entity.TransactionType;
import maxim.module4_4.transaction_service_api.entity.Wallet;
import maxim.module4_4.transaction_service_api.exception.ResourceNotFoundException;
import maxim.module4_4.transaction_service_api.mapper.PaymentRequestMapper;
import maxim.module4_4.transaction_service_api.repository.PaymentRequestRepository;
import maxim.module4_4.transaction_service_api.repository.TransactionRepository;
import maxim.module4_4.transaction_service_api.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с платежными запросами.
 * 
 * Основные задачи:
 * 1. Создание новых платежных запросов
 * 2. Получение информации о существующих запросах
 * 3. Управление статусами запросов (принятие/отклонение)
 * 4. Получение списка запросов пользователя
 * 
 * Логика работы:
 * - При создании запроса генерируется уникальный UUID
 * - Все операции с базой данных выполняются в транзакциях
 * - При изменении статуса обновляется время последнего изменения
 * - При получении запроса проверяется его существование
 * - Поиск запросов пользователя включает как отправленные, так и полученные запросы
 */
@Service
@RequiredArgsConstructor
public class PaymentRequestService {
    private final PaymentRequestRepository paymentRequestRepository;
    private final PaymentRequestMapper paymentRequestMapper;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    /**
     * Создает новый платежный запрос.
     * 
     * @param request данные для создания запроса
     * @return созданный платежный запрос
     */
    @Transactional
    public PaymentRequestResponseDto createPaymentRequest(CreatePaymentRequest request) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUid(UUID.randomUUID());
        paymentRequest.setUserUid(request.getUserUid());
        paymentRequest.setWalletUid(request.getWalletUid());
        paymentRequest.setAmount(request.getAmount());
        paymentRequest.setStatus("PENDING");
        paymentRequest.setComment(request.getComment());
        paymentRequest.setPaymentMethodId(request.getPaymentMethodId());
        return paymentRequestMapper.toResponseDto(paymentRequestRepository.save(paymentRequest));
    }

    /**
     * Получает платежный запрос по его идентификатору.
     * 
     * @param uid идентификатор запроса
     * @return найденный платежный запрос
     * @throws RuntimeException если запрос не найден
     */
    public PaymentRequestResponseDto getPaymentRequest(UUID uid) {
        PaymentRequest request = paymentRequestRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Платежный запрос не найден"));
        return paymentRequestMapper.toResponseDto(request);
    }

    /**
     * Получает список платежных запросов пользователя.
     * 
     * @param walletId идентификатор пользователя
     * @return список запросов, где пользователь является отправителем или получателем
     */
    public List<PaymentRequestResponseDto> getWalletPaymentRequests(UUID walletId) {
        List<PaymentRequest> requests = paymentRequestRepository.findAll().stream()
                .filter(r -> r.getWalletUid().equals(walletId) || r.getUserUid().equals(walletId))
                .collect(Collectors.toList());
        return requests.stream().map(paymentRequestMapper::toResponseDto).collect(Collectors.toList());
    }

    /**
     * Принимает платежный запрос.
     * 
     * @param uid идентификатор запроса
     * @return обновленный платежный запрос
     */
    @Transactional
    public PaymentRequestResponseDto approvePaymentRequest(UUID uid) {
        PaymentRequest request = paymentRequestRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Платежный запрос не найден"));
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Запрос уже обработан");
        }
        
        // Обновляем статус запроса
        request.setStatus("APPROVED");
        PaymentRequest savedRequest = paymentRequestRepository.save(request);
        
        // Получаем кошелек
        Wallet wallet = walletRepository.findById(request.getWalletUid())
                .orElseThrow(() -> new ResourceNotFoundException("Кошелек не найден"));
        
        // Создаем транзакцию
        Transaction transaction = new Transaction();
        transaction.setUid(UUID.randomUUID());
        transaction.setUserUid(request.getUserUid());
        transaction.setWallet(wallet);
        transaction.setWalletName(wallet.getName());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setPaymentRequest(savedRequest);
        transactionRepository.save(transaction);
        
        // Обновляем баланс кошелька
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        walletRepository.save(wallet);
        
        return paymentRequestMapper.toResponseDto(savedRequest);
    }

    /**
     * Отклоняет платежный запрос.
     * 
     * @param uid идентификатор запроса
     * @return обновленный платежный запрос
     */
    @Transactional
    public PaymentRequestResponseDto rejectPaymentRequest(UUID uid) {
        PaymentRequest request = paymentRequestRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Платежный запрос не найден"));
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Запрос уже обработан");
        }
        request.setStatus("REJECTED");
        return paymentRequestMapper.toResponseDto(paymentRequestRepository.save(request));
    }

    @Transactional
    public PaymentRequestResponseDto createWithdrawalRequest(WithdrawalRequestDto request) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserUid(request.getWalletId());
        paymentRequest.setWalletUid(null);
        paymentRequest.setAmount(request.getAmount());
        paymentRequest.setStatus("PENDING");
        paymentRequest.setComment(request.getDescription());
        paymentRequest.setPaymentMethodId(null);
        return paymentRequestMapper.toResponseDto(paymentRequestRepository.save(paymentRequest));
    }

    public PaymentRequestResponseDto getWithdrawalRequest(UUID id) {
        PaymentRequest request = paymentRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запрос на вывод средств не найден"));
        return paymentRequestMapper.toResponseDto(request);
    }

    @Transactional
    public PaymentRequestResponseDto approveWithdrawalRequest(UUID id) {
        PaymentRequest request = paymentRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запрос на вывод средств не найден"));
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Запрос уже обработан");
        }
        request.setStatus("APPROVED");
        return paymentRequestMapper.toResponseDto(paymentRequestRepository.save(request));
    }

    @Transactional
    public PaymentRequestResponseDto rejectWithdrawalRequest(UUID id) {
        PaymentRequest request = paymentRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запрос на вывод средств не найден"));
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Запрос уже обработан");
        }
        request.setStatus("REJECTED");
        return paymentRequestMapper.toResponseDto(paymentRequestRepository.save(request));
    }
} 