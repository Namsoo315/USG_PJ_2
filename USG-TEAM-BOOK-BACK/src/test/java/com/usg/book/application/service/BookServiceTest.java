package com.usg.book.application.service;

import com.usg.book.IntegrationExternalApiMockingTestSupporter;
import com.usg.book.application.port.in.BookDeleteCommend;
import com.usg.book.application.port.in.BookRegisterCommend;
import com.usg.book.application.port.in.BookUpdateCommend;
import com.usg.book.application.port.in.GetBookServiceResponse;
import com.usg.book.application.port.out.BookImagePersistencePort;
import com.usg.book.application.port.out.BookPersistencePort;
import com.usg.book.application.port.out.MemberPersistencePort;
import com.usg.book.domain.Book;
import com.usg.book.domain.Image;
import com.usg.book.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;

public class BookServiceTest extends IntegrationExternalApiMockingTestSupporter {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookPersistencePort bookPersistencePort;
    @Autowired
    private MemberPersistencePort memberPersistencePort;
    @Autowired
    private BookImagePersistencePort bookImagePersistencePort;

    @Test
    @DisplayName("Commend 객체를 입력받아 책을 등록한다.")
    void registerBookTest() {
        // given
        BookRegisterCommend commend = bookRegisterCommend();

        // stub
        doNothing().when(bookISBNCheckAdapter).bookIsbnCheck(anyString(), anyInt());

        // when
        Long savedBookId = bookService.registerBook(commend);

        // then
        Book findBook = bookPersistencePort.findBookById(savedBookId);
        assertThat(findBook.getBookId()).isEqualTo(savedBookId);
    }

    @Test
    @DisplayName("책 PK 를 이용해 책의 상세 정보를 조회한다.")
    void getBookTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        Image image = createImage();
        bookImagePersistencePort.saveImage(image, savedBookId);
        Member member = Member.builder().email("email").nickname("nickname").build();
        memberPersistencePort.saveMember(member);

        // when
        GetBookServiceResponse response = bookService.getBook(savedBookId);

        // then
        assertThat(response.getNickname()).isEqualTo(member.getNickname());
        assertThat(response.getImageUrls()).hasSize(1);
    }

    @Test
    @DisplayName("Commend 객체를 입력받아 책을 수정한다.")
    void updateBookTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        BookUpdateCommend updateCommend = createBookUpdateCommend(savedBookId, book.getEmail());

        // when
        bookService.updateBook(updateCommend);

        // then
        Book findBook = bookPersistencePort.findBookById(savedBookId);
        assertThat(findBook.getBookId()).isEqualTo(savedBookId);
        assertThat(findBook.getBookPostName()).isEqualTo(updateCommend.getBookPostName());
    }

    @Test
    @DisplayName("책을 등록한 이메일과 요청한 이메일이 다르면 예외가 발생한다.")
    void updateBookFailTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        BookUpdateCommend updateCommend = createBookUpdateCommend(savedBookId, book.getEmail() + "X");

        // when // then
        assertThatThrownBy(() -> bookService.updateBook(updateCommend))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("You are not authorized to update this book.");
    }

    @Test
    @DisplayName("Commend 객체를 입력받아 책을 삭제한다.")
    void deleteBookTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        BookDeleteCommend deleteCommend = createBookDeleteCommend(savedBookId, book.getEmail());

        // when
        bookService.deleteBook(deleteCommend);

        // then
        assertThatThrownBy(() -> bookPersistencePort.findBookById(savedBookId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book Not Exist");
    }

    @Test
    @DisplayName("책을 등록한 이메일과 요청한 이메일이 다르면 예외가 발생한다.")
    void deleteBookFailTest() {
        // given
        Book book = createBook();
        Long savedBookId = bookPersistencePort.registerBook(book);
        BookDeleteCommend deleteCommend = createBookDeleteCommend(savedBookId, book.getEmail() + "X");

        // when // then
        assertThatThrownBy(() -> bookService.deleteBook(deleteCommend))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("You are not authorized to update this book.");
    }

    private BookRegisterCommend bookRegisterCommend() {
        return BookRegisterCommend.builder()
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

    private Image createImage() {
        MockMultipartFile imageFile
                = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);
        return Image.builder()
                .storeFilename("storeFilename")
                .originalFilename("originalFilename")
                .gcsUrl("gcsUrl")
                .image(imageFile)
                .build();
    }

    private BookUpdateCommend createBookUpdateCommend(Long bookId, String email) {
        return BookUpdateCommend.builder()
                .bookId(bookId)
                .email(email)
                .bookPostName("updateBookPostName")
                .bookComment("updateBookCommend")
                .bookPrice(20000)
                .build();
    }

    private BookDeleteCommend createBookDeleteCommend(Long bookId, String email) {
        return BookDeleteCommend.builder()
                .bookId(bookId)
                .email(email)
                .build();
    }
}
