package com.amakedon.om.data.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {

    private long id;

    private Double sum;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}
