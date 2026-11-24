package com.example.anochat.integration;

import com.example.anochat.DTO.CreateOrLoginUserRequest;
import com.example.anochat.DTO.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.net.CookieManager;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import(PostgresTestConfiguration.class)
abstract class IntegrationTestSupport {

    @LocalServerPort
    private int port;

    private final List<HttpClient> clients = new ArrayList<>();

    RestTestClient newSession() {
        var client = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
        clients.add(client);
        return RestTestClient.bindToServer(new JdkClientHttpRequestFactory(client))
                .baseUrl("http://127.0.0.1:" + port)
                .build();
    }

    @AfterEach
    void closeSessions() {
        clients.forEach(HttpClient::close);
    }

    static CsrfToken csrfToken(RestTestClient session) {
        return okBody(session.get().uri("/api/auth/csrf").exchange(), CsrfToken.class);
    }

    static RestTestClient.ResponseSpec post(RestTestClient session, String path, Object body) {
        return post(session, path, body, csrfToken(session));
    }

    static RestTestClient.ResponseSpec post(RestTestClient session, String path, Object body, CsrfToken csrf) {
        return session.post().uri(path).contentType(APPLICATION_JSON)
                .header(csrf.headerName(), csrf.token()).body(body).exchange();
    }

    static CreateOrLoginUserRequest newUserCredentials() {
        return new CreateOrLoginUserRequest("user-" + UUID.randomUUID(), "registration-test-password");
    }

    static UserResponse login(RestTestClient session, CreateOrLoginUserRequest credentials) {
        return okBody(post(session, "/api/auth/login", credentials), UserResponse.class);
    }

    static UserResponse register(RestTestClient session, CreateOrLoginUserRequest credentials) {
        return okBody(post(session, "/api/users", credentials), UserResponse.class);
    }

    static <T> T okBody(RestTestClient.ResponseSpec response, Class<T> type) {
        T body = response.expectStatus().isOk().returnResult(type).getResponseBody();
        assertThat(body).isNotNull();
        return body;
    }

    record CsrfToken(String headerName, String token) {}
}
