package com.usg.book.application.service;

import com.usg.book.application.port.in.BookImageDeleteUseCase;
import com.usg.book.application.port.in.BookImageUpdateUseCase;
import com.usg.book.application.port.in.BookImageUploadUseCase;
import com.usg.book.application.port.out.BookImageGcsPort;
import com.usg.book.application.port.out.BookImagePersistencePort;
import com.usg.book.domain.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookImageService implements BookImageUploadUseCase, BookImageDeleteUseCase, BookImageUpdateUseCase{

    private final BookImagePersistencePort bookImagePersistencePort;
    private final BookImageGcsPort bookImageGcsPort;

    @Override
    @Transactional
    public void saveImages(List<MultipartFile> images, Long bookId) {

        if (images.size() > 10) {
            throw new IllegalArgumentException("Image Capacity Exceeded");
        }

        for (MultipartFile imageFile : images) {
            if (!imageFile.isEmpty()) {

                String originalFilename = imageFile.getOriginalFilename();
                String storeFilename = createStoreFilename(originalFilename);

                Image image = Image.builder()
                        .image(imageFile)
                        .bookId(bookId)
                        .storeFilename(storeFilename)
                        .originalFilename(originalFilename)
                        .build();

                String gcsUrl = bookImageGcsPort.uploadImage(image);

                image.setGcsUrl(gcsUrl);

                bookImagePersistencePort.saveImage(image, bookId);
            }
        }
    }

    private String createStoreFilename(String originalFilename) {
        // 서버에 저장하는 관리명
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1); // 확장자 .png가져오기
    }


    @Override
    @Transactional
    public void deleteImages(Long bookId) {
        List<Image> findImages = bookImagePersistencePort.getImagesByBookId(bookId);

        for (Image images : findImages) {

            String gcsImageUrl = images.getGcsUrl();
            if (gcsImageUrl != null) {
                bookImageGcsPort.deleteImage(gcsImageUrl);
            }

            bookImagePersistencePort.deleteImage(images.getImageId());
        }
    }

    @Override
    @Transactional
    public void updateImages(List<MultipartFile> images, Long bookId) {

        if (images.size() > 10) {
            throw new IllegalArgumentException("Image Capacity Exceeded");
        }

        //책과 연관된 이미지를 삭제
        deleteImages(bookId);

        // 새로운 이미지를 업로드하고 저장
        saveImages(images, bookId);
    }
}
