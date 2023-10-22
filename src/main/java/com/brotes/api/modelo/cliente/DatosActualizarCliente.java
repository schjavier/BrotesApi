package com.brotes.api.modelo.cliente;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarCliente(@NotNull Long id, String nombre, String direccion, String telefono) {
}
