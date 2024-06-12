package com.usg.book.adapter.out.persistence;

import com.usg.book.adapter.out.persistence.entity.BookRepository;
import com.usg.book.adapter.out.persistence.entity.BookEntity;
import com.usg.book.application.port.out.BookPersistencePort;
import com.usg.book.domain.Book;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookPersistenceAdapter implements BookPersistencePort {

    private final BookRepository bookRepository;

    @Override
    public Long registerBook(Book book) {
        BookEntity bookEntity = bookToBookIdNotContainEntity(book);

        BookEntity savedBookEntity = bookRepository.save(bookEntity);

        return savedBookEntity.getId();
    }

    @Override
    public Book findBookById(Long bookId) {

        BookEntity findBookEntity = bookRepository.findById(bookId).orElseThrow(
                () -> new IllegalArgumentException("Book Not Exist")
        );

        return bookEntityToBook(findBookEntity);
    }

    @Override
    public void deleteById(Long bookId){
        bookRepository.deleteById(bookId);
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable){
        return bookRepository.findAll(pageable);
    }

    @Override
    public void updateBook(Long bookId, String bookPostName, String bookComment, Integer bookPrice) {

        bookRepository.updateBookByBookId(bookId, bookPostName, bookComment, bookPrice);
    }

    private BookEntity bookToBookIdNotContainEntity(Book book) {
        return BookEntity
                .builder()
                .email(book.getEmail())
                .bookName(book.getBookName())
                .bookRealPrice(book.getBookRealPrice())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .bookPostName(book.getBookPostName())
                .bookComment(book.getBookComment())
                .bookPrice(book.getBookPrice())
                .isbn(book.getIsbn())
                .build();
    }

    private Book bookEntityToBook(BookEntity bookEntity) {
        return Book.builder()
                .bookId(bookEntity.getId())
                .email(bookEntity.getEmail())
                .bookName(bookEntity.getBookName())
                .bookRealPrice(bookEntity.getBookRealPrice())
                .author(bookEntity.getAuthor())
                .publisher(bookEntity.getPublisher())
                .bookPostName(bookEntity.getBookPostName())
                .bookComment(bookEntity.getBookComment())
                .bookPrice(bookEntity.getBookPrice())
                .isbn(bookEntity.getIsbn())
                .build();
    }
}
