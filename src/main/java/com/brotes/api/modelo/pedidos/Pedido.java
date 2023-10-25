package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity(name="Pedido")
@Table(name="pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> items = new ArrayList<>();

    private Float precioTotal;
    private LocalDateTime fecha = LocalDateTime.now();


}
