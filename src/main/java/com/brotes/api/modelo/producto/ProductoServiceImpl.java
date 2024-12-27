package com.brotes.api.modelo.producto;

import com.brotes.api.validations.ProductValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductValidations productValidations;

    //Inyeccion de dependencias
    public ProductoServiceImpl(ProductoRepository productoRepository, ProductValidations productValidations){
        this.productoRepository = productoRepository;
        this.productValidations = productValidations;
    }

    @Override
    public Page<DatosListaProductos> listarProductos(Pageable paginacion) {
        return productoRepository.findAll(paginacion).map(DatosListaProductos::new);
    }

    @Override
    public DatosRespuestaProducto listarUnProducto(Long id) {

        productValidations.existValidation(id);

        Producto producto = productoRepository.getReferenceById(id);
        return new DatosRespuestaProducto(producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getCategoria());
    }

    @Override
    public DatosRespuestaProductoUrl registrarProducto(DatosRegistroProductos datosRegistroProducto, UriComponentsBuilder uriBuilder) {

        productValidations.ProductUniqueValidation(datosRegistroProducto.nombre(), datosRegistroProducto.categoria());
        Producto producto = productoRepository.save(new Producto(datosRegistroProducto));


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

        productValidations.existValidation(datosActualizarProducto.id());

        Producto producto = productoRepository.getReferenceById(datosActualizarProducto.id());

        producto.actualizarDatos(datosActualizarProducto);
        productoRepository.save(producto);

        return new DatosRespuestaProducto(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCategoria());
    }

    @Override
    public boolean eliminarProducto(Long id) {
        boolean respuesta = false;

        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public boolean desactivarProducto(Long id) {
        boolean respuesta = false;

        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isPresent()){
            Producto producto = productoRepository.getReferenceById(id);
            productValidations.inactiveProductValidation(producto);
            producto.desactivar();
            productoRepository.save(producto);
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public boolean activarProducto(Long id) {
        boolean respuesta = false;

        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isPresent()){
            Producto producto = productoRepository.getReferenceById(id);
            productValidations.activeProductValidation(producto);
            producto.activar();
            productoRepository.save(producto);
            respuesta = true;
        }

        return respuesta;
    }
}

