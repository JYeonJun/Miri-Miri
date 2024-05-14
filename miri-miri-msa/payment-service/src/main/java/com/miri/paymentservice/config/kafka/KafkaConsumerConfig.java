package com.miri.paymentservice.config.kafka;

import com.miri.coremodule.config.KafkaProperties;
import com.miri.coremodule.dto.kafka.CancelOrderEventDto;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    private Map<String, Object> getConsumerConfigurations(JsonDeserializer<?> deserializer) {
        Map<String, Object> consumerConfigurations = new HashMap<>();
        consumerConfigurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer());
        consumerConfigurations.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getPaymentGroupId());
        consumerConfigurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfigurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.getClass());
        consumerConfigurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return consumerConfigurations;
    }

    @Bean
    public ConsumerFactory<String, PaymentRequestEventDto> kafkaPaymentRequestConsumer() {

        JsonDeserializer<PaymentRequestEventDto> deserializer = new JsonDeserializer<>();

        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(getConsumerConfigurations(deserializer), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConsumerFactory<String, CancelOrderEventDto> kafkaCancelOrderConsumer() {

        JsonDeserializer<CancelOrderEventDto> deserializer = new JsonDeserializer<>();

        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(getConsumerConfigurations(deserializer), new StringDeserializer(), deserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, PaymentRequestEventDto> kafkaPaymentRequestContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentRequestEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaPaymentRequestConsumer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, CancelOrderEventDto> kafkaCancelOrderContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CancelOrderEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaCancelOrderConsumer());
        return factory;
    }
}
