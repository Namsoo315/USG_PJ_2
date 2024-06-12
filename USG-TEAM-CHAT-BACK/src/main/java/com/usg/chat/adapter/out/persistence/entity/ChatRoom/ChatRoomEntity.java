package com.usg.chat.adapter.out.persistence.entity.ChatRoom;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RoomId;

    @Column(name = "senderAndReceiver", unique = true)
    private String senderAndReceiver;

    @Builder
    public ChatRoomEntity(String senderAndReceiver){
        this.senderAndReceiver = senderAndReceiver;
    }
}


