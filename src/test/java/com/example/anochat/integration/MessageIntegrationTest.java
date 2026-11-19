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
class MessageIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    com.example.anochat.message.MessageRepository messageRepository;

    @Test
    void returnsExactlyTheThreePostedMessages() {
        messageRepository.deleteAll();
        RestClient client = messagesClient();

        var messages = List.of(
                new NewMessage("Ada", "Hallo zusammen!"),
                new NewMessage("Linus", "Hallo Ada!"),
                new NewMessage("Ada", "Bis später.")
        );
        var expected = new ArrayList<StoredMessage>();

        for (var message : messages) {
            var created = client.post()
                    .uri("/api/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(message)
                    .retrieve()
                    .body(StoredMessage.class);

            assertThat(created).isNotNull();
            assertThat(created.id()).isNotNull();
            expected.add(new StoredMessage(created.id(), message.sender(), message.text()));
        }

        var actual = client.get()
                .uri("/api/messages")
                .retrieve()
                .body(new ParameterizedTypeReference<List<StoredMessage>>() {});

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    private RestClient messagesClient() {
        return RestClient.builder()
                .baseUrl("http://127.0.0.1:" + port)
                .build();
    }


    record NewMessage(String sender, String text) {}

    record StoredMessage(UUID id, String sender, String text) {}

}