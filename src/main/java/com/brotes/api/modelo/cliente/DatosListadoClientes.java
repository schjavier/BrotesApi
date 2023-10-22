package com.brotes.api.modelo.cliente;

public record DatosListadoClientes(Long id, String nombre, String direccion, String telefono) {

public DatosListadoClientes(Cliente cliente) {
    this(cliente.getId(), cliente.getNombre(), cliente.getDireccion(), cliente.getTelefono());

}
}
