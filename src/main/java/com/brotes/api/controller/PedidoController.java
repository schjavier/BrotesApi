package com.brotes.api.controller;

import com.brotes.api.modelo.pedidos.DatosRegistroPedido;
import com.brotes.api.modelo.pedidos.DatosRespuestaPedido;
import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.pedidos.PedidoRepository;
import com.brotes.api.modelo.producto.DatosRespuestaProducto;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaPedido> registarPedido (@RequestBody DatosRegistroPedido datosRegistroPedido,
                                                                UriComponentsBuilder uriComponentsBuilder){
        Pedido pedido = pedidoRepository.save(new Pedido(datosRegistroPedido));
        DatosRespuestaPedido datosRespuestaPedido = new DatosRespuestaPedido(pedido.getId(),
                                                                                pedido.getCliente(),
                                                                                pedido.getItems(),
                                                                                pedido.getPrecioTotal(),
                                                                                pedido.getFecha());

        URI url = uriComponentsBuilder.path("/pedido/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaPedido);
    }
}
