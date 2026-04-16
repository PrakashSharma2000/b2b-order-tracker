package com.prakash.b2bordertracker.service;

import com.prakash.b2bordertracker.dto.OrderItemRequest;
import com.prakash.b2bordertracker.dto.OrderRequest;
import com.prakash.b2bordertracker.entity.Order;
import com.prakash.b2bordertracker.entity.OrderItem;
import com.prakash.b2bordertracker.entity.TradingPartner;
import com.prakash.b2bordertracker.enums.EventType;
import com.prakash.b2bordertracker.enums.OrderStatus;
import com.prakash.b2bordertracker.repository.OrderItemRepository;
import com.prakash.b2bordertracker.repository.OrderRepository;
import com.prakash.b2bordertracker.repository.TradingPartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final TradingPartnerRepository partnerRepository;
    private final OrderEventService eventService;


    public Order createOrder(OrderRequest request) {

        // Check duplicate reference number
        if (orderRepository.existsByReferenceNumber(request.getReferenceNumber())) {
            throw new RuntimeException("Reference number already exists: " + request.getReferenceNumber());
        }

        // Fetch the trading partner
        TradingPartner partner = partnerRepository.findById(request.getPartnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found: " + request.getPartnerId()));

        // Calculate total
        double total = request.getItems().stream()
                .mapToDouble(i -> i.getQuantity() * i.getUnitPrice())
                .sum();

        // Build and save order
        Order order = new Order();
        order.setPartner(partner);
        order.setReferenceNumber(request.getReferenceNumber());
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // Save order items
        for (OrderItemRequest itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setItemCode(itemReq.getItemCode());
            item.setDescription(itemReq.getDescription());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());
            orderItemRepository.save(item);
        }

        eventService.log(
                savedOrder,
                EventType.ORDER_CREATED,
                "Order created with reference: " + savedOrder.getReferenceNumber()
        );
        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public List<Order> getByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getByPartnerId(Long partnerId) {
        return orderRepository.findByPartnerId(partnerId);
    }

    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = getOrderById(id);
        order.setStatus(newStatus);

        eventService.log(
                order,
                EventType.STATUS_CHANGED,
                "Status changed to: " + newStatus
        );

        return orderRepository.save(order);
    }


}
