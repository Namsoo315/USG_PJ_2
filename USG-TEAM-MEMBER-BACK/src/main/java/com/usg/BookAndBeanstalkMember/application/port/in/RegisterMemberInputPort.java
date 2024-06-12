package com.usg.BookAndBeanstalkMember.application.port.in;

import com.usg.BookAndBeanstalkMember.adaptor.out.kafka.MemberPublishDTO;
import com.usg.BookAndBeanstalkMember.application.port.out.MemberJoinOutputPort;
import com.usg.BookAndBeanstalkMember.application.port.out.MemberKafkaOutPort;
import com.usg.BookAndBeanstalkMember.application.usecases.JoinMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegisterMemberInputPort implements JoinMemberUseCase {

    private final MemberJoinOutputPort memberJoinOutputPort;
    private final MemberKafkaOutPort memberKafkaOutPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(String email, String password, String nickname) {

        Long savedMemberId = memberJoinOutputPort.join(email, passwordEncoder.encode(password), nickname);

        MemberPublishDTO memberPublishDTO = createMemberPublishDTO(savedMemberId, email, nickname);
        memberKafkaOutPort.publishMember(memberPublishDTO);
    }

    private MemberPublishDTO createMemberPublishDTO(Long memberId, String email, String nickname) {
        return MemberPublishDTO
                .builder()
                .memberId(memberId)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
