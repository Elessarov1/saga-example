package ru.saga.payment_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.saga.payment_service.model.PaymentInput;
import ru.saga.payment_service.service.PaymentService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pay", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public String payTheOrder(@RequestBody PaymentInput paymentInput) {
        return paymentService.payTheOrder(paymentInput.paymentId(), paymentInput.balance());
    }
}
