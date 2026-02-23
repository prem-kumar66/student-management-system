package com.sms.controller;

import com.sms.entity.User;
import com.sms.service.UserService;
import com.sms.util.JwtUtil;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Register user
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        user.setRole("USER");

        return userService.saveUser(user);
    }

    // Login and generate token
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Optional<User> existingUser =
                userService.findByUsername(user.getUsername());

        if (existingUser.isPresent()
                && existingUser.get().getPassword().equals(user.getPassword())) {

            return jwtUtil.generateToken(user.getUsername());
        }

        return "Invalid username or password";
    }
}