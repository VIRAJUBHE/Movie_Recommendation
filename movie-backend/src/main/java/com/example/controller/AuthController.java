package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // LOGIN
    @PostMapping("/login")
    public User login(@RequestBody User loginUser) {

        User user = userRepository
                .findByEmailAndPassword(
                        loginUser.getEmail(),
                        loginUser.getPassword());

        if(user == null){
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    // SIGNUP
    @PostMapping("/signup")
    public User signup(@RequestBody User user) {

        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(user);
    }
}
