package com.example.anochat.user;

import com.example.anochat.user.DTO.CreateOrLoginUserRequest;
import com.example.anochat.user.DTO.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateOrLoginUserRequest request){
        User newUser = new User (request.username(), passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(newUser);
        return new UserResponse(savedUser.getId(), savedUser.getUsername());
    }

    public User getUserById (UUID id){
        return userRepository.getReferenceById(id);
    }

    public void createInitialUser(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            createUser(new CreateOrLoginUserRequest(username, password));
        }
    }
}
