package com.usg.chat.adapter.in.web;

import com.usg.chat.adapter.in.web.dto.Result;
import com.usg.chat.adapter.in.web.token.MemberEmailGetter;
import com.usg.chat.adapter.out.persistence.entity.Chat.ChatEntity;
import com.usg.chat.application.port.in.Chat.GetMessageHistoryRes;
import com.usg.chat.application.port.in.Chat.GetMessageHistoryUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetMessageApiController {
    private final GetMessageHistoryUseCase getMessageHistoryUseCase;
    private final MemberEmailGetter memberEmailGetter;

    @GetMapping("/api/messages")
    public ResponseEntity<Result<List<ChatEntity>>> getMessages(@RequestParam String receiverEmail,
                                                                HttpServletRequest servletRequest){
        //로그인 한 회원 찾기
        String senderEmail = memberEmailGetter.getMemberEmail(servletRequest.getHeader("Authorization"));

        List<GetMessageHistoryRes> Messages = getMessageHistoryUseCase.getMessages(senderEmail,receiverEmail);

        return ResponseEntity.ok(new Result(Messages));
    }
}