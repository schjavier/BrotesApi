package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;
import jakarta.validation.constraints.NotNull;

public record DatosActualizarProducto(@NotNull Long id, String nombre, Float precio, Categoria categoria) {
}
