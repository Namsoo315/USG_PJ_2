package com.usg.chat.adapter.out.persistence.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoomEntity> findChatRoomsByMemberId(Long memberId);
}
