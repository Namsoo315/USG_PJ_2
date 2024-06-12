package com.usg.chat.adapter.in.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomReq {
    private String opponentEmail;

}
