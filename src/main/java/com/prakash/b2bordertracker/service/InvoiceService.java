package com.prakash.b2bordertracker.service;

import com.prakash.b2bordertracker.dto.InvoiceRequest;
import com.prakash.b2bordertracker.entity.Invoice;
import com.prakash.b2bordertracker.entity.Order;
import com.prakash.b2bordertracker.enums.EventType;
import com.prakash.b2bordertracker.enums.InvoiceStatus;
import com.prakash.b2bordertracker.enums.OrderStatus;
import com.prakash.b2bordertracker.repository.InvoiceRepository;
import com.prakash.b2bordertracker.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final OrderEventService eventService;


    public Invoice createInvoice(InvoiceRequest request) {

        // Duplicate invoice number check
        if (invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
            throw new RuntimeException("Invoice number already exists: " + request.getInvoiceNumber());
        }

        // Fetch order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + request.getOrderId()));

        // Business rule — can only invoice a SHIPPED order
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("Order must be SHIPPED before invoicing. Current status: " + order.getStatus());
        }

        // Build invoice
        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setAmount(request.getAmount());
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(request.getDueDate());
        invoice.setStatus(InvoiceStatus.DRAFT);

        // Auto-update order status to INVOICED
        order.setStatus(OrderStatus.INVOICED);
        orderRepository.save(order);

        eventService.log(
                order,
                EventType.INVOICE_CREATED,
                "Invoice created: " + invoice.getInvoiceNumber()
                        + " for amount: " + invoice.getAmount()
        );

        return invoiceRepository.save(invoice);
    }

    public Invoice getById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + id));
    }

    public List<Invoice> getByOrderId(Long orderId) {
        return invoiceRepository.findByOrderId(orderId);
    }

    public List<Invoice> getByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }

    public Invoice updateStatus(Long id, InvoiceStatus status) {
        Invoice invoice = getById(id);
        invoice.setStatus(status);

        // If invoice is paid, close the order
        if (status == InvoiceStatus.PAID) {
            Order order = invoice.getOrder();
            order.setStatus(OrderStatus.CLOSED);
            orderRepository.save(order);
        }

        eventService.log(
                invoice.getOrder(),
                EventType.INVOICE_PAID,
                "Invoice " + invoice.getInvoiceNumber() + " marked as PAID. Order closed."
        );

        return invoiceRepository.save(invoice);
    }
}