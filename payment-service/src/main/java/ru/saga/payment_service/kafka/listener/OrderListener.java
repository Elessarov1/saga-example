package ru.saga.payment_service.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import ru.saga.payment_service.model.OrderMessage;
import ru.saga.payment_service.service.PaymentService;

@Component
@AllArgsConstructor
@Slf4j
public class OrderListener implements MessageListener<GenericRecord, GenericRecord> {
    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    @Override
    public void onMessage(ConsumerRecord<GenericRecord, GenericRecord> data) {
        try {
            var orderMessage = objectMapper.readValue(data.value().toString(), OrderMessage.class);
            log.info("Received order record: {}", orderMessage.toString());
            paymentService.processMessage(orderMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
