package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;

public record DatosListaProductos(Long id, String nombre, Float precio, Categoria categoria) {

    public DatosListaProductos(Producto producto){
        this(producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getCategoria());
    }
}
