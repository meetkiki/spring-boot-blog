package com.meetkiki.blog.exception;

public class BaseException extends RuntimeException {
    private String msg;

    public BaseException(String msg){
        super(msg);
    }


}
