package com.brotes.api.PedidoRecurrenteTest;

import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.PedidoRecurrenteExistsException;
import com.brotes.api.mappers.PedidoRecurrenteMapper;
import com.brotes.api.modelo.categoria.Categoria;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.cliente.ClienteService;
import com.brotes.api.modelo.itemPedidoRecurrente.DatosRegistroItemPedidoRecurrente;
import com.brotes.api.modelo.itemPedidoRecurrente.DatosRespuestaItemRecurrente;
import com.brotes.api.modelo.itemPedidoRecurrente.ItemPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.*;
import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.validations.ClientValidations;
import com.brotes.api.validations.PedidoRecurrenteValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoRecurrenteServiceTest {

    @Mock
    private PedidoRecurrenteMapper pedidoRecurrenteMapper;
    @Mock
    private PedidoRecurrenteRepository pedidoRecurrenteRepository;
    @Mock
    private ClienteService clienteService;
    @Mock
    private PedidoRecurrenteValidations pedidoRecurrenteValidations;

    @InjectMocks
    PedidoRecurrenteServiceImpl pedidoRecurrenteService;

    private final Long CLIENTE_ID = 1L;
    private final String CLIENTE_NOMBRE = "cliente1";
    private final Long PEDIDO_RECURRENTE_ID = 1L;
    private final Long PRODUCTO_ID = 1L;
    private final String PRODUCTO_NAME = "Remolacha";
    private final Double CANTIDAD = 2.0;
    private final DiaDeEntrega DIA_ENTREGA = DiaDeEntrega.MARTES;
    private final Categoria CATEGORIA = Categoria.VERDURA;


    private Cliente clienteMock;
    private Producto productoMock;
    private DatosRegistroItemPedidoRecurrente itemDto;
    private List<DatosRegistroItemPedidoRecurrente> itemsDtoList;
    private DatosActualizarItemRecurrente updateItemDto;
    private List<DatosActualizarItemRecurrente> itemsDtoList2Update;
    private DatosRegistroPedidoRecurrente datosRegistroPedidoRecurrente;
    private PedidoRecurrente pedidoRecurrente;
    private PedidoRecurrente pedidoRecurrenteSinID;
    private ItemPedidoRecurrente itemPedidoRecurrente;
    private List<ItemPedidoRecurrente> itemsList;
    private DatosRespuestaPedidoRecurrente datosRespuestaPedidoRecurrente;
    private DatosRespuestaItemRecurrente datosRespuestaItemRecurrente;
    private List<DatosRespuestaItemRecurrente> itemRespuestaList;

    @BeforeEach
    public void setUp(){
        clienteMock =  new Cliente();
        clienteMock.setId(CLIENTE_ID);

        productoMock = new Producto();
        productoMock.setId(PRODUCTO_ID);

        itemDto = new DatosRegistroItemPedidoRecurrente(CANTIDAD, PEDIDO_RECURRENTE_ID, PRODUCTO_ID);
        itemsDtoList = new ArrayList<>();
        itemsDtoList.add(itemDto);

        updateItemDto = new DatosActualizarItemRecurrente(PRODUCTO_ID, CANTIDAD);
        itemsDtoList2Update =  new ArrayList<>();
        itemsDtoList2Update.add(updateItemDto);

        datosRegistroPedidoRecurrente = new DatosRegistroPedidoRecurrente(CLIENTE_ID, itemsDtoList, DIA_ENTREGA);

        pedidoRecurrente = new PedidoRecurrente();
        pedidoRecurrente.setId(PEDIDO_RECURRENTE_ID);
        pedidoRecurrente.setCliente(clienteMock);
        itemPedidoRecurrente = new ItemPedidoRecurrente(3.0, pedidoRecurrente, productoMock);
        itemsList = new ArrayList<>();
        itemsList.add(itemPedidoRecurrente);
        pedidoRecurrente.setItems(itemsList);

        pedidoRecurrenteSinID = new PedidoRecurrente(clienteMock, itemsList, DIA_ENTREGA);

        datosRespuestaItemRecurrente = new DatosRespuestaItemRecurrente(2.0, PRODUCTO_ID, PRODUCTO_NAME, CATEGORIA);
        itemRespuestaList = new ArrayList<>();
        itemRespuestaList.add(datosRespuestaItemRecurrente);

        datosRespuestaPedidoRecurrente = new DatosRespuestaPedidoRecurrente(
                PEDIDO_RECURRENTE_ID,
                CLIENTE_ID,
                CLIENTE_NOMBRE,
                itemRespuestaList,
                DIA_ENTREGA
        );
    }

    @Test
    public void registrarPedidoRecurrente_shouldCreatePedidoRecurrenteCorrectly(){

        when(clienteService.getClienteById(CLIENTE_ID)).thenReturn(clienteMock);
        when(pedidoRecurrenteMapper.toEntity(datosRegistroPedidoRecurrente, clienteMock)).thenReturn(pedidoRecurrenteSinID);

        doNothing().when(pedidoRecurrenteValidations).PedidoRecurrenteExists(CLIENTE_ID, DIA_ENTREGA);

        when(pedidoRecurrenteRepository.save(any(PedidoRecurrente.class))).thenReturn(pedidoRecurrente);

        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente)).thenReturn(datosRespuestaPedidoRecurrente);

        DatosRespuestaPedidoRecurrente result = pedidoRecurrenteService.registrarPedidoRecurrente(datosRegistroPedidoRecurrente);

        assertEquals(datosRegistroPedidoRecurrente.idCliente(), result.idCliente());
        assertEquals(datosRegistroPedidoRecurrente.diaEntrega(), result.diaDeEntrega());
        assertEquals(datosRegistroPedidoRecurrente.items().size(), result.item().size());

        verify(pedidoRecurrenteRepository, times(1)).save(pedidoRecurrenteSinID);
        verify(clienteService, times(1)).getClienteById(CLIENTE_ID);


    }

    @Test
    public void registrarPedidoRecurrente_shouldThrowException_whenClientDoNotExists(){

        doThrow(ClientNotExistException.class)
                .when(clienteService)
                .getClienteById(CLIENTE_ID);

        assertThrows(ClientNotExistException.class, () -> {
            pedidoRecurrenteService.registrarPedidoRecurrente(datosRegistroPedidoRecurrente);
        });

        verify(pedidoRecurrenteRepository, never()).save(pedidoRecurrenteSinID);

    }

    @Test
    public void registrarPedidoRecurrente_shouldThrowException_whenPedidoRecurrenteExists() {

        when(clienteService.getClienteById(CLIENTE_ID)).thenReturn(clienteMock);
        when(pedidoRecurrenteMapper.toEntity(datosRegistroPedidoRecurrente, clienteMock)).thenReturn(pedidoRecurrenteSinID);

        doThrow(PedidoRecurrenteExistsException.class)
                .when(pedidoRecurrenteValidations).PedidoRecurrenteExists(CLIENTE_ID, DIA_ENTREGA);

        assertThrows(PedidoRecurrenteExistsException.class, () -> {
            pedidoRecurrenteService.registrarPedidoRecurrente(datosRegistroPedidoRecurrente);
        });

        verify(pedidoRecurrenteValidations, times(1)).PedidoRecurrenteExists(CLIENTE_ID, DIA_ENTREGA);
        verify(pedidoRecurrenteRepository, never()).save(pedidoRecurrenteSinID);

    }

    @Test
    public void traerPedidosRecurrentes_shouldReturnListOfRespuestaPedidosRecurrente(){

        PedidoRecurrente pedidoRecurrente2 = new PedidoRecurrente();
        pedidoRecurrente2.setId(2L);
        pedidoRecurrente2.setItems(itemsList);
        pedidoRecurrente2.setCliente(clienteMock);
        pedidoRecurrente2.setDiaEntrega(DiaDeEntrega.VIERNES);

        List<PedidoRecurrente> pedidoRecurrenteList = new ArrayList<>();
        pedidoRecurrenteList.add(pedidoRecurrente);
        pedidoRecurrenteList.add(pedidoRecurrente2);

        DatosRespuestaPedidoRecurrente datosRespuestaPedidoRecurrente2 = new DatosRespuestaPedidoRecurrente(
                2L,
                clienteMock.getId(),
                clienteMock.getNombre(),
                itemRespuestaList,
                DiaDeEntrega.VIERNES
        );

        when(pedidoRecurrenteRepository.findAll()).thenReturn(pedidoRecurrenteList);

        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente)).thenReturn(datosRespuestaPedidoRecurrente);
        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente2)).thenReturn(datosRespuestaPedidoRecurrente2);

        List<DatosRespuestaPedidoRecurrente> listaEsperada = pedidoRecurrenteService.traerPedidosRecurrentes();

        assertEquals(pedidoRecurrenteList.size(), listaEsperada.size());

        verify(pedidoRecurrenteRepository, times(1)).findAll();
        verify(pedidoRecurrenteMapper, times(2)).toDto(any());
    }

    @Test
    public void paginarPedidosRecurrentes_shouldReturnPageOfRespuestaPedidosRecurrente(){

        PedidoRecurrente pedidoRecurrente2 = new PedidoRecurrente();
        pedidoRecurrente2.setId(2L);
        pedidoRecurrente2.setItems(itemsList);
        pedidoRecurrente2.setCliente(clienteMock);
        pedidoRecurrente2.setDiaEntrega(DiaDeEntrega.VIERNES);

        DatosRespuestaPedidoRecurrente datosRespuestaPedidoRecurrente2 = new DatosRespuestaPedidoRecurrente(
                2L,
                clienteMock.getId(),
                clienteMock.getNombre(),
                itemRespuestaList,
                DiaDeEntrega.VIERNES
        );

        Page<PedidoRecurrente> pagedOrders = new PageImpl<>(List.of(pedidoRecurrente, pedidoRecurrente2));

        Pageable mockedPageable = PageRequest.of(0, 2);

        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente)).thenReturn(datosRespuestaPedidoRecurrente);
        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente2)).thenReturn(datosRespuestaPedidoRecurrente2);

        when(pedidoRecurrenteRepository.findAll(any(Pageable.class))).thenReturn(pagedOrders);

        pedidoRecurrenteService.paginarPedidosRecurrentes(mockedPageable) ;

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(pedidoRecurrenteRepository, times(1)).findAll(pageableCaptor.capture());
        verify(pedidoRecurrenteMapper, times(2)).toDto(any(PedidoRecurrente.class));

        Pageable usedPageable = pageableCaptor.getValue();
        assertEquals(0, usedPageable.getPageNumber());
        assertEquals(2, usedPageable.getPageSize());
        assertNotNull(usedPageable.getSort().getOrderFor("id"));

    }

    @Test
    public void modifyRecurrentOrder_shouldReturnDto_withModifiedData(){

        DatosActualizarPedidoRecurrente dataToModify = new DatosActualizarPedidoRecurrente(PEDIDO_RECURRENTE_ID, CLIENTE_ID, itemsDtoList2Update, DiaDeEntrega.VIERNES);
        DatosRespuestaPedidoRecurrente expectedData = new DatosRespuestaPedidoRecurrente(
                PEDIDO_RECURRENTE_ID, CLIENTE_ID, CLIENTE_NOMBRE, itemRespuestaList, DiaDeEntrega.VIERNES );

        when(clienteService.getClienteById(CLIENTE_ID)).thenReturn(clienteMock);
        when(pedidoRecurrenteMapper.toEntity(dataToModify, clienteMock, PEDIDO_RECURRENTE_ID)).thenReturn(pedidoRecurrente);

        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente)).thenReturn(expectedData);

        DatosRespuestaPedidoRecurrente result = pedidoRecurrenteService.modifyRecurrentOrder(pedidoRecurrente.getId(), dataToModify);

        assertNotNull(result);

        verify(pedidoRecurrenteMapper, times(1)).toEntity(dataToModify, clienteMock, PEDIDO_RECURRENTE_ID);
        verify(pedidoRecurrenteMapper, times(1)).toDto(pedidoRecurrente);
        verify(pedidoRecurrenteRepository, times(1)).save(pedidoRecurrente);

    }

    @Test
    public void deletePedidoRecurrente_shouldReturnTrue_ifPedidoRecurrenteWasDeleted(){
        boolean expectedResponse = true;

        when(pedidoRecurrenteRepository.findById(PEDIDO_RECURRENTE_ID))
                .thenReturn(Optional.ofNullable(pedidoRecurrente));

        boolean response = pedidoRecurrenteService.deletePedidoRecurrente(PEDIDO_RECURRENTE_ID);

        assertEquals(expectedResponse, response);
        verify(pedidoRecurrenteRepository, times(1)).findById(PEDIDO_RECURRENTE_ID);
        verify(pedidoRecurrenteRepository, times(1)).delete(pedidoRecurrente);
    }

    @Test
    public void deletePedidoRecurrente_shouldReturnFalse_ifPedidoRecurrenteWasNotDeleted(){
        boolean expectedResponse = false;

        when(pedidoRecurrenteRepository.findById(PEDIDO_RECURRENTE_ID))
                .thenReturn(Optional.empty());

        boolean response = pedidoRecurrenteService.deletePedidoRecurrente(PEDIDO_RECURRENTE_ID);

        assertEquals(expectedResponse, response);
        verify(pedidoRecurrenteRepository, times(1)).findById(PEDIDO_RECURRENTE_ID);
        verify(pedidoRecurrenteRepository, never()).delete(pedidoRecurrente);
    }


}
