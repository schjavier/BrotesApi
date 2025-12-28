package com.brotes.api.modelo.itemPedidoRecurrente;

import com.brotes.api.modelo.categoria.Categoria;

public record DatosRespuestaItemRecurrente(
        int cantidad,
        Long id,
        String nombreProducto,
        Categoria categoria) {
}
