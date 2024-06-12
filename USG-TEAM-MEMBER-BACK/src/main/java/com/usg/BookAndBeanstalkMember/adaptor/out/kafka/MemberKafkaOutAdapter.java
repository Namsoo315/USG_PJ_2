package com.usg.BookAndBeanstalkMember.adaptor.out.kafka;

import com.usg.BookAndBeanstalkMember.application.port.out.MemberKafkaOutPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberKafkaOutAdapter implements MemberKafkaOutPort {

    private final KafkaTemplate<String, MemberPublishDTO> kafkaTemplate;

    @Override
    public void publishMember(MemberPublishDTO memberPublishDTO) {
        kafkaTemplate.send("member", memberPublishDTO);
    }
}
