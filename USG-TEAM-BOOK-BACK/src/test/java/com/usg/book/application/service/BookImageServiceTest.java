package com.usg.book.application.service;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import com.usg.book.application.port.out.BookImagePersistencePort;
import com.usg.book.application.port.out.BookPersistencePort;
import com.usg.book.domain.Book;
import com.usg.book.domain.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class BookImageServiceTest extends IntegrationExternalApiMockingTestSupporter {

    @Autowired
    private BookImageService bookImageService;
    @Autowired
    private BookPersistencePort bookPersistencePort;
    @Autowired
    private BookImagePersistencePort bookImagePersistencePort;

    @Test
    @DisplayName("이미지 파일들과 책 PK 로 이미지들을 저장한다.")
    void saveImagesTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        List<MultipartFile> imageLists = createImageLists(10);

        // stub
        doReturn("gcsUrl").when(imageGcsAdapter).uploadImage(any(Image.class));

        // when
        bookImageService.saveImages(imageLists, savedBookId);

        // then
        List<Image> findImages = bookImagePersistencePort.getImagesByBookId(savedBookId);
        assertThat(findImages).hasSize(10)
                .extracting("gcsUrl", "bookId")
                .containsExactlyInAnyOrder(
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId),
                        tuple("gcsUrl", savedBookId)
                );
    }

    @Test
    @DisplayName("이미지 파일 개수가 10개 보다 많으면 예외가 발생한다.")
    void saveImageFailTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        List<MultipartFile> imageLists = createImageLists(11);

        // stub
        doReturn("gcsUrl").when(imageGcsAdapter).uploadImage(any(Image.class));

        // when // then
        assertThatThrownBy(() -> bookImageService.saveImages(imageLists, savedBookId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Image Capacity Exceeded");
    }

    @Test
    @DisplayName("책 PK 를 이용해 연관된 이미지들을 삭제한다.")
    void deleteImagesTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        List<MultipartFile> imageLists = createImageLists(1);

        // stub
        doNothing().when(imageGcsAdapter).deleteImage(anyString());

        // when
        bookImageService.deleteImages(savedBookId);

        // then
        assertThat(bookImagePersistencePort.getImagesByBookId(savedBookId)).isEmpty();
    }

    @Test
    @DisplayName("이미지 파일과 책 PK 를 이용해 이미지를 수정한다.")
    void updateImagesTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        List<MultipartFile> imageList = createImageLists(1);
        bookImageService.saveImages(imageList, savedBookId);
        List<MultipartFile> updateImageList = createImageLists(10);

        // stub
        doReturn("gcsUrl").when(imageGcsAdapter).uploadImage(any(Image.class));
        doNothing().when(imageGcsAdapter).deleteImage(anyString());

        // when
        bookImageService.updateImages(updateImageList, savedBookId);

        // then
        List<Image> findImages = bookImagePersistencePort.getImagesByBookId(savedBookId);
        assertThat(findImages).hasSize(10);
    }

    private Book createBook() {
        return Book.builder()
                .email("email")
                .bookName("bookName")
                .bookRealPrice(30000)
                .author("author")
                .publisher("publisher")
                .bookPostName("bookPostName")
                .bookComment("bookComment")
                .bookPrice(28000)
                .isbn("isbn")
                .build();
    }

    private List<MultipartFile> createImageLists(int loop) {
        List<MultipartFile> imageFiles = new ArrayList<>();

        for (int i = 0; i < loop; i++) {
            MockMultipartFile imageFile
                    = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);

            imageFiles.add(imageFile);
        }

        return imageFiles;
    }
}
