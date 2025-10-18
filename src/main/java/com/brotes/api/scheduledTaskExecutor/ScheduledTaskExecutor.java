package com.brotes.api.scheduledTaskExecutor;

import com.brotes.api.modelo.pedidoRecurrente.PedidoRecurrenteService;
import com.brotes.api.modelo.pedidos.PedidoService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskExecutor {

    private PedidoRecurrenteService pedidoRecurrenteService;
    private PedidoService pedidoService;

    public ScheduledTaskExecutor(PedidoRecurrenteService pedidoRecurrenteService, PedidoService pedidoService) {
        this.pedidoRecurrenteService = pedidoRecurrenteService;
        this.pedidoService = pedidoService;
    }

    @Scheduled (cron = "0 0 5 ? * MON")
    @Transactional
    public void executeScheduledTasks(){

        pedidoService.markAllOrdersDelivered();
        pedidoRecurrenteService.saveRecurrentOrders();

        //todo: Here I will implement a notifier, it will responsible of send an email to user,
        // telling that the scheduled method executes successfully.

    }
}
