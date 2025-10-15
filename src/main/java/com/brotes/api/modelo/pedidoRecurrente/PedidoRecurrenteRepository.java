package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRecurrenteRepository extends JpaRepository<PedidoRecurrente, Long> {

    boolean existsByClienteIdAndDiaEntrega(Long clienteId, DiaDeEntrega diaDeEntrega);

}
