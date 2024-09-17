package com.tcs.microservices.cuentas_movimientos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.microservices.cuentas_movimientos.model.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuenta_NumeroCuentaAndFechaBetween(String numeroCuenta, LocalDateTime fechaInicio,
            LocalDateTime fechaFin);
}
