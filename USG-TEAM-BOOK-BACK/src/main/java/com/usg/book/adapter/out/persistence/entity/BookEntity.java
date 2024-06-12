package com.usg.book.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "book")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookEntity extends BaseTime{

    @Id @GeneratedValue
    @Column(name = "book_id")
    private Long id;
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
    public BookEntity(String email, String bookName, Integer bookRealPrice, String author, String publisher, String bookPostName, String bookComment, Integer bookPrice, String isbn) {
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

