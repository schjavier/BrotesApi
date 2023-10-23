package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Float precio;

    @OneToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}
