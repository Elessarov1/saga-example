package ru.saga.payment_service.kafka;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.saga.payment_service.kafka.listener.OrderListener;
import ru.saga.payment_service.kafka.properties.KafkaProperties;
import ru.saga.payment_service.service.KafkaProducingService;

@Component
@AllArgsConstructor
@Slf4j
public class TopicInitializer {
    private final KafkaProperties kafkaProperties;
    private final ListenerContainer listenersContainer;
    private final OrderListener orderListener;
    private final KafkaProducingService producingService;

    @PostConstruct
    public void initTopics() {
        log.info("Initializing topics");
        producingService.registerPaymentTopic();
        producingService.registerDltTopic();
        listenersContainer.createListener(kafkaProperties.getOrdersTopic(), kafkaProperties.getGroupId(), orderListener);
    }
}
