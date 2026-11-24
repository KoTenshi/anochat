package com.example.anochat.user;

import com.example.anochat.user.DTO.CreateOrLoginUserRequest;
import com.example.anochat.user.DTO.UserResponse;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateOrLoginUserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") UUID id) {
        return userService.getUserById(id);
    }
}
