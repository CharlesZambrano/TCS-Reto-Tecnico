package com.tcs.microservices.cuentas_movimientos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcs.microservices.cuentas_movimientos.dto.MovimientoDTO;
import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
import com.tcs.microservices.cuentas_movimientos.model.Movimiento;
import com.tcs.microservices.cuentas_movimientos.repository.CuentaRepository;
import com.tcs.microservices.cuentas_movimientos.repository.MovimientoRepository;
import com.tcs.microservices.cuentas_movimientos.util.mapper.CuentaMovimientoMapper;
import com.tcs.microservices.cuentas_movimientos.util.mapper.UniqueIdGeneration;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final CuentaMovimientoMapper cuentaMovimientoMapper;
    private final UniqueIdGeneration uniqueIdGeneration;

    public MovimientoService(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository,
            CuentaMovimientoMapper cuentaMovimientoMapper,
            UniqueIdGeneration uniqueIdGeneration) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.cuentaMovimientoMapper = cuentaMovimientoMapper;
        this.uniqueIdGeneration = uniqueIdGeneration;
    }

    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerMovimientos() {
        return movimientoRepository.findAll()
                .stream()
                .map(cuentaMovimientoMapper::movimientoToMovimientoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovimientoDTO obtenerMovimientoPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado"));
        return cuentaMovimientoMapper.movimientoToMovimientoDTO(movimiento);
    }

    @Transactional
    public MovimientoDTO crearMovimiento(MovimientoDTO movimientoDTO) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoDTO.getNumeroCuenta())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));

        Movimiento movimiento = cuentaMovimientoMapper.movimientoDTOToMovimiento(movimientoDTO);
        movimiento.setCuenta(cuenta);
        movimiento.setUniqueId(uniqueIdGeneration.getUniqueId());
        movimiento = movimientoRepository.save(movimiento);
        return cuentaMovimientoMapper.movimientoToMovimientoDTO(movimiento);
    }

    @Transactional
    public MovimientoDTO actualizarMovimiento(Long id, MovimientoDTO movimientoDTO) {
        Movimiento movimientoExistente = movimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado"));

        movimientoExistente.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimientoExistente.setValor(movimientoDTO.getValor());
        movimientoExistente.setSaldo(movimientoDTO.getSaldo());
        movimientoExistente.getCuenta().setNumeroCuenta(movimientoDTO.getNumeroCuenta());

        movimientoRepository.save(movimientoExistente);
        return cuentaMovimientoMapper.movimientoToMovimientoDTO(movimientoExistente);
    }

    @Transactional
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado"));
        movimientoRepository.delete(movimiento);
    }
}
