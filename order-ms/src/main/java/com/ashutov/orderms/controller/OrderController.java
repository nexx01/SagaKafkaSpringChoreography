package com.ashutov.orderms.controller;

import com.ashutov.orderms.events.OrderEvent;
import com.ashutov.orderms.model.CustomerOrder;
import com.ashutov.orderms.model.Order;
import com.ashutov.orderms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
@Autowired
private OrderRepository orderRepository;
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @PostMapping("/order")
    public void createOrder(@RequestBody CustomerOrder customerOrder) {
        Order order = new Order();
        order.setItem(customerOrder.getItem());
        order.setAmount(customerOrder.getAmount());
        order.setQuantity(customerOrder.getQuantity());
        order.setStatus("Created");

        try {
            order = orderRepository.save(order);
            customerOrder.setOrderId(order.getOrderId());
            var orderEvent = new OrderEvent();
            orderEvent.setCustomerOrder(customerOrder);
            orderEvent.setType("ORDER_CREATED");
            kafkaTemplate.send("new-order", orderEvent);
        } catch (Exception e) {
            order.setStatus("Failed");
            orderRepository.save(order);

        }
    }
}
