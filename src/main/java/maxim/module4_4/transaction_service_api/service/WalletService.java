package maxim.module4_4.transaction_service_api.service;

import maxim.module4_4.transaction_service_api.dto.request.WalletRequest;
import maxim.module4_4.transaction_service_api.dto.response.WalletResponse;
import java.util.List;
import java.util.UUID;

public interface WalletService {
    WalletResponse createWallet(WalletRequest request);
    List<WalletResponse> getWalletsByUserUid(UUID userUid);
    List<WalletResponse> getWalletsByUserUidAndCurrencyCode(UUID userUid, String currencyCode);
    WalletResponse getWalletByUserUidAndCurrency(UUID userUid, String currencyCode);
    WalletResponse updateWallet(UUID walletUid, WalletRequest request);
}

