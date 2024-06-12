package com.usg.book.adapter.in.web;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Profile("test")
public class MockJWTGenerator {

    private static String secretKey = "ThisIsMockSecretKeyForTestingApiControllersInTestPackage";
    private static Long jwtExpiration = 3600000L;

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject("user")
                .claim("email", email)
                .setExpiration(Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
}
