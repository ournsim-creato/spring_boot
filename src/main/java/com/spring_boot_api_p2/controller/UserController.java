package com.spring_boot_api_p2.controller;

import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}