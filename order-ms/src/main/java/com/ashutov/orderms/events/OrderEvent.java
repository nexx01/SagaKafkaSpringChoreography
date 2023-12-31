package com.ashutov.orderms.events;

import com.ashutov.orderms.model.CustomerOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEvent {
    private CustomerOrder customerOrder;
    private String type;
}
