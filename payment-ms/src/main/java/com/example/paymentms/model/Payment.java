package com.example.paymentms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String mode;
    @Column
    private Long orderId;

    @Column
    private String amount;
    @Column
    private String status;

}
