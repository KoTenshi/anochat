package com.example.anochat.message;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/messages")

public class MessageController {
    private final List<Message> messages = new CopyOnWriteArrayList<>();

    @GetMapping
    public List<Message> getMessages(){
        return messages;
    }
}
