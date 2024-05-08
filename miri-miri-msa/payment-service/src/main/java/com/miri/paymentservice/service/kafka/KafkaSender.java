package com.miri.paymentservice.service.kafka;

import com.miri.coremodule.dto.kafka.OrderUpdateEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

    private final KafkaTemplate<String, OrderUpdateEventDto> kafkaOrderUpdateTemplate;
    private final KafkaTemplate<String, StockRollbackEventDto> kafkaRollbackTemplate;

    public KafkaSender(KafkaTemplate<String, OrderUpdateEventDto> kafkaOrderUpdateTemplate,
                       KafkaTemplate<String, StockRollbackEventDto> kafkaRollbackTemplate) {
        this.kafkaOrderUpdateTemplate = kafkaOrderUpdateTemplate;
        this.kafkaRollbackTemplate = kafkaRollbackTemplate;
    }

    public void sendOrderUpdateRequestEvent(String topic, OrderUpdateEventDto orderUpdateEventDto) {
        kafkaOrderUpdateTemplate.send(topic, orderUpdateEventDto);
    }

    public void sendRollbackRequestEvent(String topic, StockRollbackEventDto stockRollbackEventDto) {
        kafkaRollbackTemplate.send(topic, stockRollbackEventDto);
    }
}
