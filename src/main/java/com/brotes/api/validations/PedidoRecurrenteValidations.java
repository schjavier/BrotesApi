package com.brotes.api.validations;

import com.brotes.api.exceptions.PedidoRecurrenteExistsException;
import com.brotes.api.modelo.pedidoRecurrente.PedidoRecurrenteRepository;
import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import org.springframework.stereotype.Component;

@Component
public class PedidoRecurrenteValidations {

    private final PedidoRecurrenteRepository pedidoRecurrenteRepository;

    public PedidoRecurrenteValidations(PedidoRecurrenteRepository pedidoRecurrenteRepository){
        this.pedidoRecurrenteRepository = pedidoRecurrenteRepository;
    }

    public void PedidoRecurrenteExists(Long clienteId, DiaDeEntrega diaDeEntrega){

        if (pedidoRecurrenteRepository.existsByClienteIdAndDiaEntrega(clienteId, diaDeEntrega)){
            throw new PedidoRecurrenteExistsException("El Pedido recurrente ya se encuentra registrado");
        }

    }

}
