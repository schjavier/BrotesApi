package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.itemPedido.ItemPedido;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record DatosActualizarPedido(
        @NotNull
        Long idPedido,
        Long idCliente,
        List<ItemPedido> items,
        DiaDeEntrega diaDeEntrega) {}
