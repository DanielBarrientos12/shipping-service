package com.eventia.shipping.controller;

import com.eventia.shipping.dto.OrderCreatedEvent;
import com.eventia.shipping.dto.StatusRequest;
import com.eventia.shipping.entity.Shipment;
import com.eventia.shipping.repository.ShipmentRepository;
import com.eventia.shipping.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentRepository repo;
    private final ShipmentService service;

    @GetMapping
    public Page<Shipment> getAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return repo.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @GetMapping("/{id}")
    public Shipment getbyId(@PathVariable Integer id) {
        return repo.findById(id).orElseThrow();
    }

    @GetMapping("/user/{userId}")
    public Page<Shipment> getByUser(@PathVariable String userId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return repo.findByUserId(userId, PageRequest.of(page, size));
    }

    @PutMapping("/{id}/status")
    public Shipment setStatus(@PathVariable Integer id, @RequestBody StatusRequest req) {
        Shipment.Status st = Shipment.Status.valueOf(req.status());
        return service.updateStatus(id, st, req.trackingNumber(), req.carrier(), req.correlationId());
    }

    @PostMapping
    public Shipment manualCreate(@RequestBody OrderCreatedEvent ev) {
        service.handleOrderCreated(ev);
        return repo.findByOrderId(ev.orderId()).orElseThrow();
    }
}
