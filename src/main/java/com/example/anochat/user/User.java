package com.example.anochat.user;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String passwordHash;

    protected User() {}

    public User (String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public UUID getId(){
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPasswordHash(){
        return passwordHash;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
}
