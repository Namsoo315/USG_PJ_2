package com.usg.book;

import com.usg.book.adapter.out.persistence.entity.BookEntity;
import com.usg.book.adapter.out.persistence.entity.BookRepository;
import com.usg.book.adapter.out.persistence.entity.ImageEntity;
import com.usg.book.adapter.out.persistence.entity.ImageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final BookRepository bookRepository;
        private final ImageRepository imageRepository;

        public void dbInit() {
            for (int i = 0; i < 30; i++) {
                BookEntity bookEntity = BookEntity.builder()
                        .email("email")
                        .bookName("bookName" + i)
                        .bookRealPrice(30000)
                        .author("author" + i)
                        .publisher("publisher" + i)
                        .bookPostName("bookPostName" + i)
                        .bookComment("bookComment" + i)
                        .bookPrice(28000)
                        .isbn("isbn" + i)
                        .build();
                BookEntity savedBookEntity = bookRepository.save(bookEntity);

                ImageEntity savedImageEntity = ImageEntity.builder()
                        .uploadFilename("uploadFilename")
                        .storeFilename("storeFilename")
                        .gcsUrl("https://storage.googleapis.com/usg_bucket/45782293-d20a-4100-9909-07e9453f473b.png")
                        .bookEntity(savedBookEntity)
                        .build();
                imageRepository.save(savedImageEntity);
            }
        }
    }
}
