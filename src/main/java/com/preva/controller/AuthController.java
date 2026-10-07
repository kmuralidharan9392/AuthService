package com.preva.controller;

import com.preva.dto.login.LogInCustomerRequest;
import com.preva.dto.login.LogInCustomerResponse;
import com.preva.dto.signup.SignUpCustomerRequest;
import com.preva.dto.verify.VerificationCustomerRequest;
import com.preva.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signupCustomer(@Valid @RequestBody SignUpCustomerRequest signUpCustomerRequest) {
        return ResponseEntity.ok(authService.signUpCustomer(signUpCustomerRequest));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCustomer(@Valid @RequestBody VerificationCustomerRequest verificationCustomerRequest) {
        return ResponseEntity.ok(authService.verifyCustomer(verificationCustomerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LogInCustomerResponse> loginCustomer(@Valid @RequestBody LogInCustomerRequest logInCustomerRequest) {
        return ResponseEntity.ok(authService.logInCustomer(logInCustomerRequest));
    }
}
