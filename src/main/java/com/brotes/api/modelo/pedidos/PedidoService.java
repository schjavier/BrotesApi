package com.brotes.api.modelo.pedidos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {

    DatosDetallePedido tomarPedido(DatosTomarPedido datosTomarPedido);

    Page<DatosListaPedidos> listarPedidos(Pageable paginacion);
    Pedido listarUnPedido (Long id);


}
