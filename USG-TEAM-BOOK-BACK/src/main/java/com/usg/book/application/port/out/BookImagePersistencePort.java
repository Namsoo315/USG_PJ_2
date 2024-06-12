package com.usg.book.application.port.out;

import com.usg.book.domain.Image;

import java.util.List;

public interface BookImagePersistencePort {

    Long saveImage(Image image, Long bookId);
    List<String> getImageUrls(Long bookId);
    List<Image> getImagesByBookId(Long bookId);
    void deleteImage(Long imageId);
}
