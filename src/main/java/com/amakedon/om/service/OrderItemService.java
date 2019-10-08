package com.amakedon.om.service;

import com.amakedon.om.data.model.OrderItem;

import java.util.List;

public interface OrderItemService {

    OrderItem findById(long id);

    OrderItem save(OrderItem orderItem);

    void update(OrderItem orderItem);

    void deleteById(long id);

    List<OrderItem> findAll();

    void deleteAll();
}
