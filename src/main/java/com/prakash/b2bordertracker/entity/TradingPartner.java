package com.prakash.b2bordertracker.entity;

import com.prakash.b2bordertracker.enums.PartnerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trading_partners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradingPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ediId;          // e.g. "WALMART001"

    @Enumerated(EnumType.STRING)
    private PartnerType type;      // SUPPLIER or RETAILER

    private String contactEmail;
}
