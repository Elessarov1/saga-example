package ru.saga.payment_service.model;

public record PaymentInput(
        int paymentId,
        int balance
) {
}
