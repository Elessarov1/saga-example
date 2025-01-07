package ru.saga.order_service.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("order-service.kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private String groupId;
    private String paymentTopic;
    private Integer retriesCount;
    private boolean testErrorEnabled;
}
