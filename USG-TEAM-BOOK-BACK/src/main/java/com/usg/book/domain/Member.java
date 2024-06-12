package com.usg.book.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private String email;
    private String nickname;

    @Builder
    public Member(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
