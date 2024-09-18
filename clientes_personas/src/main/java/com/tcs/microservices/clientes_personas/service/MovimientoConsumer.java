package com.tcs.microservices.clientes_personas.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.tcs.microservices.clientes_personas.config.RabbitMQConfig;

@Service
public class MovimientoConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void recibirMensaje(String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
    }
}
