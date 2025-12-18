package com.brotes.api.mappers;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.itemPedidoRecurrente.DatosRegistroItemPedidoRecurrente;
import com.brotes.api.modelo.itemPedidoRecurrente.DatosRespuestaItemRecurrente;
import com.brotes.api.modelo.itemPedidoRecurrente.ItemPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.*;
import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoRecurrenteMapper {

    private final ProductoRepository productoRepository;
    private final PedidoRecurrenteRepository pedidoRecurrenteRepository;

    public PedidoRecurrenteMapper(ProductoRepository productoRepository, PedidoRecurrenteRepository pedidoRecurrenteRepository) {
        this.productoRepository = productoRepository;
        this.pedidoRecurrenteRepository = pedidoRecurrenteRepository;
    }

    public PedidoRecurrente toEntity(DatosRegistroPedidoRecurrente datosRegistroPedidoRecurrente, Cliente cliente) {

        PedidoRecurrente pedidoRecurrente = new PedidoRecurrente();
        pedidoRecurrente.setCliente(cliente);

        List<ItemPedidoRecurrente> listaItemsRecurrentes = datosRegistroPedidoRecurrente.items().stream()
                .map(datosProducto -> toItemEntity(datosProducto, pedidoRecurrente))
                        .collect(Collectors.toList());

        pedidoRecurrente.setItems(listaItemsRecurrentes);
        pedidoRecurrente.setDiaEntrega(datosRegistroPedidoRecurrente.diaEntrega());

        return pedidoRecurrente;

    }

    /**
     * Pasa de DTO a entidad cuando hay que actualizar un pedido recurrente
     *
     * @param datosActualizarPedidoRecurrente DTO con los datos para actualizar
     * @param cliente El que tiene el pedido
     * @param idPedidoRecurrente id del pedido que estamos por editar
     * @return PedidoRecurrente
     */
    public PedidoRecurrente toEntity(DatosActualizarPedidoRecurrente datosActualizarPedidoRecurrente, Cliente cliente, Long idPedidoRecurrente) {

        PedidoRecurrente pedidoRecurrente = pedidoRecurrenteRepository.getReferenceById(idPedidoRecurrente);
        pedidoRecurrente.setCliente(cliente);

        List<ItemPedidoRecurrente> listaItemsRecurrentes = datosActualizarPedidoRecurrente.items().stream()
                .map(datosProducto -> toItemEntity(datosProducto, pedidoRecurrente))
                        .toList();

        pedidoRecurrente.getItems().clear();
        pedidoRecurrente.getItems().addAll(listaItemsRecurrentes);

        pedidoRecurrente.setDiaEntrega(datosActualizarPedidoRecurrente.diaEntrega());

        return pedidoRecurrente;

    }

    private ItemPedidoRecurrente toItemEntity(DatosRegistroItemPedidoRecurrente datosRegistroItemPedidoRecurrente,
                                              PedidoRecurrente pedidoRecurrente) {

        Producto producto = productoRepository.getReferenceById(datosRegistroItemPedidoRecurrente.idProducto());

        return new ItemPedidoRecurrente(
                datosRegistroItemPedidoRecurrente.cantidad(),
                pedidoRecurrente,
                producto
        );

    }

    public DatosRespuestaPedidoRecurrente toDto(PedidoRecurrente pedidoRecurrente) {
        return new DatosRespuestaPedidoRecurrente(
                pedidoRecurrente.getId(),
                pedidoRecurrente.getCliente().getId(),
                pedidoRecurrente.getItems().stream().map(this::toItemDto).collect(Collectors.toList()),
                pedidoRecurrente.getDiaEntrega()
        );

    }

    private DatosRespuestaItemRecurrente toItemDto(ItemPedidoRecurrente itemPedidoRecurrente) {
        return new DatosRespuestaItemRecurrente(
                itemPedidoRecurrente.getCantidad(),
                itemPedidoRecurrente.getProducto().getId());

    }

    public Pedido toPedido(PedidoRecurrente pedidoRecurrente) {
        Pedido pedido = new Pedido();

        pedido.setCliente(pedidoRecurrente.getCliente());

        List<ItemPedido> items = pedidoRecurrente.getItems().stream()
                .map(item -> toItemPedido(item, pedido))
                .toList();

        pedido.setItems(items);
        pedido.setFecha(LocalDateTime.now());
        pedido.setDiaEntrega(pedidoRecurrente.getDiaEntrega());
        pedido.setEntregado(false);

        return pedido;
    }


        private ItemPedido toItemPedido(ItemPedidoRecurrente itemPedidoRecurrente, Pedido pedido){
            return new ItemPedido(itemPedidoRecurrente.getCantidad(), itemPedidoRecurrente.getProducto(), pedido);

        }


    }

