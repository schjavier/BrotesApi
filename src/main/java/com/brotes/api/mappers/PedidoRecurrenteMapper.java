package com.brotes.api.mappers;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.itemPedidoRecurrente.DatosRegistroItemPedidoRecurrente;
import com.brotes.api.modelo.itemPedidoRecurrente.DatosRespuestaItemRecurrente;
import com.brotes.api.modelo.itemPedidoRecurrente.ItemPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.DatosRegistroPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.DatosRespuestaPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.PedidoRecurrente;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoRecurrenteMapper {

    private final ProductoRepository productoRepository;

    public PedidoRecurrenteMapper(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
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

}


