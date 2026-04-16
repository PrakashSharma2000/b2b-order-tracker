package com.prakash.b2bordertracker.controller;

import com.prakash.b2bordertracker.dto.TradingPartnerRequest;
import com.prakash.b2bordertracker.entity.TradingPartner;
import com.prakash.b2bordertracker.enums.PartnerType;
import com.prakash.b2bordertracker.service.TradingPartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
@Tag(name = "Trading Partners", description = "Manage suppliers and retailers")
public class TradingPartnerController {

    private final TradingPartnerService service;

    @PostMapping
    @Operation(summary = "Create a new trading partner")
    public ResponseEntity<TradingPartner> create(@Valid @RequestBody TradingPartnerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    @Operation(summary = "Get all partners, optionally filter by type")
    public ResponseEntity<List<TradingPartner>> getAll(
            @RequestParam(required = false) PartnerType type) {
        if (type != null) {
            return ResponseEntity.ok(service.getByType(type));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a trading partner by ID")
    public ResponseEntity<TradingPartner> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a trading partner")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}