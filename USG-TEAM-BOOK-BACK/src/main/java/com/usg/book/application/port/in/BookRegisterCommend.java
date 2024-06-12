package com.usg.book.application.port.in;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRegisterCommend {

    // Request 가 아닌 Commend 에서 유효성 검사를 해야된다
    // https://velog.io/@msung99/Hexagonal-Architecture-%ED%97%A5%EC%82%AC%EA%B3%A0%EB%82%A0-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98%EC%97%90%EC%84%9C-%EC%9C%A0%EC%A6%88%EC%BC%80%EC%9D%B4%EC%8A%A4UserCase-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0

    private String email; // 어떤 사용자가 책을 등록했는지 알기 위함
    private String bookName; // 책 이름
    private Integer bookRealPrice; // 책 원가
    private String author; // 저자
    private String publisher; // 출판사
    private String bookPostName; // 책 등록 시 게시글 이름
    private String bookComment; // 상세정보
    private Integer bookPrice; // 책 판매가
    private String isbn; // ISBN

    @Builder
    public BookRegisterCommend(String email, String bookName, Integer bookRealPrice, String author, String publisher, String bookPostName, String bookComment, Integer bookPrice, String isbn) {
        this.email = email;
        this.bookName = bookName;
        this.bookRealPrice = bookRealPrice;
        this.author = author;
        this.publisher = publisher;
        this.bookPostName = bookPostName;
        this.bookComment = bookComment;
        this.bookPrice = bookPrice;
        this.isbn = isbn;
    }
}
