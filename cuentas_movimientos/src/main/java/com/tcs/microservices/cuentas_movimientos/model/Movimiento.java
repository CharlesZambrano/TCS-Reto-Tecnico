package com.tcs.microservices.cuentas_movimientos.model;

public class Movimiento {
    private String tipoMovimiento;
    private double valor;
    private double saldo;

    // Getters y Setters
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "tipoMovimiento='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", saldo=" + saldo +
                '}';
    }
}