package com.example.deliveryms.controller;

import com.ashutov.orderms.model.CustomerOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryEvent {

	private String type;

	private CustomerOrder order;

}