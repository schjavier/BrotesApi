package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;


@Entity(name="Pedido")
@Table(name="pedidos")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private DiaDeEntrega diaEntrega;

    private boolean entregado;

    public Pedido(Cliente cliente, List<ItemPedido> items) {
        this.cliente = cliente;
        this.items = items;
        this.fecha = LocalDateTime.now();
    }

    public Pedido(Cliente cliente, List<ItemPedido> items, DiaDeEntrega diaDeEntrega) {
        this.cliente = cliente;
        this.items = items;
        this.fecha = LocalDateTime.now();
        this.diaEntrega = diaDeEntrega;
        this.entregado = false;
    }

    public void markAsDelivered(){
        this.entregado = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(cliente, pedido.cliente) &&
                Objects.equals(items, pedido.items) &&
                Objects.equals(fecha.getDayOfMonth(), pedido.fecha.getDayOfMonth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, items, fecha);
    }

    public void actualizarDatos(DatosActualizarPedido datosActualizarPedido, ProductoRepository productoRepository, ClienteRepository clienteRepository){

        if (datosActualizarPedido.idCliente() != null){
            setClienteFromRepository(datosActualizarPedido.idCliente(), clienteRepository);
        }

        if (datosActualizarPedido.items() != null){
            this.items.clear();

            datosActualizarPedido.items().forEach(itemDto -> {

                Producto producto = productoRepository.getReferenceById(itemDto.id());
                ItemPedido nuevoItem = new ItemPedido(
                        itemDto.cantidad(),
                        producto,
                        this
                );
                this.items.add(nuevoItem);

            });

        }

        if (datosActualizarPedido.diaEntrega() != null) {
            this.diaEntrega = datosActualizarPedido.diaEntrega();
        }

    }

    private void setClienteFromRepository(Long id, ClienteRepository clienteRepository){
        Cliente cliente = clienteRepository.getReferenceById(id);
        setCliente(cliente);


    }



}
