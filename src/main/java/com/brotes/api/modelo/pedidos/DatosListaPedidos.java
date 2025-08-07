package com.brotes.api.modelo.pedidos;

import java.util.List;
import java.util.stream.Collectors;

public record DatosListaPedidos(
        Long idPedido,
        Long idCliente,
        String nombreCliente,
        List<DatosDetalleItemPedido> item,
        Float precioTotal,
        String fecha,
        DiaDeEntrega diaDeEntrega) {

    public DatosListaPedidos(Pedido pedido){
        this(pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNombre(),
                pedido.getItems().stream()
                        .map(DatosDetalleItemPedido::new)
                        .collect(Collectors.toList()),

                pedido.getPrecioTotal(),
                pedido.getFecha().toString(),
                pedido.getDiaEntrega());
    }


}
