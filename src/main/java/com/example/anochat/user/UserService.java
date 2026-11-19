package com.example.anochat.user;

import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        userRepository.save(user);
        return user;
    }

    public User getUserById (UUID id){
        return userRepository.getReferenceById(id);
    }
}
