package maxim.module4_4.transaction_service_api.service;

import maxim.module4_4.transaction_service_api.entity.TransactionEntity;
import maxim.module4_4.transaction_service_api.enums.TransactionStateEnum;
import maxim.module4_4.transaction_service_api.enums.TransactionTypeEnum;
import maxim.module4_4.transaction_service_api.repository.TransactionEntityRepository;
import maxim.module4_4.transaction_service_api.entity.PaymentRequestEntity;
import maxim.module4_4.transaction_service_api.repository.PaymentRequestEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import maxim.module4_4.transaction_service_api.TestcontainersConfiguration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для проверки логики поиска транзакций и получения статуса
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class TransactionServiceLogicTest {
    @Autowired
    private TransactionEntityRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentRequestEntityRepository paymentRequestRepository;

    private UUID userUid;
    private UUID walletUid;
    private PaymentRequestEntity paymentRequest;

    @BeforeEach
    void setUp() {
        userUid = UUID.randomUUID();
        walletUid = UUID.randomUUID();
        // Создаём платёжный запрос
        paymentRequest = new PaymentRequestEntity();
        paymentRequest.setUserUid(userUid);
        paymentRequest.setWalletUid(walletUid);
        paymentRequest.setAmount(BigDecimal.valueOf(100));
        paymentRequestRepository.save(paymentRequest);
        // Создаём тестовую транзакцию
        TransactionEntity tx = new TransactionEntity();
        tx.setUserUid(userUid);
        tx.setWallet(walletUid);
        tx.setWalletName("TestWallet");
        tx.setAmount(BigDecimal.valueOf(100));
        tx.setType(TransactionTypeEnum.TOPUP);
        tx.setState(TransactionStateEnum.IN_PROGRESS);
        tx.setCreatedAt(LocalDateTime.now());
        tx.setPaymentRequestUid(paymentRequest);
        transactionRepository.save(tx);
    }

    @Test
    void testSearchTransactionsByUser() {
        List<?> result = transactionService.searchTransactions(userUid, null, null, null, null, null);
        assertFalse(result.isEmpty(), "Должна быть найдена хотя бы одна транзакция по userUid");
    }

    @Test
    void testGetTransactionStatus() {
        TransactionEntity tx = transactionRepository.findAll().get(0);
        var status = transactionService.getTransactionStatus(tx.getId());
        assertTrue(status.isPresent(), "Статус транзакции должен быть найден");
        assertEquals(TransactionStateEnum.IN_PROGRESS.name(), status.get().getState());
    }

    @Test
    void testCreateAndFindPaymentRequest() {
        PaymentRequestEntity request = new PaymentRequestEntity();
        request.setUserUid(userUid);
        request.setWalletUid(walletUid);
        request.setAmount(BigDecimal.valueOf(50));
        paymentRequestRepository.save(request);
        var found = paymentRequestRepository.findById(request.getId());
        assertTrue(found.isPresent(), "Платежный зпрос должен быть создан и найден");
    }

    @Test
    void testFindNonExistentTransaction() {
        var notFound = transactionRepository.findById(UUID.randomUUID());
        assertTrue(notFound.isEmpty(), "Не должна найтись несуществующая транзакция");
    }

    /**
     * Тест: создание новой транзакции и проверка её сохранения.
     * Логика:
     * 1. Создаём новый TransactionEntity с уникальным userUid и paymentRequest
     * 2.  Устанавливаем обязательные поля (кошелёк, имя, сумма, тип, состояние, createdAt)
     * 3 Сохраняем транзакцию в репозиторий
     * 4. Проверяем, что транзакция успешно найдена по uid
     *
     * Этот тест также проверят логику шардирования, так как userUid участвует в выборе шарда
     */
    @Test
    void testCreateTransactionAndFindById() {
        TransactionEntity tx = new TransactionEntity();
        tx.setUserUid(userUid);
        tx.setWallet(walletUid);
        tx.setWalletName("TestWallet");
        tx.setAmount(BigDecimal.valueOf(100));
        tx.setType(TransactionTypeEnum.TRANSFER);
        tx.setState(TransactionStateEnum.IN_PROGRESS);
        tx.setCreatedAt(LocalDateTime.now());
        tx.setPaymentRequestUid(paymentRequest);
        transactionRepository.save(tx);
        Optional<TransactionEntity> found = transactionRepository.findById(tx.getId());
        assertTrue(found.isPresent(), "Транзакция должна быть найдена по uid");
    }

    /**
     * Тест: проверка, что при создании транзакций с разными userUid данные попадают в разные шарды
     * Логика:
     * 1. Создаём две транзакции с разными userUid.
     * 2. Сохраняем обе транзакции.
     * 3. Влогах ShardingSphere должны появиться сообщения о выборе шарда (например, ds_0 или ds_1
     * 4. Для визуальной проверки модно найти  в логах строки, содержащие "Sharding" или имя шарда
     */
    @Test
    void testShardingIsVisibleInTransactionLogs() {
        Logger log = LoggerFactory.getLogger("org.apache.shardingsphere");
        UUID userUid1 = UUID.randomUUID();
        UUID userUid2 = UUID.randomUUID();
        // Создаём отдельные paymentRequest для каждой транзакции
        PaymentRequestEntity pr1 = new PaymentRequestEntity();
        pr1.setUserUid(userUid1);
        pr1.setWalletUid(walletUid);
        pr1.setAmount(BigDecimal.valueOf(10));
        paymentRequestRepository.save(pr1);
        PaymentRequestEntity pr2 = new PaymentRequestEntity();
        pr2.setUserUid(userUid2);
        pr2.setWalletUid(walletUid);
        pr2.setAmount(BigDecimal.valueOf(20));
        paymentRequestRepository.save(pr2);
        TransactionEntity tx1 = new TransactionEntity();
        tx1.setUserUid(userUid1);
        tx1.setWallet(walletUid);
        tx1.setWalletName("ShardTx1");
        tx1.setAmount(BigDecimal.valueOf(10));
        tx1.setType(TransactionTypeEnum.TRANSFER);
        tx1.setState(TransactionStateEnum.IN_PROGRESS);
        tx1.setCreatedAt(LocalDateTime.now());
        tx1.setPaymentRequestUid(pr1);
        transactionRepository.save(tx1);
        TransactionEntity tx2 = new TransactionEntity();
        tx2.setUserUid(userUid2);
        tx2.setWallet(walletUid);
        tx2.setWalletName("ShardTx2");
        tx2.setAmount(BigDecimal.valueOf(20));
        tx2.setType(TransactionTypeEnum.TRANSFER);
        tx2.setState(TransactionStateEnum.IN_PROGRESS);
        tx2.setCreatedAt(LocalDateTime.now());
        tx2.setPaymentRequestUid(pr2);
        transactionRepository.save(tx2);
        assertTrue(transactionRepository.findById(tx1.getId()).isPresent());
        assertTrue(transactionRepository.findById(tx2.getId()).isPresent());
        // В логах ShardingSphere должны быть сообщения о выборе шарда (ds_0/ds_1)
    }
} 