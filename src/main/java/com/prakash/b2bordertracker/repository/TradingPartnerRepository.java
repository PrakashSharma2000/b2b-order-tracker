package com.prakash.b2bordertracker.repository;

import com.prakash.b2bordertracker.entity.TradingPartner;
import com.prakash.b2bordertracker.enums.PartnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradingPartnerRepository extends JpaRepository<TradingPartner, Long> {

    List<TradingPartner> findByType(PartnerType type);
    Optional<TradingPartner> findByEdiId(String ediId);
    boolean existsByEdiId(String ediId);
}
