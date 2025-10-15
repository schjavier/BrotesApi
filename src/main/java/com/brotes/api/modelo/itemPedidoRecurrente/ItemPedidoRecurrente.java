package com.brotes.api.modelo.itemPedidoRecurrente;

import com.brotes.api.modelo.pedidoRecurrente.PedidoRecurrente;
import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.producto.Producto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@ToString (exclude = {"pedidoRecurrente", "producto"})
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode (of="id")
public class ItemPedidoRecurrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_recurrente_id")
    @JsonBackReference
    private PedidoRecurrente pedidoRecurrente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @JsonBackReference
    private Producto producto;

    public ItemPedidoRecurrente(int cantidad, PedidoRecurrente pedidoRecurrente, Producto producto) {
        this.cantidad = cantidad;
        this.pedidoRecurrente = pedidoRecurrente;
        this.producto = producto;

    }
}
