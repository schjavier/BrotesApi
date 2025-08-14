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
    public ResponseEntity<Page<DatosListaPedidos>> listarPedidos(@PageableDefault(size = 10) Pageable paginacion){
        return ResponseEntity.ok(pedidosService.listarPedidos(paginacion));
    }

    @GetMapping("/all/undelivered")
    public ResponseEntity<Page<DatosListaPedidos>> listarPedidosSinEntregar(@PageableDefault(size = 10) Pageable paginacion){
        return ResponseEntity.ok(pedidosService.listarPedidosSinEntregar(paginacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetallePedido> listarUnPedido(@PathVariable Long id){
        return ResponseEntity.ok(pedidosService.listarUnPedido(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<DatosDetallePedido>> listarPedidosPorDiaDeEntrega(@RequestParam DiaDeEntrega dia){
        return ResponseEntity.ok(pedidosService.listarPedidosPorDiaEntrega(dia));
    }

    @GetMapping("/generar/planilla")
    public ResponseEntity<List<PlanillaPorCategoria>> generarPlanillaProduccion(@RequestParam DiaDeEntrega dia){
        return ResponseEntity.ok(pedidosService.generarPlanillaProduccion(dia));
    }

    @PutMapping("/{id}")
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

    @PatchMapping("/allDelivered")
    public ResponseEntity<?> markAllAsDelivered(){
        boolean entregados = pedidosService.markAllOrdersDelivered();

        if(entregados){
            return ResponseEntity.ok("Todos los pedidos han sido marcados como entregados");
        } else {
            return ResponseEntity.badRequest().body("ocurrio un problema");
        }
    }

    }

