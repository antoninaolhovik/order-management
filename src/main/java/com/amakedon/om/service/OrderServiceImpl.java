package com.amakedon.om.service;

import com.amakedon.om.data.exception.EntityNotFoundException;
import com.amakedon.om.data.model.Order;
import com.amakedon.om.data.repository.es.EsOrderRepository;
import com.amakedon.om.data.repository.jpa.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private EsOrderRepository esOrderRepository;

    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, EsOrderRepository esOrderRepository, ElasticsearchOperations elasticsearchOperations) {
        this.orderRepository = orderRepository;
        this.esOrderRepository = esOrderRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Order save(Order order) {
        Order savedOrder = orderRepository.save(order);
        esOrderRepository.save(savedOrder);
        return savedOrder;
    }

    @Override
    public void update(Order order) {
        orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        orderRepository.save(order);
        esOrderRepository.save(order);
    }

    @Override
    public void deleteById(long id) {
        orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        orderRepository.deleteById(id);
        esOrderRepository.deleteById(String.valueOf(id));
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
        esOrderRepository.deleteAll();
    }

    @Override
    public Page<Order> searchByProductName(String productName, Pageable pageable) {
        return esOrderRepository.findByOrderItemsProductName(productName, pageable);
    }
}
