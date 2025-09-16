package com.brotes.api.validations;

import com.brotes.api.DatesUtil;
import com.brotes.api.exceptions.PedidoDuplicadoException;
import com.brotes.api.exceptions.PedidoNotExistException;
import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.pedidos.PedidoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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

        if(pedidoRepository.existsByClienteAndDiaEntregaAndEntregadoFalse(pedido.getCliente(), pedido.getDiaEntrega())){

            throw new PedidoDuplicadoException( "Se encontro un pedido a nombre de " +
                    pedido.getCliente().getNombre() + " para entregar el dia: " + pedido.getDiaEntrega().name());

        }

    }

    /**
     * Metodo que busca en una lista de Pedidos, si la semana del año de la fecha pasada por parametro se encuentra
     * en la lista de pedidos
     *
     * @param fecha La fecha que se quiere buscar en la lista de pedidos
     * @param pedidos La lista a iterar
     * @return true si encuentra la semana del año en la lista de pedidos, false de cualquier otra manera.
     */

    private boolean isWeekOfYearDuplicated(LocalDate fecha, List<Pedido> pedidos){
        boolean resultado = false;

        for (Pedido pedido : pedidos){
            if (DatesUtil.isSameWeekOfYear(pedido.getFecha().toLocalDate(), fecha)){
                resultado = true;
                break;
            }
        }

        return resultado;
    }

    public void isDiaDeEntregaValid(String dia){
        if (!dia.equals("MARTES") && !dia.equals("VIERNES")){
            throw new IllegalArgumentException("El dia de entrega es incorrecto");
        }
    }

}
