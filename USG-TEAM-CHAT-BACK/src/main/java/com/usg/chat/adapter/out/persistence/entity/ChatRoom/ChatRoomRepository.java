package com.usg.chat.adapter.out.persistence.entity.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> , ChatRoomRepositoryCustom{

    @Query("SELECT c FROM ChatRoomEntity c WHERE c.senderAndReceiver = :senderAndReceiver")
    ChatRoomEntity findBySenderAndReceiver(@Param("senderAndReceiver") String senderAndReceiver);

}
