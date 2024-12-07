package com.brotes.api.modelo.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p JOIN FETCH p.items i JOIN FETCH i.producto WHERE p.id = :id")
    Optional<Pedido> findByIdWithItemsAndProductos (@Param("id") Long id);

}
