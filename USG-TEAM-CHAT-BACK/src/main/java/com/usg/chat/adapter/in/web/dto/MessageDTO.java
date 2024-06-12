package com.usg.chat.adapter.in.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDTO {

    private String message;
    private String senderEmail;
    private String receiverEmail;
    private LocalDateTime timestamp;
    private Long chatRoomId;

    @Builder
    public MessageDTO (String message, String receiverEmail,String senderEmail, LocalDateTime timestamp,Long chatRoomId){
        this.senderEmail = senderEmail;
        this.message = message;
        this.receiverEmail = receiverEmail;
        this.timestamp = timestamp;
        this.chatRoomId = chatRoomId;
    }
}
