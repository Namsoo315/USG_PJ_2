package com.usg.book.application.service;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import com.usg.book.application.port.in.MemberWriteCommend;
import com.usg.book.application.port.out.MemberPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class MemberServiceTest extends IntegrationExternalApiMockingTestSupporter {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberPersistencePort memberPersistencePort;

    @Test
    @DisplayName("Cqrs 서비스를 이용해 Redis 에 회원 정보를 저장한다.")
    void memberWriteTest() {
        // given
        MemberWriteCommend memberWriteCommend = MemberWriteCommend.builder()
                .email("email").nickname("nickname").build();

        // when
        memberService.memberWrite(memberWriteCommend);

        // then
        String nickname = memberPersistencePort.getNicknameByEmail(memberWriteCommend.getEmail());
        assertThat(nickname).isEqualTo(memberWriteCommend.getNickname());
    }
}
