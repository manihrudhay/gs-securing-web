package com.project.springsecurity.controller;

import com.project.springsecurity.entity.UserEntity;
import com.project.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MyController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/home2")
    public String hello2() {
        return "Hello World";
    }

    @GetMapping("/save")
    public ResponseEntity<String> save(@RequestParam String username,@RequestParam String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        // Check if username is already taken
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Validate password
        if (userEntity.getPassword() == null || userEntity.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters long");
        }

        // Set enabled status and encode the password before saving
        userEntity.setEnabled(true);
        registerNewUser(userEntity);
        userRepository.save(userEntity);
        return ResponseEntity.ok("User saved successfully");
    }



    public void registerNewUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }


}

