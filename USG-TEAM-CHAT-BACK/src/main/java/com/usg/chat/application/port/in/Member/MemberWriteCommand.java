package com.usg.chat.application.port.in.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberWriteCommand {
    private Long memberId;
    private String email;
    private String nickname;

    @Builder
    public MemberWriteCommand(Long memberId, String email, String nickname){
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
    }
}
