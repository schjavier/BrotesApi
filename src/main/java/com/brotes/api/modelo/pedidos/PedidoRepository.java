package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    boolean existsByCliente(Cliente cliente);
    List<Pedido> findAllByCliente(Cliente cliente);
    boolean existsByClienteAndDiaEntrega(Cliente cliente, DiaDeEntrega diaDeEntrega);

}
