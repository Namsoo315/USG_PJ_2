package com.usg.chat.adapter.in.web;

import com.usg.chat.adapter.in.web.dto.ChatRoomReq;
import com.usg.chat.adapter.in.web.dto.ChatRoomRes;
import com.usg.chat.adapter.in.web.dto.Result;
import com.usg.chat.adapter.in.web.token.MemberEmailGetter;
import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomEntity;
import com.usg.chat.adapter.out.persistence.entity.ChatRoom.ChatRoomRepository;
import com.usg.chat.application.port.in.ChatRoom.*;
import com.usg.chat.util.SortedStringEditor;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class ChatRoomApiController {
    private final ChatRoomUseCase chatRoomUseCase;
    private final FindChatRoomUseCase findChatRoomUseCase;
    private final GetChatRoomsUseCase getChatRoomsUseCase;
    private final MemberEmailGetter memberEmailGetter;

    //채팅방 생성 API
    @PostMapping("/api/chat-rooms")
    public ResponseEntity<Result> createChatRoom(@RequestBody ChatRoomReq request, HttpServletRequest servletRequest) {
        String senderEmail = memberEmailGetter.getMemberEmail(servletRequest.getHeader("Authorization"));
        String opponentEmail = request.getOpponentEmail();

        // 채팅방 인원 찾기
        FindChatRoomCommand findChatRoomCommand = FindChatRoomCommand.builder()
                .senderEmail(senderEmail)
                .receiverEmail(opponentEmail)
                .build();

        Long findChatRoom = findChatRoomUseCase.FindChatRoom(findChatRoomCommand);

        // 채팅방이 없을 경우 생성
        if (findChatRoom == null) {
            ChatRoomCommand command = ChatRoomCommand.builder()
                    .senderEmail(senderEmail)
                    .receiverEmail(opponentEmail)
                    .build();
            chatRoomUseCase.createChatRoom(command);
        }

        // 채팅방이 있을 경우 응답 생성
        ChatRoomRes response = ChatRoomRes.builder()
                .chatRoomId(findChatRoom)
                .build();

        return ResponseEntity.ok(new Result(response, "채팅방을 생성하였습니다."));
    }

    // 채팅방 조회 API
    @GetMapping("/api/chat-rooms/")
    @Operation(summary = "채팅방 목록 조회")
    public ResponseEntity<Result<List<GetChatRoomsRes>>> getChatRooms(HttpServletRequest servletRequest) {

        String email = memberEmailGetter.getMemberEmail(servletRequest.getHeader("Authorization"));
        // ChatRoomEntity 대신 ChatRoomRes를 사용하여 응답 생성
        List<GetChatRoomsRes> chatRoomResList = getChatRoomsUseCase.findChatRooms(email);
        return ResponseEntity.ok(new Result<>(chatRoomResList, "채팅방 목록을 찾았습니다."));
    }
}

