package com.prakash.b2bordertracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "Partner ID is required")
    private Long partnerId;

    @NotBlank(message = "Reference number is required")
    private String referenceNumber;

    @NotEmpty(message = "Order must have at least one item")
    private List<OrderItemRequest> items;
}