package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.modelo.itemPedidoRecurrente.DatosRegistroItemPedidoRecurrente;
import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DatosRegistroPedidoRecurrente(
        @NotNull
        Long idCliente,
        @NotBlank
        List<DatosRegistroItemPedidoRecurrente> items,
        @NotBlank
        DiaDeEntrega diaEntrega) {
}
