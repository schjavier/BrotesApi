package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.producto.DatosProductoPedido;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DatosActualizarPedido(
        @NotNull
        Long idPedido,
        Long idCliente,
        List<DatosProductoPedido> items,
        DiaDeEntrega diaEntrega) {}
