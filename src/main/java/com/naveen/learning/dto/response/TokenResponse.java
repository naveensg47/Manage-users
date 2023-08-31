package com.naveen.learning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    private String jwtToken;
    private String refreshToken;
    private Instant expirationTime;
}
