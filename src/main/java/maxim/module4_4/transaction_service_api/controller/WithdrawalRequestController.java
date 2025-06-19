package maxim.module4_4.transaction_service_api.controller;

import lombok.RequiredArgsConstructor;
import maxim.module4_4.transaction_service_api.dto.request.WithdrawalRequestDto;
import maxim.module4_4.transaction_service_api.dto.response.PaymentRequestResponseDto;
import maxim.module4_4.transaction_service_api.service.PaymentRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/withdrawal-requests")
@RequiredArgsConstructor
public class WithdrawalRequestController {

    private final PaymentRequestService paymentRequestService;

    @PostMapping
    public ResponseEntity<PaymentRequestResponseDto> createWithdrawalRequest(@RequestBody WithdrawalRequestDto request) {
        PaymentRequestResponseDto response = paymentRequestService.createWithdrawalRequest(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentRequestResponseDto> getWithdrawalRequest(@PathVariable UUID id) {
        PaymentRequestResponseDto response = paymentRequestService.getWithdrawalRequest(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PaymentRequestResponseDto> approveWithdrawalRequest(@PathVariable UUID id) {
        PaymentRequestResponseDto response = paymentRequestService.approveWithdrawalRequest(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PaymentRequestResponseDto> rejectWithdrawalRequest(@PathVariable UUID id) {
        PaymentRequestResponseDto response = paymentRequestService.rejectWithdrawalRequest(id);
        return ResponseEntity.ok(response);
    }
} 