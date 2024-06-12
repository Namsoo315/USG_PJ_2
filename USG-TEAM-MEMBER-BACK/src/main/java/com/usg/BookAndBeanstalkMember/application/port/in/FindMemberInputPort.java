package com.usg.BookAndBeanstalkMember.application.port.in;

import com.usg.BookAndBeanstalkMember.application.port.out.MemberFindOutputPort;
import com.usg.BookAndBeanstalkMember.domain.Member;
import com.usg.BookAndBeanstalkMember.application.usecases.FindOneMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FindMemberInputPort implements FindOneMemberUseCase {
    private final MemberFindOutputPort memberFindOutputPort;

    @Override
    public Optional<Member> findOne(String userId) {

        return memberFindOutputPort.findOne(userId);
    }
}
