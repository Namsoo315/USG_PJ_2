package com.usg.book.adapter.in.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRegisterResponse {

    private Long savedBookId;

    @Builder
    public BookRegisterResponse(Long savedBookId) {
        this.savedBookId = savedBookId;
    }
}
