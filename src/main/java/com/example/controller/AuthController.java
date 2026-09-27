package com.example.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.AuthRequest;
import com.example.model.AuthResponse;

@RestController
public class AuthController {
    
    @PostMapping("/authenticate")
    public AuthResponse authenticate(@RequestBody AuthRequest request) {
        // Here we would typically call the AWS Cognito service
        // For now, let's return a mock response
        return new AuthResponse("mock-token", "Success");
    }
    
    @PostMapping("/generate-otp")
    public String generateOtp(@RequestBody AuthRequest request) {
        // Generate OTP
        String otp = OtpUtil.generateOtp();
        
        // Send OTP via email
        OtpUtil.sendOtpByEmail(request.getEmail(), otp);
        
        return "OTP sent successfully";
    }
}
