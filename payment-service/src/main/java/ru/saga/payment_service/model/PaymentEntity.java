package ru.saga.payment_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.saga.order_service.model.OrderRecord;
import ru.saga.order_service.model.enumeration.PaymentStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    private Integer paymentId;
    @Column
    private String username;
    @Column
    private String productName;
    @Column
    private Integer cost;
    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public static PaymentEntity ofOrder(final OrderRecord order) {
        return new PaymentEntity(
                order.id(),
                order.username(),
                order.productName(),
                order.cost(),
                order.status()
        );
    }
}
