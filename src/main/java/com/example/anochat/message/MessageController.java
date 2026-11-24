package com.example.anochat.message;

import com.example.anochat.DTO.CreateMessageRequest;
import com.example.anochat.user.UserRepository;
import com.example.anochat.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Message> getMessages(){
        return messageService.getMessages();
    }

    @PostMapping
    public Message saveMessage(@RequestBody CreateMessageRequest createMessageRequest, Principal principal){
        var sender = userRepository.findByUsername(principal.getName()).orElseThrow();

        return messageService.saveMessage(new Message(sender, createMessageRequest.text()));
    }
}
