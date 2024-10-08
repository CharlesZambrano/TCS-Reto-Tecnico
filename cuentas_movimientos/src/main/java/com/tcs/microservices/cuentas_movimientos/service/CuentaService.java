package com.tcs.microservices.cuentas_movimientos.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.tcs.microservices.cuentas_movimientos.dto.ClienteDTO;
import com.tcs.microservices.cuentas_movimientos.dto.CuentaDTO;
import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
import com.tcs.microservices.cuentas_movimientos.repository.CuentaRepository;
import com.tcs.microservices.cuentas_movimientos.util.exception.ClienteNoEncontradoException;
import com.tcs.microservices.cuentas_movimientos.util.mapper.CuentaMovimientoMapper;
import com.tcs.microservices.cuentas_movimientos.util.mapper.UniqueIdGeneration;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMovimientoMapper cuentaMovimientoMapper;
    private final UniqueIdGeneration uniqueIdGeneration;
    private final RestTemplate restTemplate;

    @Value("${clientes.personas.base.url}")
    private String clientesPersonasBaseUrl;

    public CuentaService(CuentaRepository cuentaRepository, CuentaMovimientoMapper cuentaMovimientoMapper,
            UniqueIdGeneration uniqueIdGeneration, RestTemplate restTemplate) {
        this.cuentaRepository = cuentaRepository;
        this.cuentaMovimientoMapper = cuentaMovimientoMapper;
        this.uniqueIdGeneration = uniqueIdGeneration;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<CuentaDTO> obtenerCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(cuentaMovimientoMapper::cuentaToCuentaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaPorId(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuenta);
    }

    @Transactional
    public CuentaDTO crearCuenta(CuentaDTO cuentaDTO) {
        // Validar que el cliente existe antes de proceder
        validarClienteExistente(cuentaDTO.getClienteId());

        // Validar que el saldo inicial no sea negativo
        validarSaldoInicial(cuentaDTO.getSaldoInicial());

        // Mapear el DTO a la entidad Cuenta
        Cuenta cuenta = cuentaMovimientoMapper.cuentaDTOToCuenta(cuentaDTO);
        // Generar un uniqueId para la cuenta
        cuenta.setUniqueId(uniqueIdGeneration.getUniqueId());
        // Guardar la cuenta en el repositorio
        cuenta = cuentaRepository.save(cuenta);

        // Devolver el DTO mapeado desde la entidad
        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuenta);
    }

    @Transactional
    public CuentaDTO actualizarCuenta(Long id, CuentaDTO cuentaDTO) {
        Cuenta cuentaExistente = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));

        cuentaMovimientoMapper.cuentaDTOToCuenta(cuentaDTO);
        cuentaExistente.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuentaExistente.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuentaRepository.save(cuentaExistente);

        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuentaExistente);
    }

    @Transactional
    public void eliminarCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        cuentaRepository.delete(cuenta);
    }

    private void validarClienteExistente(Long clienteId) {
        String url = clientesPersonasBaseUrl + "/clientes/" + clienteId;

        try {
            ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(url, ClienteDTO.class);
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null
                    || response.getBody().getPersona() == null) {
                throw new ClienteNoEncontradoException("Cliente no encontrado con el ID: " + clienteId);
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ClienteNoEncontradoException("Cliente no encontrado con el ID: " + clienteId);
        }
    }

    private void validarSaldoInicial(BigDecimal saldoInicial) {
        // Verifica que el saldo inicial sea mayor o igual a 0
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo para ningún tipo de cuenta.");
        }
    }
}
