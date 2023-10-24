package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.itemPedido.ItemPedido;

import java.time.LocalDateTime;
import java.util.ArrayList;

public record DatosRegistroPedido(String nombre,
                                  Cliente cliente,
                                  ArrayList<ItemPedido> items,
                                  Float precioTotal,
                                  LocalDateTime fecha) {
}
