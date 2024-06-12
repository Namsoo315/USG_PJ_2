package com.usg.BookAndBeanstalkMember.application.usecases;

public interface CheckMemberUseCase {
    boolean checkEmailDuplicate(String email);
    boolean checkNicknameDuplicate(String nickname);
}
