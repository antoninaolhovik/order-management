package com.amakedon.om.service;

import com.amakedon.om.exception.ResourceNotFoundException;
import com.amakedon.om.data.model.Order;
import com.amakedon.om.data.repository.es.EsOrderRepository;
import com.amakedon.om.data.repository.jpa.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private EsOrderRepository esOrderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, EsOrderRepository esOrderRepository) {
        this.orderRepository = orderRepository;
        this.esOrderRepository = esOrderRepository;
    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + "not found"));
    }

    @Override
    //@Transactional
    public Order save(Order order) {
        Order savedOrder = orderRepository.save(order);
        esOrderRepository.save(savedOrder);
        return savedOrder;
    }

    @Transactional
    @Override
    public void update(Order order) {
        findById(order.getId());
        orderRepository.save(order);
        esOrderRepository.save(order);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        findById(id);
        orderRepository.deleteById(id);
        esOrderRepository.deleteById(id);
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        orderRepository.findAll().forEach(list::add);
        return list;
    }

    @Transactional
    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
        esOrderRepository.deleteAll();
    }

    @Override
    public Page<Order> searchByProductName(String productName, Pageable pageable) {
        return esOrderRepository.findByOrderItemsProductName(productName, pageable);
    }
}
