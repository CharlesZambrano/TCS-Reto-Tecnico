package com.tcs.microservices.clientes_personas.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.tcs.microservices.clientes_personas.config.RabbitMQConfig;

@Service
public class ClientePublisher {

    private final RabbitTemplate rabbitTemplate;

    public ClientePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishClienteCreado(String mensaje) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, mensaje);
    }

    public void publishClienteActualizado(String mensaje) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, mensaje);
    }

    public void publishClienteEliminado(String mensaje) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, mensaje);
    }
}
