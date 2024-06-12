package com.usg.book.application.port.in;

import lombok.Builder;
import lombok.Data;

@Data
public class BookDeleteCommend {
    private String email; // 어떤 사용자가 책을 삭제하는지 알기 위함
    private Long bookId; // 삭제할 책의 고유 ID

    @Builder
    public BookDeleteCommend(String email, Long bookId) {
        this.email = email;
        this.bookId = bookId;
    }
}
