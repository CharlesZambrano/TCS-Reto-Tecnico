package com.tcs.microservices.cuentas_movimientos.service;

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
        validarClienteExistente(cuentaDTO.getClienteId());

        Cuenta cuenta = cuentaMovimientoMapper.cuentaDTOToCuenta(cuentaDTO);
        cuenta.setUniqueId(uniqueIdGeneration.getUniqueId());
        cuenta = cuentaRepository.save(cuenta);

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
}
