package com.example.stockms.repository;

import com.example.stockms.model.WareHouse;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<WareHouse,Long> {

    Iterable<WareHouse> findByItem(String item);
}
