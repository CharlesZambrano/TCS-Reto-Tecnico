package com.tcs.microservices.cuentas_movimientos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "movimiento")
public class Movimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long id;

    @NotBlank(message = "El identificador único no puede estar vacío")
    @Size(max = 16, message = "El identificador único tiene maximo 16 caracteres")
    @Column(name = "unique_id", unique = true, nullable = false)
    private String uniqueId;

    @NotBlank(message = "La fecha no puede estar vacía")
    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    @Size(max = 3, min = 3, message = "Los tipo de cuenta permitidos son 'RET' = 'RETIROS' y 'DEP' = 'DEPOSITOS'")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull(message = "El valor del movimiento no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor del movimiento debe ser mayor que cero")
    @Column(name = "valor", precision = 17, scale = 2, nullable = false)
    private BigDecimal valor;

    @NotNull(message = "El saldo inicial no puede estar vacío")
    @Column(name = "saldo_inicial", precision = 17, scale = 2, nullable = false)
    private BigDecimal saldoInicial;

    @NotNull(message = "El saldo disponible no puede estar vacío")
    @Column(name = "saldo_disponible", precision = 17, scale = 2, nullable = false)
    private BigDecimal saldoDisponible;

    @ManyToOne
    @JoinColumn(name = "numero_cuenta", referencedColumnName = "numero_cuenta", nullable = false)
    private Cuenta cuenta;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }
}
