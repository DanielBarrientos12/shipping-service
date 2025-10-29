package com.eventia.shipping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "shipments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderId;
    private String userId;
    private String userFullname;
    private String userEmail;
    private String shippingAddress;
    private String telephone;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String carrier;
    private String trackingNumber;
    private Instant dispatchedAt;
    private Instant deliveredAt;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;

    public enum Status {PENDING, PROCESSING, DISPATCHED, DELIVERED, CANCELLED}

}

