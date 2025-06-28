package maxim.module4_4.transaction_service_api.entity;

/**
 * Интерфейс для сущностей со статусом.
 */
public interface EntityWithStatus<T> {
    void setStatus(T status);
} 