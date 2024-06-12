package com.usg.chat.adapter.in.web.dto;

import lombok.Data;

@Data
public class Result <T>{        //로그 남기기 위한 메시지 파트
    private T data;
    private String message;

    public Result(T data) {this.data = data;}
    public Result(T data, String message){
        this.data = data;
        this.message = message;
    }
}
