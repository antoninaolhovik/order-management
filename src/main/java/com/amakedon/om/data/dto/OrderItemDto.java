package com.amakedon.om.data.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderItemDto {

    private long id;

    @Min(1)
    private int quantity;

    @NotNull(message = "product is required")
    private ProductDto product;
}
