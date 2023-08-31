package com.naveen.learning.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequestToken {

    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "New password can not be empty")
    private String password;

    @NotBlank(message = "Confirm password can not be empty")
    private String confirmPassword;

    @NotBlank(message = "token can not be empty")
    private String token;
}
