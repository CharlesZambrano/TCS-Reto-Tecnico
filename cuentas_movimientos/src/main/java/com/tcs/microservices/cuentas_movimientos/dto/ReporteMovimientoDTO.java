package com.tcs.microservices.cuentas_movimientos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReporteMovimientoDTO {
    private String clienteNombre;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoDisponible;
    private LocalDateTime fechaMovimiento;
    private String tipoMovimiento;
    private BigDecimal valorMovimiento;
}
