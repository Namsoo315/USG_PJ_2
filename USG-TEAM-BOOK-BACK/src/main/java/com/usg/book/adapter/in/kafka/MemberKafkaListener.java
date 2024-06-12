package com.usg.book.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usg.book.application.port.in.MemberWriteCommend;
import com.usg.book.application.port.in.MemberWriteUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberKafkaListener {

    private final MemberWriteUseCase memberWriteUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "member", groupId = "book")
    public void memberListener(@Payload String kafka) throws JsonProcessingException {
        MemberPublishDTO memberPublishDTO = objectMapper.readValue(kafka, MemberPublishDTO.class);
        MemberWriteCommend memberWriteCommend = publishToCommend(memberPublishDTO);

        memberWriteUseCase.memberWrite(memberWriteCommend);
    }

    private MemberWriteCommend publishToCommend(MemberPublishDTO memberPublishDTO) {
        return MemberWriteCommend
                .builder()
                .email(memberPublishDTO.getEmail())
                .nickname(memberPublishDTO.getNickname())
                .build();
    }
}
