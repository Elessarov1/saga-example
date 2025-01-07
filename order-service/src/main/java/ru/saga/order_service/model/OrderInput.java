package ru.saga.order_service.model;

public record OrderInput(
        String username,
        String product,
        int cost
) {

}
