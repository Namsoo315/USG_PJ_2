package com.usg.book.application.port.in;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookImageUploadUseCase {

    void saveImages(List<MultipartFile> images, Long bookId);
}
