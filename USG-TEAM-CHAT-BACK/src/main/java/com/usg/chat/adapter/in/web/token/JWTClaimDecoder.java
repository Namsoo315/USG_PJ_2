package com.usg.chat.adapter.in.web.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
public class JWTClaimDecoder {

    // 복호화
    public String getToken(String jwt) {

        String[] jwtParts = jwt.split("\\.");
        String claims = new String(Base64.getDecoder().decode(jwtParts[1]));

        return claims;
    }
}
