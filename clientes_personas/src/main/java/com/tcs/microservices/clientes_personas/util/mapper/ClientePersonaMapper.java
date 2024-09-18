package com.tcs.microservices.clientes_personas.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tcs.microservices.clientes_personas.dto.ClienteDTO;
import com.tcs.microservices.clientes_personas.dto.PersonaDTO;
import com.tcs.microservices.clientes_personas.model.Cliente;
import com.tcs.microservices.clientes_personas.model.Persona;

@Mapper(componentModel = "spring")
public interface ClientePersonaMapper {
    ClientePersonaMapper INSTANCE = Mappers.getMapper(ClientePersonaMapper.class);

    ClienteDTO clienteToClienteDTO(Cliente cliente);

    Cliente clienteDTOToCliente(ClienteDTO clienteDTO);

    PersonaDTO personaToPersonaDTO(Persona persona);

    Persona personaDTOToPersona(PersonaDTO personaDTO);
}
