package com.usg.book.application.service;

import com.usg.book.adapter.out.api.dto.BookAllResponse;
import com.usg.book.adapter.out.persistence.entity.BookEntity;
import com.usg.book.adapter.out.persistence.entity.BookRepository;
import com.usg.book.adapter.out.persistence.entity.ImageEntity;
import com.usg.book.adapter.out.persistence.entity.ImageRepository;
import com.usg.book.application.port.in.BookDeleteCommend;
import com.usg.book.application.port.in.BookDeleteUseCase;
import com.usg.book.application.port.in.BookRegisterCommend;
import com.usg.book.application.port.in.BookRegisterUseCase;
import com.usg.book.application.port.in.BookUpdateCommend;
import com.usg.book.application.port.in.BookUpdateUseCase;
import com.usg.book.application.port.in.BookAllServiceResponse;
import com.usg.book.application.port.in.BookAllUseCase;
import com.usg.book.application.port.in.GetBookServiceResponse;
import com.usg.book.application.port.in.GetBookUseCase;
import com.usg.book.application.port.out.BookISBNCheckPort;
import com.usg.book.application.port.out.BookImagePersistencePort;
import com.usg.book.application.port.out.BookPersistencePort;
import com.usg.book.application.port.out.MemberPersistencePort;
import com.usg.book.domain.Book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService implements BookRegisterUseCase, GetBookUseCase, BookUpdateUseCase, BookDeleteUseCase ,BookAllUseCase{

    private final BookPersistencePort bookPersistencePort;
    private final BookISBNCheckPort bookISBNCheckPort;
    private final BookImagePersistencePort bookImagePersistencePort;
    private final BookRepository bookRepository;
    private final ImageRepository imageRepository;
    private final MemberPersistencePort memberPersistencePort;


    @Override
    @Transactional
    public Long registerBook(BookRegisterCommend commend) {

        // 중앙 도서 API 연동
        bookISBNCheckPort.bookIsbnCheck(commend.getIsbn(), commend.getBookRealPrice());

        // 책 데이터 저장
        Book book = commendToBook(commend);
        Long savedBookId = bookPersistencePort.registerBook(book);

        return savedBookId;
    }

    private Book commendToBook(BookRegisterCommend commend) {
        return Book
                .builder()
                .email(commend.getEmail())
                .bookName(commend.getBookName())
                .bookRealPrice(commend.getBookRealPrice())
                .author(commend.getAuthor())
                .publisher(commend.getPublisher())
                .bookPostName(commend.getBookPostName())
                .bookComment(commend.getBookComment())
                .bookPrice(commend.getBookPrice())
                .isbn(commend.getIsbn())
                .build();
    }

    @Override
    public GetBookServiceResponse getBook(Long bookId) {
        Book findBook = bookPersistencePort.findBookById(bookId);
        List<String> imageUrls = bookImagePersistencePort.getImageUrls(bookId);

        // redis 를 이용해 닉네임 알아오기
        String nickname = memberPersistencePort.getNicknameByEmail(findBook.getEmail());

        return GetBookServiceResponse
                .builder()
                .bookName(findBook.getBookName())
                .bookComment(findBook.getBookComment())
                .bookPostName(findBook.getBookPostName())
                .bookPrice(findBook.getBookPrice())
                .bookRealPrice(findBook.getBookRealPrice())
                .nickname(nickname)
                .imageUrls(imageUrls)
                .author(findBook.getAuthor())
                .publisher(findBook.getPublisher())
                .build();

    }

    public Page<BookAllResponse> findAll(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 20;

        Page<BookEntity> bookPages= bookRepository.findAll(PageRequest.of(page,pageLimit, Sort.by(Sort.Direction.DESC,"id")));
        List<BookEntity> books=bookPages.stream().toList();

        List<BookAllResponse> bookAllResponseList = new ArrayList<>();
        for (BookEntity book:books) {
            List<ImageEntity> image=imageRepository.findByBookEntity(book);
            BookAllResponse bookAllResponse = BookAllResponse.toDto(book, image);
            bookAllResponseList.add(bookAllResponse);
        }


        return new PageImpl<>(bookAllResponseList, pageable, bookPages.getTotalElements());

    }

//    @Override
//    public Page<BookAllServiceResponse> findAll(Pageable pageable) {
//        int page = pageable.getPageNumber() - 1;
//        int pageLimit = 20;
//
//        Page<BookEntity> bookPages= bookPersistencePort.findAll(PageRequest.of(page,pageLimit, Sort.by(Sort.Direction.DESC,"id")));
//        List<BookEntity> books=bookPages.stream().toList();
//
//        List<BookAllServiceResponse> bookAllResponseList = new ArrayList<>();
//
//        for (BookEntity book:books) {
//            //List<ImageEntity> image = bookImagePersistencePort.findByBookEntity(book);
//            // BookAllResponse bookAllResponse = BookAllResponse.toDto(book, image);
//            // bookAllResponseList.add(bookAllResponse);
//
//            List<String> imageUrls = bookImagePersistencePort.getImageUrls(book.getId());
//            BookAllServiceResponse bookAllServiceResponse = BookAllServiceResponse.builder()
//                .bookName(book.getBookName())
//                .bookPostName(book.getBookPostName())
//                .bookPrice(book.getBookPrice())
//                .bookRealPrice(book.getBookRealPrice())
//                .imageUrls(imageUrls)
//                .author(book.getAuthor())
//                .publisher(book.getPublisher())
//                .build();
//
//            bookAllResponseList.add(bookAllServiceResponse);
//        }
//        return new PageImpl<>(bookAllResponseList, pageable, bookPages.getTotalElements());
//    }
    
    @Override
    @Transactional
    public Long updateBook(BookUpdateCommend bookUpdateCommend) {

        // BookPersistencePort 를 사용하여 책을 찾아 업데이트
        Book findBook = bookPersistencePort.findBookById(bookUpdateCommend.getBookId());

         // 책의 소유자 이메일과 요청에서 받은 이메일이 일치하는지 확인
        findBook.checkSelfEmail(bookUpdateCommend.getEmail());

        bookPersistencePort.updateBook(
                findBook.getBookId(),
                bookUpdateCommend.getBookPostName(),
                bookUpdateCommend.getBookComment(),
                bookUpdateCommend.getBookPrice()
        );

        return findBook.getBookId();
    }

    @Override
    @Transactional
    public void deleteBook(BookDeleteCommend bookDeleteCommend) {

        Book findBook = bookPersistencePort.findBookById(bookDeleteCommend.getBookId());
        // 소유자의 경우에만 삭제 수행
        findBook.checkSelfEmail(bookDeleteCommend.getEmail());

        bookPersistencePort.deleteById(bookDeleteCommend.getBookId());
    }
}

