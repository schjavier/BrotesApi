package com.brotes.api.modelo.pedidoRecurrente;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.itemPedidoRecurrente.ItemPedidoRecurrente;
import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name="pedido_recurrente")
@Table(name="pedidos_recurrentes")
@Getter
@Setter
@ToString (exclude = {"items"})
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRecurrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedidoRecurrente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemPedidoRecurrente> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private DiaDeEntrega diaEntrega;

    public PedidoRecurrente(Cliente cliente, List<ItemPedidoRecurrente> listItems, DiaDeEntrega diaEntrega) {
        this.cliente = cliente;
        this.items = listItems;
        this.diaEntrega = diaEntrega;


    }
}
