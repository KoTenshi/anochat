package com.example.anochat.message;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MessageService {

    public final List<Message> messages = new CopyOnWriteArrayList<>();

    public List<Message> getMessages(){
        return List.copyOf(messages);
    }

    public Message saveMessage(Message message){
        messages.add(message);
        return message;
    }
}
