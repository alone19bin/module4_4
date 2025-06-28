package maxim.module4_4.transaction_service_api.service;

import maxim.module4_4.transaction_service_api.dto.response.WalletResponseModel;
import maxim.module4_4.transaction_service_api.entity.WalletEntity;
import maxim.module4_4.transaction_service_api.mapper.WalletModelMapper;
import maxim.module4_4.transaction_service_api.repository.WalletEntityRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с кошельками пользователя.
 */
@Service
public class WalletService {
    private final WalletEntityRepository walletRepository;
    private final WalletModelMapper walletMapper;

    public WalletService(WalletEntityRepository walletRepository, WalletModelMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    /**
     * Получить все кошельки пользователя по userUid.
     */
    public List<WalletResponseModel> getWalletsByUserUid(UUID userUid) {
        List<WalletEntity> wallets = walletRepository.findAllByUserUuid(userUid);
        return wallets.stream().map(walletMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Получить кошелек пользователя по userUid и валюте.
     */
    public Optional<WalletResponseModel> getWalletByUserUidAndCurrency(UUID userUid, String currency) {
        return walletRepository.findByUserUuidAndCurrencyCode(userUid, currency)
                .map(walletMapper::toDto);
    }
} 