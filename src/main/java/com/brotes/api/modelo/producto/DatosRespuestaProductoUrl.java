package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;

public record DatosRespuestaProductoUrl (Long id, String nombre, Float precio, Categoria categoria, String url){
}
