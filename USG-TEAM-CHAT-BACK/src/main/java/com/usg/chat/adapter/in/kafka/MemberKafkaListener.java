package com.usg.chat.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usg.chat.application.port.in.Member.MemberWriteCommand;
import com.usg.chat.application.port.in.Member.MemberWriteUseCase;
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

    @KafkaListener(topics = "member", groupId = "chat")
    public void memberListener(@Payload String kafka) throws JsonProcessingException {
        MemberPublishDTO memberPublishDTO = objectMapper.readValue(kafka, MemberPublishDTO.class);
        MemberWriteCommand memberWriteCommand = publishToCommand(memberPublishDTO);

        memberWriteUseCase.memberWrite(memberWriteCommand);
    }

    private MemberWriteCommand publishToCommand(MemberPublishDTO memberPublishDTO) {
        return MemberWriteCommand
                .builder()
                .memberId(memberPublishDTO.getMemberId())
                .email(memberPublishDTO.getEmail())
                .nickname(memberPublishDTO.getNickname())
                .build();
    }
}