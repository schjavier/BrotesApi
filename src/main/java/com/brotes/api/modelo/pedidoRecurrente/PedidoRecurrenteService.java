package com.brotes.api.modelo.pedidoRecurrente;

import java.util.List;

public interface PedidoRecurrenteService {

    DatosRespuestaPedidoRecurrente registrarPedidoRecurrente(DatosRegistroPedidoRecurrente datosRegistroPedidoRecurrente);
    List<DatosRespuestaPedidoRecurrente> traerPedidosRecurrentes();
    void saveRecurrentOrders();

}
