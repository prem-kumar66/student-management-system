package com.sms.controller;

import com.sms.entity.User;
import com.sms.service.UserService;
import com.sms.util.JwtUtil;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public AuthController(UserService userService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Register API
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        user.setRole("USER");   // default role

        return userService.saveUser(user);  
        // password encryption happens inside UserService
    }

    // ✅ Login API
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Optional<User> existingUser =
                userService.findByUsername(user.getUsername());

        if (existingUser.isPresent() &&
            passwordEncoder.matches(
                    user.getPassword(),              // raw password
                    existingUser.get().getPassword() // encrypted password
            )) {

            return jwtUtil.generateToken(user.getUsername());
        }

        return "Invalid username or password";
    }
}