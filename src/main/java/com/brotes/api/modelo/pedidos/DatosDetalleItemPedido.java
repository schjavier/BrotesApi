package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.itemPedido.ItemPedido;

public record DatosDetalleItemPedido(String nombreProducto, int cantidad, Float precioProducto) {

    public DatosDetalleItemPedido(ItemPedido item){
        this(item.getProducto().getNombre(), item.getCantidad(), item.getProducto().getPrecio());

    }

}
