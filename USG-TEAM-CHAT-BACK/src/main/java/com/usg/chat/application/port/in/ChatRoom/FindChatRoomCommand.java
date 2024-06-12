package com.usg.chat.application.port.in.ChatRoom;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindChatRoomCommand {
    private String senderEmail;
    private String receiverEmail;

    @Builder
    public FindChatRoomCommand(String senderEmail, String receiverEmail) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
    }
}
