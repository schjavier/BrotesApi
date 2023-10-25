package com.brotes.api.modelo.itemPedido;

import com.brotes.api.modelo.pedidos.DatosTomarPedido;
import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.pedidos.PedidoRepository;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemPedidoService {


    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public List<Producto> agregarItem(DatosItemPedido datosItemPedido){
        Producto producto = productoRepository.findById(datosItemPedido.idProducto()).get();
        Pedido pedido = pedidoRepository.findById(datosItemPedido.idPedido()).get();
        List<Producto> listaProductos = new ArrayList<>();
        listaProductos.add(producto);
        return listaProductos;

    }

    public void guardar(DatosItemPedido datosItemPedido){
        int cantidad = datosItemPedido.cantidad();
        Producto producto = productoRepository.getReferenceById(datosItemPedido.idProducto());
        Pedido pedido = pedidoRepository.getReferenceById(datosItemPedido.idPedido());
        itemPedidoRepository.save(new ItemPedido(cantidad, producto, pedido));
    }

}
