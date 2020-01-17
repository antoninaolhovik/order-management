package com.amakedon.om.controller;

import com.amakedon.om.data.dto.OrderDto;
import com.amakedon.om.data.dto.OrderItemDto;
import com.amakedon.om.data.model.Order;
import com.amakedon.om.exception.ResourceNotFoundException;
import com.amakedon.om.service.OrderService;
import com.amakedon.om.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    private ProductService productService;

    private ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ProductService productService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public Iterable findAll() {
        List<Order> orders = orderService.findAll();
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDto findOne(@PathVariable Long id) {
        return convertToDto(orderService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@RequestBody OrderDto orderDto) {
        validateProductExistence(orderDto);
        Order order = convertToEntity(orderDto);
        return convertToDto(orderService.save(order));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (orderService.findById(id) == null) {
            throw new ResourceNotFoundException("Order with id " + id + "not found");
        }
        orderService.deleteById(id);
    }

    @PutMapping("/{id}")
    public OrderDto update(@RequestBody OrderDto orderDto, @PathVariable Long id) {
        Order orderOld = orderService.findById(id);
        if (orderOld == null) {
            throw new ResourceNotFoundException("Order with id " + id + "not found");
        }
        Order order = convertToEntity(orderDto);
        order.setId(id);
        return convertToDto(orderService.save(order));
    }

    @GetMapping("/search/{productName}")
    public Iterable searchByProductName(@PathVariable String productName) {
        //FIXME
        Page<Order> orders = orderService.searchByProductName(productName, Pageable.unpaged());
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    private Order convertToEntity(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    private void validateProductExistence(OrderDto orderDto) {
        List<OrderItemDto> list = orderDto.getOrderItems()
                .stream()
                .filter(orderItem -> Objects.isNull(productService.findById(orderItem
                        .getProduct()
                        .getId())))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)) {
            new ResourceNotFoundException("Product not found"); //Add ids
        }
    }

}
