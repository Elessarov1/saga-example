package ru.saga.payment_service.kafka;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@AllArgsConstructor
public class ListenerContainer {
    private static final String BEAN_NAME_POSTFIX = "-listener-container";

    private final ConcurrentKafkaListenerContainerFactory<GenericRecord, GenericRecord> containerFactory;
    private final Map<String, MessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();

    public void createListener(final String topic, final String groupId, final MessageListener<GenericRecord, GenericRecord> listener) {
        log.warn("Starting {} for topic: {}, group: {}", listener.getClass().getSimpleName(), topic, groupId);

        var container = containerFactory.createContainer(topic);
        container.getContainerProperties().setMessageListener(listener);
        container.getContainerProperties().setGroupId(groupId);
        container.setBeanName(topic + BEAN_NAME_POSTFIX);
        container.start();
        listenerContainers.put(topic, container);
    }

    @PreDestroy
    public void destroy() {
        listenerContainers.values().forEach(MessageListenerContainer::destroy);
    }
}
