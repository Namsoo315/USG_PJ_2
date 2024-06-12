package com.usg.chat.application.port.in.ChatRoom;

import com.usg.chat.domain.ChatRoom;

public interface FindChatRoomUseCase {

    Long FindChatRoom(FindChatRoomCommand command);
}
