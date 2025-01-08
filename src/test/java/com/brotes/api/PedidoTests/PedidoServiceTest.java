package com.brotes.api.PedidoTests;

import com.brotes.api.exceptions.ClientNotExistException;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
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

    private Cliente clienteMock;
    private Producto productoMock;
    private Pedido pedidoMock;
    private ItemPedido itemPedidoMock;
    private List<ItemPedido> itemPedidoList;

    private UriComponentsBuilder uriComponentsBuilder;

    private DatosTomarPedido datosTomarPedidoMock;
    private DatosProductoPedido datosProductoPedidoMock;
    private List<DatosProductoPedido> datosProductoPedidoList;
    private DatosActualizarPedido datosActualizarPedidoMock;

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

        itemPedidoMock = new ItemPedido(CANTIDAD_ITEM, productoMock, pedidoMock);
        itemPedidoList = List.of(itemPedidoMock);

        uriComponentsBuilder = UriComponentsBuilder.newInstance();

        pedidoMock.setItems(itemPedidoList);

        datosProductoPedidoMock = new DatosProductoPedido(ID_PRODUCTO, CANTIDAD_ITEM);
        datosProductoPedidoList = List.of(datosProductoPedidoMock);
        datosTomarPedidoMock = new DatosTomarPedido(ID_CLIENTE, datosProductoPedidoList);
        datosActualizarPedidoMock = new DatosActualizarPedido(ID_PEDIDO, ID_CLIENTE, itemPedidoList);

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



}
