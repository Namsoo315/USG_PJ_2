package com.usg.chat.adapter.out.persistence;

import com.usg.chat.adapter.out.persistence.entity.Chat.ChatEntity;
import com.usg.chat.adapter.out.persistence.entity.Chat.ChatRepository;
import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomEntity;
import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomRepository;
import com.usg.chat.application.port.out.ChatPersistencePort;
import com.usg.chat.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatPersistenceAdapter implements ChatPersistencePort {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Long saveMessage(Chat chat){
        //채팅방을 찾음.
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chat.getChatRoomId()).orElseThrow(
                () -> new IllegalAccessError("채팅방을 찾을 수 없습니다.")
        );

        ChatEntity chatEntity = ChatEntity
                .builder()
                .message(chat.getMessage())
                .senderId(chat.getSenderId())
                .receiverId(chat.getReceiverId())
                .timestamp(chat.getTimestamp())
                .chatRoomId(chatRoomEntity)
                .build();

        ChatEntity savedChatEntity = chatRepository.save(chatEntity);

        return savedChatEntity.getId();
    }

    @Override
    public List<Chat> getMessages(Long senderId, Long receiverId) {
        List<ChatEntity> chatEntities = chatRepository.getMessages(senderId,receiverId);
        return chatEntities.stream().map(entity -> Chat.builder()
                .message(entity.getMessage())
                .senderId(entity.getSenderId())
                .receiverId(entity.getReceiverId())
                .timestamp(entity.getTimestamp())
                .chatRoomId(entity.getChatRoomId().getRoomId())
                .build()).collect(Collectors.toList());
    }
}
