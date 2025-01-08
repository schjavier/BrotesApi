package com.brotes.api.modelo.pedidos;

import java.time.LocalDateTime;
import java.util.List;

public record DatosDetallePedidoUrl (Long idPedido, Long idCliente, String nombreCliente, List<DatosDetalleItemPedido> item, Float precioTotal, LocalDateTime fecha, String url) {

}
