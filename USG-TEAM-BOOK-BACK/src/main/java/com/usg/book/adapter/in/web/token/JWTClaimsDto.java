package com.usg.book.adapter.in.web.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JWTClaimsDto {

    @JsonProperty("sub")
    private String subject;
    @JsonProperty("exp")
    private Long expiration;
    @JsonProperty("email")
    private String email;
}
