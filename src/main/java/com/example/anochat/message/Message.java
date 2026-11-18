package com.example.anochat.message;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(updatable = false, nullable = false)
    private String sender;

    private String text;

    protected Message() {}

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }
    public String getSender() {
        return sender;
    }
    public String getText() {
        return text;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setText(String text) {
        this.text = text;
    }
}
