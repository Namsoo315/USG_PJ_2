package com.usg.book.adapter.in.web.dto;


import com.usg.book.adapter.out.persistence.entity.BookEntity;
import com.usg.book.adapter.out.persistence.entity.ImageEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookAllResponse {

    private String bookName;
    private String bookPostName;
    private Integer bookPrice;
    private Integer bookRealPrice;
    private List<String> imageUrls;
    private String author;
    private String publisher;

    @Builder
    public BookAllResponse(String bookName, String bookPostName, Integer bookPrice, Integer bookRealPrice, List<String> imageUrls, String author, String publisher) {
        this.bookName = bookName;
        this.bookPostName = bookPostName;
        this.bookPrice = bookPrice;
        this.bookRealPrice = bookRealPrice;
        this.imageUrls = imageUrls;
        this.author = author;
        this.publisher = publisher;
    }
}


