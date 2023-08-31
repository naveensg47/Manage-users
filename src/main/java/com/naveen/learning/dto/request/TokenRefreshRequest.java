package com.naveen.learning.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshRequest {

    @NotBlank(message = "RefreshToken cannot be blank")
    private String refreshToken;

}
