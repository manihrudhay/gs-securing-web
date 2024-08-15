package com.project.springsecurity.controller;

import com.project.springsecurity.entity.UserEntity;
import com.project.springsecurity.repository.UserRepository;
import com.project.springsecurity.service.CustomUserDetailsService;
import com.project.springsecurity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/home")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/home2")
    public String hello2() {
        return "Hello World";
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserEntity userEntity) {
        // Check if username is already taken
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // Validate password
        if (userEntity.getPassword() == null || userEntity.getPassword().length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 6 characters long");
        }

        // Encode the password and set enabled status before saving
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setEnabled(true);
        userRepository.save(userEntity);

        // Generate JWT token
        String token = jwtUtil.generateToken(userEntity.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body("User saved successfully. Token: " + token);
    }
}


