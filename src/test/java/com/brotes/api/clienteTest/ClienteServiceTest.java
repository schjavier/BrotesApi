package com.brotes.api.clienteTest;

import com.brotes.api.modelo.cliente.*;
import com.brotes.api.validations.ClientValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    ClientValidations clientValidations;

    @InjectMocks
    ClienteServiceImpl clienteService;

    @Test
    void registrarCliente_deberiaRegistrarClienteCorrectamente(){
        DatosRegistroCliente datosRegistro = new DatosRegistroCliente("Juan perez", "calle false 123", "2236547891");

        Cliente cliente = new Cliente(datosRegistro);
        cliente.setId(1L);

        Mockito.doNothing().when(clientValidations).validarClienteUnico(datosRegistro.nombre(), datosRegistro.direccion());
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(cliente);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

        DatosRespuestaClienteConUrl respuesta = clienteService.registrarCliente(datosRegistro, uriComponentsBuilder);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.id());
        assertEquals("Juan perez", respuesta.nombre());
        assertEquals("calle false 123", respuesta.direccion());
        assertEquals("2236547891", respuesta.telefono());

        Mockito.verify(clientValidations).validarClienteUnico(datosRegistro.nombre(), datosRegistro.direccion());
        Mockito.verify(clienteRepository).save(Mockito.any(Cliente.class));


    }




}
