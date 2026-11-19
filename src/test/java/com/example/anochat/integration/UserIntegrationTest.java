package com.example.anochat.integration;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop"
)

@Import(PostgresTestConfiguration.class)
public class UserIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    com.example.anochat.user.UserRepository userRepository;

    @Test
    void returnCreatedUser(){
        RestClient client = messagesClient();

        var user = new NewUser("John Cena 29", "%sdfLDF89()234$kdsflsHD");

        var created = client.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .body(StoredUser.class);
        assertThat(created).isNotNull();
        assertThat(created.id()).isNotNull();

        var expected = new StoredUser(created.id(), user.username, user.passwordHash);

        UUID userId = created.id();

        var actual = client.get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .body(StoredUser.class);

        assertThat(actual).isEqualTo(expected);
    }

    record NewUser(String username, String passwordHash) {}

    record StoredUser(UUID id, String username, String passwordHash) {}

    private RestClient messagesClient() {
        return RestClient.builder()
                .baseUrl("http://127.0.0.1:" + port)
                .build();
    }
}
