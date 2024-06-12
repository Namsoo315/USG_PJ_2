package com.usg.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {
    private Long chatRoomId;
    private String senderAndReceiver;

    @Builder
    public ChatRoom(String senderAndReceiver,Long chatRoomId) {
        this.chatRoomId = chatRoomId;
        this.senderAndReceiver = senderAndReceiver;
    }


}

