package com.prakash.b2bordertracker.service;

import com.prakash.b2bordertracker.dto.TradingPartnerRequest;
import com.prakash.b2bordertracker.entity.TradingPartner;
import com.prakash.b2bordertracker.enums.PartnerType;
import com.prakash.b2bordertracker.repository.TradingPartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradingPartnerService {

    private final TradingPartnerRepository repository;

    public TradingPartner create(TradingPartnerRequest request) {
        if (repository.existsByEdiId(request.getEdiId())) {
            throw new RuntimeException("EDI ID already exists: " + request.getEdiId());
        }

        TradingPartner partner = new TradingPartner();
        partner.setName(request.getName());
        partner.setEdiId(request.getEdiId());
        partner.setType(request.getType());
        partner.setContactEmail(request.getContactEmail());

        return repository.save(partner);
    }

    public List<TradingPartner> getAll() {
        return repository.findAll();
    }

    public TradingPartner getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + id));
    }

    public List<TradingPartner> getByType(PartnerType type) {
        return repository.findByType(type);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Partner not found with id: " + id);
        }
        repository.deleteById(id);
    }
}