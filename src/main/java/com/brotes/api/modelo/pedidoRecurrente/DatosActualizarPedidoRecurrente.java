package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.modelo.itemPedidoRecurrente.DatosRegistroItemPedidoRecurrente;
import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DatosActualizarPedidoRecurrente(

        @NotNull
        Long idPedido,
        Long idCliente,
        List<DatosActualizarItemRecurrente> items,
        DiaDeEntrega diaEntrega) {}
