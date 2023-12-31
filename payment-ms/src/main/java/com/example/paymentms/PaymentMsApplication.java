package com.example.paymentms;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan("com.example.paymentms")
public class PaymentMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentMsApplication.class, args);
    }

}
