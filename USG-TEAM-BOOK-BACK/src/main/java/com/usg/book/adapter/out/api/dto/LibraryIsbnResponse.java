package com.usg.book.adapter.out.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class LibraryIsbnResponse {
    @JsonProperty("TOTAL_COUNT")
    private String TOTAL_COUNT;
    @JsonProperty("docs")
    private List<DocsDto> docs;
    @JsonProperty("PAGE_NO")
    private String PAGE_NO;
}
