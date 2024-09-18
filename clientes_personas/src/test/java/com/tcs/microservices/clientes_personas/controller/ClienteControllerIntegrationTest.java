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
        public void testObtenerClientes() throws Exception {
                mockMvc.perform(get("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isArray());
        }

        @Test
        public void testCrearCliente() throws Exception {
                PersonaDTO personaDTO = PersonaDTO.builder()
                                .id(1L)
                                .nombre("John Doe")
                                .genero("MAS")
                                .identificacion("1234567890")
                                .direccion("123 Calle Falsa")
                                .telefono("5551234567")
                                .build();

                ClienteDTO clienteDTO = ClienteDTO.builder()
                                .id(1L)
                                .contraseña("pass1234")
                                .estado(true)
                                .persona(personaDTO)
                                .build();

                assertNotNull(clienteDTO.getPersona().getNombre(), "Nombre no puede ser nulo");
                assertNotNull(clienteDTO.getContraseña(), "Contraseña no puede ser nula");
                assertNotNull(clienteDTO.getPersona().getIdentificacion(), "Identificación no puede ser nula");

                String jsonContent = objectMapper.writeValueAsString(clienteDTO);
                System.out.println("JSON Enviado: " + jsonContent);

                mockMvc.perform(post("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent))
                                .andExpect(status().isCreated());
        }
}
