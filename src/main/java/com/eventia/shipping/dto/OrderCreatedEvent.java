package com.eventia.shipping.dto;

import java.time.Instant;

public record OrderCreatedEvent(
        String eventId,
        String type,
        Instant occurredAt,
        String correlationId,
        String orderId,
        String userId,
        String userFullname,
        String userEmail,
        String shippingAddress,
        String telephone
) {
}
