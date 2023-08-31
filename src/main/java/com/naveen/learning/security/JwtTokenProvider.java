package com.naveen.learning.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.naveen.learning.model.CustomUserDetails;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
public class JwtTokenProvider {

    private static final String AUTHORITIES_CLAIM = "authorities";
    private final String jwtSecret;
    private final long jwtExpirationInMs;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String jwtSecret, @Value("${app.jwt.expiration}") long jwtExpirationInMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    //Generates the token for Principal User
    public String generateToken(CustomUserDetails customUserDetails) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        String authorities = getUserAuthorities(customUserDetails);
        return Jwts.builder()
                .setSubject(Long.toString(customUserDetails.getId()))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .claim(AUTHORITIES_CLAIM, authorities)
                .compact();
    }

    //Generates the token from user Id
    public String generateTokenByUserId(Long userId) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    //private method to extract authorities from the user
    private String getUserAuthorities(CustomUserDetails customUserDetails) {
        return customUserDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    //Get user id from the token
    public Long getUserIdFromToken(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    //Method to get Authorities from the token
    public List<GrantedAuthority> getAuthoritiesFromToken(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Arrays.stream(claims.get(AUTHORITIES_CLAIM).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
