package com.tcs.microservices.cuentas_movimientos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcs.microservices.cuentas_movimientos.dto.CuentaDTO;
import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
import com.tcs.microservices.cuentas_movimientos.repository.CuentaRepository;
import com.tcs.microservices.cuentas_movimientos.util.mapper.CuentaMovimientoMapper;
import com.tcs.microservices.cuentas_movimientos.util.mapper.UniqueIdGeneration;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMovimientoMapper cuentaMovimientoMapper;
    private final UniqueIdGeneration uniqueIdGeneration;

    public CuentaService(CuentaRepository cuentaRepository, CuentaMovimientoMapper cuentaMovimientoMapper,
            UniqueIdGeneration uniqueIdGeneration) {
        this.cuentaRepository = cuentaRepository;
        this.cuentaMovimientoMapper = cuentaMovimientoMapper;
        this.uniqueIdGeneration = uniqueIdGeneration;
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
}
