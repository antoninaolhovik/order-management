package com.amakedon.om.data.dto;

import com.amakedon.om.data.model.Order;
import com.amakedon.om.data.model.OrderItem;
import com.amakedon.om.data.model.Product;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderDtoUnitTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void whenConvertPostEntityToPostDto_thenCorrect() {
        Order order = new Order();
        order.setId(1L);
        order.setSum(BigDecimal.ONE);
        order.setCreatedDate(LocalDate.now());
        order.setModifiedDate(LocalDate.now());

        Product product = new Product();
        product.setId(1L);
        product.setName(randomAlphabetic(6));
        product.setSku(randomAlphabetic(6));
        product.setPrice(BigDecimal.ONE);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(5);
        orderItem.setProduct(product);

        order.setOrderItems(Arrays.asList(orderItem));

        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        assertEquals(order.getId(), orderDto.getId());
        assertEquals(order.getSum(), orderDto.getSum());

        assertNotNull(orderDto.getOrderItems());
        orderDto.getOrderItems().forEach(item -> {
                    assertEquals(orderItem.getId(), item.getId());
                    assertEquals(orderItem.getQuantity(), item.getQuantity());
                    assertEquals(product.getId(), item.getProductId());
                }
        );
    }

    @Test
    void whenConvertPostDtoToPostEntity_thenCorrect() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setSum(BigDecimal.ONE);

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(1L);
        orderItemDto.setQuantity(5);
        orderItemDto.setProductId(1L);

        orderDto.setOrderItems(Arrays.asList(orderItemDto));

        Order order = modelMapper.map(orderDto, Order.class);
        assertEquals(orderDto.getId(), order.getId());
        assertEquals(orderDto.getSum(), order.getSum());

        assertNotNull(order.getOrderItems());
        order.getOrderItems().forEach(item -> {
                    assertEquals(orderItemDto.getId(), item.getId());
                    assertEquals(orderItemDto.getQuantity(), item.getQuantity());
                    assertEquals(orderItemDto.getProductId(), 1L);
                }
        );
    }
}
