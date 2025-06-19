package maxim.module4_4.transaction_service_api.controller;

import lombok.RequiredArgsConstructor;
import maxim.module4_4.transaction_service_api.dto.request.CreatePaymentRequest;
import maxim.module4_4.transaction_service_api.dto.response.PaymentRequestResponseDto;
import maxim.module4_4.transaction_service_api.service.PaymentRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с платежными запросами.
 * 
 Основные задачи:
 * 1. Обработка HTTPзапросов для работы с платежными запросами
 * 2. Преобразование DTO в сущности и обратно
 * 3. Валидация входящих данных
 * 4. Формирование HTTP-ответов
 *
 * Логика :
 * - Все методы возвращают ResponseEntity для гибкой настройки HTTP ответов
 * - Используется маппер для преобразования между DTO и сущностями
 * - Все операции выполняются через сервисный слой
 * - Поддерживаются операции создания, получения, принятия и отклонения запросов
 */
@RestController
@RequestMapping("/api/v1/payment-requests")
@RequiredArgsConstructor
public class PaymentRequestController {
    private final PaymentRequestService paymentRequestService;

    /**
     * Создает новый платежный запрос
     */
    @PostMapping
    public ResponseEntity<PaymentRequestResponseDto> createPaymentRequest(@RequestBody CreatePaymentRequest request) {
        PaymentRequestResponseDto response = paymentRequestService.createPaymentRequest(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Получение платежного запрос по его идентификатору
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentRequestResponseDto> getPaymentRequest(@PathVariable UUID id) {
        PaymentRequestResponseDto response = paymentRequestService.getPaymentRequest(id);
        return ResponseEntity.ok(response);
    }

    /**
     * список платежных запросов пользователя
     * @param walletId идентификатор пользователя
     */
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<PaymentRequestResponseDto>> getWalletPaymentRequests(@PathVariable UUID walletId) {
        List<PaymentRequestResponseDto> responses = paymentRequestService.getWalletPaymentRequests(walletId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Принимает платежный запрос
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<PaymentRequestResponseDto> approvePaymentRequest(@PathVariable UUID id) {
        PaymentRequestResponseDto response = paymentRequestService.approvePaymentRequest(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Отклоняет платежный запрос.
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<PaymentRequestResponseDto> rejectPaymentRequest(@PathVariable UUID id) {
        PaymentRequestResponseDto response = paymentRequestService.rejectPaymentRequest(id);
        return ResponseEntity.ok(response);
    }
} 