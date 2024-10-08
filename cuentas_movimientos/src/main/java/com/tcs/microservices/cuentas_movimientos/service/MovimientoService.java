package com.tcs.microservices.cuentas_movimientos.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcs.microservices.cuentas_movimientos.dto.MovimientoDTO;
import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
import com.tcs.microservices.cuentas_movimientos.model.Movimiento;
import com.tcs.microservices.cuentas_movimientos.repository.CuentaRepository;
import com.tcs.microservices.cuentas_movimientos.repository.MovimientoRepository;
import com.tcs.microservices.cuentas_movimientos.util.exception.SaldoInsuficienteException;
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
            CuentaMovimientoMapper cuentaMovimientoMapper, UniqueIdGeneration uniqueIdGeneration) {
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

    @Transactional(readOnly = true)
    public MovimientoDTO obtenerMovimientoPorUniqueId(String uniqueId) {
        Movimiento movimiento = movimientoRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con uniqueId: " + uniqueId));
        return cuentaMovimientoMapper.movimientoToMovimientoDTO(movimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerMovimientosPorTipo(String tipo) {
        List<Movimiento> movimientos = movimientoRepository.findByTipo(tipo);
        if (movimientos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron movimientos con el tipo: " + tipo);
        }
        return movimientos.stream()
                .map(cuentaMovimientoMapper::movimientoToMovimientoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerMovimientosPorNumeroCuenta(String numeroCuenta) {
        List<Movimiento> movimientos = movimientoRepository.findByCuenta_NumeroCuenta(numeroCuenta);
        if (movimientos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron movimientos para la cuenta: " + numeroCuenta);
        }
        return movimientos.stream()
                .map(cuentaMovimientoMapper::movimientoToMovimientoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MovimientoDTO crearMovimiento(MovimientoDTO movimientoDTO) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoDTO.getNumeroCuenta())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));

        BigDecimal nuevoSaldoDisponible = calcularNuevoSaldo(cuenta, movimientoDTO);

        Movimiento movimiento = cuentaMovimientoMapper.movimientoDTOToMovimiento(movimientoDTO);
        movimiento.setCuenta(cuenta);
        movimiento.setUniqueId(uniqueIdGeneration.getUniqueId());
        movimiento.setSaldoInicial(cuenta.getSaldoInicial());
        movimiento.setSaldoDisponible(nuevoSaldoDisponible);

        cuenta.setSaldoInicial(nuevoSaldoDisponible);
        cuentaRepository.save(cuenta);

        movimiento = movimientoRepository.save(movimiento);
        return cuentaMovimientoMapper.movimientoToMovimientoDTO(movimiento);
    }

    private BigDecimal calcularNuevoSaldo(Cuenta cuenta, MovimientoDTO movimientoDTO) {
        BigDecimal saldoActual = cuenta.getSaldoInicial();
        BigDecimal valorMovimiento = movimientoDTO.getValor();

        if (movimientoDTO.getTipo().equals("RET")) {
            if (cuenta.getTipo().equals("AHO")) {
                if (saldoActual.compareTo(valorMovimiento) < 0) {
                    throw new SaldoInsuficienteException(
                            "Saldo insuficiente, retiros no permitidos en cuentas de ahorro con saldo negativo.");
                }
                return saldoActual.subtract(valorMovimiento);
            }

            if (cuenta.getTipo().equals("COR")) {
                return saldoActual.subtract(valorMovimiento);
            }
        }

        if (movimientoDTO.getTipo().equals("DEP")) {
            return saldoActual.add(valorMovimiento);
        }

        throw new IllegalArgumentException("Tipo de movimiento no reconocido: " + movimientoDTO.getTipo());
    }

    @Transactional
    public MovimientoDTO actualizarMovimiento(Long id, MovimientoDTO movimientoDTO) {
        Movimiento movimientoExistente = movimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado"));

        movimientoExistente.setTipo(movimientoDTO.getTipo());
        movimientoExistente.setValor(movimientoDTO.getValor());

        if (!movimientoExistente.getCuenta().getNumeroCuenta().equals(movimientoDTO.getNumeroCuenta())) {
            Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoDTO.getNumeroCuenta())
                    .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
            movimientoExistente.setCuenta(cuenta);
        }

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
