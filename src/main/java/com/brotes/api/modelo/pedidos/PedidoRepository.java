package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    boolean existsByCliente(Cliente cliente);
    List<Pedido> findAllByCliente(Cliente cliente);
    boolean existsByClienteAndDiaEntrega(Cliente cliente, DiaDeEntrega diaDeEntrega);
    boolean existsByClienteAndDiaEntregaAndEntregadoFalse(Cliente cliente, DiaDeEntrega diaEntrega);
    List<Pedido> findAllByDiaEntrega(DiaDeEntrega diaDeEntrega);

    @Modifying
    @Query("UPDATE Pedido p SET p.entregado = true")
    void updateAllToEntregadoTrue();

    Page<Pedido> findAllByEntregadoFalse(Pageable paginacion);
    List<Pedido> findAllByEntregadoFalse();
    List<Pedido> findAllByEntregadoFalseAndDiaEntrega(DiaDeEntrega diaEntrega);
}
