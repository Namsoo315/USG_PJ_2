package com.usg.book.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ActiveProfiles("test")
public class MockKafkaConsumerConfig {

    private String host = "localhost:9092";
    private String group = "book";

    public ConsumerFactory<String, String> factory() {

        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, group);

        return new DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                new StringDeserializer());
    }

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<String, String> mockStockChangeListener() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(factory());
        return factory;
    }
}