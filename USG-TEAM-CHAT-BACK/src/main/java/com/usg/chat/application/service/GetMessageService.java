package com.usg.chat.application.service;

import com.usg.chat.application.port.in.Chat.GetMessageHistoryRes;
import com.usg.chat.application.port.in.Chat.GetMessageHistoryUseCase;
import com.usg.chat.application.port.out.ChatPersistencePort;
import com.usg.chat.application.port.out.MemberPersistencePort;
import com.usg.chat.domain.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetMessageService implements GetMessageHistoryUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final ChatPersistencePort chatPersistencePort;

    @Override
    @Transactional
    public List<GetMessageHistoryRes> getMessages(String senderEmail, String receiverEmail) {
        Long senderId = memberPersistencePort.getIdByEmail(senderEmail);
        Long receiverId = memberPersistencePort.getIdByEmail(receiverEmail);
        List<Chat> chatMessages = chatPersistencePort.getMessages(senderId, receiverId);

        return chatMessages.stream()
                .map(chatMessage -> GetMessageHistoryRes.builder()
                        .message(chatMessage.getMessage())
                        .senderEmail(senderEmail)
                        .receiverEmail(receiverEmail)
                        .timestamp(chatMessage.getTimestamp())
                        .chatRoomId(chatMessage.getChatRoomId())
                        .build())
                .collect(Collectors.toList());
    }
}
