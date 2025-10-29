package com.eventia.shipping.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.exchange}")
    private String exchange;

    @Value("${app.rabbit.routingKey.out}")
    private String routingKeyOut;

    public void publish(Object event) {
        rabbitTemplate.convertAndSend(exchange, routingKeyOut, event, msg -> {
            msg.getMessageProperties().setContentType("application/json");
            msg.getMessageProperties().setTimestamp(new Date());
            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return msg;
        });
    }
}
