package com.tcs.microservices.clientes_personas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 4, max = 10, message = "La contraseña debe tener entre 4 y 10 caracteres")
    private String contraseña;

    @NotNull(message = "El estado no puede estar vacío")
    private Boolean estado;

    @NotNull(message = "La persona no puede estar vacía")
    private PersonaDTO persona;
}
