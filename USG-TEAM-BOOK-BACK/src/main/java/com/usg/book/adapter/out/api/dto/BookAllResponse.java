package com.usg.book.adapter.out.api.dto;

import com.usg.book.adapter.out.persistence.entity.BookEntity;
import com.usg.book.adapter.out.persistence.entity.ImageEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAllResponse {
    private BookEntity book;
    private List<String> image;
    public static BookAllResponse toDto(BookEntity book,List<ImageEntity> image){
        List<String> url=new ArrayList<>();
        for (ImageEntity images:image) {
            url.add(images.getGcsUrl());
        }
        return BookAllResponse.builder()
                .book(book)
                .image(url)
                .build();
    }
}

