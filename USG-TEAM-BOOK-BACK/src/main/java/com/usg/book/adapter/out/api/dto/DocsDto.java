package com.usg.book.adapter.out.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DocsDto {
    @JsonProperty("PUBLISHER")
    private String PUBLISHER;
    @JsonProperty("AUTHOR")
    private String AUTHOR;
    @JsonProperty("PRE_PRICE")
    private Integer PRE_PRICE;
    @JsonProperty("EA_ISBN")
    private String EA_ISBN;
}
