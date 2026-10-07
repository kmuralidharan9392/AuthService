package com.preva.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/*
  This is a security related DTO.
  Hence, the fields are:
  1) final to ensure the fields can never be changed during business logic.
  2) Also no setter and no args constructor because for final fields, it does not make sense.
 */
@Getter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@RequiredArgsConstructor
public class LogInCustomerRequest {
    @NotBlank
    private final String email;
    @NotBlank
    private final String password;
}
