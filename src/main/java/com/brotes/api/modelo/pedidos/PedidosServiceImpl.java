package com.brotes.api.modelo.pedidos;

import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.ProductNotExistException;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.itemPedido.ItemPedidoRepository;
import com.brotes.api.modelo.producto.DatosProductoPedido;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import com.brotes.api.validations.ClientValidations;
import com.brotes.api.validations.PedidoValidations;
import com.brotes.api.validations.ProductValidations;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidosServiceImpl implements PedidoService{

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final ClientValidations clientValidations;
    private final ProductValidations productValidations;
    private final PedidoValidations pedidoValidations;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidosServiceImpl(ClienteRepository clienteRepository,
                              ProductoRepository productoRepository,
                              PedidoRepository pedidoRepository,
                              ClientValidations clientValidations,
                              ProductValidations productValidations,
                              PedidoValidations pedidoValidations,
                              ItemPedidoRepository itemPedidoRepository){

        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.clientValidations = clientValidations;
        this.productValidations = productValidations;
        this.pedidoValidations = pedidoValidations;
        this.itemPedidoRepository = itemPedidoRepository;

    }

    @Override
    public DatosDetallePedidoUrl tomarPedido(DatosTomarPedido datosTomarPedido, UriComponentsBuilder uriComponentsBuilder) throws ClientNotExistException, ProductNotExistException {

        Cliente cliente = obtenerClienteValidado(datosTomarPedido.idCliente());
        List<ItemPedido> itemsPedido = new ArrayList<>();
        URI url;

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFecha(LocalDateTime.now());

        for (DatosProductoPedido datosProducto : datosTomarPedido.items()){

            Producto producto = obtenerProductoValidado(datosProducto.id());

            ItemPedido itemPedido = new ItemPedido(datosProducto.cantidad(), producto, pedido);
                itemsPedido.add(itemPedido);
            }

        pedido.setItems(itemsPedido);
        Float precioTotal = pedido.calcularTotal();
        pedido.setPrecioTotal(precioTotal);
        pedido.setDiaEntrega(datosTomarPedido.diaEntrega());

        pedidoValidations.validarPedidoUnico(pedido);
        pedidoRepository.save(pedido);

        List<DatosDetalleItemPedido> detalleItemPedidos = detallarItemPedido(itemsPedido);

        url = uriComponentsBuilder.path("/pedidos/{id}").buildAndExpand(pedido.getId()).toUri();

        return new DatosDetallePedidoUrl(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNombre(),
                detalleItemPedidos,
                pedido.getPrecioTotal(),
                pedido.getFecha(),
                pedido.getDiaEntrega(),
                url.toString());

        }

    @Override
    public Page<DatosListaPedidos> listarPedidos(Pageable paginacion) {
        return pedidoRepository.findAll(paginacion).map(DatosListaPedidos::new);
    }

    @Override
    public Page<DatosListaPedidos> listarPedidosSinEntregar(Pageable paginacion) {
        return pedidoRepository.findAllByEntregadoFalse(paginacion).map(DatosListaPedidos::new);
    }

    @Override
    public DatosDetallePedido listarUnPedido(Long id) {

        pedidoValidations.existValidation(id);

        Pedido pedido = pedidoRepository.getReferenceById(id);

        List<DatosDetalleItemPedido> detalleItemPedidos = detallarItemPedido(pedido.getItems());

        return new DatosDetallePedido(pedido.getId(), pedido.getCliente().getId(), pedido.getCliente().getNombre(), detalleItemPedidos, pedido.getPrecioTotal(), pedido.getFecha(), pedido.getDiaEntrega());
    }

    @Override
    public DatosDetallePedido modificarPedido(DatosActualizarPedido datosActualizarPedido) {
        pedidoValidations.existValidation(datosActualizarPedido.idPedido());
        clientValidations.validarExistencia(datosActualizarPedido.idCliente());

        Pedido pedido = pedidoRepository.getReferenceById(datosActualizarPedido.idPedido());
        Cliente cliente = clienteRepository.getReferenceById(datosActualizarPedido.idCliente());

        updateItems(pedido, pedido.getItems());

        pedido.actualizarDatos(datosActualizarPedido, productoRepository, clienteRepository);
        pedido.setPrecioTotal(pedido.calcularTotal());

        pedidoRepository.save(pedido);

        return new DatosDetallePedido(
                datosActualizarPedido.idPedido(),
                datosActualizarPedido.idCliente(),
                cliente.getNombre(),
                detallarItemPedido(pedido.getItems()),
                pedido.getPrecioTotal(),
                pedido.getFecha(),
                datosActualizarPedido.diaEntrega()
                );

    }

    @Override
    public boolean eliminarPedido(Long id) {
        boolean response = false;

        if(pedidoRepository.existsById(id)){
            pedidoRepository.deleteById(id);
            response = true;
        }

        return response;
    }

    @Override
    public List<DatosDetallePedido> listarPedidosPorDiaEntrega(DiaDeEntrega diaDeEntrega) {

        List<Pedido> pedidosList = pedidoRepository.findAllByDiaEntrega(diaDeEntrega);

        return pedidosList.stream().map(pedido -> new DatosDetallePedido(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNombre(),
                detallarItemPedido(pedido.getItems()),
                pedido.getPrecioTotal(),
                pedido.getFecha(),
                pedido.getDiaEntrega()
        )).collect(Collectors.toList());
    }

    private Cliente obtenerClienteValidado(Long idCliente) throws ClientNotExistException{
            clientValidations.validarExistencia(idCliente);
            return clienteRepository.findById(idCliente).get();

        }

        private Producto obtenerProductoValidado(Long idProducto){
            productValidations.existValidation(idProducto);
            return productoRepository.findById(idProducto).get();

        }

        private List<DatosDetalleItemPedido> detallarItemPedido(List<ItemPedido> itemsPedido){

           return itemsPedido.stream().map(DatosDetalleItemPedido::new).toList();

        }

        @Transactional
    public void updateItems(Pedido pedido, List<ItemPedido> updatedItems){

            Set<Long> updatedItemIds = updatedItems.stream()
                    .map(ItemPedido::getId)
                    .collect(Collectors.toSet());

            List<ItemPedido> itemsToRemove = pedido.getItems().stream()
                    .filter(item -> !updatedItemIds.contains(item.getId()))
                    .collect(Collectors.toList());

            if(itemsToRemove.isEmpty()){
                itemPedidoRepository.deleteAll(itemsToRemove);
            }

            updatedItems.forEach(updatedItem -> {
                itemPedidoRepository.findById(updatedItem.getId()).ifPresent( existingItem -> {
                    existingItem.setCantidad(updatedItem.getCantidad());
                    itemPedidoRepository.save(existingItem);
                });
            });

        }
        @Scheduled(cron = "0 0 0 ? * SUN")
        @Transactional
        @Override
        public boolean markAllOrdersDelivered() {
            boolean response = false;

            List<Pedido> pedidosActuales = pedidoRepository.findAllByEntregadoFalse();

            if(pedidosActuales != null) {
                pedidoRepository.updateAllToEntregadoTrue();
                response = true;
            }

            return response;
        }


    @Override
    public List<PlanillaPorCategoria> generarPlanillaProduccion(DiaDeEntrega diaDeEntrega) {

        List<Pedido> pedidosList = pedidoRepository.findAllByEntregadoFalse()
                .stream()
                .filter(pedido -> pedido.getDiaEntrega() == diaDeEntrega)
                .toList();

        Map<String, Map<String, Integer>> itemsPorCategoria = pedidosList.stream()
                .flatMap(pedido -> pedido.getItems().stream())
                .collect(Collectors.groupingBy( item ->
                        item.getProducto().getCategoria().toString(),
                        Collectors.groupingBy(item -> item.getProducto().getNombre(),
                                Collectors.summingInt(ItemPedido::getCantidad)
                        )
                ));

        List<PlanillaPorCategoria> planilla = new ArrayList<>();

        itemsPorCategoria.forEach((categoria, productos) -> {
            List<ItemPlanillaProduccion> items = productos.entrySet().stream()
                    .map(entry -> new ItemPlanillaProduccion(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            planilla.add(new PlanillaPorCategoria(categoria, items));
        });

        return planilla;

    }
}





