package com.brotes.api.modelo.categoria;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

}
