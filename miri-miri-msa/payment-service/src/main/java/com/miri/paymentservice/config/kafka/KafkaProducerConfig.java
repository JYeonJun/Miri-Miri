package com.miri.paymentservice.config.kafka;

import com.miri.coremodule.config.KafkaProperties;
import com.miri.coremodule.dto.kafka.OrderUpdateEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ProducerFactory<String, OrderUpdateEventDto> producerOrderUpdateFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());
    }

    @Bean
    public KafkaTemplate<String, OrderUpdateEventDto> kafkaOrderUpdateTemplate() {
        return new KafkaTemplate<>(producerOrderUpdateFactory());
    }

    @Bean
    public ProducerFactory<String, StockRollbackEventDto> producerStockRollbackFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());
    }

    @Bean
    public KafkaTemplate<String, StockRollbackEventDto> kafkaRollbackTemplate() {
        return new KafkaTemplate<>(producerStockRollbackFactory());
    }

    @Bean
    public Map<String, Object> producerConfigurations() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        return props;
    }
}
