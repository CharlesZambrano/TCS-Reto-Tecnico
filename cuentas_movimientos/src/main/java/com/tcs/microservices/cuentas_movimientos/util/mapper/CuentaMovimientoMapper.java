package com.tcs.microservices.cuentas_movimientos.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tcs.microservices.cuentas_movimientos.dto.CuentaDTO;
import com.tcs.microservices.cuentas_movimientos.dto.MovimientoDTO;
import com.tcs.microservices.cuentas_movimientos.dto.ReporteMovimientoDTO;
import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
import com.tcs.microservices.cuentas_movimientos.model.Movimiento;

@Mapper(componentModel = "spring")
public interface CuentaMovimientoMapper {

        CuentaMovimientoMapper INSTANCE = Mappers.getMapper(CuentaMovimientoMapper.class);

        CuentaDTO cuentaToCuentaDTO(Cuenta cuenta);

        Cuenta cuentaDTOToCuenta(CuentaDTO cuentaDTO);

        @Mapping(source = "cuenta.numeroCuenta", target = "numeroCuenta")
        MovimientoDTO movimientoToMovimientoDTO(Movimiento movimiento);

        @Mapping(source = "numeroCuenta", target = "cuenta.numeroCuenta")
        Movimiento movimientoDTOToMovimiento(MovimientoDTO movimientoDTO);

        @Mapping(source = "cuenta.numeroCuenta", target = "numeroCuenta")
        @Mapping(source = "cuenta.saldoInicial", target = "saldoInicial")
        @Mapping(source = "cuenta.tipo", target = "tipoCuenta")
        @Mapping(source = "movimiento.saldoDisponible", target = "saldoDisponible")
        @Mapping(source = "movimiento.fecha", target = "fechaMovimiento")
        @Mapping(source = "movimiento.tipo", target = "tipoMovimiento")
        @Mapping(source = "movimiento.valor", target = "valorMovimiento")
        @Mapping(target = "clienteNombre", ignore = true)
        ReporteMovimientoDTO toReporteMovimientoDTO(Cuenta cuenta, Movimiento movimiento);
}
