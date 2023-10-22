package com.brotes.api.modelo.pedidos;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.producto.Producto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



//@Entity (name="Pedido")
//@Table (name="pedidos")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@EqualsAndHashCode (of = "id")
public class Pedidos {

//    @Id
//    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany
//    @JoinColumn(name = "producto_id")
    private List<Producto> productos = new ArrayList<>();

    private int cantidades;

//    @ManyToOne (fetch = FetchType.LAZY)
    private Cliente cliente;

    private LocalDateTime fechaRemito;
}
