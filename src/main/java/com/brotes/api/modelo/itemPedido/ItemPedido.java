package com.brotes.api.modelo.itemPedido;

import com.brotes.api.modelo.pedidos.DatosDetalleItemPedido;
import com.brotes.api.modelo.pedidos.Pedido;
import com.brotes.api.modelo.producto.Producto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items_pedido")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @JsonBackReference
    private Producto producto;


    public ItemPedido(int cantidad, Producto producto, Pedido pedido) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.pedido = pedido;
    }


}

