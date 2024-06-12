package com.usg.book.adapter.out.persistence;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import com.usg.book.adapter.out.persistence.entity.BookEntity;
import com.usg.book.adapter.out.persistence.entity.BookRepository;
import com.usg.book.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
public class BookPersistenceAdapterIntegrationTest extends IntegrationExternalApiMockingTestSupporter {

    @Autowired
    private BookPersistenceAdapter bookPersistenceAdapter;
    @Autowired
    private BookRepository bookRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("어뎁터를 이용해 책을 DB에 저장한다.")
    void registerBookTest() {
        // given
        Book book = createBook();

        // when
        Long savedBookId = bookPersistenceAdapter.registerBook(book);

        // then
        assertThat(savedBookId).isNotNull();
        BookEntity findBookEntity = bookRepository.findById(savedBookId).get();
        assertThat(findBookEntity).isNotNull();
        assertThat(findBookEntity.getId()).isEqualTo(savedBookId);
    }

    @Test
    @DisplayName("책 PK를 이용해 책 엔티티를 조회하여 책 도메인으로 반환한다.")
    void findBookByIdTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistenceAdapter.registerBook(book);

        // when
        Book findBook = bookPersistenceAdapter.findBookById(savedBookId);

        // then
        assertThat(findBook.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("다른 책 PK를 이용해 책 엔티티를 조회하면 실패한다.")
    void findBookByIdFailTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistenceAdapter.registerBook(book);

        // when // then
        assertThatThrownBy(() -> bookPersistenceAdapter.findBookById((savedBookId) + 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book Not Exist");
    }

    @Test
    @DisplayName("책 PK 를 이용해 책 엔티티를 삭제한다.")
    void deleteByIdTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistenceAdapter.registerBook(book);

        // when
        bookPersistenceAdapter.deleteById(savedBookId);

        // then
        Optional<BookEntity> findBookEntityOptional = bookRepository.findById(savedBookId);
        assertThat(findBookEntityOptional).isEmpty();
    }

    @Test
    @DisplayName("책 PK 를 이용해 책 엔티티를 update 한다.")
    void updateBookTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistenceAdapter.registerBook(book);
        String bookPostName = "updateBookPostName";
        String bookComment = "updateBookComment";
        int bookPrice = 25000;
        em.flush();
        em.clear();

        // when
        bookPersistenceAdapter.updateBook(savedBookId, bookPostName, bookComment, bookPrice);

        // then
        BookEntity findBook = bookRepository.findById(savedBookId).get();
        assertThat(findBook.getBookPrice()).isEqualTo(bookPrice);
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
}
