package com.brotes.api.modelo.pedidos;

import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.ProductNotExistException;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.producto.DatosProductoPedido;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import com.brotes.api.validations.ClientValidations;
import com.brotes.api.validations.PedidoValidations;
import com.brotes.api.validations.ProductValidations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidosServiceImpl implements PedidoService{

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final ClientValidations clientValidations;
    private final ProductValidations productValidations;
    private final PedidoValidations pedidoValidations;

    public PedidosServiceImpl(ClienteRepository clienteRepository,
                              ProductoRepository productoRepository,
                              PedidoRepository pedidoRepository,
                              ClientValidations clientValidations,
                              ProductValidations productValidations,
                              PedidoValidations pedidoValidations){

        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.clientValidations = clientValidations;
        this.productValidations = productValidations;
        this.pedidoValidations = pedidoValidations;

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
                url.toString());

        }

    @Override
    public Page<DatosListaPedidos> listarPedidos(Pageable paginacion) {
        return pedidoRepository.findAll(paginacion).map(DatosListaPedidos::new);
    }

    @Override
    public DatosDetallePedido listarUnPedido(Long id) {

        pedidoValidations.existValidation(id);

        Pedido pedido = pedidoRepository.getReferenceById(id);

        List<DatosDetalleItemPedido> detalleItemPedidos = detallarItemPedido(pedido.getItems());

        return new DatosDetallePedido(pedido.getId(), pedido.getCliente().getId(), pedido.getCliente().getNombre(), detalleItemPedidos, pedido.getPrecioTotal(), pedido.getFecha());
    }

    @Override
    public DatosDetallePedido modificarPedido(DatosActualizarPedido datosActualizarPedido) {
        pedidoValidations.existValidation(datosActualizarPedido.idPedido());
        clientValidations.validarExistencia(datosActualizarPedido.idCliente());

        Pedido pedido = pedidoRepository.getReferenceById(datosActualizarPedido.idPedido());
        Cliente cliente = clienteRepository.getReferenceById(datosActualizarPedido.idCliente());

        pedido.actualizarDatos(datosActualizarPedido, productoRepository, clienteRepository);
        pedido.setPrecioTotal(pedido.calcularTotal());
        pedidoRepository.save(pedido);

        return new DatosDetallePedido(
                datosActualizarPedido.idPedido(),
                datosActualizarPedido.idCliente(),
                cliente.getNombre(),
                detallarItemPedido(pedido.getItems()),
                pedido.getPrecioTotal(),
                pedido.getFecha()
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

}





