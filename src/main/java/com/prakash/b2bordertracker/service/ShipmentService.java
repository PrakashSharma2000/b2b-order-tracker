package com.prakash.b2bordertracker.service;

import com.prakash.b2bordertracker.dto.ShipmentRequest;
import com.prakash.b2bordertracker.entity.Order;
import com.prakash.b2bordertracker.entity.Shipment;
import com.prakash.b2bordertracker.enums.EventType;
import com.prakash.b2bordertracker.enums.OrderStatus;
import com.prakash.b2bordertracker.enums.ShipmentStatus;
import com.prakash.b2bordertracker.repository.OrderRepository;
import com.prakash.b2bordertracker.repository.ShipmentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final OrderEventService eventService;


    public Shipment createShipment(@Valid ShipmentRequest request) {

        // Duplicate tracking number check
        if (shipmentRepository.existsByTrackingNumber(request.getTrackingNumber())) {
            throw new RuntimeException("Tracking number already exists: " + request.getTrackingNumber());
        }

        // Fetch order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + request.getOrderId()));

        // Business rule — can only ship a CONFIRMED order
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Order must be CONFIRMED before shipping. Current status: " + order.getStatus());
        }

        // Build shipment
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setTrackingNumber(request.getTrackingNumber());
        shipment.setCarrier(request.getCarrier());
        shipment.setShipDate(request.getShipDate());
        shipment.setEstimatedDelivery(request.getEstimatedDelivery());
        shipment.setStatus(ShipmentStatus.CREATED);

        // Auto-update order status to SHIPPED
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);

        eventService.log(
                order,
                EventType.SHIPMENT_CREATED,
                "Shipment created with tracking: " + shipment.getTrackingNumber()
                        + " via " + shipment.getCarrier()
        );

        return shipmentRepository.save(shipment);
    }

    public List<Shipment> getByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId);
    }

    public Shipment getById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + id));
    }

    public Shipment updateStatus(Long id, ShipmentStatus status) {
        Shipment shipment = getById(id);
        shipment.setStatus(status);

        eventService.log(
                shipment.getOrder(),
                EventType.SHIPMENT_UPDATED,
                "Shipment status updated to: " + status
        );

        return shipmentRepository.save(shipment);
    }
}