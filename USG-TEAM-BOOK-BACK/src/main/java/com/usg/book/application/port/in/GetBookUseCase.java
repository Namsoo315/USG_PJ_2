package com.usg.book.application.port.in;

public interface GetBookUseCase {

    GetBookServiceResponse getBook(Long bookId);
}
