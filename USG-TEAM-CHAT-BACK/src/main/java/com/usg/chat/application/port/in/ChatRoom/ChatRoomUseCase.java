package com.usg.chat.application.port.in.ChatRoom;

import com.usg.chat.domain.ChatRoom;

import java.util.List;

public interface ChatRoomUseCase {
    // 채팅방 생성
    Long createChatRoom(ChatRoomCommand command);

}

