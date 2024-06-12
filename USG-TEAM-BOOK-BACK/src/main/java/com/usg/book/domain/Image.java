package com.usg.book.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    private MultipartFile image;
    private Long imageId;
    private Long bookId;
    private String gcsUrl;
    private String storeFilename;
    private String originalFilename;

    @Builder
    public Image(MultipartFile image, Long imageId, Long bookId, String gcsUrl, String storeFilename, String originalFilename) {
        this.image = image;
        this.imageId = imageId;
        this.bookId = bookId;
        this.gcsUrl = gcsUrl;
        this.storeFilename = storeFilename;
        this.originalFilename = originalFilename;
    }

    public void setGcsUrl(String gcsUrl) {
        this.gcsUrl = gcsUrl;
    }
}
