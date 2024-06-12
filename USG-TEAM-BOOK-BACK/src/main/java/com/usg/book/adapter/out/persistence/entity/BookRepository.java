package com.usg.book.adapter.out.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<BookEntity, Long> {


    @Modifying
    @Query("UPDATE BookEntity b" +
            " SET b.bookPostName = :bookPostName, b.bookComment = :bookComment, b.bookPrice = :bookPrice" +
            " where b.id = :bookId")
    int updateBookByBookId(@Param("bookId") Long bookId,
                           @Param("bookPostName") String bookPostName,
                           @Param("bookComment") String bookComment,
                           @Param("bookPrice") Integer bookPrice);
}
