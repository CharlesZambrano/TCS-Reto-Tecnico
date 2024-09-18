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
            return ResponseEntity.noContent().build(); // Devuelve 204 si no hay cuentas
        }
        return ResponseEntity.ok(cuentas); // Devuelve 200 si hay cuentas
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
            return ResponseEntity.ok(cuenta); // Devuelve 200 si la cuenta es encontrada
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devuelve 404 si no se encuentra la cuenta
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
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta); // Devuelve 201 si la cuenta es creada
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
            return ResponseEntity.ok(cuentaActualizada); // Devuelve 200 si la cuenta es actualizada
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devuelve 404 si no se encuentra la cuenta
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
            return ResponseEntity.noContent().build(); // Devuelve 204 si la cuenta es eliminada
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devuelve 404 si no se encuentra la cuenta
        }
    }
}
