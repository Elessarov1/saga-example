package ru.saga.order_service.model;

import jakarta.persistence.*;
import lombok.*;
import ru.saga.order_service.model.enumeration.PaymentStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String userName;
    @Column
    private String productName;
    @Column
    private Integer cost;
    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", productName='" + productName + '\'' +
                ", cost=" + cost +
                ", orderStatus=" + paymentStatus +
                '}';
    }
}
