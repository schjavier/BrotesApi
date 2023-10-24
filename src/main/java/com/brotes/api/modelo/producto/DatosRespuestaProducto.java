package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;

public record DatosRespuestaProducto(Long id, String nombre, Float precio, Categoria categoria) {
}
