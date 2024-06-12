package com.usg.BookAndBeanstalkMember.application.port.out;

public interface MemberJoinOutputPort {

    Long join(String email, String password, String nickname);
}
