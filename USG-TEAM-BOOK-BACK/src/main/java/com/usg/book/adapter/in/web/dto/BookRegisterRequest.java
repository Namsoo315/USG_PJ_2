package com.usg.book.adapter.in.web.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class BookRegisterRequest {

    private String bookName; // 책 이름
    private Integer bookRealPrice; // 책 원가
    private String author; // 저자
    private String publisher; // 출판사
    private String bookPostName; // 책 등록 시 게시글 이름
    private String bookComment; // 상세정보
    private Integer bookPrice; // 책 판매가
    private String isbn; // ISBN
    private List<MultipartFile> images = new ArrayList<>(); // 이미지

    @Builder
    public BookRegisterRequest(String bookName, Integer bookRealPrice, String author, String publisher, String bookPostName, String bookComment, Integer bookPrice, String isbn, List<MultipartFile> images) {
        this.bookName = bookName;
        this.bookRealPrice = bookRealPrice;
        this.author = author;
        this.publisher = publisher;
        this.bookPostName = bookPostName;
        this.bookComment = bookComment;
        this.bookPrice = bookPrice;
        this.isbn = isbn;
        this.images = images;
    }
}
