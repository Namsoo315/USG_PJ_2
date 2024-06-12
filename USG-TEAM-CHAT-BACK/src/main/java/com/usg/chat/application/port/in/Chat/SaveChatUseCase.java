package com.usg.chat.application.port.in.Chat;

public interface SaveChatUseCase {
    Long saveMessage(SaveChatCommand command);
}
