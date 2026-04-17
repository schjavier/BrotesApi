package com.brotes.api.PedidoRecurrenteTest;

import com.brotes.api.mappers.PedidoRecurrenteMapper;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.itemPedidoRecurrente.DatosRegistroItemPedidoRecurrente;
import com.brotes.api.modelo.itemPedidoRecurrente.ItemPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.*;
import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoRecurrenteMapperTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private PedidoRecurrenteRepository pedidoRecurrenteRepository;

    @InjectMocks
    private PedidoRecurrenteMapper pedidoRecurrenteMapper;

    private final Long CLIENTE_ID = 1L;
    private final Long PEDIDO_RECURRENTE_ID = 1L;
    private final Long PRODUCTO_ID = 1L;
    private final Long PRODUCTO2_ID = 2L;
    private final DiaDeEntrega DIA_ENTREGA = DiaDeEntrega.VIERNES;

    private Cliente cliente;
    private Producto producto2;
    private Producto producto1 ;
    private DatosRegistroPedidoRecurrente datosEntrada;
    private DatosActualizarPedidoRecurrente datosParaActualizar;
    private PedidoRecurrente pedidoRecurrente;
    private List<ItemPedidoRecurrente> listaItemsRecurrentes;

    private DatosRegistroItemPedidoRecurrente itemDTO1 ;
    private DatosRegistroItemPedidoRecurrente itemDTO2 ;
    private List<DatosRegistroItemPedidoRecurrente> itemDTOList ;

    private DatosActualizarItemRecurrente itemParaActualizar;
    private DatosActualizarItemRecurrente itemParaActualizar2;
    private List<DatosActualizarItemRecurrente> listaDeItemsParaActualizar;

    private ItemPedidoRecurrente itemPedidoRecurrente1;
    private ItemPedidoRecurrente itemPedidoRecurrente2;

    @BeforeEach
    void setUp(){

        producto1 = new Producto();
        producto1.setId(PRODUCTO_ID);
        producto2 = new Producto();
        producto2.setId(PRODUCTO2_ID);
        cliente = new Cliente();
        cliente.setId(CLIENTE_ID);

        itemDTO1 = new DatosRegistroItemPedidoRecurrente(10.0, 1L, 1L);
        itemDTO2 = new DatosRegistroItemPedidoRecurrente(5.0, 1L, 2L);
        itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO1);
        itemDTOList.add(itemDTO2);

        datosEntrada = new DatosRegistroPedidoRecurrente(
                1L,
                itemDTOList,
                DIA_ENTREGA);

        itemParaActualizar = new DatosActualizarItemRecurrente(1L, 10.0);
        itemParaActualizar2 = new DatosActualizarItemRecurrente(2L, 5.0);
        listaDeItemsParaActualizar = new ArrayList<>();
        listaDeItemsParaActualizar.add(itemParaActualizar);
        listaDeItemsParaActualizar.add(itemParaActualizar2);

        datosParaActualizar = new DatosActualizarPedidoRecurrente(
                PEDIDO_RECURRENTE_ID,
                CLIENTE_ID,
                listaDeItemsParaActualizar,
                DIA_ENTREGA
        );

        pedidoRecurrente = new PedidoRecurrente();

        itemPedidoRecurrente1 = new ItemPedidoRecurrente(10.0, pedidoRecurrente, producto1);
        itemPedidoRecurrente2 = new ItemPedidoRecurrente(5.0, pedidoRecurrente, producto2);

        listaItemsRecurrentes = new ArrayList<>();
        listaItemsRecurrentes.add(itemPedidoRecurrente1);
        listaItemsRecurrentes.add(itemPedidoRecurrente2);
        pedidoRecurrente.setCliente(cliente);
        pedidoRecurrente.setItems(listaItemsRecurrentes);
        pedidoRecurrente.setDiaEntrega(DIA_ENTREGA);

    }

    @Test
    public void toEntity_shouldReturn_PedidoRecurrente(){

        PedidoRecurrente result = pedidoRecurrenteMapper.toEntity(datosEntrada, cliente);

        assertNotNull(result);

    }

    @Test
    public void toEntity_shouldReturnPedidoRecurrente_withAllFieldsCorrectly(){
        PedidoRecurrente result =  pedidoRecurrenteMapper.toEntity(datosEntrada, cliente);

        assertEquals(DIA_ENTREGA, result.getDiaEntrega());
        assertEquals(cliente, result.getCliente());
        assertEquals(itemDTOList.size(), result.getItems().size());
        assertEquals(itemDTOList.get(0).cantidad(), result.getItems().get(0).getCantidad());
        assertEquals(itemDTOList.get(1).cantidad(), result.getItems().get(1).getCantidad());

        ItemPedidoRecurrente itemPedidoRecurrenteResult = result.getItems().get(0);
        ItemPedidoRecurrente itemPedidoRecurrenteResult2 = result.getItems().get(1);

        assertEquals(result, itemPedidoRecurrenteResult.getPedidoRecurrente() , "El Item debe tener referencia al padre");
        assertEquals(result, itemPedidoRecurrenteResult2.getPedidoRecurrente() , "El item debe tener referencia al padre");

        verify(productoRepository).getReferenceById(PRODUCTO_ID);
        verify(productoRepository).getReferenceById(PRODUCTO2_ID);

    }

    @Test
    public void toEntityOverload_shouldReturnPedidoRecurrenteEdited(){

        when(pedidoRecurrenteRepository.getReferenceById(PEDIDO_RECURRENTE_ID)).thenReturn(pedidoRecurrente);

        PedidoRecurrente result =  pedidoRecurrenteMapper.toEntity(datosParaActualizar, cliente, PEDIDO_RECURRENTE_ID);

        assertEquals(DIA_ENTREGA, result.getDiaEntrega());
        assertEquals(cliente, result.getCliente());
        assertEquals(listaDeItemsParaActualizar.size(), result.getItems().size());
        assertEquals(listaDeItemsParaActualizar.get(0).cantidad(), result.getItems().get(0).getCantidad());
        assertEquals(listaDeItemsParaActualizar.get(1).cantidad(), result.getItems().get(1).getCantidad());

        ItemPedidoRecurrente itemPedidoRecurrenteResult = result.getItems().get(0);
        ItemPedidoRecurrente itemPedidoRecurrenteResult2 = result.getItems().get(1);

        assertEquals(result, itemPedidoRecurrenteResult.getPedidoRecurrente() , "El Item debe tener referencia al padre");
        assertEquals(result, itemPedidoRecurrenteResult2.getPedidoRecurrente() , "El item debe tener referencia al padre");

        verify(pedidoRecurrenteRepository).getReferenceById(PEDIDO_RECURRENTE_ID);
        verify(productoRepository).getReferenceById(PRODUCTO_ID);
        verify(productoRepository).getReferenceById(PRODUCTO2_ID);
    }


    @Test
    public void toDTO_shouldReturn_DatosRespuestaPedidoRecurrente(){

        DatosRespuestaPedidoRecurrente result = pedidoRecurrenteMapper.toDto(pedidoRecurrente);

        assertNotNull(result);


    }
    @Test
    public void toDTO_shouldReturn_DatosRespuestaPedidoRecurrente_withAllFieldsCorrectly(){

        DatosRespuestaPedidoRecurrente result = pedidoRecurrenteMapper.toDto(pedidoRecurrente);

        assertEquals(DIA_ENTREGA, result.diaDeEntrega());
        assertEquals(cliente.getId(), result.idCliente());
        assertEquals(2, result.item().size());
        assertEquals(10, result.item().get(0).cantidad());
        assertEquals(5, result.item().get(1).cantidad());


    }





}
