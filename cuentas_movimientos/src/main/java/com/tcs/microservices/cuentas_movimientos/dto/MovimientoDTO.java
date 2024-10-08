package com.tcs.microservices.cuentas_movimientos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Size(max = 3, min = 3, message = "Los tipo de cuenta permitidos son 'RET' = 'RETIROS' y 'DEP' = 'DEPOSITOS'")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull(message = "El valor del movimiento no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor del movimiento debe ser mayor que cero")
    private BigDecimal valor;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(max = 20, message = "El número de cuenta tiene un máximo de 20 números")
    @Pattern(regexp = "^[0-9]+$", message = "El número de cuenta solo debe contener dígitos numéricos")
    private String numeroCuenta;

    private BigDecimal saldoInicial;
    private BigDecimal saldoDisponible;
}
