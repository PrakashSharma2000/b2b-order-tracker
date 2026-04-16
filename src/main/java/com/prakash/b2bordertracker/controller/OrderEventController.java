package com.prakash.b2bordertracker.controller;

import com.prakash.b2bordertracker.entity.OrderEvent;
import com.prakash.b2bordertracker.service.OrderEventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Audit Log", description = "Full order event history and audit trail")
public class OrderEventController {

    private final OrderEventService eventService;

    @GetMapping("/{orderId}/events")
    public ResponseEntity<List<OrderEvent>> getEvents(@PathVariable Long orderId) {
        return ResponseEntity.ok(eventService.getEventsByOrderId(orderId));
    }
}