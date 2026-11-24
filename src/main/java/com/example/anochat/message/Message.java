package com.example.anochat.message;

import com.example.anochat.user.User;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false, updatable = false)
    private User sender;

    private String text;

    protected Message() {}

    public Message(User sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }
    public User getSender() {
        return sender;
    }
    public String getText() {
        return text;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public void setText(String text) {
        this.text = text;
    }
}
