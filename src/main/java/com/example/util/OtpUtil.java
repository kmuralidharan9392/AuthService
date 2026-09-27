package com.example.util;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SESClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResult;

public class OtpUtil {
    
    private static final String AWS_REGION = "us-west-2";
    private static final String AWS_ACCESS_KEY = "YOUR_ACCESS_KEY";
    private static final String AWS_SECRET_KEY = "YOUR_SECRET_KEY";
    
    public static String generateOtp() {
        // Generate a random 6-digit number
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
    
    public static void sendOtpByEmail(String email, String otp) {
        // Initialize the SES client
        SESClient sesClient = new SESClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(CredentialProvider.create())
                .build();
        
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(email))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withText(new Content().withData("Your OTP is: " + otp)))
                        .withSubject(new Content().withData("Your Authentication OTP")))
                .withSource("your-verified-email@example.com");
        
        sesClient.sendEmail(request);
    }
    
    private static_credentialsProvider {
        // Implement your credentials provider logic here
        return new AwsCredentialsProvider();
    }
}
