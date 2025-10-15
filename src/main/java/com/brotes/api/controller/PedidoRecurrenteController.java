package com.brotes.api.controller;

import com.brotes.api.modelo.pedidoRecurrente.DatosRegistroPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.DatosRespuestaPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.PedidoRecurrenteService;
import jakarta.transaction.Transactional;
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
    public ResponseEntity<List<DatosRespuestaPedidoRecurrente>> listarPedidosRecurrente(){
        return ResponseEntity.ok(pedidoRecurrenteService.traerPedidosRecurrentes());
    }


}
