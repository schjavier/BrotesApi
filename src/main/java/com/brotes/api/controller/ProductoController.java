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

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<DatosListaProductos>> mostrarProductos(@PageableDefault(size = 5) Pageable paginacion){
        return ResponseEntity.ok(productoService.listarProductos(paginacion));

    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaProducto> mostrarUnProducto(@PathVariable Long id){
        return ResponseEntity.ok(productoService.listarUnProducto(id));
    }

    @PostMapping
    public ResponseEntity<DatosRespuestaProducto> registrarProducto(@RequestBody DatosRegistroProductos datosRegistroProducto,
                                                                    UriComponentsBuilder uriComponentsBuilder){
        Producto producto = productoRepository.save(new Producto(datosRegistroProducto));
        DatosRespuestaProducto datosRespuestaProducto = new DatosRespuestaProducto( producto.getId(),
                                                                                    producto.getNombre(),
                                                                                    producto.getPrecio(),
                                                                                    producto.getCategoria());

        URI url = uriComponentsBuilder.path("/productos/{id}").buildAndExpand(producto.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaProducto);

    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaProducto> editarProducto(@RequestBody @Valid DatosActualizarProducto datosActualizarProducto){
        Producto producto = productoRepository.getReferenceById(datosActualizarProducto.id());
        producto.actualizarDatos(datosActualizarProducto);
        productoRepository.save(producto);
        return ResponseEntity.ok(new DatosRespuestaProducto(producto.getId(),
                                                            producto.getNombre(),
                                                            producto.getPrecio(),
                                                            producto.getCategoria()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarProducto(@PathVariable Long id){
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
