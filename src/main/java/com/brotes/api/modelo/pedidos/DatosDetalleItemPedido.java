package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.itemPedido.ItemPedido;

public record DatosDetalleItemPedido(Long id, String nombreProducto, int cantidad, Float precioProducto) {

    public DatosDetalleItemPedido(ItemPedido item){
        this(item.getProducto().getId(), item.getProducto().getNombre(), item.getCantidad(), item.getProducto().getPrecio());

    }

}
