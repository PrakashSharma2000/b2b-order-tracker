package com.prakash.b2bordertracker.repository;

import com.prakash.b2bordertracker.entity.OrderEvent;
import com.prakash.b2bordertracker.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {

    List<OrderEvent> findByOrderIdOrderByTimestampAsc(Long orderId);
    List<OrderEvent> findByEventType(EventType eventType);
}
