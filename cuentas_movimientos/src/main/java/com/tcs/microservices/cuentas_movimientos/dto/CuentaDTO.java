package com.tcs.microservices.cuentas_movimientos.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CuentaDTO {

    private Long id;
    private String uniqueId;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(max = 20, message = "El número de cuenta tiene un máximo de 20 números")
    @Pattern(regexp = "^[0-9]+$", message = "El número de cuenta solo debe contener dígitos numéricos")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta no puede estar vacío")
    @Size(max = 3, min = 3, message = "Los tipo de cuenta permitidos son 'AHO' = 'AHORROS' y 'COR' = 'CORRIENTE'")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull(message = "El saldo inicial no puede estar vacío")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado no puede estar vacío")
    private Boolean estado;

    private ClienteDTO cliente;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }
}
