package com.usg.chat.application.port.in.ChatRoom;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCommand {
    private String senderEmail;
    private String receiverEmail;

    @Builder
    public ChatRoomCommand(String senderEmail, String receiverEmail) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
    }
}
