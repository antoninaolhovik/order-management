package com.amakedon.om.controller;

import com.amakedon.om.data.model.Order;
import com.amakedon.om.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public Order findOne(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody Order order) {
        return orderService.save(order);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.deleteById(id);
    }

    @PutMapping
    public Order update(@RequestBody Order order) {
        return orderService.save(order);
    }

}
