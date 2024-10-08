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
                ClienteDTO clienteDTO = new ClienteDTO();
                clienteDTO.setId(1L);
                clienteDTO.setContraseña("password");
                clienteDTO.setEstado(true);
                clienteDTO.setPersona(new PersonaDTO());

                // En lugar de usar el builder, asignamos los valores directamente
                cuentaDTO = new CuentaDTO();
                cuentaDTO.setId(1L);
                cuentaDTO.setNumeroCuenta("1234567890");
                cuentaDTO.setTipo("COR");
                cuentaDTO.setSaldoInicial(new BigDecimal("1000.00"));
                cuentaDTO.setEstado(true);
                cuentaDTO.setCliente(clienteDTO);

                Cuenta cuenta = new Cuenta();
                cuenta.setId(1L);
                cuenta.setNumeroCuenta("1234567890");
                cuenta.setEstado(true);
                cuenta.setSaldoInicial(new BigDecimal("1000.00"));

                when(cuentaMovimientoMapper.cuentaDTOToCuenta(any(CuentaDTO.class))).thenReturn(cuenta);
                when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

                when(cuentaMovimientoMapper.cuentaToCuentaDTO(any(Cuenta.class), any(ClienteDTO.class)))
                                .thenReturn(cuentaDTO);

                when(restTemplate.getForEntity(eq("http://clientes-personas:8080/api/v1/clientes/5"),
                                eq(ClienteDTO.class)))
                                .thenReturn(new ResponseEntity<>(clienteDTO, HttpStatus.OK));

                when(uniqueIdGeneration.getUniqueId()).thenReturn("some-unique-id");
        }

        @Test
        public void testCrearCuenta() throws Exception {
                String jsonContent = objectMapper.writeValueAsString(cuentaDTO);

                mockMvc.perform(post("/api/v1/cuentas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent))
                                .andExpect(status().isCreated());
        }
}
