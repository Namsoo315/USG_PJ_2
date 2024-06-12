package com.usg.chat.application.port.out;

import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomEntity;
import com.usg.chat.domain.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomPersistencePort {
    // 채팅방 생성
    Long createChatRoom(ChatRoom chatRoom);
    //채팅방 생성할때 중복 확인을 위한 senderId receiverId 조회
    ChatRoomEntity findBySenderAndReceiver(Long senderId, Long receiverId);

    Long findReceiverIdByChatRoomId(Long chatRoomId);
    // 채팅방 조회
    List<ChatRoom> findChatRooms(Long memberId);
}

