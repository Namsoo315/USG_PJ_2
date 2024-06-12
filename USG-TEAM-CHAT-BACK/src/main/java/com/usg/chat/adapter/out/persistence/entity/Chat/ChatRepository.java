package com.usg.chat.adapter.out.persistence.entity.Chat;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    @Query("SELECT c FROM ChatEntity c WHERE " +
            "(c.senderId = :senderId AND c.receiverId = :receiverId) OR " +
            "(c.senderId = :receiverId AND c.receiverId = :senderId) " + "ORDER BY c.timestamp DESC")     //시간 비교 추가해야함. ORDER BY c.createdAt DES
    List<ChatEntity> getMessages(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    @Query("SELECT c.receiverId FROM ChatEntity c WHERE c.chatRoomId.RoomId = :chatRoomId")
    Long findReceiverIdByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
