package com.prakash.b2bordertracker.repository;

import com.prakash.b2bordertracker.entity.Invoice;
import com.prakash.b2bordertracker.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByOrderId(Long orderId);
    boolean existsByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByStatus(InvoiceStatus status);
}
