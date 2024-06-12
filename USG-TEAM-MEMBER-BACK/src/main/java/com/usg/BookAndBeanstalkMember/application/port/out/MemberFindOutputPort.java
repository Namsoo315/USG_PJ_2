package com.usg.BookAndBeanstalkMember.application.port.out;

import com.usg.BookAndBeanstalkMember.domain.Member;

import java.util.Optional;

public interface MemberFindOutputPort {

    Optional<Member> findOne(String email);
}
