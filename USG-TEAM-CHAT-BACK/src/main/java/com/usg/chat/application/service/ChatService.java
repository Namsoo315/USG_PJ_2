package com.usg.chat.application.service;

import com.usg.chat.application.port.in.Chat.SaveChatCommand;
import com.usg.chat.application.port.in.Chat.SaveChatUseCase;
import com.usg.chat.application.port.out.ChatPersistencePort;
import com.usg.chat.application.port.out.MemberPersistencePort;
import com.usg.chat.domain.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService implements SaveChatUseCase {

    private final ChatPersistencePort chatPersistencePort;
    private final MemberPersistencePort memberPersistencePort;

    @Override
    @Transactional
    public Long saveMessage(SaveChatCommand command){
        //센더와 리시버를 찾는 것.
        Long senderId = memberPersistencePort.getIdByEmail(command.getSenderEmail());
        Long receiverId = memberPersistencePort.getIdByEmail(command.getReceiverEmail());

        //커맨드 등록
        Chat chat = commandToChat(command, senderId,receiverId);
        Long savedChatId = chatPersistencePort.saveMessage(chat);

        return savedChatId;
    }

    private Chat commandToChat(SaveChatCommand command, Long senderId, Long receiverId){
        return Chat
                .builder()
                .message(command.getMessage())
                .receiverId(senderId)
                .senderId(receiverId)
                .timestamp(command.getTimestamp())
                .chatRoomId(command.getChatRoomId())
                .build();
    }
}
