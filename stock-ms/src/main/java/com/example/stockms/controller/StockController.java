package com.example.stockms.controller;

import com.ashutov.orderms.model.CustomerOrder;
import com.example.paymentms.dto.Stock;
import com.example.stockms.model.WareHouse;
import com.example.paymentms.events.PaymentEvent;
import com.example.stockms.model.DeliveryEvent;
import com.example.stockms.repository.StockRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private KafkaTemplate<String, DeliveryEvent> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> paymentEventKafkaTemplate;

    @KafkaListener(topics = "new-payments", groupId = "payment-group")
    public void updateStock(String paymentEvent) throws JsonProcessingException {
        System.out.println("Inside update inventory for order " + paymentEvent);

        var event = new DeliveryEvent();
        PaymentEvent p = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
        CustomerOrder order = p.getCustomOrder();

        try {
            Iterable<WareHouse> inventories = stockRepository.findByItem(order.getItem());
            boolean exists = inventories.iterator().hasNext();
            if (!exists) {
                System.out.println("Stock not exists so revertiong the order " + order.getOrderId());
                throw new Exception("Stock not available");
            }

            inventories.forEach(i -> {
                i.setQuantity(i.getQuantity() - order.getQuantity());
                stockRepository.save(i);
            });

            event.setType("STOCK_UPDATED");
            event.setOrder(p.getCustomOrder());
            kafkaTemplate.send("new-stock", event);


        } catch (Exception e) {
            PaymentEvent pe = new PaymentEvent();
            pe.setCustomOrder(order);
            pe.setType("STOCK_REVERCED");
            paymentEventKafkaTemplate.send("reversed-payment", pe);
        }
    }

    @PostMapping("/addItems")
    public void addItems(@RequestBody Stock stock) {
        Iterable<WareHouse> items = stockRepository.findByItem(stock.getItem());

        if (items.iterator().hasNext()) {
            items.forEach(i -> {
                i.setQuantity(stock.getQuantity() - i.getQuantity());
                stockRepository.save(i);
            });
        } else {
            WareHouse i = new WareHouse();
            i.setItem(stock.getItem());
            i.setQuantity(stock.getQuantity());
            stockRepository.save(i);
        }
    }
}
