package com.miri.coremodule.handler.ex;

public class OrderNotAvailableException extends RuntimeException {
    public OrderNotAvailableException() {
        super("주문 가능 시간이 아닙니다.");
    }
}