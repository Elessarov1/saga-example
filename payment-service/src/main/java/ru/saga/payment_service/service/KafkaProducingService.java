package ru.saga.payment_service.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;
import ru.saga.payment_service.kafka.CustomKafkaTemplate;
import ru.saga.payment_service.kafka.properties.KafkaProperties;

@Slf4j
@Service
public class KafkaProducingService {
    public static final String DLT_SUFFIX = ".dlt";

    private final KafkaProperties kafkaProperties;
    private final CustomKafkaTemplate<String, byte[]> producerTemplate;
    private final KafkaAdmin kafkaAdmin;

    public KafkaProducingService(
            KafkaProperties kafkaProperties,
            CustomKafkaTemplate<String, byte[]> producerTemplate,
            KafkaAdmin kafkaAdmin) {
        this.kafkaProperties = kafkaProperties;
        this.producerTemplate = producerTemplate;
        this.kafkaAdmin = kafkaAdmin;
        kafkaAdmin.setModifyTopicConfigs(true);
    }

    public void createTopic(final String topic) {
        kafkaAdmin.createOrModifyTopics(
                TopicBuilder.name(topic)
                        .config(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, kafkaProperties.getMessageSize())
                        .build()
        );
    }

    public void createDltTopic(final String topic) {
        int maxBytes = Integer.parseInt(kafkaProperties.getMessageSize()) * 2;
        kafkaAdmin.createOrModifyTopics(
                TopicBuilder.name(topic)
                        .config(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, String.valueOf(maxBytes))
                        .build()
        );
    }

    public void setShortRetention(final String topic) {
        kafkaAdmin.createOrModifyTopics(
                TopicBuilder.name(topic)
                        .config(TopicConfig.RETENTION_MS_CONFIG, kafkaProperties.getTopicShortRetention())
                        .build()
        );
    }

    public void send(final String topic, final byte[] data) {
        log.info("Send message with size {} to topic {}", data.length, topic);
        producerTemplate.send(topic, data);
    }

    public void registerPaymentTopic() {
        final String topic = kafkaProperties.getPaymentTopic();
        createTopic(topic);
        setShortRetention(topic);
    }

    public void registerDltTopic() {
        final String topic = kafkaProperties.getPaymentTopic() + DLT_SUFFIX;
        createDltTopic(topic);
    }
}
