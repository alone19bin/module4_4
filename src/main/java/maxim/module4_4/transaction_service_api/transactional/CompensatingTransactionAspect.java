package maxim.module4_4.transaction_service_api.transactional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Deque;

/**
 * Аспект для обработки компенсирующих транзакций.
 */
@Aspect
@Component
public class CompensatingTransactionAspect {
    private static final Logger log = LoggerFactory.getLogger(CompensatingTransactionAspect.class);
    @Around("@annotation(maxim.module4_4.transaction_service_api.transactional.CompensatingTransaction)")
    public Object run(ProceedingJoinPoint joinPoint) throws Throwable {
        Object afterSave;
        Object[] args = joinPoint.getArgs();
        try {
            afterSave = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            for (Object arg : args) {
                if (arg instanceof Deque<?>) {
                    Deque<CompensationDto> compensationSteps = (Deque<CompensationDto>) arg;
                    while (!compensationSteps.isEmpty()) {
                        CompensationDto compensationDto = compensationSteps.removeLast();
                        log.debug("Compensation step [{}] retrieved", compensationDto.stepName());
                        if (compensationDto.compensate()) {
                            compensationDto.runnable().run();
                            log.debug("Compensation step [{}] completed", compensationDto.stepName());
                        }
                    }
                }
            }
            throw e;
        }
        return afterSave;
    }
} 