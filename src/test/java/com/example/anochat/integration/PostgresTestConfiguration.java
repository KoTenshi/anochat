package com.example.anochat.integration;

import com.example.anochat.DTO.CreateOrLoginUserRequest;
import com.example.anochat.user.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
class PostgresTestConfiguration {

    static final CreateOrLoginUserRequest ADA = new CreateOrLoginUserRequest("Ada", "ada-test-password");
    static final CreateOrLoginUserRequest LINUS = new CreateOrLoginUserRequest("Linus", "linus-test-password");

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgres() {
        return new PostgreSQLContainer("postgres:17")
                .withDatabaseName("anochat_test");
    }

    @Bean
    ApplicationRunner seedTestUsers(UserService users) {
        return args -> {
            users.createUser(ADA);
            users.createUser(LINUS);
        };
    }
}

