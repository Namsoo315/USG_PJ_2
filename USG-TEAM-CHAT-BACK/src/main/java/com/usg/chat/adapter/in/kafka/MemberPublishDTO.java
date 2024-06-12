package com.usg.chat.adapter.in.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MemberPublishDTO {

    private Long memberId;
    private String email;
    private String nickname;

    @Builder
    public MemberPublishDTO(Long memberId, String email, String nickname) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
    }
}
