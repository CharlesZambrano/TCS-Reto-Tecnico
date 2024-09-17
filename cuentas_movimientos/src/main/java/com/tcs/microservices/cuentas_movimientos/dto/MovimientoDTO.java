package com.tcs.microservices.cuentas_movimientos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovimientoDTO {

    private Long id;
    private String uniqueId;
    private LocalDateTime fecha;

    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    @Size(max = 3, message = "El tipo de movimiento no puede tener más de 3 caracteres")
    private String tipo;

    @NotNull(message = "El valor del movimiento no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor del movimiento debe ser mayor que cero")
    private BigDecimal valor;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    private String numeroCuenta;

    private BigDecimal saldoDisponible;
}
