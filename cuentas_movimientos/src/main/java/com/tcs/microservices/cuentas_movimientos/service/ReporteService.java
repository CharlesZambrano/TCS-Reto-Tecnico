package com.tcs.microservices.cuentas_movimientos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
                    // Obtener movimientos por cuenta y fecha
                    List<Movimiento> movimientos = movimientoRepository
                            .findByCuenta_NumeroCuentaAndFechaBetween(cuenta.getNumeroCuenta(), inicio, fin);
                    return movimientos.stream()
                            .map(movimiento -> {
                                ReporteMovimientoDTO reporte = cuentaMovimientoMapper.toReporteMovimientoDTO(cuenta,
                                        movimiento);
                                reporte.setClienteNombre(clienteNombre); // Asignar el nombre del cliente
                                return reporte;
                            });
                })
                .collect(Collectors.toList());
    }

    private String obtenerNombreCliente(Long clienteId) {
        String url = clientesPersonasBaseUrl + "/clientes/" + clienteId;
        ClienteDTO clienteDTO = restTemplate.getForObject(url, ClienteDTO.class);

        return clienteDTO != null && clienteDTO.getPersona() != null ? clienteDTO.getPersona().getNombre()
                : "Cliente Desconocido";
    }
}
