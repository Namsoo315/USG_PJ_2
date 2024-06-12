package com.usg.BookAndBeanstalkMember.adaptor.out.persistance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinEntity {

    private String email;
    private String password;
    private String nickname;
}
