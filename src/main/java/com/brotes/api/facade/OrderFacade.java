package com.brotes.api.facade;

import com.brotes.api.modelo.itemPedidoRecurrente.DatosRegistroItemPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.DatosRegistroPedidoRecurrente;
import com.brotes.api.modelo.pedidoRecurrente.PedidoRecurrenteService;
import com.brotes.api.modelo.pedidos.DatosDetallePedidoUrl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderFacade {

    private final PedidoRecurrenteService pedidoRecurrenteService;

    public OrderFacade(PedidoRecurrenteService pedidoRecurrenteService) {
        this.pedidoRecurrenteService = pedidoRecurrenteService;
    }

    public void setPedidoRecurrente(DatosDetallePedidoUrl datos) {

        List<DatosRegistroItemPedidoRecurrente> itemsRecurrentes = datos.item().stream()
                .map(item -> new DatosRegistroItemPedidoRecurrente(item.cantidad(), datos.idPedido(), item.id()))
                .toList();

        DatosRegistroPedidoRecurrente datosRegistroPedidoRecurrente = new DatosRegistroPedidoRecurrente(
                datos.idCliente(), itemsRecurrentes, datos.diaDeEntrega()
        );

        pedidoRecurrenteService.registrarPedidoRecurrente(datosRegistroPedidoRecurrente);


    }


}
