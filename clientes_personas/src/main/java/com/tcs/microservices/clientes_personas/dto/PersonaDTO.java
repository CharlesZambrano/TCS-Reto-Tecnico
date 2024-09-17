package com.tcs.microservices.clientes_personas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonaDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @Size(max = 3, message = "El género no puede tener más de 3 caracteres")
    private String genero;

    private Integer edad;

    @NotBlank(message = "La identificación no puede estar vacía")
    @Size(max = 20, message = "La identificación no puede tener más de 20 caracteres")
    private String identificacion;

    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    private String direccion;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    private String telefono;
}
