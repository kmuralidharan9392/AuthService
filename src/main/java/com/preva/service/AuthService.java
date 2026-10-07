package com.preva.service;

import com.preva.dto.login.LogInCustomerRequest;
import com.preva.dto.login.LogInCustomerResponse;
import com.preva.dto.signup.SignUpCustomerRequest;
import com.preva.dto.verify.VerificationCustomerRequest;

public interface AuthService {
    String signUpCustomer(SignUpCustomerRequest signUpCustomerRequest);
    String verifyCustomer(VerificationCustomerRequest verificationCustomerRequest);
    LogInCustomerResponse logInCustomer(LogInCustomerRequest logInCustomerRequest);
}
