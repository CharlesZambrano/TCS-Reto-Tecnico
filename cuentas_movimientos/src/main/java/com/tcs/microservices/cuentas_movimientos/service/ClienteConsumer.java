package com.tcs.microservices.cuentas_movimientos.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.tcs.microservices.cuentas_movimientos.config.RabbitMQConfig;

@Service
public class ClienteConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void recibirClienteEvento(String mensaje) {
        System.out.println("Evento recibido: " + mensaje);
    }
}
