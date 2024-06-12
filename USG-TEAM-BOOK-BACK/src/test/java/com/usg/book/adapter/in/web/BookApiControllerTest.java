package com.usg.book.adapter.in.web;

import com.usg.book.adapter.in.web.token.JWTClaimDecoder;
import com.usg.book.adapter.in.web.token.MemberEmailGetter;
import com.usg.book.application.port.in.BookDeleteCommend;
import com.usg.book.application.port.in.BookUpdateCommend;
import com.usg.book.application.port.in.GetBookServiceResponse;
import com.usg.book.application.service.BookImageService;
import com.usg.book.application.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        BookApiController.class,
        JWTClaimDecoder.class,
        MemberEmailGetter.class
})
public class BookApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private BookImageService bookImageService;

    @Test
    @DisplayName("책 등록 API 의 입력값을 확인한다.")
    void registerBookTest() throws Exception {
        // given
        String bookName = "bookName";
        Integer bookRealPrice = 100;
        String author = "author";
        String publisher = "publisher";
        String bookPostName = "bookPostName";
        String bookComment = "bookComment";
        Integer bookPrice = 10;
        String isbn = "isbn";
        String jwt = "Bearer " + MockJWTGenerator.generateToken("email");

        // when
        ResultActions perform = mockMvc.perform(multipart("/api/book")
                .file("images[0]", new byte[10])
                .param("bookName", bookName)
                .param("bookRealPrice", bookRealPrice.toString())
                .param("author", author)
                .param("publisher", publisher)
                .param("bookComment", bookComment)
                .param("bookPostName", bookPostName)
                .param("bookPrice", bookPrice.toString())
                .param("isbn", isbn)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", jwt));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("책 등록이 완료되었습니다."));
    }

    @Test
    @DisplayName("책 삭제 API 의 입력값을 확인한다.")
    void deleteBookTest() throws Exception {
        // given
        Long bookId = 1L;
        String jwt = "Bearer " + MockJWTGenerator.generateToken("email");

        // stub
        doNothing().when(bookImageService).deleteImages(anyLong());
        doNothing().when(bookService).deleteBook(any(BookDeleteCommend.class));

        // when
        ResultActions perform = mockMvc.perform(delete("/api/book/" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", jwt));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("책 삭제가 완료되었습니다."));
    }

    @Test
    @DisplayName("책 수정 API 의 입력값을 확인한다.")
    void updateBookTest() throws Exception {
        // given
        Long bookId = 1L;
        String jwt = "Bearer " + MockJWTGenerator.generateToken("email");

        // stub
        doNothing().when(bookImageService).updateImages(anyList(), anyLong());
        doReturn(bookId).when(bookService).updateBook(any(BookUpdateCommend.class));

        // when
        ResultActions perform = mockMvc.perform(multipart(HttpMethod.PUT, "/api/book/" + bookId)
                .file("images[0]", new byte[10])
                .param("bookPostName", "updateBookPostName")
                .param("bookComment", "updateBookComment")
                .param("bookPrice", "25000")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header("Authorization", jwt));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("책 수정이 완료되었습니다."));
    }

    @Test
    @DisplayName("책 상세조회 API 입력값을 확인한다.")
    void getBookTest() throws Exception {
        // given
        Long bookId = 1L;
        String jwt = "Bearer " + MockJWTGenerator.generateToken("email");

        // stub
        GetBookServiceResponse getBookServiceResponse = createGetBookResponse();
        doReturn(getBookServiceResponse).when(bookService).getBook(bookId);

        // when
        ResultActions perform = mockMvc.perform(get("/api/book/" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", jwt));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("책 조회가 완료되었습니다."))
                .andExpect(jsonPath("$.data.imageUrls").isArray());
    }

    private GetBookServiceResponse createGetBookResponse() {
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("gcsUrl1");
        imageUrls.add("gcsUrl2");
        return GetBookServiceResponse
                .builder()
                .bookName("bookName")
                .bookComment("bookComment")
                .bookPostName("bookPostName")
                .bookPrice(28000)
                .bookRealPrice(30000)
                .nickname("nickname")
                .imageUrls(imageUrls)
                .author("author")
                .publisher("publisher")
                .build();
    }
}
