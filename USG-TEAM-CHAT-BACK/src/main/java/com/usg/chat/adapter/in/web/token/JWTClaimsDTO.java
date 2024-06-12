package com.usg.chat.adapter.in.web.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JWTClaimsDTO {
    @JsonProperty("sub")
    private String subject;
    @JsonProperty("exp")
    private Long expiration;
    @JsonProperty("email")
    private String email;
}
