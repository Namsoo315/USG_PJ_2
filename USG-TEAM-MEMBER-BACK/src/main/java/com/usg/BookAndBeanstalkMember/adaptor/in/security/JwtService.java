package com.usg.BookAndBeanstalkMember.adaptor.in.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return JWT.create()
                .withSubject("BookAndBeanstalkMember")
                .withClaim("email", email)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC512(jwtSecret));
    }
}
