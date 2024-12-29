package com.brotes.api.productTests;

import com.brotes.api.exceptions.DuplicateProductException;
import com.brotes.api.exceptions.ProductNotExistException;
import com.brotes.api.exceptions.ProductoDesactivadoException;
import com.brotes.api.modelo.categoria.Categoria;
import com.brotes.api.modelo.producto.*;
import com.brotes.api.validations.ProductValidations;
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
public class ProductServiceTest {

    @Mock
    ProductoRepository productoRepository;

    @Mock
    ProductValidations productValidations;

    @InjectMocks
    ProductoServiceImpl productoService;

    private static final Long ID_PRODUCTO = 1L;
    private static final String NOMBRE_PRODUCTO = "Brocoli";
    private static final Float PRECIO_PRODUCTO = 20F;
    private static final Categoria CATEGORIA_PRODUCTO = Categoria.BROTES;

    private Producto productoActivo;
    private Producto productoInactivo;
    private DatosRegistroProductos datosRegistro;
    private DatosActualizarProducto datosActualizarProducto;
    private DatosActualizarProducto datosActualizarProductoInexistente;
    private Long idInexistente;

    private UriComponentsBuilder uriComponentsBuilder;

    @BeforeEach
    void setUp(){
        productoActivo = new Producto(ID_PRODUCTO, NOMBRE_PRODUCTO, PRECIO_PRODUCTO, CATEGORIA_PRODUCTO, true);

        productoInactivo = new Producto(2L, "Kale", 30F, Categoria.BROTES, false);

        datosRegistro = new DatosRegistroProductos(
                NOMBRE_PRODUCTO,
                PRECIO_PRODUCTO,
                CATEGORIA_PRODUCTO
        );

        datosActualizarProducto = new DatosActualizarProducto(
                ID_PRODUCTO,
                "Brocoli Romanescu",
                50F,
                Categoria.VERDURA
        );

        uriComponentsBuilder = UriComponentsBuilder.newInstance();

        datosActualizarProductoInexistente = new DatosActualizarProducto(
                30L,
                "Flores de Sabor",
                20F,
                Categoria.FLORES
        );

        idInexistente = 99L;

    }


    @Test
    void registrarProducto_debeRegistrarProductoCorrectamente(){

        Mockito.doNothing().when(productValidations).productUniqueValidation(datosRegistro.nombre(), datosRegistro.categoria());
        Mockito.when(productoRepository.save(any(Producto.class))).thenReturn(productoActivo);

        DatosRespuestaProductoUrl respuesta = productoService.registrarProducto(datosRegistro, uriComponentsBuilder);

        assertNotNull(respuesta);
        assertEquals(ID_PRODUCTO, respuesta.id());
        assertEquals(NOMBRE_PRODUCTO, respuesta.nombre());
        assertEquals(PRECIO_PRODUCTO, respuesta.precio());
        assertEquals(CATEGORIA_PRODUCTO, respuesta.categoria());

        verify(productValidations).productUniqueValidation(datosRegistro.nombre(), datosRegistro.categoria());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void registarProducto_SiExiste_debeLanzarException(){

        doThrow(new DuplicateProductException("El producto ya se encuentra registrado"))
                .when(productValidations)
                .productUniqueValidation(datosRegistro.nombre(), datosRegistro.categoria());

        DuplicateProductException exception = assertThrows(DuplicateProductException.class,
                () -> productoService.registrarProducto(datosRegistro, uriComponentsBuilder));

        assertEquals("El producto ya se encuentra registrado", exception.getMessage());

        verify(productValidations).productUniqueValidation(datosRegistro.nombre(), datosRegistro.categoria());

        //Verifica que no se ha interactuado con el repositorio
        verifyNoInteractions(productoRepository);
    }

    @Test
    void listarProductos_siHay_debeRetornarListaPaginada(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Producto> productoPage = new PageImpl<>(List.of(productoActivo));

        when(productoRepository.findAll(pageable)).thenReturn(productoPage);

        Page<DatosListaProductos> resultado = productoService.listarProductos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(NOMBRE_PRODUCTO, resultado.getContent().get(0).nombre());
        verify(productoRepository).findAll(pageable);
    }

    @Test
    void listarProducto_siExiste_debeRetornarUnProducto(){
        when(productoRepository.getReferenceById(1L)).thenReturn(productoActivo);

        DatosRespuestaProducto respuestaProducto = productoService.listarUnProducto(1L);

        assertNotNull(respuestaProducto);
        assertEquals(ID_PRODUCTO, respuestaProducto.id());
        assertEquals(NOMBRE_PRODUCTO, respuestaProducto.nombre());
        assertEquals(PRECIO_PRODUCTO, respuestaProducto.precio());
        assertEquals(CATEGORIA_PRODUCTO, respuestaProducto.categoria());
        assertTrue(respuestaProducto.activo());

        verify(productoRepository).getReferenceById(1L);

    }
    @Test
    void listarProducto_cuandoNoExiste_debeLanzarException(){

        when(productoRepository.getReferenceById(idInexistente))
                .thenThrow(ProductNotExistException.class);

        assertThrows(ProductNotExistException.class,
                () -> productoService.listarUnProducto(idInexistente));

    }

    @Test
    void modificarProducto_cuandoExiste_debeRetornarProductoModificado(){

        when(productoRepository.getReferenceById(1L)).thenReturn(productoActivo);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActivo);

        DatosRespuestaProducto productoModificado = productoService.modificarProducto(datosActualizarProducto);

        assertNotNull(productoModificado);
        assertEquals(ID_PRODUCTO, productoModificado.id());
        assertEquals("Brocoli Romanescu", productoModificado.nombre());
        assertEquals(50L, productoModificado.precio());
        assertEquals(Categoria.VERDURA, productoModificado.categoria());
        assertTrue(productoModificado.activo());

        verify(productoRepository).getReferenceById(ID_PRODUCTO);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void modificarProducto_cuandoNoExiste_debeLanzarException(){
        when(productoRepository.getReferenceById(30L)).thenThrow(ProductNotExistException.class);

        assertThrows(ProductNotExistException.class,
                () -> productoService.modificarProducto(datosActualizarProductoInexistente));

    }

    @Test
    void eliminarProducto_cuandoExiste_debeRetornarTrue(){
        when(productoRepository.existsById(ID_PRODUCTO)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(ID_PRODUCTO);

        boolean productoBorrado = productoService.eliminarProducto(ID_PRODUCTO);

        assertTrue(productoBorrado);

        verify(productoRepository).deleteById(ID_PRODUCTO);
        verify(productoRepository).existsById(ID_PRODUCTO);
    }

    @Test
    void eliminarProducto_cuandoNoExiste_debeRetronarFalse(){

        when(productoRepository.existsById(idInexistente)).thenReturn(false);

        boolean productoBorrado = productoService.eliminarProducto(idInexistente);

        assertFalse(productoBorrado);

        verify(productoRepository).existsById(idInexistente);
        verify(productoRepository, never()).deleteById(idInexistente);

    }

    @Test
    void desactivarProducto_cuandoExisteActivo_debeRetornarTrue(){

        Optional<Producto> productoOptional = Optional.of(productoActivo);

        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(productoOptional);
        when(productoRepository.getReferenceById(ID_PRODUCTO)).thenReturn(productoActivo);
        doNothing().when(productValidations).activeProductValidation(productoActivo);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActivo);

        boolean result = productoService.desactivarProducto(ID_PRODUCTO);

        assertTrue(result);
        verify(productoRepository).save(productoActivo);
        assertFalse(productoActivo.isActivo());

    }

    @Test
    void desactivarProducto_cuandoNoExiste_debeRetornarFalse(){
        when(productoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        boolean result = productoService.desactivarProducto(idInexistente);

        assertFalse(result);
        verify(productoRepository, never()).save(any());

    }

    @Test
    void desactivarProducto_cuandoExisteDesactivado_debeLanzarException(){
        Optional<Producto> productoOptional = Optional.of(productoInactivo);

        when(productoRepository.findById(2L)).thenReturn(productoOptional);
        when(productoRepository.getReferenceById(2L)).thenReturn(productoInactivo);

        doThrow(new ProductoDesactivadoException("El producto ya se encuentra desactivado"))
                .when(productValidations).activeProductValidation(productoInactivo);

        assertThrows(ProductoDesactivadoException.class, () -> productoService.desactivarProducto(2L));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void activarCliente_cuandoExisteDesactivado_debeRetornarTrue(){
        Optional<Producto> productoOptional = Optional.of(productoInactivo);

        when(productoRepository.findById(2L)).thenReturn(productoOptional);
        when(productoRepository.getReferenceById(2L)).thenReturn(productoInactivo);
        doNothing().when(productValidations).inactiveProductValidation(productoInactivo);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoInactivo);

        boolean result = productoService.activarProducto(2L);

        assertTrue(result);
        verify(productoRepository).save(productoInactivo);
        assertTrue(productoInactivo.isActivo());

    }



}
