package com.brotes.api.productTests;

import com.brotes.api.exceptions.DuplicateProductException;
import com.brotes.api.exceptions.ProductNotExistException;
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
        Long idInexistente = 99L;

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
    void modificarProducto_CuandoNoExiste_debeLanzarException(){
        when(productoRepository.getReferenceById(30L)).thenThrow(ProductNotExistException.class);

        assertThrows(ProductNotExistException.class,
                () -> productoService.modificarProducto(datosActualizarProductoInexistente));

    }




}
