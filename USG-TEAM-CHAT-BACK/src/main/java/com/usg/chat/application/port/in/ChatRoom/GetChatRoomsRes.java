package com.usg.chat.application.port.in.ChatRoom;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetChatRoomsRes {
    private Long chatRoomId;
    private String opponentEmail;
    private String opponentNickName;

    @Builder
    public GetChatRoomsRes(Long chatRoomId, String opponentEmail, String opponentNickName){
        this.chatRoomId = chatRoomId;
        this.opponentEmail =opponentEmail;
        this.opponentNickName = opponentNickName;
    }
}
