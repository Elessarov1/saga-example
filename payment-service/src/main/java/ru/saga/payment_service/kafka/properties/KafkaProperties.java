package ru.saga.payment_service.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("payment-service.kafka")
@Getter
@Setter
public class KafkaProperties {
    private String bootstrapServers;
    private String groupId;
    private String ordersTopic;
    private String paymentTopic;
    private Integer retriesCount;
    private Integer delayMs;
    private String messageSize;
    private String topicShortRetention;
    private String schemaRegistryUrl;
    private boolean simulateError;
}
