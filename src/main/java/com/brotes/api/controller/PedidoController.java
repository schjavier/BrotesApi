package com.brotes.api.controller;


import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.ProductNotExistException;
import com.brotes.api.modelo.pedidos.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidosService;

    public PedidoController(PedidoService pedidoService){
        this.pedidosService = pedidoService;
    }



    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetallePedidoUrl> tomarPedido(@RequestBody @Valid DatosTomarPedido datosTomarPedido, UriComponentsBuilder uriComponentsBuilder) throws ProductNotExistException, ClientNotExistException {

        DatosDetallePedidoUrl detallePedido = pedidosService.tomarPedido(datosTomarPedido, uriComponentsBuilder);

        return ResponseEntity.created(URI.create(detallePedido.url())).body(detallePedido);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<DatosListaPedidos>> listarPedidos(@PageableDefault(size = 5) Pageable paginacion){
        return ResponseEntity.ok(pedidosService.listarPedidos(paginacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetallePedido> listarUnPedido(@PathVariable Long id){
        return ResponseEntity.ok(pedidosService.listarUnPedido(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<DatosDetallePedido>> listarPedidosPorDiaDeEntrega(@RequestParam DiaDeEntrega dia){
        return ResponseEntity.ok(pedidosService.listarPedidosPorDiaEntrega(dia));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosDetallePedido> modificarPedido(@RequestBody @Valid DatosActualizarPedido datosActualizarPedido){

        DatosDetallePedido respuesta = pedidosService.modificarPedido(datosActualizarPedido);

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id){
        boolean eliminado = pedidosService.eliminarPedido(id);

        if (eliminado){
            return ResponseEntity.ok("Pedido Eliminado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    }

