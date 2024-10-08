package com.tcs.microservices.cuentas_movimientos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "cuenta")
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuenta_id")
    private Long id;

    @NotBlank(message = "El identificador único no puede estar vacío")
    @Size(max = 16, message = "El identificador único tiene maximo 16 caracteres")
    @Column(name = "unique_id", unique = true, nullable = false)
    private String uniqueId;

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(max = 20, message = "El número de cuenta tiene un máximo de 20 números")
    @Pattern(regexp = "^[0-9]+$", message = "El número de cuenta solo debe contener dígitos numéricos")
    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta no puede estar vacío")
    @Size(max = 3, min = 3, message = "Los tipo de cuenta permitidos son 'AHO' = 'AHORROS' y 'COR' = 'CORRIENTE'")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull(message = "El saldo inicial no puede estar vacío")
    @Column(name = "saldo_inicial", precision = 17, scale = 2, nullable = false)
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado no puede estar vacío")
    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @NotNull(message = "El cliente asociado no puede estar vacío")
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;
}
