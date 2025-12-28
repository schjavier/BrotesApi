package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.modelo.itemPedidoRecurrente.DatosRespuestaItemRecurrente;
import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import java.util.List;

public record DatosRespuestaPedidoRecurrente(
        Long idPedido,
        Long idCliente,
        String nombreCliente,
        List<DatosRespuestaItemRecurrente> item,
        DiaDeEntrega diaDeEntrega) {
}
