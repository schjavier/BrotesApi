package com.brotes.api.modelo.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DatosRegistroCliente(
        @NotBlank (message = "El nombre no puede estar vacio")
        @Size (min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,
        @NotBlank (message = "La direccion no puede estar vacia")
        String direccion
        ) {
}
