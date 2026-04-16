package com.prakash.b2bordertracker.controller;

import com.prakash.b2bordertracker.dto.OrderRequest;
import com.prakash.b2bordertracker.entity.Order;
import com.prakash.b2bordertracker.enums.OrderStatus;
import com.prakash.b2bordertracker.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Manage B2B purchase orders (EDI 850)")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order with line items")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping
    @Operation(summary = "Get all orders, filter by status or partner")
    public ResponseEntity<List<Order>> getAll(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Long partnerId) {

        if (status != null) return ResponseEntity.ok(orderService.getByStatus(status));
        if (partnerId != null) return ResponseEntity.ok(orderService.getByPartnerId(partnerId));
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Update order status")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}