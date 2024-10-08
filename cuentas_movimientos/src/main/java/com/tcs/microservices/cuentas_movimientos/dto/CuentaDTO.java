package com.tcs.microservices.cuentas_movimientos.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
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
public class CuentaDTO {

    private Long id;

    private String uniqueId;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(max = 20, message = "El número de cuenta tiene un máximo de 20 números")
    @Pattern(regexp = "^[0-9]+$", message = "El número de cuenta solo debe contener dígitos numéricos")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta no puede estar vacío")
    @Size(max = 3, min = 3, message = "Los tipo de cuenta permitidos son 'AHO' = 'AHORROS' y 'COR' = 'CORRIENTE'")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull(message = "El saldo inicial no puede estar vacío")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado no puede estar vacío")
    private Boolean estado;

    @NotNull(message = "El cliente asociado no puede estar vacío")
    private Long clienteId;
}
