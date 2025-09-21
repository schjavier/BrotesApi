package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;
import com.brotes.api.modelo.itemPedido.ItemPedido;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table (name = "productos")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany (mappedBy = "producto")
    private Set<ItemPedido> items = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private boolean activo;

    public Producto(DatosRegistroProductos datosRegistroProducto) {
        this.nombre = datosRegistroProducto.nombre();
        this.categoria = datosRegistroProducto.categoria();
        this.activo = true;
    }

    public Producto(Long idProducto, String nombreProducto, Categoria categoriaProducto, boolean activo) {
        this.id = idProducto;
        this.nombre = nombreProducto;
        this.categoria = categoriaProducto;
        this.activo = activo;

    }


    public void actualizarDatos(DatosActualizarProducto datosActualizarProducto) {
        if (datosActualizarProducto.nombre() != null) {
            this.nombre = datosActualizarProducto.nombre();
        }
        if(datosActualizarProducto.categoria() != null) {
            this.categoria = datosActualizarProducto.categoria();
        }
    }

    public void desactivar(){
        this.activo = false;
    }

    public void activar(){
        this.activo = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(nombre, producto.nombre) && categoria == producto.categoria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, categoria);
    }
}
