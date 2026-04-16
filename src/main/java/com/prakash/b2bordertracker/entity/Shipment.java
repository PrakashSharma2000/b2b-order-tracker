package com.prakash.b2bordertracker.entity;

import com.prakash.b2bordertracker.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String trackingNumber;

    private String carrier;           // e.g. "FedEx", "DHL"

    private LocalDate shipDate;

    private LocalDate estimatedDelivery;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;
}
