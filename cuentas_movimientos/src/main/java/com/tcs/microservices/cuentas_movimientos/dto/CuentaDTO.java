package com.tcs.microservices.cuentas_movimientos.dto;

import java.math.BigDecimal;

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
public class CuentaDTO {

    private Long id;

    private String uniqueId;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(max = 20, message = "El número de cuenta no puede tener más de 20 caracteres")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta no puede estar vacío")
    @Size(max = 20, message = "El tipo de cuenta no puede tener más de 20 caracteres")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial debe ser positivo")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado no puede estar vacío")
    private Boolean estado;

    @NotNull(message = "El cliente asociado no puede estar vacío")
    private Long clienteId;
}
