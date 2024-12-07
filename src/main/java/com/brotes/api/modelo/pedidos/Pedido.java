package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.producto.DatosListaProductos;
import com.brotes.api.modelo.producto.DatosProductoPedido;
import com.brotes.api.modelo.producto.Producto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity(name="Pedido")
@Table(name="pedidos")
@Getter
@Setter
@ToString
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

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();

    private Float precioTotal;
    private LocalDateTime fecha;

    public Pedido(Cliente cliente, List<ItemPedido> items, float precioTotal) {
        this.cliente = cliente;
        this.items = items;
        this.precioTotal = precioTotal;
        this.fecha = LocalDateTime.now();
    }

    public Float calcularTotal(){
        Float total = 0F;

        for (ItemPedido item : items){
            Float subTotal = item.getProducto().getPrecio() * item.getCantidad();
            total += subTotal;
        }

        return total;
    }

}
