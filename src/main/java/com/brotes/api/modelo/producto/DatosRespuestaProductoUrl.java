package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;

public record DatosRespuestaProductoUrl (Long id, String nombre, Categoria categoria, String url){
}
