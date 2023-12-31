package com.example.paymentms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PaymentOrder {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String item;
    @Column

    private int quantity;
    @Column
    private double amount;
    @Column
    private String status;

    @Column
    private long orderId;

    private String address;

}
