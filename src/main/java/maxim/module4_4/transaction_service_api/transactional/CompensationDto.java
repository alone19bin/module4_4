package maxim.module4_4.transaction_service_api.transactional;

/**
 * DTO для описания шага компенсации.
 */
public record CompensationDto(String stepName, Runnable runnable, boolean compensate) {
} 