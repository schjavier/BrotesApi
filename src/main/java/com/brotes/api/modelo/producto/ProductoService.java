package com.brotes.api.modelo.producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;


public interface ProductoService {

    Page<DatosListaProductos> listarProductos(Pageable paginacion);

    DatosRespuestaProducto listarUnProducto(Long id);

    DatosRespuestaProductoUrl registrarProducto(DatosRegistroProductos datosRegistroProductos, UriComponentsBuilder uriComponentsBuilder);

    DatosRespuestaProducto modificarProducto(DatosActualizarProducto datosActualizarProducto);

    boolean eliminarProducto(Long id);

    boolean desactivarProducto(Long id);
}
