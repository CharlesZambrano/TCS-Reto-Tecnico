package com.tcs.microservices.cuentas_movimientos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaDTO {
    private Long id;
    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}