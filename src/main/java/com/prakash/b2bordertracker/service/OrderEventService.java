package com.prakash.b2bordertracker.service;

import com.prakash.b2bordertracker.entity.Order;
import com.prakash.b2bordertracker.entity.OrderEvent;
import com.prakash.b2bordertracker.enums.EventType;
import com.prakash.b2bordertracker.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final OrderEventRepository eventRepository;

    public OrderEvent log(Order order, EventType eventType, String description) {
        OrderEvent event = new OrderEvent();
        event.setOrder(order);
        event.setEventType(eventType);
        event.setDescription(description);
        event.setTimestamp(LocalDateTime.now());
        event.setTriggeredBy("SYSTEM");
        return eventRepository.save(event);
    }

    public List<OrderEvent> getEventsByOrderId(Long orderId) {
        return eventRepository.findByOrderIdOrderByTimestampAsc(orderId);
    }
}
