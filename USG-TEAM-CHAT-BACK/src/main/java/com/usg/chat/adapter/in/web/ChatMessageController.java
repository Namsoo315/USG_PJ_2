package com.usg.chat.adapter.in.web;

import com.usg.chat.adapter.in.web.dto.MessageDTO;
import com.usg.chat.adapter.in.web.dto.Result;
import com.usg.chat.adapter.in.web.token.MemberEmailGetter;
import com.usg.chat.application.port.in.Chat.SaveChatCommand;
import com.usg.chat.application.port.in.Chat.SaveChatUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SaveChatUseCase saveChatUseCase;
    private final MemberEmailGetter memberEmailGetter;

    @MessageMapping("/chat/{chatRoomId}") // 채팅방 ID에 따라 주제 설정
    @SendTo("/topic/chat/{chatRoomId}") // 채팅방 ID에 따라 주제 설정
    public ResponseEntity<Result> sendChat(@Payload MessageDTO message, StompHeaderAccessor headerAccessor,
                                           @DestinationVariable Long chatRoomId) {

        message.setTimestamp(LocalDateTime.now());  //메시지 전송 시간 설정

        // Jwt에서 이메일 가져오기
        String email = memberEmailGetter.getMemberEmail(headerAccessor.getFirstNativeHeader("Authorization"));
        SaveChatCommand command = requestToCommand(message,email,chatRoomId);
        saveChatUseCase.saveMessage(command);

        return ResponseEntity.ok(new Result(command));
    }

    private SaveChatCommand requestToCommand(MessageDTO messageDTO, String email, Long chatRoomId){
        return SaveChatCommand.builder()
                .message(messageDTO.getMessage())
                .senderEmail(email)
                .receiverEmail(messageDTO.getReceiverEmail())
                .timestamp(messageDTO.getTimestamp())
                .chatRoomId(chatRoomId)
                .build();
    }

}