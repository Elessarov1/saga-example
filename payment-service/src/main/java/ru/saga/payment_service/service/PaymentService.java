package ru.saga.payment_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.saga.order_service.model.enumeration.PaymentStatus;
import ru.saga.payment_service.kafka.properties.KafkaProperties;
import ru.saga.payment_service.model.OrderMessage;
import ru.saga.payment_service.model.PaymentEntity;
import ru.saga.payment_service.repository.PaymentRepository;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final KafkaProducingService producingService;
    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;

    @Transactional
    public String payTheOrder(final int id, final int amount) {
        var payment = paymentRepository.findById(id)
                .stream()
                .peek(it -> it.setPaymentStatus(amount >= it.getCost() ? PaymentStatus.PAID : PaymentStatus.CANCELLED))
                .map(paymentRepository::save)
                .findFirst()
                .orElseThrow();

        sendMessage(payment);
        return "Payment status: " + payment.getPaymentStatus();
    }

    @Transactional
    public void processMessage(final OrderMessage orderMessage) {
        assert orderMessage.after() != null;

        if (orderMessage.before() == null) {
            var order = orderMessage.after();
            log.info("Saving order: {}", order);
            this.save(PaymentEntity.ofOrder(order));
        }
    }

    @Transactional
    public void save(final PaymentEntity payment) {
        paymentRepository.save(payment);
    }

    private void sendMessage(final PaymentEntity paymentEntity) {
        try {
            final byte[] message = objectMapper.writeValueAsBytes(paymentEntity);
            producingService.send(kafkaProperties.getPaymentTopic(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
