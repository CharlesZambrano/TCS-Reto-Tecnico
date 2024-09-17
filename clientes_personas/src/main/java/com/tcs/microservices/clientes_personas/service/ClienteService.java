package com.tcs.microservices.clientes_personas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcs.microservices.clientes_personas.dto.ClienteDTO;
import com.tcs.microservices.clientes_personas.model.Cliente;
import com.tcs.microservices.clientes_personas.repository.ClienteRepository;
import com.tcs.microservices.clientes_personas.util.mapper.ClientePersonaMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClientePersonaMapper clientePersonaMapper;
    private final ClientePublisher clientePublisher;

    public ClienteService(ClienteRepository clienteRepository, ClientePersonaMapper clientePersonaMapper,
            ClientePublisher clientePublisher) {
        this.clienteRepository = clienteRepository;
        this.clientePersonaMapper = clientePersonaMapper;
        this.clientePublisher = clientePublisher;
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(clientePersonaMapper::clienteToClienteDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        return clientePersonaMapper.clienteToClienteDTO(cliente);
    }

    @Transactional
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        Cliente cliente = clientePersonaMapper.clienteDTOToCliente(clienteDTO);
        cliente = clienteRepository.save(cliente);

        String mensaje = "Cliente creado con ID: " + cliente.getId();
        clientePublisher.publishClienteCreado(mensaje);

        return clientePersonaMapper.clienteToClienteDTO(cliente);
    }

    @Transactional
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        clientePersonaMapper.clienteDTOToCliente(clienteDTO);
        clienteExistente.setContraseña(clienteDTO.getContraseña());
        clienteExistente.setEstado(clienteDTO.getEstado());
        clienteExistente.getPersona().setNombre(clienteDTO.getPersona().getNombre());
        clienteRepository.save(clienteExistente);

        String mensaje = "Cliente actualizado con ID: " + clienteExistente.getId();
        clientePublisher.publishClienteActualizado(mensaje);

        return clientePersonaMapper.clienteToClienteDTO(clienteExistente);
    }

    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        clienteRepository.delete(cliente);

        String mensaje = "Cliente eliminado con ID: " + cliente.getId();
        clientePublisher.publishClienteEliminado(mensaje);
    }
}
