package com.brotes.api.modelo.producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


public class ProductoServiceImpl implements ProductoService{

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Page<DatosListaProductos> listarProductos(Pageable paginacion) {
        return productoRepository.findAll(paginacion).map(DatosListaProductos::new);
    }

    @Override
    public DatosRespuestaProducto listarUnProducto(Long id) {
        Producto producto = productoRepository.getReferenceById(id);
        // todo add validation por si no existe el producto
        return new DatosRespuestaProducto(producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getCategoria());
    }

    @Override
    public DatosRespuestaProductoUrl registrarProducto(DatosRegistroProductos datosRegistroProducto, UriComponentsBuilder uriBuilder) {

        Producto producto = productoRepository.save(new Producto(datosRegistroProducto));
        // todo add validation por si ya esta registrado.

        URI url = uriBuilder.path("/productos/{id}").buildAndExpand(producto.getId()).toUri();

        return new DatosRespuestaProductoUrl(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCategoria(),
                url.toString());

    }

    @Override
    public DatosRespuestaProducto modificarProducto(DatosActualizarProducto datosActualizarProducto) {
        return null;
    }

    @Override
    public boolean eliminarProducto(Long id) {
        return false;
    }

    @Override
    public boolean desactivarProducto(Long id) {
        return false;
    }
}

