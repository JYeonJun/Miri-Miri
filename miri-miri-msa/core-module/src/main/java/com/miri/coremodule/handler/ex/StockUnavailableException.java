package com.miri.coremodule.handler.ex;

public class StockUnavailableException extends RuntimeException {
    public StockUnavailableException() {
        super("재고가 부족합니다.");
    }
}
