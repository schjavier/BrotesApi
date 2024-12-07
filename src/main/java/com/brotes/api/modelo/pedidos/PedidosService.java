package com.brotes.api.modelo.pedidos;

import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.ProductNotExistException;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.producto.DatosProductoPedido;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidosService {


    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public DatosDetallePedido tomarPedido(DatosTomarPedido datosTomarPedido) throws ClientNotExistException, ProductNotExistException {

        Cliente cliente = clienteRepository.findById(datosTomarPedido.idCliente())
                .orElseThrow( () -> new ClientNotExistException("El cliente con el ID: " + datosTomarPedido.idCliente() + " no existe"));

//        List<DatosProductoPedido> productos = datosTomarPedido.items();
        List<ItemPedido> itemsPedido = new ArrayList<>();
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFecha(LocalDateTime.now());

        for (DatosProductoPedido datosProducto : datosTomarPedido.items()){

            Producto producto = productoRepository.findById(datosProducto.id())
                    .orElseThrow( () -> new ProductNotExistException("El producto con el ID: " + datosProducto.id() + " no existe"));

            ItemPedido itemPedido = new ItemPedido(datosProducto.cantidad(), producto, pedido);
                itemsPedido.add(itemPedido);
            }

        pedido.setItems(itemsPedido);
        Float precioTotal = pedido.calcularTotal();
        pedido.setPrecioTotal(precioTotal);

        pedidoRepository.save(pedido);

        List<DatosDetalleItemPedido> detalleItemPedidos = itemsPedido.stream()
                .map(
                        item -> new DatosDetalleItemPedido(
                        item.getId(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        item.getProducto().getPrecio()
                ))
                .toList();


        return new DatosDetallePedido(pedido.getId(), pedido.getCliente().getId(), detalleItemPedidos, pedido.getPrecioTotal(), pedido.getFecha());

        }


//        todo: desacoplar validaciones.

    }





