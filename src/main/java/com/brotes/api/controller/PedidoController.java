package com.brotes.api.controller;

import com.brotes.api.modelo.itemPedido.ItemPedidoService;
import com.brotes.api.modelo.pedidos.DatosDetallePedido;
import com.brotes.api.modelo.pedidos.DatosTomarPedido;
import com.brotes.api.modelo.pedidos.PedidoRepository;
import com.brotes.api.modelo.pedidos.PedidosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidosService pedidosService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @PostMapping
    public ResponseEntity tomarPedido(@RequestBody @Valid DatosTomarPedido datosTomarPedido){
        pedidosService.tomarPedido(datosTomarPedido);
        return ResponseEntity.ok(new DatosDetallePedido(null, null, null, null, null));

    }
    }

