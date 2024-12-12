package com.brotes.api.modelo.producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductoService {

    Page<DatosListaProductos> listarProductos(Pageable paginacion);

    DatosRespuestaProducto listarUnProducto(Long id);

    DatosRespuestaProducto registrarProducto(DatosRegistroProductos datosRegistroProductos);

    DatosRespuestaProducto modificarProducto(DatosActualizarProducto datosActualizarProducto);

    boolean eliminarProducto(Long id);

    boolean desactivarProducto(Long id);
}
