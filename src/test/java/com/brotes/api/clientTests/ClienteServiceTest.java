package com.brotes.api.clientTests;

import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.ClienteActivadoException;
import com.brotes.api.exceptions.ClienteDesactivadoException;
import com.brotes.api.exceptions.ClienteDuplicadoException;
import com.brotes.api.modelo.cliente.*;
import com.brotes.api.validations.ClientValidations;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    ClientValidations clientValidations;

    @InjectMocks
    ClienteServiceImpl clienteService;

    private static final Long ID_CLIENTE = 1L;
    private static final String NOMBRE_CLIENTE = "Juan Perez";
    private static final String DIRECCION_CLIENTE = "Calle Falsa 123";

    private Cliente clienteActivo;
    private Cliente clienteInactivo;
    private DatosRegistroCliente datosRegistro;
    private DatosActualizarCliente datosActualizarCliente;
    private DatosActualizarCliente datosActualizarClienteInexistente;

    private UriComponentsBuilder uriComponentsBuilder;

    @BeforeEach
    void setUp(){
        //inicializamos cliente Activo
        clienteActivo = new Cliente(ID_CLIENTE, NOMBRE_CLIENTE, DIRECCION_CLIENTE, true);

        //inicializamos el cliente Inactivo
        clienteInactivo = new Cliente(2L, "Jose Inactivo", "Calle Imaginaria 343", false);

        //datos de registro
        datosRegistro = new DatosRegistroCliente(
                NOMBRE_CLIENTE,
                DIRECCION_CLIENTE
        );

        datosActualizarCliente = new DatosActualizarCliente(
                ID_CLIENTE,
                "Juan Perez Actualizado",
                "Nueva Direccion"
        );

        uriComponentsBuilder = UriComponentsBuilder.newInstance();

        datosActualizarClienteInexistente = new DatosActualizarCliente(
                23L,
                "Pedro",
                "Calle 123"
        );

    }

    @Test
    void registrarCliente_deberiaRegistrarClienteCorrectamente(){

        Mockito.doNothing().when(clientValidations).validarClienteUnico(datosRegistro.nombre(), datosRegistro.direccion());
        Mockito.when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActivo);

        DatosRespuestaClienteConUrl respuesta = clienteService.registrarCliente(datosRegistro, uriComponentsBuilder);

        assertNotNull(respuesta);
        assertEquals(ID_CLIENTE, respuesta.id());
        assertEquals(NOMBRE_CLIENTE, respuesta.nombre());
        assertEquals(DIRECCION_CLIENTE, respuesta.direccion());

        verify(clientValidations).validarClienteUnico(datosRegistro.nombre(), datosRegistro.direccion());
        verify(clienteRepository).save(any(Cliente.class));


    }

@Test
    void registrarCliente_debeLanzarexceptionSiExiste(){

    Mockito.doThrow(new ClienteDuplicadoException("El cliente ya existe"))
            .when(clientValidations)
            .validarClienteUnico(datosRegistro.nombre(), datosRegistro.direccion());

   // UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

    ClienteDuplicadoException exception = assertThrows(ClienteDuplicadoException.class,
            () -> clienteService.registrarCliente(datosRegistro, uriComponentsBuilder));


    assertEquals("El cliente ya existe", exception.getMessage());
    verify(clientValidations).validarClienteUnico(datosRegistro.nombre(), datosRegistro.direccion());
    Mockito.verifyNoInteractions(clienteRepository);

}

@Test
void listarClientes_deRetornarListaPaginada(){
    Pageable pageable = PageRequest.of(0, 10);
    Page<Cliente> clientesPage = new PageImpl<>(List.of(clienteActivo));

    Mockito.when(clienteRepository.findAll(pageable)).thenReturn(clientesPage);

    Page<DatosListadoClientes> resultado = clienteService.listarClientes(pageable);

    assertNotNull(resultado);
    assertEquals(1, resultado.getTotalElements());
    assertEquals(NOMBRE_CLIENTE, resultado.getContent().get(0).nombre());
    verify(clienteRepository).findAll(pageable);

}
@Test
void listarCliente_debeRetornarUnCliente(){

    Mockito.when(clienteRepository.getReferenceById(1L)).thenReturn(clienteActivo);

    DatosRespuestaCliente clienteRespuesta = clienteService.listarUnCliente(1L);

    assertNotNull(clienteRespuesta);
    assertEquals(ID_CLIENTE, clienteRespuesta.id());
    assertEquals(NOMBRE_CLIENTE, clienteRespuesta.nombre());
    assertEquals(DIRECCION_CLIENTE, clienteRespuesta.direccion());

    assertTrue(clienteRespuesta.activo());

    verify(clienteRepository).getReferenceById(1L);

    }

@Test
    void listarCliente_cuandoNoExiste_debeLanzarException(){
        Long idInexistente = 99L;

        Mockito.when(clienteRepository.getReferenceById(idInexistente))
                .thenThrow(ClientNotExistException.class);

        assertThrows(ClientNotExistException.class, () -> clienteService.listarUnCliente(idInexistente));

}

@Test
void modificarCliente_debeRetornarClienteModificado(){

        Mockito.when(clienteRepository.getReferenceById(1L)).thenReturn(clienteActivo);
        Mockito.when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActivo);

        DatosRespuestaCliente clienteModificado = clienteService.modificarCliente(datosActualizarCliente);

        assertNotNull(clienteModificado);
        assertEquals(ID_CLIENTE, clienteModificado.id());
        assertEquals("Juan Perez Actualizado", clienteModificado.nombre());
        assertEquals("Nueva Direccion", clienteModificado.direccion());
        assertTrue(clienteModificado.activo());

        verify(clienteRepository).getReferenceById(ID_CLIENTE);
        verify(clienteRepository).save(any(Cliente.class));

}
@Test
void modificarCliente_cuandoNoExiste_debeLanzarException(){


        Mockito.when(clienteRepository.getReferenceById(23L)).thenThrow(ClientNotExistException.class);

        assertThrows(ClientNotExistException.class, () ->
                clienteService.modificarCliente(datosActualizarClienteInexistente));

}
@Test
void eliminarCliente_cuandoExiste_debeRetornarTrue(){

Mockito.when(clienteRepository.existsById(ID_CLIENTE)).thenReturn(true);
Mockito.doNothing().when(clienteRepository).deleteById(ID_CLIENTE);

boolean clienteBorrado = clienteService.eliminarCliente(ID_CLIENTE);

assertTrue(clienteBorrado);

verify(clienteRepository).deleteById(ID_CLIENTE);
verify(clienteRepository).existsById(ID_CLIENTE);
}

@Test
void eliminarCliente_cuandoNoExiste_debeRetornarFalse(){

    Long idInexistente = 12L;

    Mockito.when(clienteRepository.existsById(idInexistente)).thenReturn(false);

    boolean clienteBorrado = clienteService.eliminarCliente(idInexistente);

    assertFalse(clienteBorrado);

    verify(clienteRepository).existsById(idInexistente);
    verify(clienteRepository, Mockito.never()).deleteById(idInexistente);
}

@Test
void desactivarCliente_cuandoExisteActivo_debeRetornarTrue(){

    Optional<Cliente> clienteOptional = Optional.of(clienteActivo);

    Mockito.when(clienteRepository.findById(ID_CLIENTE)).thenReturn(clienteOptional);
    Mockito.when(clienteRepository.getReferenceById(ID_CLIENTE)).thenReturn(clienteActivo);
    Mockito.doNothing().when(clientValidations).validarClienteActivo(clienteActivo);
    Mockito.when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActivo);

    boolean result = clienteService.desactivarCliente(ID_CLIENTE);

    assertTrue(result);
    verify(clienteRepository).save(clienteActivo);
    assertFalse(clienteActivo.isActivo());
}

@Test
void desactivarCliente_cuandoNoExiste_debeRetornarFalse(){
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = clienteService.desactivarCliente(999L);

        assertFalse(result);
        verify(clienteRepository, never()).save(any());

}

@Test
void desactivarCliente_cuandoExisteDesactivado_debeLanzarException(){

    Optional<Cliente> clienteOptional = Optional.of(clienteInactivo);

    when(clienteRepository.findById(2L)).thenReturn(clienteOptional);

    when(clienteRepository.getReferenceById(2L)).thenReturn(clienteInactivo);

    doThrow(new ClienteDesactivadoException("El cliente ya se encuentra dasactivado"))
            .when(clientValidations).validarClienteActivo(clienteInactivo);

    assertThrows(ClienteDesactivadoException.class, () -> clienteService.desactivarCliente(2L));

    verify(clienteRepository, never()).save(any());

}

@Test
void activarCliente_cuandoExisteDesactivado_debeRetornarTrue(){
        Optional<Cliente> clienteOptional = Optional.of(clienteInactivo);

        when(clienteRepository.findById(2L)).thenReturn(clienteOptional);
        when(clienteRepository.getReferenceById(2L)).thenReturn(clienteInactivo);
        doNothing().when(clientValidations).validarClienteDesactivado(clienteInactivo);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteInactivo);

        boolean result = clienteService.activarCliente(2L);

        assertTrue(result);
        verify(clienteRepository).save(clienteInactivo);
        assertTrue(clienteInactivo.isActivo());
}
@Test
void activarCliente_cuandoNoExiste_debeRetornarFalse(){

        when(clienteRepository.findById(23L)).thenReturn(Optional.empty());

        boolean result = clienteService.activarCliente(23L);

        assertFalse(result);
        verify(clienteRepository, never()).save(any());

}

@Test
void activarCliente_cuandoExisteActivo_debeLanzarException(){
    Optional<Cliente> clienteOptional = Optional.of(clienteActivo);

    when(clienteRepository.findById(ID_CLIENTE)).thenReturn(clienteOptional);
    when(clienteRepository.getReferenceById(ID_CLIENTE)).thenReturn(clienteActivo);

    doThrow(new ClienteActivadoException("El cliente ya se encuentra Activo"))
            .when(clientValidations).validarClienteDesactivado(clienteActivo);

    assertThrows(ClienteActivadoException.class, () -> clienteService.activarCliente(ID_CLIENTE));
    verify(clienteRepository, never()).save(any());
}

}
