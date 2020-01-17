package com.amakedon.om.service;

import com.amakedon.om.exception.ResourceNotFoundException;
import com.amakedon.om.data.model.OrderItem;
import com.amakedon.om.data.repository.jpa.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem findById(long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem with id " + id + "not found"));
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void update(OrderItem orderItem) {
        findById(orderItem.getId());
        orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteById(long id) {
        findById(id);
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItem> findAll() {
        List<OrderItem> list = new ArrayList<>();
        orderItemRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public void deleteAll() {
        orderItemRepository.deleteAll();
    }
}
