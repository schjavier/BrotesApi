package com.brotes.api.modelo.pedidos;


import com.brotes.api.modelo.producto.DatosProductoPedido;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record DatosTomarPedido(@NotNull Long idCliente,
                               @NotNull
                               List<DatosProductoPedido> items,
                               @NotNull DiaDeEntrega diaEntrega,
                               @NotNull boolean isRecurrent
                             ) {
}
