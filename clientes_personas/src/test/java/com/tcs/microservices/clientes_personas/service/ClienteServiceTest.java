package com.tcs.microservices.clientes_personas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tcs.microservices.clientes_personas.dto.ClienteDTO;
import com.tcs.microservices.clientes_personas.model.Cliente;
import com.tcs.microservices.clientes_personas.repository.ClienteRepository;
import com.tcs.microservices.clientes_personas.util.mapper.ClientePersonaMapper;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClientePersonaMapper clientePersonaMapper;

    @Mock
    private ClientePublisher clientePublisher;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    public void setup() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setContraseña("2222");
        cliente.setEstado(true);

        clienteDTO = ClienteDTO.builder()
                .id(1L)
                .contraseña("2222")
                .estado(true)
                .build();
    }

    @Test
    public void testObtenerClientePorId_Existente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clientePersonaMapper.clienteToClienteDTO(cliente)).thenReturn(clienteDTO);

        ClienteDTO resultado = clienteService.obtenerClientePorId(1L);

        assertEquals(clienteDTO.getId(), resultado.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerClientePorId_NoExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clienteService.obtenerClientePorId(1L));
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    public void testCrearCliente() {
        when(clientePersonaMapper.clienteDTOToCliente(clienteDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clientePersonaMapper.clienteToClienteDTO(cliente)).thenReturn(clienteDTO);

        doNothing().when(clientePublisher).publishClienteCreado(anyString());

        ClienteDTO resultado = clienteService.crearCliente(clienteDTO);

        assertEquals(clienteDTO.getId(), resultado.getId());
        verify(clienteRepository, times(1)).save(cliente);
        verify(clientePublisher, times(1)).publishClienteCreado(anyString());
    }
}
