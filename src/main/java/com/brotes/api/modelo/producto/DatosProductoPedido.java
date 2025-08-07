package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;
import jakarta.validation.constraints.NotNull;

public record DatosProductoPedido(
        @NotNull
        Long id,
        @NotNull
        int cantidad) {
}
