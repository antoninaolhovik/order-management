package com.amakedon.om.data.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {

    private long id;

    @NotEmpty(message = "name is required")
    private String name;

    //private List<ProductDto> products = new ArrayList<>();

}
