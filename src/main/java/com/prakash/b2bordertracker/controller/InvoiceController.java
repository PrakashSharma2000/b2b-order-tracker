package com.prakash.b2bordertracker.controller;

import com.prakash.b2bordertracker.dto.InvoiceRequest;
import com.prakash.b2bordertracker.entity.Invoice;
import com.prakash.b2bordertracker.enums.InvoiceStatus;
import com.prakash.b2bordertracker.service.InvoiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoices", description = "Manage invoices and payments (EDI 810)")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> create(@Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Invoice>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(invoiceService.getByOrderId(orderId));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAll(
            @RequestParam(required = false) InvoiceStatus status) {
        if (status != null) return ResponseEntity.ok(invoiceService.getByStatus(status));
        return ResponseEntity.ok(invoiceService.getByOrderId(null));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Invoice> updateStatus(
            @PathVariable Long id,
            @RequestParam InvoiceStatus status) {
        return ResponseEntity.ok(invoiceService.updateStatus(id, status));
    }
}
