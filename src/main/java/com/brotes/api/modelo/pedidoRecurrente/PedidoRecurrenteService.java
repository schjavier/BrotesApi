package com.brotes.api.modelo.pedidoRecurrente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PedidoRecurrenteService {

    DatosRespuestaPedidoRecurrente registrarPedidoRecurrente(DatosRegistroPedidoRecurrente datosRegistroPedidoRecurrente);
    List<DatosRespuestaPedidoRecurrente> traerPedidosRecurrentes();
    Page<DatosRespuestaPedidoRecurrente>  paginarPedidosRecurrentes(Pageable pageable);
    void saveRecurrentOrders();

    DatosRespuestaPedidoRecurrente modifyRecurrentOrder(Long id, DatosActualizarPedidoRecurrente datosEditarPedidosRecurrentes);

    boolean deletePedidoRecurrente(Long id);
}
