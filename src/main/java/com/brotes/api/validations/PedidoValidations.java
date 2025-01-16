package com.brotes.api.validations;

import com.brotes.api.DatesUtil;
import com.brotes.api.exceptions.PedidoDuplicadoException;
import com.brotes.api.exceptions.PedidoNotExistException;
import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.pedidos.PedidoRepository;
import org.springframework.stereotype.Component;
import java.time.temporal.ChronoField;

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

    public void validarPedidoUnico(Pedido pedido){
        int nroSemanaPedido = pedido.getFecha().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        int nroSemanaActual = DatesUtil.devolverNumeroSemanaActual();

        if(pedidoRepository.existsByCliente(pedido.getCliente()) && nroSemanaPedido == nroSemanaActual){

            throw new PedidoDuplicadoException("Se encontro un pedido a nombre de " +
                    pedido.getCliente().getNombre() + " para esta semana");

        }


    }

}
