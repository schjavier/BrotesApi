package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.itemPedido.ItemPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DatosListaPedidos(Long idPedido, Long idCliente, String nombreCliente, List<DatosDetalleItemPedido> items, Float precioTotal, LocalDateTime fecha) {

    public DatosListaPedidos(Pedido pedido){
        this(pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNombre(),
                pedido.getItems().stream()
                        .map(DatosDetalleItemPedido::new)
                        .collect(Collectors.toList()),

                pedido.getPrecioTotal(),
                pedido.getFecha());
    }


}
