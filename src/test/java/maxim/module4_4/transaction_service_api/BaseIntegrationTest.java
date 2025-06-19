package maxim.module4_4.transaction_service_api;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgresContainer0 = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("transaction_service_test_0")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    protected static final PostgreSQLContainer<?> postgresContainer1 = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("transaction_service_test_1")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void beforeAll() {
        postgresContainer0.start();
        postgresContainer1.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.shardingsphere.datasource.ds0.jdbc-url", postgresContainer0::getJdbcUrl);
        registry.add("spring.shardingsphere.datasource.ds0.username", postgresContainer0::getUsername);
        registry.add("spring.shardingsphere.datasource.ds0.password", postgresContainer0::getPassword);
        
        registry.add("spring.shardingsphere.datasource.ds1.jdbc-url", postgresContainer1::getJdbcUrl);
        registry.add("spring.shardingsphere.datasource.ds1.username", postgresContainer1::getUsername);
        registry.add("spring.shardingsphere.datasource.ds1.password", postgresContainer1::getPassword);
    }
} 