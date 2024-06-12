package com.usg.book.application.port.out;

import com.usg.book.domain.Image;

public interface BookImageGcsPort {

    String uploadImage(Image image) ;
    void deleteImage(String imageUrl);
}
