package com.usg.BookAndBeanstalkMember.application.usecases;

public interface JoinMemberUseCase {

    void join(String email, String password, String nickname);
}
