package com.tcs.microservices.cuentas_movimientos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {
    private Long id;
    private String contraseña;
    private Boolean estado;
    private PersonaDTO persona;
}
