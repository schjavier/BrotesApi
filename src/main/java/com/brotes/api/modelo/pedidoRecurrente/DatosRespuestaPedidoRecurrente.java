package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.modelo.itemPedido.DatosItemPedido;
import com.brotes.api.modelo.itemPedidoRecurrente.DatosRespuestaItemRecurrente;
import com.brotes.api.modelo.itemPedidoRecurrente.ItemPedidoRecurrente;
import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import java.util.List;

public record DatosRespuestaPedidoRecurrente(
        Long id,
        Long clienteId,
        List<DatosRespuestaItemRecurrente> items,
        DiaDeEntrega diaEntrega) {
}
