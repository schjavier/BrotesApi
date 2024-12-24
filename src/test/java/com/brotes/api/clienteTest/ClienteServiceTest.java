package com.brotes.api.clienteTest;

import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.ClienteDuplicadoException;
import com.brotes.api.modelo.cliente.*;
import com.brotes.api.validations.ClientValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

@Test
    void registrarCliente_debeLanzarexceptionSiExiste(){

    DatosRegistroCliente datosRegistro = new DatosRegistroCliente("Juan perez", "calle false 123", "2236547891");

    Mockito.doThrow(new ClienteDuplicadoException("El cliente ya existe"))
            .when(clientValidations)
            .validarClienteUnico(datosRegistro.nombre(), datosRegistro.direccion());

    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

    ClienteDuplicadoException exception = assertThrows(ClienteDuplicadoException.class,
            () -> clienteService.registrarCliente(datosRegistro, uriComponentsBuilder));


    assertEquals("El cliente ya existe", exception.getMessage());
    Mockito.verify(clientValidations).validarClienteUnico(datosRegistro.nombre(), datosRegistro.direccion());
    Mockito.verifyNoInteractions(clienteRepository);

}

@Test
void listarClientes_deRetornarListaPaginada(){
    Pageable pageable = PageRequest.of(0, 10);
    Page<Cliente> clientesPage = new PageImpl<>(List.of(new Cliente("Juan Pérez", "Calle Falsa 123", "123456789", true)));

    Mockito.when(clienteRepository.findAll(pageable)).thenReturn(clientesPage);

    Page<DatosListadoClientes> resultado = clienteService.listarClientes(pageable);

    assertNotNull(resultado);
    assertEquals(1, resultado.getTotalElements());
    assertEquals("Juan Pérez", resultado.getContent().get(0).nombre());
    Mockito.verify(clienteRepository).findAll(pageable);

}
@Test
void listarCliente_debeRetornarUnCliente(){

    Cliente cliente = new Cliente("Juan Pérez", "Calle Falsa 123", "123456789", true);
    cliente.setId(1L);

    Mockito.when(clienteRepository.getReferenceById(1L)).thenReturn(cliente);

    DatosRespuestaCliente clienteRespuesta = clienteService.listarUnCliente(1L);

    assertNotNull(clienteRespuesta);
    assertEquals(1L, clienteRespuesta.id());
    assertEquals("Juan Pérez", clienteRespuesta.nombre());
    assertEquals("Calle Falsa 123", clienteRespuesta.direccion());
    assertEquals("123456789", clienteRespuesta.telefono());
    assertTrue(clienteRespuesta.activo());

    Mockito.verify(clienteRepository).getReferenceById(1L);

    }

@Test
    void listarCliente_cuandoNoExiste_debeLanzarException(){
        Long idInexistente = 99L;

        Mockito.when(clienteRepository.getReferenceById(idInexistente))
                .thenThrow(ClientNotExistException.class);

        assertThrows(ClientNotExistException.class, () -> clienteService.listarUnCliente(idInexistente));

}



}
