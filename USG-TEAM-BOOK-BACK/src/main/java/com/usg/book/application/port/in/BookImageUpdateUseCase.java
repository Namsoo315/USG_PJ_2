package com.usg.book.application.port.in;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface BookImageUpdateUseCase {
    void updateImages(List<MultipartFile> images, Long bookId);
}
