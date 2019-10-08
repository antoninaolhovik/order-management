package com.amakedon.om.service;

import com.amakedon.om.data.exception.EntityNotFoundException;
import com.amakedon.om.data.model.Order;
import com.amakedon.om.data.model.OrderItem;
import com.amakedon.om.data.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void update(Order order) {
        orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        orderRepository.save(order);
    }

    @Override
    public void deleteById(long id) {
        orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        orderRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }
}
