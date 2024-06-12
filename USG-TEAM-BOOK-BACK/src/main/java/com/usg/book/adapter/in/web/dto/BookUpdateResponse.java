package com.usg.book.adapter.in.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookUpdateResponse {
    private Long updatedBookId;

    @Builder
    public BookUpdateResponse(Long updatedBookId) {
        this.updatedBookId = updatedBookId;
    }
}
