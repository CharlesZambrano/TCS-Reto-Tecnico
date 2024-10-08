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
                .map(cuenta -> {
                    ClienteDTO clienteDTO = obtenerCliente(cuenta.getClienteId());
                    return cuentaMovimientoMapper.cuentaToCuentaDTO(cuenta, clienteDTO);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaPorId(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        ClienteDTO clienteDTO = obtenerCliente(cuenta.getClienteId());
        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuenta, clienteDTO);
    }

    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaPorNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cuenta no encontrada con el número de cuenta: " + numeroCuenta));
        ClienteDTO clienteDTO = obtenerCliente(cuenta.getClienteId());
        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuenta, clienteDTO);
    }

    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaPorUniqueId(String uniqueId) {
        Cuenta cuenta = cuentaRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con el uniqueId: " + uniqueId));
        ClienteDTO clienteDTO = obtenerCliente(cuenta.getClienteId());
        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuenta, clienteDTO);
    }

    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaPorIdentificacionCliente(String identificacion) {
        String url = clientesPersonasBaseUrl + "/clientes/identificacion/" + identificacion;
        ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(url, ClienteDTO.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado con la identificación: " + identificacion);
        }

        ClienteDTO clienteDTO = response.getBody();
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteDTO.getId());
        if (cuentas.isEmpty()) {
            throw new EntityNotFoundException(
                    "Cuenta no encontrada para el cliente con identificación: " + identificacion);
        }
        Cuenta cuenta = cuentas.get(0);

        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuenta, clienteDTO);
    }

    @Transactional
    public CuentaDTO crearCuenta(CuentaDTO cuentaDTO) {
        validarClienteExistente(cuentaDTO.getCliente().getId());
        validarSaldoInicial(cuentaDTO.getSaldoInicial());

        Cuenta cuenta = cuentaMovimientoMapper.cuentaDTOToCuenta(cuentaDTO);
        cuenta.setUniqueId(uniqueIdGeneration.getUniqueId());
        cuenta = cuentaRepository.save(cuenta);

        ClienteDTO clienteDTO = obtenerCliente(cuenta.getClienteId());

        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuenta, clienteDTO);
    }

    @Transactional
    public CuentaDTO actualizarCuenta(Long id, CuentaDTO cuentaDTO) {
        Cuenta cuentaExistente = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));

        cuentaExistente.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuentaExistente.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuentaExistente.setEstado(cuentaDTO.getEstado());
        cuentaRepository.save(cuentaExistente);

        ClienteDTO clienteDTO = obtenerCliente(cuentaExistente.getClienteId());

        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuentaExistente, clienteDTO);
    }

    @Transactional
    public CuentaDTO actualizarCuentaPorNumeroCuenta(String numeroCuenta, CuentaDTO cuentaDTO) {
        Cuenta cuentaExistente = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cuenta no encontrada con el número de cuenta: " + numeroCuenta));

        cuentaExistente.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuentaExistente.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuentaExistente.setEstado(cuentaDTO.getEstado());

        cuentaRepository.save(cuentaExistente);
        ClienteDTO clienteDTO = obtenerCliente(cuentaExistente.getClienteId());

        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuentaExistente, clienteDTO);
    }

    @Transactional
    public CuentaDTO actualizarCuentaPorUniqueId(String uniqueId, CuentaDTO cuentaDTO) {
        Cuenta cuentaExistente = cuentaRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con el uniqueId: " + uniqueId));

        cuentaExistente.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuentaExistente.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuentaExistente.setEstado(cuentaDTO.getEstado());

        cuentaRepository.save(cuentaExistente);
        ClienteDTO clienteDTO = obtenerCliente(cuentaExistente.getClienteId());

        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuentaExistente, clienteDTO);
    }

    @Transactional
    public CuentaDTO actualizarCuentaPorIdentificacionCliente(String identificacion, CuentaDTO cuentaDTO) {
        String url = clientesPersonasBaseUrl + "/clientes/identificacion/" + identificacion;
        ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(url, ClienteDTO.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado con la identificación: " + identificacion);
        }

        ClienteDTO clienteDTO = response.getBody();
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteDTO.getId());
        if (cuentas.isEmpty()) {
            throw new EntityNotFoundException(
                    "Cuenta no encontrada para el cliente con identificación: " + identificacion);
        }

        Cuenta cuentaExistente = cuentas.get(0);
        cuentaExistente.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuentaExistente.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuentaExistente.setEstado(cuentaDTO.getEstado());

        cuentaRepository.save(cuentaExistente);
        return cuentaMovimientoMapper.cuentaToCuentaDTO(cuentaExistente, clienteDTO);
    }

    @Transactional
    public void eliminarCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        cuentaRepository.delete(cuenta);
    }

    @Transactional
    public void eliminarCuentaPorNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cuenta no encontrada con el número de cuenta: " + numeroCuenta));
        cuentaRepository.delete(cuenta);
    }

    @Transactional
    public void eliminarCuentaPorUniqueId(String uniqueId) {
        Cuenta cuenta = cuentaRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con el uniqueId: " + uniqueId));
        cuentaRepository.delete(cuenta);
    }

    @Transactional
    public void eliminarCuentaPorIdentificacionCliente(String identificacion) {
        String url = clientesPersonasBaseUrl + "/clientes/identificacion/" + identificacion;
        ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(url, ClienteDTO.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado con la identificación: " + identificacion);
        }

        ClienteDTO clienteDTO = response.getBody();
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteDTO.getId());
        if (cuentas.isEmpty()) {
            throw new EntityNotFoundException(
                    "Cuenta no encontrada para el cliente con identificación: " + identificacion);
        }

        Cuenta cuenta = cuentas.get(0);
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

    private ClienteDTO obtenerCliente(Long clienteId) {
        String url = clientesPersonasBaseUrl + "/clientes/" + clienteId;
        ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(url, ClienteDTO.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado con el ID: " + clienteId);
        }
        return response.getBody();
    }

    private void validarSaldoInicial(BigDecimal saldoInicial) {
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo para ningún tipo de cuenta.");
        }
    }
}
