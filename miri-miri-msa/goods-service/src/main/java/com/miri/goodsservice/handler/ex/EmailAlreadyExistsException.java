package com.miri.goodsservice.handler.ex;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("등록할 수 없는 이메일입니다.");
    }
}