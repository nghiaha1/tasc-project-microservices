package com.tasc.model;

import lombok.Data;

@Data
public class BaseResponseV2<T> {
    private int code;
    private String message;
    private T data;

    public BaseResponseV2(){
        this.code = ERROR.SUCCESS.code;
        this.message = "SUCCESS";
    }

    public boolean isSuccess(){
        return this.code == ERROR.SUCCESS.code;
    }

    public BaseResponseV2(String message) {
        this.code = ERROR.SUCCESS.code;
        this.message = message;
    }

    public BaseResponseV2(T data) {
        this.code = ERROR.SUCCESS.code;
        this.message = "SUCCESS";
        this.data = data;
    }
}
