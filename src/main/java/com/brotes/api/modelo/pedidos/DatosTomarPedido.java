package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.itemPedido.ItemPedido;

import java.time.LocalDateTime;
import java.util.ArrayList;

public record DatosTomarPedido(String nombre,
                               Long idCliente,
                               ArrayList<ItemPedido> items,
                               Float precioTotal,
                               LocalDateTime fecha) {
}
