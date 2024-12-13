package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "productos")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Float precio;

    @OneToMany (mappedBy = "producto")
    private Set<ItemPedido> items = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private Boolean activo;

    public Producto(DatosRegistroProductos datosRegistroProducto) {
        this.nombre = datosRegistroProducto.nombre();
        this.precio = datosRegistroProducto.precio();
        this.categoria = datosRegistroProducto.categoria();
        this.activo = true;
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

    public void desactivar(){
        this.activo = false;
    }
}
