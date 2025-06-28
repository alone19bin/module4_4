package maxim.module4_4.transaction_service_api.config.sharding;

import org.apache.shardingsphere.infra.exception.core.ShardingSpherePreconditions;
import org.apache.shardingsphere.sharding.algorithm.sharding.ShardingAutoTableAlgorithmUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.apache.shardingsphere.sharding.exception.data.NullShardingValueException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
  алгоритм стандартного шардирования для ShardingSphere по значению столбца
 * Применяется для таблиц, где можно однозначно определить шард по значению поля.
 * Логика:
 *   >Получает значение поля (например, user_uid)
 *   Вычисляет hashCode от UUID (строкой), берёт по модулю количества шардов (по умолчанию 2
 *   Возвращает имя нужного шарда (например, ds_0 или ds_1)
 * Пример использования: wallets, payment_requests, transactions.
 */
public final class ShardingAlgorithmImpl implements StandardShardingAlgorithm<UUID> {
    /** Количество шардов */
    public int shardingSize = 2;

    /**
     * Основной метод для точечного шардирования (по конкретному значению).
     * @param availableTargetNames список всех возможных шардов (например, ds_0, ds_1)
     * @param shardingValue значение поля для шардирования (например, user_uid)
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<UUID> shardingValue) {
        // Проверка, что значение не null
        ShardingSpherePreconditions.checkNotNull(shardingValue.getValue(), NullShardingValueException::new);
        UUID shardingValueValue = shardingValue.getValue();
        // Считаем hashCode от UUID (строкой), берём по модулю количества  шардов
        int hashCode = Objects.hashCode(shardingValueValue.toString());
        int modulo = Math.abs(hashCode % shardingSize);
        String tableNameSuffix = String.valueOf(modulo);
        //  Находим имя нужного шарда (например, ds_0 или ds_1)
        return ShardingAutoTableAlgorithmUtils.findMatchedTargetName(availableTargetNames, tableNameSuffix, shardingValue.getDataNodeInfo())
                .orElse(null);
    }


    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<UUID> shardingValue) {
        // Можно реализовать при необходимости (например, для BETWEEN)
        return List.of();
    }
} 