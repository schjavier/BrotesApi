package com.brotes.api.PedidoTests;

import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.PedidoNotExistException;
import com.brotes.api.exceptions.ProductNotExistException;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.pedidos.*;
import com.brotes.api.modelo.producto.DatosProductoPedido;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import com.brotes.api.validations.ClientValidations;
import com.brotes.api.validations.PedidoValidations;
import com.brotes.api.validations.ProductValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    ClienteRepository clienteRepository;
    @Mock
    ProductoRepository productoRepository;
    @Mock
    PedidoRepository pedidoRepository;
    @Mock
    ClientValidations clientValidations;
    @Mock
    ProductValidations productValidations;
    @Mock
    PedidoValidations pedidoValidations;

    @InjectMocks
    PedidosServiceImpl pedidoService;

    private static final Long ID_CLIENTE = 1L;
    private static final String NOMBRE_CLIENTE = "clienteTest";

    private static final Long ID_PRODUCTO = 1L;
    private static final String NOMBRE_PRODUCTO = "ProductoTest";
    private static final Float PRECIO_PRODUCTO = 100F;

    private static final Long ID_PEDIDO = 1L;
    private static final LocalDateTime FECHA_PEDIDO = LocalDateTime.now();
    private static final Float PRECIO_TOTAL = 200f;
    private static final Integer CANTIDAD_ITEM = 2;
    private static final Long ID_INEXISTENTE = 2L;
    private static final DiaDeEntrega DIA_DE_ENTREGA = DiaDeEntrega.MARTES;

    private Cliente clienteMock;
    private Producto productoMock;
    private Pedido pedidoMock;
    private ItemPedido itemPedidoMock;
    private List<ItemPedido> itemPedidoList;
    private ItemPedido itemPedidoParaModificacion;
    private List<ItemPedido> itemPedidoListParaModificacion;

    private UriComponentsBuilder uriComponentsBuilder;

    private DatosTomarPedido datosTomarPedidoMock;
    private DatosProductoPedido datosProductoPedidoMock;
    private List<DatosProductoPedido> datosProductoPedidoList;
    private DatosActualizarPedido datosActualizarPedidoMock;
    private DatosActualizarPedido datosActualizarPedidoInexisteNteMock;

    /**
     * Metodo que setea todos los objetos para las pruebas.
     *
     */
    @BeforeEach
    void setUp(){

        clienteMock = new Cliente();
        clienteMock.setId(ID_CLIENTE);
        clienteMock.setNombre(NOMBRE_CLIENTE);

        productoMock = new Producto();
        productoMock.setId(ID_PRODUCTO);
        productoMock.setNombre(NOMBRE_PRODUCTO);
        productoMock.setPrecio(PRECIO_PRODUCTO);

        pedidoMock = new Pedido();
        pedidoMock.setId(ID_PEDIDO);
        pedidoMock.setCliente(clienteMock);
        pedidoMock.setFecha(FECHA_PEDIDO);
        pedidoMock.setPrecioTotal(PRECIO_TOTAL);
        pedidoMock.setDiaEntrega(DIA_DE_ENTREGA);

        itemPedidoMock = new ItemPedido(CANTIDAD_ITEM, productoMock, pedidoMock);
        itemPedidoList = new ArrayList<>();
        itemPedidoList.add(itemPedidoMock);

        itemPedidoParaModificacion = new ItemPedido(3, productoMock, pedidoMock);
        itemPedidoListParaModificacion = new ArrayList<>();
        itemPedidoListParaModificacion.add(itemPedidoParaModificacion);

        uriComponentsBuilder = UriComponentsBuilder.newInstance();

        pedidoMock.setItems(itemPedidoList);

        datosProductoPedidoMock = new DatosProductoPedido(ID_PRODUCTO, CANTIDAD_ITEM);
        datosProductoPedidoList = new ArrayList<>();
        datosProductoPedidoList.add(datosProductoPedidoMock);
        datosTomarPedidoMock = new DatosTomarPedido(ID_CLIENTE, datosProductoPedidoList, DIA_DE_ENTREGA);
        datosActualizarPedidoMock = new DatosActualizarPedido(
                ID_PEDIDO,
                ID_CLIENTE,
                datosProductoPedidoList,
                DIA_DE_ENTREGA);

        datosActualizarPedidoInexisteNteMock = new DatosActualizarPedido(
                ID_INEXISTENTE,
                ID_CLIENTE,
                datosProductoPedidoList,
                DIA_DE_ENTREGA);

    }

    @Test
    void tomarPedido_deberiaCrearPedidoCorrectamente(){

        when(clienteRepository.findById(ID_CLIENTE)).thenReturn(Optional.of(clienteMock));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(productoMock));

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocationOnMock -> {

            Pedido pedidoGuardado = invocationOnMock.getArgument(0);
            pedidoGuardado.setId(ID_PEDIDO);
            return pedidoGuardado;

        });

        DatosDetallePedidoUrl resultado = pedidoService.tomarPedido(datosTomarPedidoMock, uriComponentsBuilder);

        assertNotNull(resultado);
        assertEquals(ID_PEDIDO, resultado.idPedido());
        assertEquals(ID_CLIENTE, resultado.idCliente());
        assertEquals(NOMBRE_CLIENTE, resultado.nombreCliente());
        assertEquals(CANTIDAD_ITEM * PRECIO_PRODUCTO, resultado.precioTotal());
        assertEquals(1, resultado.item().size());

        verify(clientValidations).validarExistencia(ID_CLIENTE);
        verify(productValidations).existValidation(ID_PRODUCTO);
        verify(pedidoRepository).save(any(Pedido.class));

    }

    @Test
    void tomarPedido_cuandoClienteNoExiste_debeLanzarException(){


        doThrow(new ClientNotExistException("EL Cliente no existe"))
                .when(clientValidations).validarExistencia(ID_CLIENTE);

        assertThrows(ClientNotExistException.class,
                () -> pedidoService.tomarPedido(datosTomarPedidoMock, uriComponentsBuilder));

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void tomarPedido_cuandoProductoNoExiste_debeLanzarException(){

        when(clienteRepository.findById(ID_CLIENTE)).thenReturn(Optional.of(clienteMock));
        doThrow(new ProductNotExistException("El producto no existe"))
                .when(productValidations).existValidation(ID_PRODUCTO);

        assertThrows(ProductNotExistException.class,
                () -> pedidoService.tomarPedido(datosTomarPedidoMock, uriComponentsBuilder));

        verify(pedidoRepository, never()).save(any());

    }

    @Test
    void listarPedidos_siHay_debeRetornarListaPaginada(){

            Pageable pageable = PageRequest.of(1, 10);
        Page<Pedido> pedidoPage = new PageImpl<>(List.of(pedidoMock));

        when(pedidoRepository.findAll(pageable)).thenReturn(pedidoPage);

        Page<DatosListaPedidos> resultado = pedidoService.listarPedidos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(ID_CLIENTE, resultado.getContent().get(0).idCliente());
        assertEquals(ID_PEDIDO, resultado.getContent().get(0).idPedido());
        assertEquals(NOMBRE_CLIENTE, resultado.getContent().get(0).nombreCliente());

        verify(pedidoRepository).findAll(pageable);

    }

    @Test
    void listarPedido_siExiste_debeRetornarPedido(){
        when(pedidoRepository.getReferenceById(1L)).thenReturn(pedidoMock);

        DatosDetallePedido resultado = pedidoService.listarUnPedido(1L);

        assertNotNull(resultado);
        assertEquals(ID_PEDIDO, resultado.idPedido());
        assertEquals(ID_CLIENTE, resultado.idCliente());
        assertEquals(NOMBRE_CLIENTE, resultado.nombreCliente());
        assertEquals(FECHA_PEDIDO, resultado.fecha());
        assertEquals(ID_PRODUCTO, resultado.item().get(0).id());
        assertEquals(NOMBRE_PRODUCTO, resultado.item().get(0).nombreProducto());
        assertEquals(PRECIO_PRODUCTO, resultado.item().get(0).precioProducto());
        assertEquals(CANTIDAD_ITEM * PRECIO_PRODUCTO, resultado.precioTotal());

        verify(pedidoRepository).getReferenceById(1L);
    }

    @Test
    void listarPedido_siNoExiste_debeLanzarException(){

        when(pedidoRepository.getReferenceById(ID_INEXISTENTE)).thenThrow(PedidoNotExistException.class);

        assertThrows(PedidoNotExistException.class,
                () -> pedidoService.listarUnPedido(ID_INEXISTENTE));

    }

    @Test
    void modificarPedido_cuandoExiste_debeRetornarPedidoModificado(){

        doReturn(productoMock).when(productoRepository).getReferenceById(ID_PRODUCTO);
        when(clienteRepository.getReferenceById(ID_CLIENTE)).thenReturn(clienteMock);
        when(pedidoRepository.getReferenceById(1L)).thenReturn(pedidoMock);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        DatosDetallePedido resultado = pedidoService.modificarPedido(datosActualizarPedidoMock);

        assertNotNull(resultado);
        assertEquals(ID_PEDIDO, resultado.idPedido());
        assertEquals(ID_CLIENTE, resultado.idCliente());
        assertEquals(ID_PRODUCTO, resultado.item().get(0).id());
        assertEquals(CANTIDAD_ITEM, resultado.item().get(0).cantidad());

        verify(pedidoRepository).getReferenceById(1L);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(productoRepository).getReferenceById(ID_PRODUCTO);
        verify(clienteRepository, atMost(2)).getReferenceById(ID_CLIENTE);
    }

    @Test
    void modificarPedido_CuandoNoExiste_debeLanzarException(){

        when(pedidoRepository.getReferenceById(ID_INEXISTENTE)).thenThrow(PedidoNotExistException.class);

        assertThrows(PedidoNotExistException.class,
                () -> pedidoService.modificarPedido(datosActualizarPedidoInexisteNteMock));


    }

    @Test
    void eliminarPedido_siExiste_debeRetornarTrue(){

        when(pedidoRepository.existsById(ID_PEDIDO)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(ID_PEDIDO);

        boolean pedidoBorrado = pedidoService.eliminarPedido(ID_PEDIDO);

        assertTrue(pedidoBorrado);

        verify(pedidoRepository).existsById(ID_PEDIDO);
        verify(pedidoRepository).deleteById(ID_PEDIDO);

    }

    @Test
    void eliminarPedido_siNoExiste_debeRetornarFalse(){

        when(pedidoRepository.existsById(ID_INEXISTENTE)).thenReturn(false);

        boolean pedidoBorrado = pedidoService.eliminarPedido(ID_INEXISTENTE);

        assertFalse(pedidoBorrado);

        verify(pedidoRepository).existsById(ID_INEXISTENTE);
        verify(pedidoRepository, never()).deleteById(ID_INEXISTENTE);


    }




}
