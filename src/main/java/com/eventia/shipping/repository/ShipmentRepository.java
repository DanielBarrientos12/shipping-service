package com.eventia.shipping.repository;

import com.eventia.shipping.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    Page<Shipment> findByUserId(String userId, Pageable pageable);
    Optional<Shipment> findByOrderId(String orderId);
}
