package com.usg.book.application.port.in;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookImageDeleteUseCase {
    
    void deleteImages(Long bookId);
}
