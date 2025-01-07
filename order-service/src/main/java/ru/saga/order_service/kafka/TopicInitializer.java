package ru.saga.order_service.kafka;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class TopicInitializer {
    private final KafkaProperties kafkaProperties;
    private final ListenerContainer listenersContainer;
    private final PaymentListener paymentListener;

    @PostConstruct
    public void initTopics() {
        log.info("Initializing topics");
        listenersContainer.createListener(kafkaProperties.getPaymentTopic(), kafkaProperties.getGroupId(), paymentListener);
    }
}
