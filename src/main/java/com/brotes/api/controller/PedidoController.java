package com.brotes.api.controller;


import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.ProductNotExistException;
import com.brotes.api.modelo.pedidos.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidosService pedidosService;


    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetallePedido> tomarPedido(@RequestBody @Valid DatosTomarPedido datosTomarPedido) throws ProductNotExistException, ClientNotExistException {

        DatosDetallePedido detallePedido = pedidosService.tomarPedido(datosTomarPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(detallePedido);
    }
    }

