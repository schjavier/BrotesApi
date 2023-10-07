package com.brotes.api.modelo.remitos;

import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.producto.Producto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Entity (name="Remito")
@Table (name="remitos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode (of = "id")
public class Remitos {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "producto_id")
    private List<Producto> productos = new ArrayList<>();

    private int cantidades;

    @ManyToOne (fetch = FetchType.LAZY)
    private Cliente cliente;

    private LocalDateTime fechaRemito;
}
