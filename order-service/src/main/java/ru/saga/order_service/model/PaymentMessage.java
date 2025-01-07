package ru.saga.order_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.saga.order_service.model.enumeration.PaymentStatus;

public record PaymentMessage(
        @JsonProperty("paymentId")
        int orderId,
        PaymentStatus paymentStatus
) {
}
