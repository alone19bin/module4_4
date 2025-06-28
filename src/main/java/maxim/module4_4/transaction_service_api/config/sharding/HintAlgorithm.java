package maxim.module4_4.transaction_service_api.config.sharding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shardingsphere.infra.exception.core.ShardingSpherePreconditions;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;
import org.apache.shardingsphere.sharding.exception.data.NullShardingValueException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 *   <Получает UUID
 *   <Вычисляет hashCode от UUID (строкой), берёт по модулю количества шардов
 *   <Возвращает имя нужного шарда
 * Пример использования: top_up_requests, withdrawal_requests, transfer_requests.
 */
public final class HintAlgorithm implements HintShardingAlgorithm<UUID> {
    private static final Logger log = LoggerFactory.getLogger(HintAlgorithm.class);

    /**
     * Основной метод шардирования по hint.
     * @param availableTargetNames список всех возможных шардов (например, ds_0, ds_1)
     * @param shardingValue значение, переданноечерез HintManage
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<UUID> shardingValue) {
        // Проверка, что значение для шардирования не null
        ShardingSpherePreconditions.checkNotNull(shardingValue.getValues(), NullShardingValueException::new);
        // Берём первое значение из переданных (обычно один UUID)
        UUID shardingValueValue = shardingValue.getValues().iterator().next();
        // Считаем hashCode от UUID (строкой), берём по модулю количества шардов (например, 2)
        int hashCode = Objects.hashCode(shardingValueValue.toString());
        int modulo = Math.abs(hashCode % 2);
        String tableNameSuffix = String.valueOf(modulo);
        // Оставляем только те шарды, чьи имена заканчиваются на нужный суффикс (0 или 1)
        return availableTargetNames.stream().filter(t -> t.endsWith(tableNameSuffix)).toList();
    }

    /**
     * Вспомогательный метод для совместимости с интерфейсом
     */
    @Override
    public Optional<String> getAlgorithmStructure(String dataNodePrefix, String shardingColumn) {
        log.info(dataNodePrefix);
        return HintShardingAlgorithm.super.getAlgorithmStructure(dataNodePrefix, shardingColumn);
    }

    @Override
    public String getType() {
        return "HINT";
    }
} 