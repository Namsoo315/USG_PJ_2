package com.usg.book.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usg.book.adapter.out.persistence.entity.BookEntity;
import com.usg.book.domain.Book;

public interface BookPersistencePort {

    Long registerBook(Book book);
    Book findBookById(Long bookId);
    void deleteById(Long bookId);
    Page<BookEntity> findAll(Pageable pageable);
    void updateBook(Long bookId, String bookPostName, String bookComment, Integer bookPrice);
}
