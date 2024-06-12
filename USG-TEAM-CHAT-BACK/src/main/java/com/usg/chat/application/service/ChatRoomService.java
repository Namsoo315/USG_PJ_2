package com.usg.chat.application.service;

import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomEntity;
import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomRepository;
import com.usg.chat.application.port.in.ChatRoom.*;
import com.usg.chat.application.port.out.ChatRoomPersistencePort;
import com.usg.chat.application.port.out.MemberPersistencePort;
import com.usg.chat.domain.Chat;
import com.usg.chat.domain.ChatRoom;
import com.usg.chat.util.SortedStringEditor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ChatRoomService implements ChatRoomUseCase , GetChatRoomsUseCase, FindChatRoomUseCase{
    private ChatRoomPersistencePort chatRoomPersistencePort;
    private MemberPersistencePort memberPersistencePort;

    // 채팅방 생성 서비스
    @Override
    @Transactional
    public Long createChatRoom(ChatRoomCommand command) {
        // 센더와 리시버 찾음.
        Long senderId = memberPersistencePort.getIdByEmail(command.getSenderEmail());
        Long receiverId = memberPersistencePort.getIdByEmail(command.getReceiverEmail());

        ChatRoom chat = commandToChatRoom(senderId,receiverId);
        Long savedChatRoomId = chatRoomPersistencePort.createChatRoom(chat);

        return savedChatRoomId;
    }
    private ChatRoom commandToChatRoom(Long senderId, Long receiverId){
        return ChatRoom.builder()
                .senderAndReceiver(SortedStringEditor.createSortedString(senderId,receiverId))
                .build();
    }

    @Override
    public List<GetChatRoomsRes> findChatRooms(String email){
        //로그인 한 회원 찾기
        Long memberId = memberPersistencePort.getIdByEmail(email);
        List<ChatRoom> chatRooms = chatRoomPersistencePort.findChatRooms(memberId);

        // 채팅방과 수신자 정보를 담을 리스트 초기화
        List<GetChatRoomsRes> chatRoomsWithReceivers = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            // 채팅방의 ID를 기반으로 수신자 ID, Email, NickName 조회
            Long receiverId = chatRoomPersistencePort.findReceiverIdByChatRoomId(chatRoom.getChatRoomId());
            String receiverEmail = memberPersistencePort.getEmailById(receiverId);
            String receiverNickName = memberPersistencePort.getNicknameByEmail(receiverEmail);


            // 채팅방과 수신자 정보를 가지고 있는 객체 생성 및 결과 리스트에 추가
            GetChatRoomsRes chatRoomWithReceiver = GetChatRoomsRes.builder()
                    .chatRoomId(chatRoom.getChatRoomId())
                    .opponentEmail(receiverEmail)
                    .opponentNickName(receiverNickName)
                    .build();

            chatRoomsWithReceivers.add(chatRoomWithReceiver);
        }

        return chatRoomsWithReceivers;
    }

    @Override
    public Long FindChatRoom(FindChatRoomCommand command){
        Long senderId = memberPersistencePort.getIdByEmail(command.getSenderEmail());
        Long receiverId = memberPersistencePort.getIdByEmail(command.getReceiverEmail());

        ChatRoomEntity findSenderAndReceiver = chatRoomPersistencePort.findBySenderAndReceiver(senderId,receiverId);

        if (findSenderAndReceiver == null) {
            ChatRoomCommand newChatRoomCommand = ChatRoomCommand.builder()
                    .senderEmail(command.getSenderEmail())
                    .receiverEmail(command.getReceiverEmail())
                    .build();
            createChatRoom(newChatRoomCommand); // 새로운 채팅방 생성
            findSenderAndReceiver = chatRoomPersistencePort.findBySenderAndReceiver(senderId, receiverId); // 다시 조회
        }

        Long findChatRoom = findSenderAndReceiver.getRoomId();
        return findChatRoom;
    }
}
