package com.miri.goodsservice.service.kafka;

import com.miri.coremodule.dto.kafka.OrderRequestEventReqDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

    private final KafkaTemplate<String, OrderRequestEventReqDto> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, OrderRequestEventReqDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderRequestEvent(String topic, OrderRequestEventReqDto orderRequestEventReqDto) {
        kafkaTemplate.send(topic, orderRequestEventReqDto);
    }
}
