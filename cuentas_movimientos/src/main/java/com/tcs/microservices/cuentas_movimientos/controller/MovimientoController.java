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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@Tag(name = "Movimientos", description = "Operaciones relacionadas con los movimientos")
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

    @Operation(summary = "Obtener todos los movimientos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "204", description = "No se encontraron movimientos")
    })
    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> obtenerMovimientos() {
        List<MovimientoDTO> movimientos = movimientoService.obtenerMovimientos();
        if (movimientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movimientos);
    }

    @Operation(summary = "Obtener un movimiento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtenerMovimientoPorId(@PathVariable Long id) {
        try {
            MovimientoDTO movimiento = movimientoService.obtenerMovimientoPorId(id);
            return ResponseEntity.ok(movimiento);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener un movimiento por Unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/unique/{uniqueId}")
    public ResponseEntity<MovimientoDTO> obtenerMovimientoPorUniqueId(@PathVariable String uniqueId) {
        try {
            MovimientoDTO movimiento = movimientoService.obtenerMovimientoPorUniqueId(uniqueId);
            return ResponseEntity.ok(movimiento);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener movimientos por tipo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados"),
            @ApiResponse(responseCode = "404", description = "Movimientos no encontrados")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimientoDTO>> obtenerMovimientosPorTipo(@PathVariable String tipo) {
        try {
            List<MovimientoDTO> movimientos = movimientoService.obtenerMovimientosPorTipo(tipo);
            return ResponseEntity.ok(movimientos);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener movimientos por número de cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados"),
            @ApiResponse(responseCode = "404", description = "Movimientos no encontrados")
    })
    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<List<MovimientoDTO>> obtenerMovimientosPorNumeroCuenta(@PathVariable String numeroCuenta) {
        try {
            List<MovimientoDTO> movimientos = movimientoService.obtenerMovimientosPorNumeroCuenta(numeroCuenta);
            return ResponseEntity.ok(movimientos);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear un nuevo movimiento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la validación de los datos del movimiento")
    })
    @PostMapping
    public ResponseEntity<MovimientoDTO> crearMovimiento(@Validated @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO nuevoMovimiento = movimientoService.crearMovimiento(movimientoDTO);

        // Publicar mensajes adicionales si es necesario
        if (nuevoMovimiento.getSaldoDisponible().compareTo(BigDecimal.valueOf(100)) < 0) {
            movimientoPublisher.publishMovimiento("Saldo bajo: " + nuevoMovimiento.getSaldoDisponible());
        }

        if (movimientoDTO.getTipo().equals("RET") && movimientoDTO.getValor().compareTo(BigDecimal.valueOf(500)) > 0) {
            movimientoPublisher.publishMovimiento("Retiro grande: " + movimientoDTO.getValor());
        }

        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un movimiento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la validación de los datos del movimiento"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> actualizarMovimiento(@PathVariable Long id,
            @Validated @RequestBody MovimientoDTO movimientoDTO) {
        try {
            MovimientoDTO movimientoActualizado = movimientoService.actualizarMovimiento(id, movimientoDTO);
            return ResponseEntity.ok(movimientoActualizado);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar un movimiento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        try {
            movimientoService.eliminarMovimiento(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
