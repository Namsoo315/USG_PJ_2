package com.usg.book.application.port.in;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BookAllServiceResponse {
    private String bookName;
    private String bookPostName;
    private Integer bookPrice;
    private Integer bookRealPrice;
    private List<String> imageUrls;
    private String author;
    private String publisher;

    @Builder
    public BookAllServiceResponse(String bookName, String bookPostName, Integer bookPrice, Integer bookRealPrice, List<String> imageUrls, String author, String publisher) {
        this.bookName = bookName;
        this.bookPostName = bookPostName;
        this.bookPrice = bookPrice;
        this.bookRealPrice = bookRealPrice;
        this.imageUrls = imageUrls;
        this.author = author;
        this.publisher = publisher;
    }
}
