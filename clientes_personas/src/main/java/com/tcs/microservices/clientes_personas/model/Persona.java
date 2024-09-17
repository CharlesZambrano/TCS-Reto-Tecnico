package com.tcs.microservices.clientes_personas.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "persona")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Size(max = 3)
    @Column(name = "genero")
    private String genero;

    @Column(name = "edad")
    private Integer edad;

    @NotBlank(message = "La identificación no puede estar vacía")
    @Size(max = 20)
    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @Size(max = 255)
    @Column(name = "direccion")
    private String direccion;

    @Size(max = 15)
    @Column(name = "telefono")
    private String telefono;
}
