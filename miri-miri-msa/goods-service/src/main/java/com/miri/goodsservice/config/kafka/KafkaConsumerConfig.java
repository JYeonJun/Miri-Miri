package com.miri.goodsservice.config.kafka;

import com.google.common.collect.ImmutableMap;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
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

    @Bean
    public ConsumerFactory<String, StockRollbackEventDto> kafkaConsumer() {

        JsonDeserializer<StockRollbackEventDto> deserializer = new JsonDeserializer<>();

        deserializer.addTrustedPackages("*");

        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer())
                        .put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId())
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, StockRollbackEventDto> kafkaContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StockRollbackEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumer());
        return factory;
    }
}
