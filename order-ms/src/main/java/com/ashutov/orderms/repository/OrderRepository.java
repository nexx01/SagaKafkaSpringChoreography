package com.ashutov.orderms.repository;

import com.ashutov.orderms.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
