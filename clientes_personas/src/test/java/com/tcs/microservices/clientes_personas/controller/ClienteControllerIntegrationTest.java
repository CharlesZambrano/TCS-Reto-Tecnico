package com.tcs.microservices.clientes_personas.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.microservices.clientes_personas.dto.ClienteDTO;
import com.tcs.microservices.clientes_personas.dto.PersonaDTO;
import com.tcs.microservices.clientes_personas.service.ClientePublisher;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private ClientePublisher clientePublisher;

        @Test
        public void testCrearCliente() throws Exception {
                PersonaDTO personaDTO = PersonaDTO.builder()
                                .id(1L)
                                .nombre("John Doe")
                                .genero("MAS")
                                .edad(25) // Edad válida mayor a 18
                                .identificacion("0102030405") // Identificación válida
                                .direccion("123 Calle Falsa")
                                .telefono("0998271630") // Teléfono válido
                                .build();

                ClienteDTO clienteDTO = ClienteDTO.builder()
                                .id(1L)
                                .contraseña("1111")
                                .estado(true)
                                .persona(personaDTO)
                                .build();

                assertNotNull(clienteDTO.getPersona().getNombre(), "Nombre no puede ser nulo");
                assertNotNull(clienteDTO.getContraseña(), "Contraseña no puede ser nula");
                assertNotNull(clienteDTO.getPersona().getIdentificacion(), "Identificación no puede ser nula");

                String jsonContent = objectMapper.writeValueAsString(clienteDTO);

                mockMvc.perform(post("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent))
                                .andExpect(status().isCreated());
        }

        @Test
        public void testObtenerClientes() throws Exception {
                // Crear un cliente de prueba antes de realizar la consulta
                PersonaDTO personaDTO = PersonaDTO.builder()
                                .id(1L)
                                .nombre("John Doe")
                                .genero("MAS")
                                .edad(25) // Edad válida
                                .identificacion("0102030405") // Identificación válida
                                .direccion("123 Calle Falsa")
                                .telefono("0998271630") // Teléfono válido
                                .build();

                ClienteDTO clienteDTO = ClienteDTO.builder()
                                .id(1L)
                                .contraseña("1111")
                                .estado(true)
                                .persona(personaDTO)
                                .build();

                String jsonContent = objectMapper.writeValueAsString(clienteDTO);

                // Insertar cliente de prueba
                mockMvc.perform(post("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent))
                                .andExpect(status().isCreated());

                // Ahora realizar la consulta de clientes
                mockMvc.perform(get("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isArray());
        }
}
