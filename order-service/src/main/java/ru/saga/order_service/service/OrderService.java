package ru.saga.order_service.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.saga.order_service.converter.OrderConverter;
import ru.saga.order_service.model.OrderEntity;
import ru.saga.order_service.model.OrderInput;
import ru.saga.order_service.model.OrderRecord;
import ru.saga.order_service.model.PaymentMessage;
import ru.saga.order_service.model.enumeration.PaymentStatus;
import ru.saga.order_service.repository.OrderRepository;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderConverter converter;

    @Transactional
    public OrderRecord createOrder(final OrderInput orderInput) {
        OrderEntity order = OrderEntity.builder()
                .userName(orderInput.username())
                .productName(orderInput.product())
                .cost(orderInput.cost())
                .paymentStatus(PaymentStatus.WAITING)
                .build();
        return converter.convert(orderRepository.save(order));
    }

    @Transactional
    public void acceptPayment(PaymentMessage paymentMessage) {
        orderRepository.findById(paymentMessage.orderId())
                .ifPresent(order -> order.setPaymentStatus(paymentMessage.paymentStatus()));
    }
}
