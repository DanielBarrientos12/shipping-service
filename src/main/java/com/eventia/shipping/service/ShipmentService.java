package com.eventia.shipping.service;

import com.eventia.shipping.entity.ProcessedMessage;
import com.eventia.shipping.entity.Shipment;
import com.eventia.shipping.dto.OrderCreatedEvent;
import com.eventia.shipping.dto.ShippingDispatchedEvent;
import com.eventia.shipping.listener.EventPublisher;
import com.eventia.shipping.repository.ProcessedMessageRepository;
import com.eventia.shipping.repository.ShipmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository repo;
    private final ProcessedMessageRepository msgRepo;
    private final EventPublisher publisher;

    @Transactional
    public void handleOrderCreated(OrderCreatedEvent ev) {
        // evitar procesar el mismo evento dos veces
        if (msgRepo.existsById(ev.eventId())) return;

        Optional<Shipment> existing = repo.findByOrderId(ev.orderId());
        if (existing.isEmpty()) {
            Shipment shipment = Shipment.builder()
                    .orderId(ev.orderId())
                    .userId(ev.userId())
                    .userFullname(ev.userFullname())
                    .userEmail(ev.userEmail())
                    .shippingAddress(ev.shippingAddress())
                    .telephone(ev.telephone())
                    .status(Shipment.Status.PENDING)
                    .build();
            repo.save(shipment);
        }

        msgRepo.save(new ProcessedMessage(ev.eventId(), "order.created", Instant.now()));
    }

    @Transactional
    public Shipment updateStatus(Integer id, Shipment.Status status,
                                 String tracking, String carrier, String correlationId) {
        Shipment s = repo.findById(id).orElseThrow();
        s.setStatus(status);

        if (status == Shipment.Status.DISPATCHED) {
            s.setTrackingNumber(tracking);
            s.setCarrier(carrier);
            s.setDispatchedAt(Instant.now());
            repo.save(s);

            ShippingDispatchedEvent out = new ShippingDispatchedEvent(
                    UUID.randomUUID().toString(),
                    "shipping.dispatched.v1",
                    Instant.now(),
                    correlationId,
                    String.valueOf(s.getId()),
                    s.getOrderId(),
                    s.getUserId(),
                    s.getTrackingNumber(),
                    s.getCarrier(),
                    s.getDispatchedAt()
            );

            publisher.publish(out);
        }

        return repo.save(s);
    }
}
