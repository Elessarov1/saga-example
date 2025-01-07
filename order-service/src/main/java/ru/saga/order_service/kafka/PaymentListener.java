package ru.saga.order_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import ru.saga.order_service.model.PaymentMessage;
import ru.saga.order_service.service.OrderService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
@Slf4j
public class PaymentListener implements MessageListener<String, byte[]> {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    private final Map<String, Integer> retryCountMap = new ConcurrentHashMap<>();

    @Override
    public void onMessage(ConsumerRecord<String, byte[]> data) {
        try {
            if (kafkaProperties.isTestErrorEnabled()) {
                throw new RuntimeException("Test Error");
            }
            var paymentMessage = objectMapper.readValue(data.value(), PaymentMessage.class);
            orderService.acceptPayment(paymentMessage);
            log.info("Received order record: {}", paymentMessage);
        } catch (Exception e) {
            log.error("An exception was encountered while processing an order record {}", data, e);
            //throw new RuntimeException(e);
            retryOrSendToDlt(data);
        }
    }

    private void retryOrSendToDlt(ConsumerRecord<String, byte[]> data) {
        final int maxRetryCount = kafkaProperties.getRetriesCount();
        int attempts = retryCountMap.getOrDefault(getKeyFromData(data), 0);

        if (attempts < maxRetryCount) {
            retrySend(data, attempts);
        } else {
            sendToDlt(data);
        }
    }

    private void retrySend(final ConsumerRecord<String, byte[]> data, final int attempts) {
        retryCountMap.put(getKeyFromData(data), attempts + 1);
        onMessage(data);
    }

    private void sendToDlt(ConsumerRecord<String, byte[]> data) {
        String dltTopic = data.topic() + ".dlt";
        kafkaTemplate.send(dltTopic, data.value());
        retryCountMap.remove(getKeyFromData(data));
    }

    public String getKeyFromData(ConsumerRecord<String, byte[]> data) {
        return data.partition() + ":" + data.offset();
    }
}
