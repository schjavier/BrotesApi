package com.brotes.api.PedidoRecurrenteTest;

import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.PedidoRecurrenteExistsException;
import com.brotes.api.mappers.PedidoRecurrenteMapper;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private ClientValidations clientValidations;
    @Mock
    private PedidoRecurrenteRepository pedidoRecurrenteRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ClienteService clienteService;
    @Mock
    private PedidoRecurrenteValidations pedidoRecurrenteValidations;

    @InjectMocks
    PedidoRecurrenteServiceImpl pedidoRecurrenteService;

    private final Long CLIENTE_ID = 1L;
    private final Long PEDIDO_RECURRENTE_ID = 1L;
    private final Long PRODUCTO_ID = 1L;
    private final int CANTIDAD = 2;
    private final DiaDeEntrega DIA_ENTREGA = DiaDeEntrega.MARTES;

    private Cliente clienteMock;
    private Producto productoMock;
    private DatosRegistroItemPedidoRecurrente itemDto;
    private List<DatosRegistroItemPedidoRecurrente> itemsDtoList;
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

        datosRegistroPedidoRecurrente = new DatosRegistroPedidoRecurrente(CLIENTE_ID, itemsDtoList, DIA_ENTREGA);

        pedidoRecurrente = new PedidoRecurrente();
        pedidoRecurrente.setId(PEDIDO_RECURRENTE_ID);
        pedidoRecurrente.setCliente(clienteMock);
        itemPedidoRecurrente = new ItemPedidoRecurrente(3, pedidoRecurrente, productoMock);
        itemsList = new ArrayList<>();
        itemsList.add(itemPedidoRecurrente);
        pedidoRecurrente.setItems(itemsList);

        pedidoRecurrenteSinID = new PedidoRecurrente(clienteMock, itemsList, DIA_ENTREGA);

        datosRespuestaItemRecurrente = new DatosRespuestaItemRecurrente(2, PRODUCTO_ID);
        itemRespuestaList = new ArrayList<>();
        itemRespuestaList.add(datosRespuestaItemRecurrente);

        datosRespuestaPedidoRecurrente = new DatosRespuestaPedidoRecurrente(
                PEDIDO_RECURRENTE_ID,
                CLIENTE_ID,
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

        assertEquals(datosRegistroPedidoRecurrente.idCliente(), result.clienteId());
        assertEquals(datosRegistroPedidoRecurrente.diaEntrega(), result.diaEntrega());
        assertEquals(datosRegistroPedidoRecurrente.items().size(), result.items().size());

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
                itemRespuestaList,
                DiaDeEntrega.VIERNES
        );

        Page<PedidoRecurrente> pagedOrders = new PageImpl<>(List.of(pedidoRecurrente2, pedidoRecurrente));

        Pageable mockedPageable = PageRequest.of(0, 2);

        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente)).thenReturn(datosRespuestaPedidoRecurrente);
        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente2)).thenReturn(datosRespuestaPedidoRecurrente2);

        when(pedidoRecurrenteRepository.findAll(mockedPageable)).thenReturn(pagedOrders);

        Page<DatosRespuestaPedidoRecurrente> resultPagedData = pedidoRecurrenteService.paginarPedidosRecurrentes(mockedPageable) ;

        assertNotNull(resultPagedData);
        assertEquals(2, resultPagedData.getTotalElements());

        verify(pedidoRecurrenteRepository, times(1)).findAll(mockedPageable);
        verify(pedidoRecurrenteMapper, times(2)).toDto(any(PedidoRecurrente.class));

    }

    @Test
    public void modifyRecurrentOrder_shouldReturnDto_withModifiedData(){

        DatosActualizarPedidoRecurrente dataToModify = new DatosActualizarPedidoRecurrente(CLIENTE_ID, itemsDtoList, DiaDeEntrega.VIERNES);
        DatosRespuestaPedidoRecurrente expectedData = new DatosRespuestaPedidoRecurrente(
                PEDIDO_RECURRENTE_ID,CLIENTE_ID, itemRespuestaList, DiaDeEntrega.VIERNES );

        when(clienteService.getClienteById(CLIENTE_ID)).thenReturn(clienteMock);
        when(pedidoRecurrenteMapper.toEntity(dataToModify, clienteMock, PEDIDO_RECURRENTE_ID)).thenReturn(pedidoRecurrente);

        doNothing().when(pedidoRecurrenteValidations).PedidoRecurrenteExists(CLIENTE_ID, pedidoRecurrente.getDiaEntrega());

        when(pedidoRecurrenteMapper.toDto(pedidoRecurrente)).thenReturn(expectedData);

        DatosRespuestaPedidoRecurrente result = pedidoRecurrenteService.modifyRecurrentOrder(pedidoRecurrente.getId(), dataToModify);

        assertNotNull(result);

        verify(pedidoRecurrenteValidations, times(1)).PedidoRecurrenteExists(CLIENTE_ID, pedidoRecurrente.getDiaEntrega());
        verify(pedidoRecurrenteMapper, times(1)).toEntity(dataToModify, clienteMock, PEDIDO_RECURRENTE_ID);
        verify(pedidoRecurrenteMapper, times(1)).toDto(pedidoRecurrente);
        verify(pedidoRecurrenteRepository, times(1)).save(pedidoRecurrente);

    }

    @Test
    public void modifyRecurrentOrder_shouldThrowException_whenRecurrentOrderAlreadyExists(){

        DatosActualizarPedidoRecurrente dataToModify = new DatosActualizarPedidoRecurrente(CLIENTE_ID, itemsDtoList, DiaDeEntrega.VIERNES);

        when(clienteService.getClienteById(CLIENTE_ID)).thenReturn(clienteMock);
        when(pedidoRecurrenteMapper.toEntity(dataToModify, clienteMock, PEDIDO_RECURRENTE_ID)).thenReturn(pedidoRecurrente);

        doThrow(PedidoRecurrenteExistsException.class)
                .when(pedidoRecurrenteValidations).PedidoRecurrenteExists(pedidoRecurrente.getCliente().getId(), pedidoRecurrente.getDiaEntrega());

        assertThrows(PedidoRecurrenteExistsException.class, () ->
                pedidoRecurrenteService.modifyRecurrentOrder(PEDIDO_RECURRENTE_ID, dataToModify)
        );

        verify(clienteService, times(1)).getClienteById(CLIENTE_ID);
        verify(pedidoRecurrenteMapper, times(1)).toEntity(dataToModify, clienteMock, PEDIDO_RECURRENTE_ID);
        verify(pedidoRecurrenteRepository, never()).save(pedidoRecurrente);


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
