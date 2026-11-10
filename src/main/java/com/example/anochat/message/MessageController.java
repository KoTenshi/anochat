package com.example.anochat.message;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")

public class MessageController {

    public final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<Message> getMessages(){
        return messageService.getMessages();
    }

    @PostMapping
    public Message saveMessage(@RequestBody Message message){
        return messageService.saveMessage(message);
    }
}
