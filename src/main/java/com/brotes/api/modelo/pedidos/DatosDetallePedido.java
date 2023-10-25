package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.itemPedido.ItemPedido;

import java.time.LocalDateTime;
import java.util.List;

public record DatosDetallePedido(Long idPedido, Long idCliente, List<ItemPedido> item, Float precioTotal, LocalDateTime fecha) {
}
