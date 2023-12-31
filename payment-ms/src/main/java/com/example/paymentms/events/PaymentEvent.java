package com.example.paymentms.events;

import com.ashutov.orderms.model.CustomerOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEvent {
    private CustomerOrder customOrder;
    private String type;
}
