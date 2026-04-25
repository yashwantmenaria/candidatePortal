package com.example.candidateportal.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.candidateportal.entity.User;
import com.example.candidateportal.service.AuthService;
import com.example.candidateportal.service.ForgotPasswordService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // frontend allow
public class AuthController {

    @Autowired
    private AuthService service;
    
    @Autowired
    private ForgotPasswordService forgotService;

    
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {

        String token = service.login(user.getEmail(), user.getPassword());

        return Map.of("token", token);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }
    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        return forgotService.sendOtp(email);
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {

        return forgotService.verifyOtpAndChangePassword(email, otp, newPassword);
    }
}