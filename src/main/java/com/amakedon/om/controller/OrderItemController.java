package com.amakedon.om.controller;

import com.amakedon.om.data.model.OrderItem;
import com.amakedon.om.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public Iterable findAll() {
        return orderItemService.findAll();
    }

    @GetMapping("/{id}")
    public OrderItem findOne(@PathVariable Long id) {
        return orderItemService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItem create(@RequestBody OrderItem orderItem) {
        return orderItemService.save(orderItem);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderItemService.deleteById(id);
    }

    @PutMapping
    public OrderItem update(@RequestBody OrderItem orderItem) {
        return orderItemService.save(orderItem);
    }
}
