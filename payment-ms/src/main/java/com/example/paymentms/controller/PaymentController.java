package com.example.paymentms.controller;

import com.ashutov.orderms.model.CustomerOrder;
import com.example.paymentms.events.OrderEvent;
import com.example.paymentms.events.PaymentEvent;
import com.example.paymentms.model.Payment;
import com.example.paymentms.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String,PaymentEvent> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String,OrderEvent> orderTemplate;

    @KafkaListener(topics = "new-orders", groupId = "orders-group")
    public void processPayment(String event) throws JsonProcessingException {
        System.out.println("Process payment event: " + event);

        OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
        CustomerOrder customOrder = orderEvent.getCustomOrder();
        Payment payment = new Payment();
        payment.setAmount(String.valueOf(customOrder.getAmount()));
        payment.setMode(customOrder.getStatus());
        payment.setOrderId(customOrder.getOrderId());
        payment.setStatus("Success");

    try {
        paymentRepository.save(payment);
        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setCustomOrder(customOrder);
        paymentEvent.setType("Payment CREATED");

        kafkaTemplate.send("new-payments", paymentEvent);

    }catch (Exception e) {}
        payment.setOrderId(customOrder.getOrderId());
        payment.setStatus("failed");
        paymentRepository.save(payment);

        var oe = new OrderEvent();
        oe.setCustomOrder(customOrder);
        oe.setType("ORDER_REVERSED");

        orderTemplate.send("reversed-orders", oe);
    }
}
