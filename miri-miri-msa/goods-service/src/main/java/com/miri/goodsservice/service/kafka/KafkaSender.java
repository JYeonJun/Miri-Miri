package com.miri.goodsservice.service.kafka;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

    private final KafkaTemplate<String, OrderRequestEventDto> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, OrderRequestEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderRequestEvent(String topic, OrderRequestEventDto orderRequestEventDto) {
        kafkaTemplate.send(topic, orderRequestEventDto);
    }
}
