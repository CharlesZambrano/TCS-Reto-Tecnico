package com.tcs.microservices.cuentas_movimientos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.microservices.cuentas_movimientos.model.Movimiento;
import com.tcs.microservices.cuentas_movimientos.service.MovimientoPublisher;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoPublisher movimientoPublisher;

    @PostMapping
    public ResponseEntity<String> registrarMovimiento(@RequestBody Movimiento movimiento) {
        // Aquí agregarías la lógica para guardar el movimiento en la base de datos

        // Publicar el mensaje a RabbitMQ
        movimientoPublisher.publishMovimiento("Movimiento registrado: " + movimiento.toString());

        return ResponseEntity.ok("Movimiento registrado y mensaje publicado.");
    }
}
