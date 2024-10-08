package com.tcs.microservices.clientes_personas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonaDTO {

    private Long id;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotNull(message = "El genero no puede estar vacío")
    @Size(min = 3, max = 3, message = "Los generos son MAS=MASCULINO y FEM=FEMENINO")
    private String genero;

    @NotNull(message = "La edad no puede estar vacía")
    @Min(value = 18, message = "La edad debe ser mayor que 18")
    private Integer edad;

    @NotNull(message = "La cedula de identificación no puede estar vacía")
    @Pattern(regexp = "^(0[1-9]|1[0-9]|2[0-4]|30)[0-9]{8}$", message = "Cédula de identificación inválida")
    private String identificacion;

    @NotNull(message = "La direccion no puede estar vacía")
    @Size(max = 255, message = "La direccion no puede tener más de 255 caracteres")
    private String direccion;

    @NotNull(message = "El telefono celular no puede estar vacío")
    @Pattern(regexp = "^09[0-9]{8}$", message = "Número de celular inválido")
    private String telefono;
}
