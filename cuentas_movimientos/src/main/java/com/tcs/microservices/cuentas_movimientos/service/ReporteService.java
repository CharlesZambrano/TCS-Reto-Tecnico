package com.tcs.microservices.cuentas_movimientos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tcs.microservices.cuentas_movimientos.dto.ClienteDTO;
import com.tcs.microservices.cuentas_movimientos.dto.ReporteMovimientoDTO;
import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
import com.tcs.microservices.cuentas_movimientos.model.Movimiento;
import com.tcs.microservices.cuentas_movimientos.repository.CuentaRepository;
import com.tcs.microservices.cuentas_movimientos.repository.MovimientoRepository;
import com.tcs.microservices.cuentas_movimientos.util.mapper.CuentaMovimientoMapper;

@Service
public class ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final CuentaMovimientoMapper cuentaMovimientoMapper;
    private final RestTemplate restTemplate;

    @Value("${clientes.personas.base.url}")
    private String clientesPersonasBaseUrl;

    public ReporteService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository,
            CuentaMovimientoMapper cuentaMovimientoMapper, RestTemplate restTemplate) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.cuentaMovimientoMapper = cuentaMovimientoMapper;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<ReporteMovimientoDTO> generarReporte(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        // Obtener las cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        // Obtener el nombre del cliente desde el microservicio clientes_personas
        String clienteNombre = obtenerNombreCliente(clienteId);

        return cuentas.stream()
                .flatMap(cuenta -> {
                    // Obtener movimientos ordenados por fecha ascendente
                    List<Movimiento> movimientos = movimientoRepository
                            .findByCuenta_NumeroCuentaAndFechaBetweenOrderByFechaAsc(cuenta.getNumeroCuenta(), inicio,
                                    fin);

                    // Usar AtomicReference para manejar el saldo inicial y permitir que sea
                    // modificable dentro del stream
                    AtomicReference<BigDecimal> saldoInicialCuenta = new AtomicReference<>(cuenta.getSaldoInicial());

                    return movimientos.stream().map(movimiento -> {
                        // Crear reporte usando el saldo inicial actual
                        ReporteMovimientoDTO reporte = cuentaMovimientoMapper.toReporteMovimientoDTO(cuenta,
                                movimiento);

                        // Asignar el saldo inicial correctamente: para el primer movimiento debe ser el
                        // saldo inicial de la cuenta,
                        // para los siguientes movimientos, el saldo inicial debe ser el saldo
                        // disponible del movimiento anterior
                        reporte.setSaldoInicial(saldoInicialCuenta.get());

                        // Actualizar el saldo inicial para el siguiente movimiento (saldo disponible
                        // del movimiento actual)
                        saldoInicialCuenta.set(movimiento.getSaldoDisponible());

                        // Asignar el nombre del cliente
                        reporte.setClienteNombre(clienteNombre);

                        return reporte;
                    });
                })
                .collect(Collectors.toList());
    }

    private String obtenerNombreCliente(Long clienteId) {
        String url = clientesPersonasBaseUrl + "/clientes/" + clienteId;
        ClienteDTO clienteDTO = restTemplate.getForObject(url, ClienteDTO.class);

        // Obtener el nombre de la persona asociada al cliente
        return clienteDTO != null && clienteDTO.getPersona() != null ? clienteDTO.getPersona().getNombre()
                : "Cliente Desconocido";
    }
}
