package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.mappers.PedidoRecurrenteMapper;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.producto.ProductoRepository;
import com.brotes.api.validations.PedidoRecurrenteValidations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoRecurrenteServiceImpl implements PedidoRecurrenteService{

    private final PedidoRecurrenteValidations pedidoRecurrenteValidations;
    private final PedidoRecurrenteRepository pedidoRecurrenteRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRecurrenteMapper pedidoRecurrenteMapper;


    public PedidoRecurrenteServiceImpl(PedidoRecurrenteValidations pedidoRecurrenteValidations,
                                       PedidoRecurrenteRepository pedidoRecurrenteRepository,
                                       ClienteRepository clienteRepository,
                                       ProductoRepository productoRepository,
                                       PedidoRecurrenteMapper pedidoRecurrenteMapper) {

        this.pedidoRecurrenteValidations = pedidoRecurrenteValidations;
        this.pedidoRecurrenteRepository = pedidoRecurrenteRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.pedidoRecurrenteMapper = pedidoRecurrenteMapper;

    }

    @Override
    public DatosRespuestaPedidoRecurrente registrarPedidoRecurrente(DatosRegistroPedidoRecurrente datosRegistroPedidoRecurrente) {

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
