package com.usg.book.application.port.in;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWriteCommend {

    private String email;
    private String nickname;

    @Builder
    public MemberWriteCommend(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
