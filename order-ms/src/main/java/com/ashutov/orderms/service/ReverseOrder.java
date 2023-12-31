package com.ashutov.orderms.service;

import com.ashutov.orderms.events.OrderEvent;
import com.ashutov.orderms.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReverseOrder {

    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(topics = "reversed-orders", groupId = "orders-group")
    public void reverseOrder(String event) {
        System.out.println("Reverse order event: " + event);

        try {
            OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
            var order = orderRepository.findById(orderEvent.getCustomerOrder().getOrderId());
            order.ifPresent(o -> {
                o.setStatus("failed");
                orderRepository.save(o);
            });

        } catch (Exception e) {
            System.out.println("Exception occured while reverting order details: " + e.getMessage());

        }
    }


}
