package com.miri.orderservice.service.kafka;

import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

    private final KafkaTemplate<String, PaymentRequestEventDto> kafkaPaymentTemplate;
    private final KafkaTemplate<String, StockRollbackEventDto> kafkaRollbackTemplate;

    public KafkaSender(KafkaTemplate<String, PaymentRequestEventDto> kafkaPaymentTemplate,
                       KafkaTemplate<String, StockRollbackEventDto> kafkaRollbackTemplate) {
        this.kafkaPaymentTemplate = kafkaPaymentTemplate;
        this.kafkaRollbackTemplate = kafkaRollbackTemplate;
    }

    public void sendPaymentRequestEvent(String topic, PaymentRequestEventDto paymentRequestEventDto) {
        kafkaPaymentTemplate.send(topic, paymentRequestEventDto);
    }

    public void sendRollbackRequestEvent(String topic, StockRollbackEventDto stockRollbackEventDto) {
        kafkaRollbackTemplate.send(topic, stockRollbackEventDto);
    }
}
