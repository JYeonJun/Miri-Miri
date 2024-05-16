package com.miri.coremodule.vo;

public interface KafkaVO {
    String ORDER_REQUEST_TOPIC = "order-request";

    String STOCK_ROLLBACK_TOPIC = "stock-rollback";

    String CANCEL_ORDER_TOPIC = "cancel-order";

    String PAYMENT_REQUEST_TOPIC = "payment-request";

    String ORDER_UPDATE_TOPIC = "order-update";
}
