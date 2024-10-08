package com.tcs.microservices.cuentas_movimientos.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tcs.microservices.cuentas_movimientos.dto.ClienteDTO;
import com.tcs.microservices.cuentas_movimientos.dto.CuentaDTO;
import com.tcs.microservices.cuentas_movimientos.dto.MovimientoDTO;
import com.tcs.microservices.cuentas_movimientos.dto.ReporteMovimientoDTO;
import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
import com.tcs.microservices.cuentas_movimientos.model.Movimiento;

@Mapper(componentModel = "spring")
public interface CuentaMovimientoMapper {

        CuentaMovimientoMapper INSTANCE = Mappers.getMapper(CuentaMovimientoMapper.class);

        @Mappings({
                        @Mapping(target = "cliente.id", source = "clienteDTO.id"),
                        @Mapping(target = "cliente.contraseña", source = "clienteDTO.contraseña"),
                        @Mapping(target = "cliente.estado", source = "clienteDTO.estado"),
                        @Mapping(target = "cliente.persona.id", source = "clienteDTO.persona.id"),
                        @Mapping(target = "cliente.persona.nombre", source = "clienteDTO.persona.nombre"),
                        @Mapping(target = "cliente.persona.genero", source = "clienteDTO.persona.genero"),
                        @Mapping(target = "cliente.persona.edad", source = "clienteDTO.persona.edad"),
                        @Mapping(target = "cliente.persona.identificacion", source = "clienteDTO.persona.identificacion"),
                        @Mapping(target = "cliente.persona.direccion", source = "clienteDTO.persona.direccion"),
                        @Mapping(target = "cliente.persona.telefono", source = "clienteDTO.persona.telefono"),
                        @Mapping(target = "id", source = "cuenta.id"),
                        @Mapping(target = "estado", source = "cuenta.estado")
        })
        CuentaDTO cuentaToCuentaDTO(Cuenta cuenta, ClienteDTO clienteDTO);

        @Mapping(target = "clienteId", source = "cliente.id")
        Cuenta cuentaDTOToCuenta(CuentaDTO cuentaDTO);

        @Mapping(source = "cuenta.numeroCuenta", target = "numeroCuenta")
        MovimientoDTO movimientoToMovimientoDTO(Movimiento movimiento);

        @Mapping(source = "numeroCuenta", target = "cuenta.numeroCuenta")
        Movimiento movimientoDTOToMovimiento(MovimientoDTO movimientoDTO);

        @Mappings({
                        @Mapping(source = "cuenta.numeroCuenta", target = "numeroCuenta"),
                        @Mapping(source = "cuenta.saldoInicial", target = "saldoInicial"),
                        @Mapping(source = "cuenta.tipo", target = "tipoCuenta"),
                        @Mapping(source = "movimiento.saldoDisponible", target = "saldoDisponible"),
                        @Mapping(source = "movimiento.fecha", target = "fecha"),
                        @Mapping(source = "movimiento.tipo", target = "tipoMovimiento"),
                        @Mapping(source = "movimiento.valor", target = "valorMovimiento"),
                        @Mapping(target = "cliente", ignore = true)
        })
        ReporteMovimientoDTO toReporteMovimientoDTO(Cuenta cuenta, Movimiento movimiento);
}
