package com.eventia.shipping.listener;

import com.eventia.shipping.dto.OrderCreatedEvent;
import com.eventia.shipping.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final ShipmentService service;

    @RabbitListener(queues = "${app.rabbit.queue}")
    public void onMessage(OrderCreatedEvent event) {
        service.handleOrderCreated(event);
    }
}
