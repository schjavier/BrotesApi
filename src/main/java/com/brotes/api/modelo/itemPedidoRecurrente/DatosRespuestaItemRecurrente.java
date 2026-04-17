package com.brotes.api.modelo.itemPedidoRecurrente;

import com.brotes.api.modelo.categoria.Categoria;

public record DatosRespuestaItemRecurrente(
        Double cantidad,
        Long id,
        String nombreProducto,
        Categoria categoria) {
}
