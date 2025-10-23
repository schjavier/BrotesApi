package com.brotes.api.modelo.cliente;

public record DatosListadoClientes(Long id, String nombre, String direccion, boolean activo) {

public DatosListadoClientes(Cliente cliente) {
    this(cliente.getId(), cliente.getNombre(), cliente.getDireccion(), cliente.isActivo());

}
}
