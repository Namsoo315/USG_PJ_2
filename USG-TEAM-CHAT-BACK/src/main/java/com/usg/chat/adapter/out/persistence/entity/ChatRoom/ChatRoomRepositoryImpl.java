package com.usg.chat.adapter.out.persistence.entity.ChatRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.usg.chat.adapter.out.persistence.entity.Chat.QChatEntity;
import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public ChatRoomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChatRoomEntity> findChatRoomsByMemberId(Long memberId) {
        QChatRoomEntity chatRoom = QChatRoomEntity.chatRoomEntity;
        QChatEntity chat = QChatEntity.chatEntity;
        return queryFactory
                .select(chatRoom)
                .from(chat)
                .join(chat.chatRoomId, chatRoom)
                .where(
                        chat.senderId.eq(memberId)
                                .or(chat.receiverId.eq(memberId)))
                .groupBy(chatRoom.RoomId)
                .fetch();
    }
}
