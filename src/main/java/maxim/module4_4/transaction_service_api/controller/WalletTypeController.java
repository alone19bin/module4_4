package maxim.module4_4.transaction_service_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxim.module4_4.transaction_service_api.dto.request.WalletTypeRequest;
import maxim.module4_4.transaction_service_api.dto.response.WalletTypeResponse;
import maxim.module4_4.transaction_service_api.entity.WalletType;
import maxim.module4_4.transaction_service_api.mapper.WalletTypeMapper;
import maxim.module4_4.transaction_service_api.repository.WalletTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/wallet-types")
@RequiredArgsConstructor
public class WalletTypeController {
    private final WalletTypeRepository walletTypeRepository;
    private final WalletTypeMapper walletTypeMapper;

    /**
     * Создает новый тип кошелька.
     *
     * @param request запрос на создание типа кошелька
     */
    @PostMapping
    public ResponseEntity<WalletTypeResponse> createWalletType(@Valid @RequestBody WalletTypeRequest request) {
        log.debug("Received request to create wallet type: {}", request);
        WalletType walletType = walletTypeMapper.toEntity(request);
        walletType = walletTypeRepository.save(walletType);
        return ResponseEntity.ok(walletTypeMapper.toResponse(walletType));
    }

    /**
     * Получает список всех типов кошельков.
     */
    @GetMapping
    public ResponseEntity<List<WalletTypeResponse>> getAllWalletTypes() {
        List<WalletTypeResponse> types = walletTypeRepository.findAll().stream()
                .map(walletTypeMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }

    /**
     * Получает тип кошелька по его идентификатору.
     *
     * @param uid идентификатор типа кошелька
     */
    @GetMapping("/{uid}")
    public ResponseEntity<WalletTypeResponse> getWalletType(@PathVariable UUID uid) {
        return walletTypeRepository.findByUid(uid)
                .map(walletTypeMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получает типы кошельков по названию.
     *
     * @param name название типа кошелька
     */
    @GetMapping("/search")
    public ResponseEntity<List<WalletTypeResponse>> getWalletTypesByName(@RequestParam String name) {
        List<WalletTypeResponse> types = walletTypeRepository.findByName(name).stream()
                .map(walletTypeMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }
} 