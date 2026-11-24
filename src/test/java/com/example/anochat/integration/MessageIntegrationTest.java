package com.example.anochat.integration;

import com.example.anochat.DTO.CreateMessageRequest;
import com.example.anochat.DTO.UserResponse;
import com.example.anochat.message.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.UUID;

import static com.example.anochat.integration.PostgresTestConfiguration.ADA;
import static com.example.anochat.integration.PostgresTestConfiguration.LINUS;
import static org.assertj.core.api.Assertions.assertThat;

class MessageIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private MessageRepository messages;

    // The list contains exactly the three posted messages, each attributed to the user who sent it.
    @Test
    void listsTheThreeMessagesWithTheirAuthenticatedSenders() {
        messages.deleteAll();
        var adaSession = newSession();
        var linusSession = newSession();
        var ada = login(adaSession, ADA);
        var linus = login(linusSession, LINUS);

        var greeting = postMessage(adaSession, ada, "Hello everyone!");
        var reply = postMessage(linusSession, linus, "Hi Ada!");
        var farewell = postMessage(adaSession, ada, "See you later.");

        var listed = okBody(adaSession.get().uri("/api/messages").exchange(), StoredMessage[].class);
        assertThat(listed).containsExactlyInAnyOrder(greeting, reply, farewell);
    }

    private StoredMessage postMessage(RestTestClient session, UserResponse sender, String text) {
        var message = okBody(post(session, "/api/messages", new CreateMessageRequest(text)), StoredMessage.class);
        assertThat(message.id()).isNotNull();
        assertThat(message.sender()).isEqualTo(sender);
        assertThat(message.text()).isEqualTo(text);
        return message;
    }

    private record StoredMessage(UUID id, UserResponse sender, String text) {}
}
