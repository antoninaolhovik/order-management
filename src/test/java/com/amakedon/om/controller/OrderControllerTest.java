package com.amakedon.om.controller;

import com.amakedon.om.data.dto.OrderDto;
import com.amakedon.om.data.dto.OrderItemDto;
import com.amakedon.om.data.model.Order;
import com.amakedon.om.data.model.OrderItem;
import com.amakedon.om.data.model.Product;
import com.amakedon.om.service.OrderService;
import com.amakedon.om.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Collections;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("h2")
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @Test
    public void whenPostOrderWithoutProduct_thenBadRequest() throws Exception {
        OrderDto orderDto = createTestOrderDto(1L);
        orderDto.getOrderItems().forEach(oi -> oi.setProductId(null));

        given(orderService.save(Mockito.any())).willReturn(createTestOrder(1L));

        mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(orderDto)))
                .andExpect(status().isBadRequest());

        reset(orderService);
    }

    @Test
    public void whenPostOrder_thenCreateOrder() throws Exception {
        OrderDto orderDto = createTestOrderDto(1L);
        Product product = createTestProduct(1L);
        long productId = 1L;
        given(productService.findById(productId)).willReturn(product);

        given(orderService.save(Mockito.any())).willReturn(createTestOrder(1L));

        mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(orderDto.getSum()))
                .andExpect(jsonPath("$.orderItems", hasSize(1)));


        verify(orderService, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(orderService);
    }

    @Test
    public void whenGetOrders_thenReturnJsonArray() throws Exception {
        Order order = createTestOrder(1L);

        given(orderService.findAll()).willReturn(Collections.singletonList(order));

        mvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sum").value(order.getSum()));
    }

    @Test
    public void whenGetOrder_thenReturnOrder() throws Exception {
        long orderId = 1L;

        Order order = createTestOrder(orderId);

        given(orderService.findById(orderId)).willReturn(order);

        mvc.perform(get("/api/orders/" + orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(order.getSum()));
    }

    @Test
    public void whenUpdateOrder_thenUpdateOrder() throws Exception {
        long orderId = 1L;
        OrderDto orderDto = createTestOrderDto(orderId);

        given(orderService.findById(orderId)).willReturn(createTestOrder(orderId));


        mvc.perform(put("/api/orders/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(orderDto)))
                .andExpect(status().isOk());
        verify(orderService, VerificationModeFactory.times(1))
                .update(Mockito.any());
    }


    @Test
    public void whenDeleteOrder_thenDeleteOrder() throws Exception {
        long orderId = 1L;

        mvc.perform(delete("/api/orders/" + orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(orderService, VerificationModeFactory.times(1))
                .deleteById(orderId);
    }

    private OrderDto createTestOrderDto(long orderId) {
        OrderDto order = new OrderDto();
        order.setId(orderId);
        order.setSum(BigDecimal.TEN);

        order.setOrderItems(Collections.singletonList(createTestOrderItemDto()));

        return order;
    }

    private OrderItemDto createTestOrderItemDto() {
        OrderItemDto orderItem = new OrderItemDto();
        orderItem.setQuantity(1);
        orderItem.setId(1L);
        orderItem.setProductId(1L);
        return orderItem;
    }

    private Order createTestOrder(long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setSum(BigDecimal.TEN);

        order.setOrderItems(Collections.singletonList(createTestOrderItem()));

        return order;
    }

    private OrderItem createTestOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        orderItem.setId(1L);
        return orderItem;
    }

    private Product createTestProduct(long productId) {
        Product product = new Product();
        String name = randomAlphabetic(6);
        product.setName(name);
        product.setSku(name);
        product.setPrice(BigDecimal.TEN);
        product.setId(productId);
        return product;
    }

}