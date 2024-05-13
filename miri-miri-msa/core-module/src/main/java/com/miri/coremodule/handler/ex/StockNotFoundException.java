package com.miri.coremodule.handler.ex;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException() {
        super("상품 재고 정보가 존재하지 않습니다.");
    }
}
