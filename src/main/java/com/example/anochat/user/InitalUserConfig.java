package com.example.anochat.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitalUserConfig {

    @Bean
    ApplicationRunner createInitialUserOnStartup(
            UserService userService,
            @Value("admin") String username,
            @Value("admin") String password) {

        return args -> {
            if (username.isBlank() && password.isBlank()) {
                return;
            }

            if (username.isBlank() || password.isBlank()) {
                throw new IllegalStateException(
                        "set credencials for initial user");
            }

            userService.createInitialUser(username, password);
        };
    }
}