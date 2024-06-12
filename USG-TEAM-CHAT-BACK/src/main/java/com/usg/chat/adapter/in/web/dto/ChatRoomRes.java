package com.usg.chat.adapter.in.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRes {
    private Long chatRoomId;
    private String opponentEmail;
    private String opponentNickName;

    @Builder
    public ChatRoomRes(Long chatRoomId,String opponentEmail , String opponentNickName){
     this.chatRoomId= chatRoomId;
     this.opponentEmail =opponentEmail;
     this.opponentNickName = opponentNickName;
    }
}
