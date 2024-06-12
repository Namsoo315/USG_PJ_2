package com.usg.chat.application.port.in.Chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SaveChatCommand {
    private String senderEmail;
    private String receiverEmail;
    private String message;
    private LocalDateTime timestamp;
    private Long chatRoomId;

    @Builder
    public SaveChatCommand(String message, String senderEmail, String receiverEmail, LocalDateTime timestamp, Long chatRoomId){
        this.message = message;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.timestamp = timestamp;
        this.chatRoomId = chatRoomId;
    }

}
