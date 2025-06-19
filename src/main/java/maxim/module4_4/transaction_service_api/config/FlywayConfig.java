package maxim.module4_4.transaction_service_api.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    
    @Bean
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/transaction_db_0", "postgres", "postgres")
                .baselineOnMigrate(true)
                .load();
    }
} 