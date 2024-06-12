package com.usg.chat.application.service;

import com.usg.chat.application.port.in.Member.MemberWriteCommand;
import com.usg.chat.application.port.in.Member.MemberWriteUseCase;
import com.usg.chat.application.port.out.MemberPersistencePort;
import com.usg.chat.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CqrsService implements MemberWriteUseCase {

    private final MemberPersistencePort memberPersistencePort;

    @Override
    @Transactional
    public void memberWrite(MemberWriteCommand memberWriteCommand) {
        Member member = commandToMember(memberWriteCommand);
        memberPersistencePort.saveMember(member);
    }

    private Member commandToMember(MemberWriteCommand memberWriteCommand) {
        return Member
                .builder()
                .memberId(memberWriteCommand.getMemberId())
                .email(memberWriteCommand.getEmail())
                .nickname(memberWriteCommand.getNickname())
                .build();
    }
}