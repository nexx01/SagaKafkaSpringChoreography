package com.ashutov.orderms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrder {
    private String item;
    private int quantity;
    private double amount;
    private String status;

    private long orderId;

    private String address;

}
