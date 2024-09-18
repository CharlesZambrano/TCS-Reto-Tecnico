package com.tcs.microservices.cuentas_movimientos.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.microservices.cuentas_movimientos.dto.ClienteDTO;
import com.tcs.microservices.cuentas_movimientos.dto.CuentaDTO;
import com.tcs.microservices.cuentas_movimientos.dto.PersonaDTO;
import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
import com.tcs.microservices.cuentas_movimientos.repository.CuentaRepository;
import com.tcs.microservices.cuentas_movimientos.util.mapper.CuentaMovimientoMapper;
import com.tcs.microservices.cuentas_movimientos.util.mapper.UniqueIdGeneration;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CuentaControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private RestTemplate restTemplate;

        @MockBean
        private CuentaRepository cuentaRepository;

        @MockBean
        private CuentaMovimientoMapper cuentaMovimientoMapper;

        @MockBean
        private RabbitTemplate rabbitTemplate;

        @MockBean
        private UniqueIdGeneration uniqueIdGeneration;

        private CuentaDTO cuentaDTO;

        @BeforeEach
        public void setup() {
                // Crear una cuenta simulada
                cuentaDTO = CuentaDTO.builder()
                                .id(1L)
                                .numeroCuenta("1234567890")
                                .tipo("COR")
                                .saldoInicial(new BigDecimal("1000.00"))
                                .estado(true)
                                .clienteId(1L)
                                .build();

                // Crear una instancia simulada de Cuenta
                Cuenta cuenta = new Cuenta();
                cuenta.setId(1L);
                cuenta.setNumeroCuenta("1234567890");
                cuenta.setEstado(true);
                cuenta.setSaldoInicial(new BigDecimal("1000.00"));

                // Mockear mapper y repositorio
                when(cuentaMovimientoMapper.cuentaDTOToCuenta(any(CuentaDTO.class))).thenReturn(cuenta);
                when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
                when(cuentaMovimientoMapper.cuentaToCuentaDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

                // Mockear llamada al servicio REST
                ClienteDTO clienteDTO = new ClienteDTO();
                clienteDTO.setId(1L);
                clienteDTO.setContraseña("password");
                clienteDTO.setEstado(true);
                clienteDTO.setPersona(new PersonaDTO());

                when(restTemplate.getForEntity(eq("http://clientes-personas:8080/api/v1/clientes/1"),
                                eq(ClienteDTO.class)))
                                .thenReturn(new ResponseEntity<>(clienteDTO, HttpStatus.OK));

                // Mockear UniqueIdGeneration
                when(uniqueIdGeneration.getUniqueId()).thenReturn("some-unique-id");
        }

        @Test
        public void testCrearCuenta() throws Exception {
                // Simular la creación de la cuenta
                String jsonContent = objectMapper.writeValueAsString(cuentaDTO);

                // Realiza la petición y valida que el estado sea 201 CREATED
                mockMvc.perform(post("/api/v1/cuentas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent))
                                .andExpect(status().isCreated());
        }
}
