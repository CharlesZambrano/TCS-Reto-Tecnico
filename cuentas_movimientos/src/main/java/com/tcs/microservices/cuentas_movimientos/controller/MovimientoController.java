package com.tcs.microservices.cuentas_movimientos.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.microservices.cuentas_movimientos.dto.MovimientoDTO;
import com.tcs.microservices.cuentas_movimientos.service.MovimientoPublisher;
import com.tcs.microservices.cuentas_movimientos.service.MovimientoService;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
@RestController
@RequestMapping("/api/v1/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;
    private final MovimientoPublisher movimientoPublisher;

    public MovimientoController(MovimientoService movimientoService, MovimientoPublisher movimientoPublisher) {
        this.movimientoService = movimientoService;
        this.movimientoPublisher = movimientoPublisher;
    }

    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> obtenerMovimientos() {
        List<MovimientoDTO> movimientos = movimientoService.obtenerMovimientos();
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtenerMovimientoPorId(@PathVariable Long id) {
        MovimientoDTO movimiento = movimientoService.obtenerMovimientoPorId(id);
        return ResponseEntity.ok(movimiento);
    }

    @PostMapping
    public ResponseEntity<MovimientoDTO> crearMovimiento(@Validated @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO nuevoMovimiento = movimientoService.crearMovimiento(movimientoDTO);
        // Publicar el mensaje a RabbitMQ si es un retiro grande o saldo bajo
        if (nuevoMovimiento.getSaldoDisponible().compareTo(BigDecimal.valueOf(100)) < 0) {
            movimientoPublisher.publishMovimiento("Saldo bajo: " + nuevoMovimiento.getSaldoDisponible());
        }

        if (movimientoDTO.getTipo().equals("RET") && movimientoDTO.getValor().compareTo(BigDecimal.valueOf(500)) > 0) {
            movimientoPublisher.publishMovimiento("Retiro grande: " + movimientoDTO.getValor());
        }

        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> actualizarMovimiento(@PathVariable Long id,
            @Validated @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO movimientoActualizado = movimientoService.actualizarMovimiento(id, movimientoDTO);
        return ResponseEntity.ok(movimientoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }

}
