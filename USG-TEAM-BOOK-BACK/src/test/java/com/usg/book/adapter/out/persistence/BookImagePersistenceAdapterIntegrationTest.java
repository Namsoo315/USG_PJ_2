package com.usg.book.adapter.out.persistence;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import com.usg.book.adapter.out.persistence.entity.BookEntity;
import com.usg.book.adapter.out.persistence.entity.BookRepository;
import com.usg.book.adapter.out.persistence.entity.ImageEntity;
import com.usg.book.adapter.out.persistence.entity.ImageRepository;
import com.usg.book.domain.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
public class BookImagePersistenceAdapterIntegrationTest extends IntegrationExternalApiMockingTestSupporter {

    @Autowired
    private BookImagePersistenceAdapter bookImagePersistenceAdapter;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("어뎁터로 이미지를 DB에 저장한다.")
    void saveImageTest() {
        // given
        BookEntity bookEntity = BookEntity.builder().build();
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        MockMultipartFile imageFile
                = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);
        Image image = Image.builder()
                .storeFilename("storeFilename")
                .originalFilename("originalFilename")
                .gcsUrl("gcsUrl")
                .image(imageFile)
                .build();

        // when
        Long savedBookImageId = bookImagePersistenceAdapter.saveImage(image, savedBookEntity.getId());

        // then
        assertThat(savedBookImageId).isNotNull();
        assertThat(imageRepository.findById(savedBookImageId)).isNotEmpty();
    }

    @Test
    @DisplayName("책 엔티티가 없을 시 이미지 저장을 실패한다.")
    void saveImageFailTest() {
        // given
        BookEntity bookEntity = BookEntity.builder().build();
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        MockMultipartFile imageFile
                = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);
        Image image = Image.builder()
                .storeFilename("storeFilename")
                .originalFilename("originalFilename")
                .gcsUrl("gcsUrl")
                .image(imageFile)
                .build();

        // when // then
        assertThatThrownBy(() -> bookImagePersistenceAdapter.saveImage(image, (savedBookEntity.getId() + 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book Not Exist");
    }

    @Test
    @DisplayName("Book PK 를 이용해 이미지 gcsUrl 을 반환한다.")
    void getImageUrlsTest() {
        // given
        BookEntity savedBookEntity = createBookEntity();
        ImageEntity imageEntity1 = createImageEntity(savedBookEntity, "gcsUrl1");
        ImageEntity imageEntity2 = createImageEntity(savedBookEntity, "gcsUrl2");
        ImageEntity imageEntity3 = createImageEntity(savedBookEntity, "gcsUrl3");

        // when
        List<String> imageUrls = bookImagePersistenceAdapter.getImageUrls(savedBookEntity.getId());

        // then
        assertThat(imageUrls).hasSize(3)
                .contains("gcsUrl1", "gcsUrl2", "gcsUrl3");
    }

    @Test
    @DisplayName("Book PK 로 이미지 엔티티들을 조회한다.")
    void getImagesByBookIdTest() {
        // given
        BookEntity savedBookEntity = createBookEntity();
        ImageEntity imageEntity1 = createImageEntity(savedBookEntity, "gcsUrl");
        ImageEntity imageEntity2 = createImageEntity(savedBookEntity, "gcsUrl");
        ImageEntity imageEntity3 = createImageEntity(savedBookEntity, "gcsUrl");

        // when
        List<Image> findImages = bookImagePersistenceAdapter.getImagesByBookId(savedBookEntity.getId());

        // then
        assertThat(findImages).hasSize(3)
                .extracting("imageId")
                .contains(imageEntity1.getId(), imageEntity2.getId(), imageEntity3.getId());
    }

    @Test
    @DisplayName("이미지 PK 로 이미지 엔티티를 삭제한다.")
    void deleteImageTest() {
        // given
        BookEntity savedBookEntity = createBookEntity();
        ImageEntity imageEntity = createImageEntity(savedBookEntity, "gcsUrl");

        // when
        bookImagePersistenceAdapter.deleteImage(imageEntity.getId());

        // then
        Optional<ImageEntity> findImageOptional = imageRepository.findById(imageEntity.getId());
        assertThat(findImageOptional).isEmpty();
    }

    private BookEntity createBookEntity() {
        BookEntity bookEntity = BookEntity.builder().build();
        return bookRepository.save(bookEntity);
    }

    private ImageEntity createImageEntity(BookEntity bookEntity, String gcsUrl) {
        MockMultipartFile imageFile
                = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);
        ImageEntity imageEntity = ImageEntity.builder()
                .uploadFilename(imageFile.getOriginalFilename())
                .storeFilename("storeFilename")
                .gcsUrl(gcsUrl)
                .bookEntity(bookEntity)
                .build();
        return imageRepository.save(imageEntity);
    }
}
