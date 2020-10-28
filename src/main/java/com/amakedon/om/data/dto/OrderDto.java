package com.amakedon.om.data.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {

    private long id;

    @NotNull
    @Positive
    private BigDecimal sum;


    @Valid
    @NotNull
    @Size(min = 1, message = "orderItems must contain at least one item.")
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
