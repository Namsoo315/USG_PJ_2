package com.usg.book.application.service;

import com.usg.book.application.port.in.MemberWriteCommend;
import com.usg.book.application.port.in.MemberWriteUseCase;
import com.usg.book.application.port.out.MemberPersistencePort;
import com.usg.book.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements MemberWriteUseCase {

    private final MemberPersistencePort memberPersistencePort;

    @Override
    @Transactional
    public void memberWrite(MemberWriteCommend memberWriteCommend) {
        Member member = commendToMember(memberWriteCommend);

        memberPersistencePort.saveMember(member);
    }

    private Member commendToMember(MemberWriteCommend memberWriteCommend) {
        return Member
                .builder()
                .email(memberWriteCommend.getEmail())
                .nickname(memberWriteCommend.getNickname())
                .build();
    }
}
