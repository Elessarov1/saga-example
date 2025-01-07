package ru.saga.order_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.saga.order_service.model.enumeration.PaymentStatus;

public record OrderRecord(
        int id,
        @JsonProperty("user_name")
        String username,
        @JsonProperty("product_name")
        String productName,
        int cost,
        @JsonProperty("payment_status")
        PaymentStatus status
) {
}
