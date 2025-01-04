package com.brotes.api.validations;


import com.brotes.api.exceptions.PedidoNotExistException;
import com.brotes.api.modelo.pedidos.PedidoRepository;
import org.springframework.stereotype.Component;

@Component
public class PedidoValidations {

    private final PedidoRepository pedidoRepository;

    public PedidoValidations(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    public void existValidation(Long id) throws PedidoNotExistException {

        if (!pedidoRepository.existsById(id)){
            throw new PedidoNotExistException("El pedido no existe");
        }

    }
}
