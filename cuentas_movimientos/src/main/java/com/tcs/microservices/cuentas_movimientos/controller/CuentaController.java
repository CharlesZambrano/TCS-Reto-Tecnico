package com.tcs.microservices.cuentas_movimientos.controller;

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

import com.tcs.microservices.cuentas_movimientos.dto.CuentaDTO;
import com.tcs.microservices.cuentas_movimientos.service.CuentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@Tag(name = "Cuentas", description = "Operaciones relacionadas con las cuentas")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
@RestController
@RequestMapping("/api/v1/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @Operation(summary = "Obtener todas las cuentas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "204", description = "No se encontraron cuentas")
    })
    @GetMapping
    public ResponseEntity<List<CuentaDTO>> obtenerCuentas() {
        List<CuentaDTO> cuentas = cuentaService.obtenerCuentas();
        if (cuentas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cuentas);
    }

    @Operation(summary = "Obtener una cuenta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> obtenerCuentaPorId(@PathVariable Long id) {
        try {
            CuentaDTO cuenta = cuentaService.obtenerCuentaPorId(id);
            return ResponseEntity.ok(cuenta);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener una cuenta por Número de Cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaDTO> obtenerCuentaPorNumeroCuenta(@PathVariable String numeroCuenta) {
        try {
            CuentaDTO cuenta = cuentaService.obtenerCuentaPorNumeroCuenta(numeroCuenta);
            return ResponseEntity.ok(cuenta);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener una cuenta por Unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/unique/{uniqueId}")
    public ResponseEntity<CuentaDTO> obtenerCuentaPorUniqueId(@PathVariable String uniqueId) {
        try {
            CuentaDTO cuenta = cuentaService.obtenerCuentaPorUniqueId(uniqueId);
            return ResponseEntity.ok(cuenta);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener una cuenta por Identificación del Cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/cliente/identificacion/{identificacion}")
    public ResponseEntity<CuentaDTO> obtenerCuentaPorIdentificacionCliente(@PathVariable String identificacion) {
        try {
            CuentaDTO cuenta = cuentaService.obtenerCuentaPorIdentificacionCliente(identificacion);
            return ResponseEntity.ok(cuenta);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear una nueva cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la validación de los datos de la cuenta")
    })
    @PostMapping
    public ResponseEntity<CuentaDTO> crearCuenta(@Validated @RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO nuevaCuenta = cuentaService.crearCuenta(cuentaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
    }

    @Operation(summary = "Actualizar una cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la validación de los datos de la cuenta"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CuentaDTO> actualizarCuenta(@PathVariable Long id,
            @Validated @RequestBody CuentaDTO cuentaDTO) {
        try {
            CuentaDTO cuentaActualizada = cuentaService.actualizarCuenta(id, cuentaDTO);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Actualizar una cuenta por número de cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaDTO> actualizarCuentaPorNumeroCuenta(@PathVariable String numeroCuenta,
            @Validated @RequestBody CuentaDTO cuentaDTO) {
        try {
            CuentaDTO cuentaActualizada = cuentaService.actualizarCuentaPorNumeroCuenta(numeroCuenta, cuentaDTO);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Actualizar una cuenta por Unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/unique/{uniqueId}")
    public ResponseEntity<CuentaDTO> actualizarCuentaPorUniqueId(@PathVariable String uniqueId,
            @Validated @RequestBody CuentaDTO cuentaDTO) {
        try {
            CuentaDTO cuentaActualizada = cuentaService.actualizarCuentaPorUniqueId(uniqueId, cuentaDTO);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Actualizar una cuenta por Identificación del Cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/cliente/identificacion/{identificacion}")
    public ResponseEntity<CuentaDTO> actualizarCuentaPorIdentificacionCliente(@PathVariable String identificacion,
            @Validated @RequestBody CuentaDTO cuentaDTO) {
        try {
            CuentaDTO cuentaActualizada = cuentaService.actualizarCuentaPorIdentificacionCliente(identificacion,
                    cuentaDTO);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar una cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long id) {
        try {
            cuentaService.eliminarCuenta(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar una cuenta por número de cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @DeleteMapping("/numero/{numeroCuenta}")
    public ResponseEntity<Void> eliminarCuentaPorNumeroCuenta(@PathVariable String numeroCuenta) {
        try {
            cuentaService.eliminarCuentaPorNumeroCuenta(numeroCuenta);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar una cuenta por Unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @DeleteMapping("/unique/{uniqueId}")
    public ResponseEntity<Void> eliminarCuentaPorUniqueId(@PathVariable String uniqueId) {
        try {
            cuentaService.eliminarCuentaPorUniqueId(uniqueId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar una cuenta por Identificación del Cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @DeleteMapping("/cliente/identificacion/{identificacion}")
    public ResponseEntity<Void> eliminarCuentaPorIdentificacionCliente(@PathVariable String identificacion) {
        try {
            cuentaService.eliminarCuentaPorIdentificacionCliente(identificacion);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
