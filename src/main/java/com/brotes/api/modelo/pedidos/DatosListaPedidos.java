package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.itemPedido.ItemPedido;

import java.time.LocalDateTime;
import java.util.List;

public record DatosListaPedidos(Long idPedido, Long idCliente, List<ItemPedido> items, Float precioTotal, LocalDateTime fecha) {

    public DatosListaPedidos(Pedido pedido){
        this(pedido.getId(), pedido.getCliente().getId(), pedido.getItems(), pedido.getPrecioTotal(), pedido.getFecha());
    }


}
