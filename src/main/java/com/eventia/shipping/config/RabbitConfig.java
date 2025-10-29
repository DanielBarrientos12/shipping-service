package com.eventia.shipping.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConfig {

    @Value("${app.rabbit.exchange}") String exchangeName;
    @Value("${app.rabbit.queue}") String queueName;
    @Value("${app.rabbit.routingKey.in}") String routingKeyIn;

    @Bean TopicExchange eventExchange() { return new TopicExchange(exchangeName, true, false); }

    @Bean Queue orderCreatedQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "eventia.dlx")
                .withArgument("x-dead-letter-routing-key", "shipping.deadletter")
                .build();
    }

    @Bean Binding bindOrderCreated(Queue orderCreatedQueue, TopicExchange eventExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(eventExchange).with(routingKeyIn);
    }

    @Bean Jackson2JsonMessageConverter jsonMessageConverter() { return new Jackson2JsonMessageConverter(); }

    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }
}

