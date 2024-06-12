package com.usg.chat.application.port.in.Chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetMessageHistoryRes {

    private String message;
    private String senderEmail;
    private String receiverEmail;
    private LocalDateTime timestamp;
    private Long chatRoomId;

    @Builder
    public GetMessageHistoryRes (String message, String receiverEmail,String senderEmail, LocalDateTime timestamp,Long chatRoomId){
        this.senderEmail = senderEmail;
        this.message = message;
        this.receiverEmail = receiverEmail;
        this.timestamp = timestamp;
        this.chatRoomId = chatRoomId;
    }
}
