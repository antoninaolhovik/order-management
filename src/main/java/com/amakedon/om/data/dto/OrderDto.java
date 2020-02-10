package com.amakedon.om.data.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {

    private long id;

    private BigDecimal sum;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}
