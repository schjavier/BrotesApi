package com.brotes.api.modelo.cliente;

import jakarta.persistence.*;
import lombok.*;

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

    public Cliente(DatosRegistroCliente datosRegistroCliente) {
        this.nombre = datosRegistroCliente.nombre();
        this.direccion = datosRegistroCliente.direccion();
        this.telefono = datosRegistroCliente.telefono();
    }


    public void actualizarDatos(DatosActualizarCliente datosActualizarCliente) {
        if(datosActualizarCliente.nombre() != null) {
            this.nombre = datosActualizarCliente.nombre();
            }
        if(datosActualizarCliente.direccion() != null){
            this.direccion = datosActualizarCliente.direccion();
        }
        if(datosActualizarCliente.telefono() != null) {
            this.telefono = datosActualizarCliente.telefono();
        }
    }
}
