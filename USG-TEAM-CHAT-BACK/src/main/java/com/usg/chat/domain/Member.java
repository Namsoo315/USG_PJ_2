package com.usg.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member {
    private Long memberId;
    private String email;
    private String nickname;

    @Builder
    public Member(Long memberId, String email, String nickname){
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
    }
}
