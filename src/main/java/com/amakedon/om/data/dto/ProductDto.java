package com.amakedon.om.data.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private long id;

    private String name;

    private String sku;

    private BigDecimal price;

}
