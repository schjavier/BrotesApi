package com.brotes.api.modelo.pedidos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

public interface PedidoService {

    DatosDetallePedidoUrl tomarPedido(DatosTomarPedido datosTomarPedido, UriComponentsBuilder uriComponentsBuilder);

    Page<DatosListaPedidos> listarPedidos(Pageable paginacion);
    DatosDetallePedido listarUnPedido(Long id);
    DatosDetallePedido modificarPedido(DatosActualizarPedido datosActualizarPedido);
    boolean eliminarPedido(Long id);

}
