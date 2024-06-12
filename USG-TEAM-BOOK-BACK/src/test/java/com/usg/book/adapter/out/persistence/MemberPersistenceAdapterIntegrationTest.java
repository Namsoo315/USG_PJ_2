package com.usg.book.adapter.out.persistence;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import com.usg.book.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.*;

public class MemberPersistenceAdapterIntegrationTest extends IntegrationExternalApiMockingTestSupporter {

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("레디스를 이용해 회원 정보를 저장한다.")
    void saveMemberTest() {
        // given
        Member member = createMember();

        // when
        memberPersistenceAdapter.saveMember(member);

        // then
        String nickname = redisTemplate.opsForValue().get("member : " + member.getEmail());
        assertThat(nickname).isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("회원 이메일로 닉네임을 조회한다.")
    void getNicknameByEmailTest() {
        // given
        Member member = createMember();
        memberPersistenceAdapter.saveMember(member);

        // when
        String nickname = memberPersistenceAdapter.getNicknameByEmail(member.getEmail());

        // then
        assertThat(nickname).isEqualTo(member.getNickname());
    }

    private Member createMember() {
        return Member.builder()
                .email("email")
                .nickname("nickname")
                .build();
    }
}
