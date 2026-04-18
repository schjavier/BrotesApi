package com.brotes.api.modelo.producto;

import jakarta.validation.constraints.NotNull;

public record DatosProductoPedido(
        @NotNull
        Long id,
        @NotNull
        Double cantidad) {
}
