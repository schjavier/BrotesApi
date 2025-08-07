package com.brotes.api.modelo.producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


public interface ProductoService {

    Page<DatosListaProductos> listarProductos(Pageable paginacion);
    DatosRespuestaProducto listarUnProducto(Long id);
    DatosRespuestaProductoUrl registrarProducto(DatosRegistroProductos datosRegistroProductos, UriComponentsBuilder uriComponentsBuilder);
    DatosRespuestaProducto modificarProducto(DatosActualizarProducto datosActualizarProducto);
    List<DatosRespuestaProducto> mostrarProductoPorNombre(String nombre);
    boolean eliminarProducto(Long id);
    boolean desactivarProducto(Long id);
    boolean activarProducto(Long id);

    List<DatosRespuestaProducto> mostrarProductoPorNombreAndEstado(String nombre, Boolean estado);
}
