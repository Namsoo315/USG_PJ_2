package com.usg.BookAndBeanstalkMember.application.usecases;


import com.usg.BookAndBeanstalkMember.domain.Member;

import java.util.Optional;

public interface FindOneMemberUseCase {

    Optional<Member> findOne(String userId);
}