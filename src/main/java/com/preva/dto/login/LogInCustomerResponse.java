package com.preva.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogInCustomerResponse {
    private String accessToken;
    private String idToken;
    private String refreshToken;
    private Long expiresIn;
}
