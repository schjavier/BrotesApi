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

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    public Producto(DatosRegistroProductos datosRegistroProducto) {
        this.nombre = datosRegistroProducto.nombre();
        this.precio = datosRegistroProducto.precio();
        this.categoria = datosRegistroProducto.categoria();
    }

    public void actualizarDatos(DatosActualizarProducto datosActualizarProducto) {
        if (datosActualizarProducto.nombre() != null) {
            this.nombre = datosActualizarProducto.nombre();
        }
        if (datosActualizarProducto.precio() != null) {
            this.precio = datosActualizarProducto.precio();
        }
        if(datosActualizarProducto.categoria() != null) {
            this.categoria = datosActualizarProducto.categoria();
        }
    }
}
