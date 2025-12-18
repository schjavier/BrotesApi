package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.mappers.PedidoRecurrenteMapper;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.cliente.ClienteService;
import com.brotes.api.modelo.itemPedidoRecurrente.ItemPedidoRecurrente;
import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.pedidos.PedidoService;
import com.brotes.api.validations.ClientValidations;
import com.brotes.api.validations.PedidoRecurrenteValidations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoRecurrenteServiceImpl implements PedidoRecurrenteService{

    private final PedidoRecurrenteValidations pedidoRecurrenteValidations;
    private final PedidoRecurrenteRepository pedidoRecurrenteRepository;

    private final ClienteService clienteService;

    private final PedidoRecurrenteMapper pedidoRecurrenteMapper;
    private final PedidoService pedidoService;



    public PedidoRecurrenteServiceImpl(PedidoRecurrenteValidations pedidoRecurrenteValidations,
                                       PedidoRecurrenteRepository pedidoRecurrenteRepository,
                                       ClienteService clienteService,
                                       PedidoRecurrenteMapper pedidoRecurrenteMapper,
                                       PedidoService pedidoService) {

        this.pedidoRecurrenteValidations = pedidoRecurrenteValidations;
        this.pedidoRecurrenteRepository = pedidoRecurrenteRepository;
        this.clienteService = clienteService;
        this.pedidoRecurrenteMapper = pedidoRecurrenteMapper;
        this.pedidoService = pedidoService;

    }

    @Override
    public DatosRespuestaPedidoRecurrente registrarPedidoRecurrente(DatosRegistroPedidoRecurrente datosRegistroPedidoRecurrente) {

        Cliente cliente = clienteService.getClienteById(datosRegistroPedidoRecurrente.idCliente());

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

    @Override
    public Page<DatosRespuestaPedidoRecurrente> paginarPedidosRecurrentes(Pageable pageable) {

        return pedidoRecurrenteRepository.findAll(pageable).map(pedidoRecurrenteMapper::toDto);
    }

    @Override
    public void saveRecurrentOrders() {

        List<PedidoRecurrente> recurrentOrderList = pedidoRecurrenteRepository.findAll();

        List<Pedido> orderList = recurrentOrderList.stream()
                .map(pedidoRecurrenteMapper::toPedido).toList();

        pedidoService.saveScheduledOrders(orderList);
    }

    @Override
    public DatosRespuestaPedidoRecurrente modifyRecurrentOrder(Long id, DatosActualizarPedidoRecurrente datosEditarPedidosRecurrentes) {

       Cliente cliente = clienteService.getClienteById(datosEditarPedidosRecurrentes.idCliente());

       PedidoRecurrente pedidoEditado = pedidoRecurrenteMapper.toEntity(datosEditarPedidosRecurrentes, cliente, id);

       pedidoRecurrenteValidations.PedidoRecurrenteExists(pedidoEditado.getCliente().getId(), pedidoEditado.getDiaEntrega());

       pedidoRecurrenteRepository.save(pedidoEditado);

       return pedidoRecurrenteMapper.toDto(pedidoEditado);
    }

    @Override
    public boolean deletePedidoRecurrente(Long id) {
        boolean respuesta = false;
        PedidoRecurrente pedidoAEliminar = pedidoRecurrenteRepository.findById(id).orElse(null);

        if(pedidoAEliminar != null){
            pedidoRecurrenteRepository.delete(pedidoAEliminar);
            respuesta = true;
        }

        return respuesta;
    }
}
