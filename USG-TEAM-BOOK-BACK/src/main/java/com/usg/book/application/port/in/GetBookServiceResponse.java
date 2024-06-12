package com.usg.book.application.port.in;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBookServiceResponse {

    private String bookName;
    private String bookComment;
    private String bookPostName;
    private Integer bookPrice;
    private Integer bookRealPrice;
    private String nickname;
    private List<String> imageUrls;
    private String author;
    private String publisher;

    @Builder
    public GetBookServiceResponse(String bookName, String bookComment, String bookPostName, Integer bookPrice, Integer bookRealPrice, String nickname, List<String> imageUrls, String author, String publisher) {
        this.bookName = bookName;
        this.bookComment = bookComment;
        this.bookPostName = bookPostName;
        this.bookPrice = bookPrice;
        this.bookRealPrice = bookRealPrice;
        this.nickname = nickname;
        this.imageUrls = imageUrls;
        this.author = author;
        this.publisher = publisher;
    }
}
