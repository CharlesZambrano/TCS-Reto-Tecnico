package com.tcs.microservices.clientes_personas.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull(message = "El genero no puede estar vacío")
    @Size(min = 3, max = 3, message = "Los generos son MAS=MASCULINO y FEM=FEMENINO")
    @Column(name = "genero", nullable = false)
    private String genero;

    @NotNull(message = "La edad no puede estar vacía")
    @Min(value = 18, message = "La edad debe ser mayor que 18")
    @Column(name = "edad")
    private Integer edad;

    @NotNull(message = "La cedula de identificación no puede estar vacía")
    @Pattern(regexp = "^(0[1-9]|1[0-9]|2[0-4]|30)[0-9]{8}$", message = "Cédula de identificación inválida")
    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @NotNull(message = "La direccion no puede estar vacía")
    @Size(max = 255, message = "La direccion no puede tener más de 255 caracteres")
    @Column(name = "direccion")
    private String direccion;

    @NotNull(message = "El telefono celular no puede estar vacío")
    @Pattern(regexp = "^09[0-9]{8}$", message = "Número de celular inválido")
    @Column(name = "telefono")
    private String telefono;
}
