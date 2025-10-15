package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.mappers.PedidoRecurrenteMapper;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.validations.ClientValidations;
import com.brotes.api.validations.PedidoRecurrenteValidations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoRecurrenteServiceImpl implements PedidoRecurrenteService{

    private final PedidoRecurrenteValidations pedidoRecurrenteValidations;
    private final PedidoRecurrenteRepository pedidoRecurrenteRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoRecurrenteMapper pedidoRecurrenteMapper;
    private final ClientValidations clientValidations;


    public PedidoRecurrenteServiceImpl(PedidoRecurrenteValidations pedidoRecurrenteValidations,
                                       PedidoRecurrenteRepository pedidoRecurrenteRepository,
                                       ClienteRepository clienteRepository,
                                       PedidoRecurrenteMapper pedidoRecurrenteMapper,
                                       ClientValidations clientValidations) {

        this.pedidoRecurrenteValidations = pedidoRecurrenteValidations;
        this.pedidoRecurrenteRepository = pedidoRecurrenteRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoRecurrenteMapper = pedidoRecurrenteMapper;
        this.clientValidations = clientValidations;

    }

    @Override
    public DatosRespuestaPedidoRecurrente registrarPedidoRecurrente(DatosRegistroPedidoRecurrente datosRegistroPedidoRecurrente) {

        clientValidations.validarExistencia(datosRegistroPedidoRecurrente.idCliente());

        Cliente cliente = clienteRepository.getReferenceById(datosRegistroPedidoRecurrente.idCliente());

        PedidoRecurrente pedidoRecurrente = pedidoRecurrenteMapper.toEntity(datosRegistroPedidoRecurrente, cliente);

        pedidoRecurrenteValidations.PedidoRecurrenteExists(pedidoRecurrente.getCliente().getId(), pedidoRecurrente.getDiaEntrega());

        PedidoRecurrente pedidoRecurrenteGuardado = pedidoRecurrenteRepository.save(pedidoRecurrente);

        return pedidoRecurrenteMapper.toDto(pedidoRecurrenteGuardado);

    }

    @Override
    public List<DatosRespuestaPedidoRecurrente> traerPedidosRecurrentes() {

        return pedidoRecurrenteRepository.findAll().stream()
                .map(pedidoRecurrenteMapper::toDto).collect(Collectors.toList());

    }
}
