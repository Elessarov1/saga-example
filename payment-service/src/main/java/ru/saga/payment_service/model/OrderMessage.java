package ru.saga.payment_service.model;

import ru.saga.order_service.model.OrderRecord;

public record OrderMessage(
        OrderRecord before,
        OrderRecord after
) {
    @Override
    public String toString() {
        return "OrderMessage{" +
                "before - " + before +
                ", after - " + after +
                '}';
    }
}
