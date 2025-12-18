package com.brotes.api.controller;

import com.brotes.api.modelo.pedidoRecurrente.DatosActualizarPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.DatosRegistroPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.DatosRespuestaPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.PedidoRecurrenteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos-recurrentes")
public class PedidoRecurrenteController {

    private final PedidoRecurrenteService pedidoRecurrenteService;

    public PedidoRecurrenteController(PedidoRecurrenteService pedidoRecurrenteService){
        this.pedidoRecurrenteService = pedidoRecurrenteService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaPedidoRecurrente> registrarRecurrente(@RequestBody DatosRegistroPedidoRecurrente request){
        DatosRespuestaPedidoRecurrente response = pedidoRecurrenteService.registrarPedidoRecurrente(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaPedidoRecurrente>> listarPedidosRecurrente(Pageable pageable){
        return ResponseEntity.ok(pedidoRecurrenteService.paginarPedidosRecurrentes(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatosRespuestaPedidoRecurrente> editarPedidosRecurrentes(
            @RequestBody @Valid DatosActualizarPedidoRecurrente datosEditarPedidosRecurrentes,
            @PathVariable Long id){

            DatosRespuestaPedidoRecurrente respuesta = pedidoRecurrenteService.modifyRecurrentOrder(id, datosEditarPedidosRecurrentes);

            return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPedidosRecurrentes(@PathVariable Long id){
        boolean eliminado = pedidoRecurrenteService.deletePedidoRecurrente(id);

        if(eliminado){
            return ResponseEntity.ok("Pedido recurrente eliminado");
        }else {
            return ResponseEntity.notFound().build();
        }
    }


}
