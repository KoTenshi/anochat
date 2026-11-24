package com.example.anochat.integration;

import com.example.anochat.DTO.CreateMessageRequest;
import com.example.anochat.DTO.CreateOrLoginUserRequest;
import com.example.anochat.DTO.UserResponse;
import com.example.anochat.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.anochat.integration.PostgresTestConfiguration.ADA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

class AuthIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository users;

    // An anonymous session receives a non-blank CSRF token and the name of the header that accepts it.
    @Test
    void returnsACsrfToken() {
        var csrf = csrfToken(newSession());

        assertThat(csrf.token()).isNotBlank();
        assertThat(csrf.headerName()).isNotBlank();
    }

    // Logging in with a seeded user's credentials returns that user's ID and username.
    @Test
    void logsInAnExistingUser() {
        var existing = users.findByUsername(ADA.username()).orElseThrow();

        assertThat(login(newSession(), ADA)).isEqualTo(new UserResponse(existing.getId(), ADA.username()));
    }

    // A user registered through the API can log in from a fresh session and read protected messages.
    @Test
    void logsInANewlyRegisteredUser() {
        var credentials = newUserCredentials();
        var registered = register(newSession(), credentials);
        assertThat(registered.id()).isNotNull();
        assertThat(registered.username()).isEqualTo(credentials.username());

        var session = newSession();
        assertThat(login(session, credentials)).isEqualTo(registered);
        session.get().uri("/api/messages").exchange().expectStatus().isOk();
    }

    // An incorrect password returns 401 and leaves the same session unable to read protected messages.
    @Test
    void rejectsAnIncorrectPassword() {
        var session = newSession();
        var credentials = new CreateOrLoginUserRequest(ADA.username(), "incorrect-password");

        post(session, "/api/auth/login", credentials).expectStatus().isUnauthorized();
        session.get().uri("/api/messages").exchange().expectStatus().isUnauthorized();
    }

    // Omitting the CSRF header rejects valid login credentials and leaves the session unauthenticated.
    @Test
    void rejectsLoginWithoutACsrfToken() {
        var session = newSession();
        csrfToken(session);

        session.post().uri("/api/auth/login").contentType(APPLICATION_JSON)
                .body(ADA).exchange().expectStatus().isUnauthorized();
        session.get().uri("/api/messages").exchange().expectStatus().isUnauthorized();
    }

    // Posting a message with the same CSRF token that was used for login returns 403.
    @Test
    void rejectsThePreLoginCsrfToken() {
        var session = newSession();
        var previousToken = csrfToken(session);
        post(session, "/api/auth/login", ADA, previousToken).expectStatus().isOk();

        post(session, "/api/messages", new CreateMessageRequest("Hello!"), previousToken)
                .expectStatus().isForbidden();
    }
}
