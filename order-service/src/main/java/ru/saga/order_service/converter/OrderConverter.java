package ru.saga.order_service.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.saga.order_service.model.OrderEntity;
import ru.saga.order_service.model.OrderRecord;

@Component
public class OrderConverter implements Converter<OrderEntity, OrderRecord> {

    @Override
    public OrderRecord convert(OrderEntity source) {
        return new OrderRecord(
                source.getId(),
                source.getUserName(),
                source.getProductName(),
                source.getCost(),
                source.getPaymentStatus()
        );
    }
}
