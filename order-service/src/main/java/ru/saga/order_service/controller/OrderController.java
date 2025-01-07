package ru.saga.order_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.saga.order_service.model.OrderInput;
import ru.saga.order_service.model.OrderRecord;
import ru.saga.order_service.service.OrderService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderRecord createOrder(@RequestBody OrderInput orderInput) {
        return orderService.createOrder(orderInput);
    }
}
