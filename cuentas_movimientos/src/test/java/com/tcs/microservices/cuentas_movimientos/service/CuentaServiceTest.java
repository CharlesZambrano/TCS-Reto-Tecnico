// package com.tcs.microservices.cuentas_movimientos.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// import java.math.BigDecimal;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.util.ReflectionTestUtils;
// import org.springframework.web.client.RestTemplate;

// import com.tcs.microservices.cuentas_movimientos.dto.ClienteDTO;
// import com.tcs.microservices.cuentas_movimientos.dto.CuentaDTO;
// import com.tcs.microservices.cuentas_movimientos.dto.PersonaDTO;
// import com.tcs.microservices.cuentas_movimientos.model.Cuenta;
// import com.tcs.microservices.cuentas_movimientos.repository.CuentaRepository;
// import
// com.tcs.microservices.cuentas_movimientos.util.mapper.CuentaMovimientoMapper;
// import
// com.tcs.microservices.cuentas_movimientos.util.mapper.UniqueIdGeneration;

// @ExtendWith(MockitoExtension.class)
// public class CuentaServiceTest {

// @Mock
// private CuentaRepository cuentaRepository;

// @Mock
// private RestTemplate restTemplate;

// @Mock
// private CuentaMovimientoMapper cuentaMovimientoMapper;

// @Mock
// private UniqueIdGeneration uniqueIdGeneration;

// @InjectMocks
// private CuentaService cuentaService;

// private Cuenta cuenta;
// private CuentaDTO cuentaDTO;

// @BeforeEach
// public void setup() {
// ReflectionTestUtils.setField(cuentaService, "clientesPersonasBaseUrl",
// "http://localhost:8080");

// ClienteDTO clienteDTO = new ClienteDTO();
// clienteDTO.setId(1L);
// clienteDTO.setContraseña("password");
// clienteDTO.setEstado(true);
// clienteDTO.setPersona(new PersonaDTO());

// cuenta = new Cuenta();
// cuenta.setId(1L);
// cuenta.setNumeroCuenta("1234567890");
// cuenta.setEstado(true);
// cuenta.setSaldoInicial(new BigDecimal("1000.00"));

// cuentaDTO = new CuentaDTO();
// cuentaDTO.setId(1L);
// cuentaDTO.setNumeroCuenta("1234567890");
// cuentaDTO.setSaldoInicial(new BigDecimal("1000.00"));
// cuentaDTO.setEstado(true);
// cuentaDTO.setCliente(clienteDTO);
// }

// @Test
// public void testCrearCuenta() {
// ClienteDTO clienteDTO = new ClienteDTO();
// clienteDTO.setId(1L);
// clienteDTO.setContraseña("password");
// clienteDTO.setEstado(true);
// clienteDTO.setPersona(new PersonaDTO());

// when(restTemplate.getForEntity(eq("http://localhost:8080/api/v1/clientes/1"),
// eq(ClienteDTO.class)))
// .thenReturn(new ResponseEntity<>(clienteDTO, HttpStatus.OK));

// when(cuentaMovimientoMapper.cuentaDTOToCuenta(any(CuentaDTO.class))).thenReturn(cuenta);
// when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

// when(cuentaMovimientoMapper.cuentaToCuentaDTO(any(Cuenta.class),
// any(ClienteDTO.class)))
// .thenReturn(cuentaDTO);

// when(uniqueIdGeneration.getUniqueId()).thenReturn("some-unique-id");

// CuentaDTO resultado = cuentaService.crearCuenta(cuentaDTO);

// assertEquals(cuentaDTO.getId(), resultado.getId());
// verify(cuentaRepository, times(1)).save(cuenta);
// }
// }
