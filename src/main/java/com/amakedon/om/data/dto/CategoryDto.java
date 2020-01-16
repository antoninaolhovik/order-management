package com.amakedon.om.data.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {

    private long id;

    private String name;

    private List<ProductDto> products = new ArrayList<>();

}
