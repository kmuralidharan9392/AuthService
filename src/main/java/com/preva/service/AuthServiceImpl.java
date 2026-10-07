package com.preva.service;

import com.preva.config.CognitoProperties;
import com.preva.config.EnvironmentProperties;
import com.preva.dto.login.LogInCustomerRequest;
import com.preva.dto.login.LogInCustomerResponse;
import com.preva.dto.signup.SignUpCustomerRequest;
import com.preva.dto.verify.VerificationCustomerRequest;
import com.preva.utility.CognitoUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private CognitoIdentityProviderClient cognitoClient;
    private final EnvironmentProperties environmentProperties;
    private final CognitoProperties cognitoProperties;


    @PostConstruct
    public void init() {
        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(environmentProperties.getRegion()))
                .build();
    }

    @Override
    public String signUpCustomer(SignUpCustomerRequest signUpCustomerRequest) {
        try {
            String secretHash = CognitoUtil.calculateSecretHash(
                    signUpCustomerRequest.getEmail(),
                    cognitoProperties.getClientId(),
                    cognitoProperties.getClientSecret()
            );

            SignUpRequest signUpClientRequest = SignUpRequest
                    .builder()
                    .clientId(cognitoProperties.getClientId())
                    .secretHash(secretHash)
                    .username(signUpCustomerRequest.getEmail())
                    .password(signUpCustomerRequest.getPassword())
                    .userAttributes(AttributeType.builder().name("email").value(signUpCustomerRequest.getEmail()).build())
                    .build();

            cognitoClient.signUp(signUpClientRequest);
            return "Registration initiated. Please check your email for the verification OTP code.";

        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Cognito signup failed: " + e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public String verifyCustomer(VerificationCustomerRequest verificationCustomerRequest) {
        try {
            String secretHash = CognitoUtil.calculateSecretHash(
                    verificationCustomerRequest.getEmail(),
                    cognitoProperties.getClientId(),
                    cognitoProperties.getClientSecret()
            );

            // 1. Submit the user's OTP verification code to Cognito
            ConfirmSignUpRequest confirmSignUpRequest = ConfirmSignUpRequest.builder()
                    .clientId(cognitoProperties.getClientId())
                    .secretHash(secretHash)
                    .username(verificationCustomerRequest.getEmail())
                    .confirmationCode(verificationCustomerRequest.getCode())
                    .build();
            cognitoClient.confirmSignUp(confirmSignUpRequest);

            // 2. Now that the user is officially active, safely map them to your customer group
            AdminAddUserToGroupRequest groupRequest = AdminAddUserToGroupRequest.builder()
                    .userPoolId(cognitoProperties.getUserPoolId())
                    .username(verificationCustomerRequest.getEmail())
                    .groupName("customer")
                    .build();
            cognitoClient.adminAddUserToGroup(groupRequest);

            return "Account successfully verified and activated.";

        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Verification failed: " + e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public LogInCustomerResponse logInCustomer(LogInCustomerRequest logInCustomerRequest) {
        try {
            String secretHash = CognitoUtil.calculateSecretHash(
                    logInCustomerRequest.getEmail(),
                    cognitoProperties.getClientId(),
                    cognitoProperties.getClientSecret()
            );

            Map<String, String> authParams = Map.of(
                    "USERNAME", logInCustomerRequest.getEmail(),
                    "PASSWORD", logInCustomerRequest.getPassword(),
                    "SECRET_HASH", secretHash
            );

            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .clientId(cognitoProperties.getClientId())
                    .authParameters(authParams)
                    .build();

            InitiateAuthResponse authResponse = cognitoClient.initiateAuth(authRequest);
            AuthenticationResultType result = authResponse.authenticationResult();

            return new LogInCustomerResponse(
                    result.accessToken(),
                    result.idToken(),
                    result.refreshToken(),
                    result.expiresIn().longValue()
            );


        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Authentication failed: " + e.awsErrorDetails().errorMessage());
        }
    }
}
