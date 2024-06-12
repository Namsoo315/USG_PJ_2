package com.usg.book.adapter.out.persistence.entity;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ImageRepositoryTest extends IntegrationExternalApiMockingTestSupporter {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("책 엔티티를 저장한 뒤 이미지 엔티티를 저장한다.")
    void saveTest() {
        // given
        String email = "email";
        String bookName = "bookName";
        String bookComment = "bookComment";
        String bookPostName = "bookPostName";
        Integer bookPrice = 10;
        String isbn = "isbn";
        BookEntity bookEntity = createBookEntity(email, bookName, bookComment, bookPostName, bookPrice, isbn);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        String uploadFilename = "uploadFilename";
        String storeFilename = "storeFilename";
        String gcsUrl = "gcsUrl";

        ImageEntity imageEntity = createImageEntity(uploadFilename, storeFilename, gcsUrl, savedBookEntity);

        // when
        ImageEntity savedImageEntity = imageRepository.save(imageEntity);

        // then
        assertThat(savedImageEntity).isEqualTo(imageEntity);
        assertThat(savedImageEntity.getId()).isEqualTo(imageEntity.getId());
        assertThat(savedImageEntity.getStoreFilename()).isEqualTo(imageEntity.getStoreFilename());
        assertThat(savedImageEntity.getUploadFilename()).isEqualTo(imageEntity.getUploadFilename());
        assertThat(savedImageEntity.getGcsUrl()).isEqualTo(imageEntity.getGcsUrl());
        assertThat(savedImageEntity.getBookEntity()).isEqualTo(imageEntity.getBookEntity());
    }

    @Test
    @DisplayName("이미지 PK 로 이미지 엔티티를 조회한다.")
    void findByBookIdTest() {
        // given
        String email = "email";
        String bookName = "bookName";
        String bookComment = "bookComment";
        String bookPostName = "bookPostName";
        Integer bookPrice = 10;
        String isbn = "isbn";
        BookEntity bookEntity = createBookEntity(email, bookName, bookComment, bookPostName, bookPrice, isbn);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        String uploadFilename = "uploadFilename";
        String storeFilename = "storeFilename";
        String gcsUrl = "gcsUrl";
        ImageEntity imageEntity = createImageEntity(uploadFilename, storeFilename, gcsUrl, savedBookEntity);
        ImageEntity savedImageEntity = imageRepository.save(imageEntity);

        // when
        List<ImageEntity> findImageEntities = imageRepository.findByBookId(savedBookEntity.getId());

        // then
        assertThat(findImageEntities).hasSize(1)
                .extracting("gcsUrl", "id")
                .containsExactlyInAnyOrder(
                        tuple("gcsUrl", savedImageEntity.getId())
                );
    }

    @Test
    @DisplayName("책 PK로 이미지 엔티티를 조회한다.")
    void findImagesByBookIdJoinFetchTest() {
        // given
        String email = "email";
        String bookName = "bookName";
        String bookComment = "bookComment";
        String bookPostName = "bookPostName";
        Integer bookPrice = 10;
        String isbn = "isbn";
        BookEntity bookEntity = createBookEntity(email, bookName, bookComment, bookPostName, bookPrice, isbn);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        String uploadFilename = "uploadFilename";
        String storeFilename = "storeFilename";
        String gcsUrl = "gcsUrl";
        ImageEntity imageEntity1 = createImageEntity(uploadFilename, storeFilename, gcsUrl, savedBookEntity);
        ImageEntity savedImageEntity1 = imageRepository.save(imageEntity1);
        ImageEntity imageEntity2 = createImageEntity(uploadFilename, storeFilename, gcsUrl, savedBookEntity);
        ImageEntity savedImageEntity2 = imageRepository.save(imageEntity2);

        // when
        List<ImageEntity> findImageEntities = imageRepository.findImagesByBookIdJoinFetch(savedBookEntity.getId());

        // then
        assertThat(findImageEntities).hasSize(2)
                .extracting("gcsUrl", "id")
                .containsExactlyInAnyOrder(
                        tuple("gcsUrl", savedImageEntity1.getId()),
                        tuple("gcsUrl", savedImageEntity2.getId())
                );
    }

    private BookEntity createBookEntity(String email,
                                        String bookName,
                                        String bookComment,
                                        String bookPostName,
                                        Integer bookPrice,
                                        String isbn) {
        return BookEntity
                .builder()
                .email(email)
                .bookName(bookName)
                .bookComment(bookComment)
                .bookPostName(bookPostName)
                .bookPrice(bookPrice)
                .isbn(isbn)
                .build();
    }

    private ImageEntity createImageEntity(String uploadFilename,
                                          String storeFilename,
                                          String gcsUrl,
                                          BookEntity bookEntity) {
        return ImageEntity
                .builder()
                .uploadFilename(uploadFilename)
                .storeFilename(storeFilename)
                .gcsUrl(gcsUrl)
                .bookEntity(bookEntity)
                .build();
    }
}
