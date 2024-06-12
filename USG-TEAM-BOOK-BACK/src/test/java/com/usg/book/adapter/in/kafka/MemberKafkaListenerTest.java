package com.usg.book.adapter.in.kafka;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import com.usg.book.application.port.out.MemberPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@DirtiesContext
@EmbeddedKafka(partitions = 1,
        brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092"
        },
        ports = { 9092 })
public class MemberKafkaListenerTest extends IntegrationExternalApiMockingTestSupporter {


    @Autowired
    private MemberKafkaListener memberKafkaListener;
    @Autowired
    private KafkaTemplate<String, MemberPublishDTO> kafkaTemplate;
    @Autowired
    private MemberPersistencePort memberPersistencePort;

    @Test
    @DisplayName("카프카 member 토픽에서 데이터를 가져온다.")
    void memberListenerTest() {
        // given
        String topic = "member";
        long memberId = 1L;
        String email = "kafkaEmail";
        String nickname = "kafkaNickname";
        MemberPublishDTO memberPublishDTO = MemberPublishDTO.builder()
                .memberId(memberId)
                .email(email)
                .nickname(nickname)
                .build();

        // when
        kafkaTemplate.send(topic, memberPublishDTO);

        // then
        await().untilAsserted(() -> {
            String findNickname = memberPersistencePort.getNicknameByEmail(email);
            assertThat(findNickname).isEqualTo(nickname);
        });
    }

}
