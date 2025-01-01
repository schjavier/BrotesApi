package com.brotes.api.controller;

import com.brotes.api.modelo.producto.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    public ProductoController(ProductoRepository productoRepository, ProductoService productoService){
        this.productoRepository = productoRepository;
        this.productoService = productoService;
    }


    @GetMapping
    public ResponseEntity<Page<DatosListaProductos>> mostrarProductos(@PageableDefault(size = 5) Pageable paginacion) {
        return ResponseEntity.ok(productoService.listarProductos(paginacion));

    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaProducto> mostrarUnProducto(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.listarUnProducto(id));
    }

    @PostMapping
    public ResponseEntity<DatosRespuestaProductoUrl> registrarProducto(@RequestBody DatosRegistroProductos datosRegistroProducto,
                                                                       UriComponentsBuilder uriComponentsBuilder) {

        DatosRespuestaProductoUrl respuesta = productoService.registrarProducto(datosRegistroProducto, uriComponentsBuilder);

        return ResponseEntity.created(URI.create(respuesta.url())).body(respuesta);

    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaProducto> editarProducto(@RequestBody @Valid DatosActualizarProducto datosActualizarProducto) {

        DatosRespuestaProducto respuesta = productoService.modificarProducto(datosActualizarProducto);

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {

        boolean eliminado = productoService.eliminarProducto(id);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PatchMapping("/{id}/desactivar")
    @Transactional
    public ResponseEntity<?> desactivarProducto(@PathVariable Long id){
        boolean desactivado = productoService.desactivarProducto(id);

        if (desactivado){
            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/activar")
    @Transactional
    public ResponseEntity<?> activarProducto(@PathVariable Long id){
        boolean activado = productoService.activarProducto(id);

        if (activado){
            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
