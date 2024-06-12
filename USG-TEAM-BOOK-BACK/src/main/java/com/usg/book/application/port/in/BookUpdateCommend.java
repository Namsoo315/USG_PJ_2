package com.usg.book.application.port.in;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookUpdateCommend {

    private Long bookId;
    private String email; // 어떤 사용자가 책을 업데이트하는지 알기 위함
    private String bookPostName; // 게시글 이름
    private String bookComment; // 책 코멘트
    private Integer bookPrice; // 책 가격
    

    @Builder
    public  BookUpdateCommend(String email, String bookPostName, String bookComment, Integer bookPrice, Long bookId) {
        this.email = email;
        this.bookPostName = bookPostName;
        this.bookComment = bookComment;
        this.bookPrice = bookPrice;
        this.bookId = bookId;
    }
}
