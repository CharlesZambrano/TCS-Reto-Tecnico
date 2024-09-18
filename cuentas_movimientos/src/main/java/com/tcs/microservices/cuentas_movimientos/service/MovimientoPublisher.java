package com.tcs.microservices.cuentas_movimientos.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.microservices.cuentas_movimientos.config.RabbitMQConfig;

@Service
public class MovimientoPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishMovimiento(String mensaje) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, mensaje);
    }
}
