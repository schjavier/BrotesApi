package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidosService {


    @Autowired
    private ClienteRepository clienteRepository;
//    Aca deberia ir itemProducto me parece!
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

//faltan las validaciones...
//    metodo inclompleto
    public void tomarPedido(DatosTomarPedido datosTomarPedido) {
        Cliente cliente = clienteRepository.findById(datosTomarPedido.idCliente()).get();
    }
}
