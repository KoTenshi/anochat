package com.exampile.anochat.integration;

import com.example.anochat.DTO.UserResponse;
import org.junit.jupiter.api.Test;

import static com.example.anochat.integration.PostgresTestConfiguration.ADA;
import static org.assertj.core.api.Assertions.assertThat;

class UserIntegrationTest extends IntegrationTestSupport {

    // A profile lookup returns 401 before login and the registered ID and username after login.
    @Test
    void returnsTheCreatedUserById() {
        var session = newSession();
        var credentials = newUserCredentials();
        var created = register(session, credentials);

        assertThat(created.id()).isNotNull();
        assertThat(created.username()).isEqualTo(credentials.username());
        session.get().uri("/api/users/{id}", created.id()).exchange().expectStatus().isUnauthorized();

        login(session, ADA);
        session.get().uri("/api/users/{id}", created.id()).exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class).isEqualTo(created);
    }
}
