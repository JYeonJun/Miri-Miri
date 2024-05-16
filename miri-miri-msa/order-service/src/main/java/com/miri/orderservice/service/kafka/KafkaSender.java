package com.miri.orderservice.service.kafka;

import com.miri.coremodule.dto.kafka.CancelOrderEventDto;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

    private final KafkaTemplate<String, PaymentRequestEventDto> kafkaPaymentTemplate;
    private final KafkaTemplate<String, StockRollbackEventDto> kafkaRollbackTemplate;
    private final KafkaTemplate<String, CancelOrderEventDto> kafkaCancelOrderTemplate;

    public KafkaSender(KafkaTemplate<String, PaymentRequestEventDto> kafkaPaymentTemplate,
                       KafkaTemplate<String, StockRollbackEventDto> kafkaRollbackTemplate,
                       KafkaTemplate<String, CancelOrderEventDto> kafkaCancelOrderTemplate) {
        this.kafkaPaymentTemplate = kafkaPaymentTemplate;
        this.kafkaRollbackTemplate = kafkaRollbackTemplate;
        this.kafkaCancelOrderTemplate = kafkaCancelOrderTemplate;
    }

    public void sendPaymentRequestEvent(String topic, PaymentRequestEventDto paymentRequestEventDto) {
        kafkaPaymentTemplate.send(topic, paymentRequestEventDto);
    }

    public void sendRollbackRequestEvent(String topic, StockRollbackEventDto stockRollbackEventDto) {
        kafkaRollbackTemplate.send(topic, stockRollbackEventDto);
    }

    public void sendCancelOrderEvent(String topic, CancelOrderEventDto eventDto) {
        kafkaCancelOrderTemplate.send(topic, eventDto);
    }
}
