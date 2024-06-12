package com.usg.book.adapter.in.web.dto;

import lombok.Data;

@Data
public class Result <T> {

    private T data;
    private String message;

    public Result(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
