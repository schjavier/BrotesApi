package com.brotes.api.modelo.itemPedido;

import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.producto.Producto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidos_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productos_id")
    private Producto producto;


    public ItemPedido(int cantidad, Producto producto, Pedido pedido) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.pedido = pedido;
    }
}

