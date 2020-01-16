package com.amakedon.om.data.dto;

import lombok.Data;

@Data
public class OrderItemDto {

    private long id;

    private int quantity;

    private ProductDto product;
}
