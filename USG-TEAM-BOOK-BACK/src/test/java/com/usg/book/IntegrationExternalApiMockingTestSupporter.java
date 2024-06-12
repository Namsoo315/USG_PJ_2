package com.usg.book;

import com.usg.book.adapter.out.api.BookISBNCheckAdapter;
import com.usg.book.adapter.out.api.ImageGcsAdapter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class IntegrationExternalApiMockingTestSupporter {

    @MockBean
    protected ImageGcsAdapter imageGcsAdapter;
    @MockBean
    protected BookISBNCheckAdapter bookISBNCheckAdapter;
}
