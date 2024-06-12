package com.usg.BookAndBeanstalkMember.application.port.in;

import com.usg.BookAndBeanstalkMember.application.port.out.MemberCheckOutputPort;
import com.usg.BookAndBeanstalkMember.application.usecases.CheckMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckMemberInputPort implements CheckMemberUseCase {
    private final MemberCheckOutputPort memberCheckOutputPort;
    public boolean checkEmailDuplicate(String email) {
        return !memberCheckOutputPort.existsByEmail(email);
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return !memberCheckOutputPort.existsByNickname(nickname);
    }

}
