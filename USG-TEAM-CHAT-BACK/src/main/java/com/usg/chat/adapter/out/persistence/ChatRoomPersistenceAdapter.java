package com.usg.chat.adapter.out.persistence;

import com.usg.chat.adapter.out.persistence.entity.Chat.ChatRepository;
import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomRepository;
import com.usg.chat.domain.ChatRoom;
import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomEntity;
import com.usg.chat.application.port.out.ChatRoomPersistencePort;
import com.usg.chat.util.SortedStringEditor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomPersistencePort {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    // 채팅방 생성 후 DB저장
    @Override
    public Long createChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .senderAndReceiver(chatRoom.getSenderAndReceiver())
                .build();

        ChatRoomEntity savedChatRoomId = chatRoomRepository.save(chatRoomEntity);
        return savedChatRoomId.getRoomId();
    }

    @Override
    public ChatRoomEntity findBySenderAndReceiver(Long senderId, Long receiverId){
        //멤버 찾는게 필요할듯?
        String senderAndReceiver = SortedStringEditor.createSortedString(senderId,receiverId);
        ChatRoomEntity findChatRoom = chatRoomRepository.findBySenderAndReceiver(senderAndReceiver);
        return findChatRoom;
    }

    // 채팅방 조회 메서드
    @Override
    public List<ChatRoom> findChatRooms(Long memberId){
        List<ChatRoomEntity> chatRoomEntities = chatRoomRepository.findChatRoomsByMemberId(memberId);
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (ChatRoomEntity entity : chatRoomEntities) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setSenderAndReceiver(entity.getSenderAndReceiver());
            chatRoom.setChatRoomId(entity.getRoomId());
            // 필요한 경우 다른 속성도 매핑
            chatRooms.add(chatRoom); // 리스트에 ChatRoom 객체 추가
        }
        return chatRooms;
    }

    @Override
    public Long findReceiverIdByChatRoomId(Long chatRoomId){
        return chatRepository.findReceiverIdByChatRoomId(chatRoomId);
    }
}
