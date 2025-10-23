package com.brotes.api.modelo.cliente;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity (name = "Cliente")
@Table(name = "clientes")

public class Cliente {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;

    private boolean activo;

    public Cliente(DatosRegistroCliente datosRegistroCliente) {
        this.nombre = datosRegistroCliente.nombre();
        this.direccion = datosRegistroCliente.direccion();
        this.activo = true;
    }

    public Cliente(String nombre, String direccion, boolean activo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.activo = activo;
    }

    public void actualizarDatos(DatosActualizarCliente datosActualizarCliente) {
        if(datosActualizarCliente.nombre() != null) {
            this.nombre = datosActualizarCliente.nombre();
            }
        if(datosActualizarCliente.direccion() != null){
            this.direccion = datosActualizarCliente.direccion();
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
        Cliente cliente = (Cliente) o;
        return Objects.equals(nombre, cliente.nombre) && Objects.equals(direccion, cliente.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, direccion);
    }
}


