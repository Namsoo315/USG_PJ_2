package com.usg.book.adapter.out.api;

import com.usg.book.adapter.out.api.dto.DocsDto;
import com.usg.book.adapter.out.api.dto.LibraryIsbnResponse;
import com.usg.book.application.port.out.BookISBNCheckPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class BookISBNCheckAdapter implements BookISBNCheckPort {

    private final RestTemplate restTemplate;
    private final String libraryKey;
    private final String libraryUrl = "https://www.nl.go.kr/seoji/SearchApi.do?";

    public BookISBNCheckAdapter(@Value("${open.library.key}") String libraryKey, RestTemplate restTemplate) {
        this.libraryKey = libraryKey;
        this.restTemplate = restTemplate;
    }

    @Override
    public void bookIsbnCheck(String isbn, Integer price) {

        URI uri = UriComponentsBuilder.fromHttpUrl(libraryUrl)
                .queryParam("cert_key", libraryKey)
                .queryParam("result_style", "json")
                .queryParam("page_no", 1)
                .queryParam("page_size", 10)
                .queryParam("isbn", isbn)
                .build()
                .toUri();

        String replacedIsbn = isbn.replace("-", "");

        LibraryIsbnResponse libraryIsbnResponse = restTemplate.getForObject(uri, LibraryIsbnResponse.class);
        DocsDto docs = libraryIsbnResponse.getDocs().get(0);

        if (!docs.getEA_ISBN().equals(replacedIsbn)) {
            throw new IllegalArgumentException("Isbn Not Match");
        }

        if (!docs.getPRE_PRICE().equals(price)) {
            throw new IllegalArgumentException("Price Not Match");
        }
    }
}
