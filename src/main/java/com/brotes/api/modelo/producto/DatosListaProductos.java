package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;

public record DatosListaProductos(Long id, String nombre, Categoria categoria, boolean activo) {

    public DatosListaProductos(Producto producto){
        this(producto.getId(), producto.getNombre(), producto.getCategoria(), producto.isActivo());
    }
}
