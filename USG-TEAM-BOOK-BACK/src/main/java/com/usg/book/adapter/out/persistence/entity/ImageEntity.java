package com.usg.book.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;
    private String uploadFilename; // 사용자가 업로드한 파일명
    private String storeFilename; // 서버에 저장한 파일명
    private String gcsUrl; // 파일 저장 경로
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity; // 이미지, 책 N : 1

    @Builder
    public ImageEntity(String uploadFilename, String storeFilename, String gcsUrl, BookEntity bookEntity) {
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
        this.gcsUrl = gcsUrl;
        this.bookEntity = bookEntity;
    }
}
