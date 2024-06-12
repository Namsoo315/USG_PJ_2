package com.usg.book.adapter.in.web.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
public class BookUpdateRequest {
     private String bookPostName; 
    private String bookComment; 
    private Integer bookPrice; 
    private List<MultipartFile> images = new ArrayList<>(); 

    @Builder
    public BookUpdateRequest(String bookPostName, String bookComment, Integer bookPrice, List<MultipartFile> images) {
        this.bookPostName = bookPostName;
        this.bookComment = bookComment;
        this.bookPrice = bookPrice;
        this.images = images;
    }
}