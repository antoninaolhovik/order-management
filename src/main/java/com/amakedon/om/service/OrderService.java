package com.amakedon.om.service;

import com.amakedon.om.data.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    Order findById(long id);

    Order save(Order order);

    void update(Order order);

    void deleteById(long id);

    List<Order> findAll();

    void deleteAll();

    Page<Order> searchByProductName(String productName, Pageable pageable);
}
