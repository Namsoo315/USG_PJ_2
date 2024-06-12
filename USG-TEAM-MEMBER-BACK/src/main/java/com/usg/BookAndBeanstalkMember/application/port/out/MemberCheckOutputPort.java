package com.usg.BookAndBeanstalkMember.application.port.out;

public interface MemberCheckOutputPort {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
