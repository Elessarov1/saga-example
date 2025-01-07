package ru.saga.payment_service.kafka;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

import static ru.saga.payment_service.service.KafkaProducingService.DLT_SUFFIX;

@Slf4j
@Component
public class CustomKafkaTemplate<K,V> extends KafkaTemplate<K,V> {

    public CustomKafkaTemplate(ProducerFactory<K,V> producerFactory) {
        super(producerFactory);
    }

    @PostConstruct
    public void init() {
        this.setProducerListener(new ProducerListener<>() {
            @Override
            public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
                log.info("Message successfully sent to topic: {} - partition: {}, offset: {}",
                        producerRecord.topic(), recordMetadata.partition(), recordMetadata.offset());
            }

            @Override
            public void onError(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                log.error("Failed to send message to topic: {}", producerRecord.topic(), exception);
                handleFailedMessage(producerRecord);
            }
        });
    }

    private void handleFailedMessage(ProducerRecord<K,V> failedRecord) {
        String topic = failedRecord.topic();

        if (topic.endsWith(DLT_SUFFIX)) {
            log.error("Message already sent to DLT topic: {}, skipping further retries.", topic);
            return;
        }
        String dltTopic = topic + DLT_SUFFIX;

        try {
            this.send(dltTopic, failedRecord.key(), failedRecord.value());
            log.warn("Message sent to DLT topic: {}", dltTopic);
        } catch (Exception e) {
            log.error("Failed to send message to DLT topic: {}", dltTopic, e);
        }
    }
}
