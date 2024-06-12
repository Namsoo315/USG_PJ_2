package com.usg.book.adapter.out.persistence.entity;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class BookRepositoryTest extends IntegrationExternalApiMockingTestSupporter {

    @Autowired
    private BookRepository bookRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("책 엔티티를 데이터베이스에 저장한다.")
    void saveTest() {
        // given
        BookEntity bookEntity = createBookEntity();

        // when
        BookEntity savedBook = bookRepository.save(bookEntity);

        // then
        assertThat(savedBook).isEqualTo(bookEntity);
        assertThat(savedBook.getId()).isEqualTo(bookEntity.getId());
        assertThat(savedBook.getEmail()).isEqualTo(bookEntity.getEmail());
        assertThat(savedBook.getBookName()).isEqualTo(bookEntity.getBookName());
        assertThat(savedBook.getBookRealPrice()).isEqualTo(bookEntity.getBookRealPrice());
        assertThat(savedBook.getAuthor()).isEqualTo(bookEntity.getAuthor());
        assertThat(savedBook.getPublisher()).isEqualTo(bookEntity.getPublisher());
        assertThat(savedBook.getBookPostName()).isEqualTo(bookEntity.getBookPostName());
        assertThat(savedBook.getBookComment()).isEqualTo(bookEntity.getBookComment());
        assertThat(savedBook.getBookPrice()).isEqualTo(bookEntity.getBookPrice());
        assertThat(savedBook.getIsbn()).isEqualTo(bookEntity.getIsbn());
    }

    @Test
    @DisplayName("책 PK 로 게시글 제목, 상세내용, 가격을 수정한다.")
    void updateBookByBookIdTest() {
        // given
        String bookPostName = "updateBookPostName";
        String bookComment = "updateBookComment";
        int bookPrice = 25000;
        BookEntity bookEntity = createBookEntity();
        BookEntity savedBook = bookRepository.save(bookEntity);

        em.flush();
        em.clear();

        // when
        int updateBookCount = bookRepository.updateBookByBookId(savedBook.getId(), bookPostName, bookComment, bookPrice);

        // then
        assertThat(updateBookCount).isEqualTo(1);
        BookEntity findBookEntity = bookRepository.findById(savedBook.getId()).get();
        assertThat(findBookEntity.getBookPostName()).isEqualTo(bookPostName);
        assertThat(findBookEntity.getBookComment()).isEqualTo(bookComment);
        assertThat(findBookEntity.getBookPrice()).isEqualTo(bookPrice);
    }

    private BookEntity createBookEntity() {
        return BookEntity
                .builder()
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
}
