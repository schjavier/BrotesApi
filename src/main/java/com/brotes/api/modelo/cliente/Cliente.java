package com.brotes.api.modelo.cliente;

import com.brotes.api.modelo.remitos.Remitos;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode (of = "id")
@Entity (name = "Cliente")
@Table(name = "clientes")

public class Cliente {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String telefono;

    @OneToMany
    private List<Remitos> remitos = new ArrayList<>();
}
