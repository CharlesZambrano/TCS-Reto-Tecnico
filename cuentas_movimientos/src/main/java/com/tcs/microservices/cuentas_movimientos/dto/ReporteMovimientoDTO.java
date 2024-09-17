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
    private LocalDateTime fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipoCuenta;
    private String tipoMovimiento;
    private BigDecimal saldoInicial;
    private BigDecimal valorMovimiento;
    private BigDecimal saldoDisponible;
}
