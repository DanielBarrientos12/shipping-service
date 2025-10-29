package com.eventia.shipping.dto;

import java.time.Instant;

public record ShippingDispatchedEvent(
        String eventId,
        String type,
        Instant occurredAt,
        String correlationId,
        String shipmentId,
        String orderId,
        String userId,
        String trackingNumber,
        String carrier,
        Instant dispatchedAt
) {
}
