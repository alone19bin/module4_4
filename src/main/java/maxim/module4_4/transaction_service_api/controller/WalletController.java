package maxim.module4_4.transaction_service_api.controller;

import maxim.module4_4.transaction_service_api.dto.response.WalletResponseModel;
import maxim.module4_4.transaction_service_api.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с кошельками пользователя.
 */
@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * Получить все кошельки пользователя по user_uid.
     */
    @GetMapping("/user/{user_uid}")
    public ResponseEntity<List<WalletResponseModel>> getUserWallets(@PathVariable("user_uid") UUID userUid) {
        List<WalletResponseModel> wallets = walletService.getWalletsByUserUid(userUid);
        if (wallets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wallets);
    }

    /**
     * Получить кошелек пользователя по user_uid и валюте.
     */
    @GetMapping("/user/{user_uid}/currency/{currency}")
    public ResponseEntity<WalletResponseModel> getWalletByUserIdAndCurrency(@PathVariable("user_uid") UUID userUid,
                                                                            @PathVariable("currency") String currency) {
        return walletService.getWalletByUserUidAndCurrency(userUid, currency)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
} 