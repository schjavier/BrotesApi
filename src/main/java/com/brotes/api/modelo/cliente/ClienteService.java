package com.brotes.api.modelo.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

public interface ClienteService {

    DatosRespuestaClienteConUrl registrarCliente(DatosRegistroCliente datosRegistroCliente,
                                                 UriComponentsBuilder uriComponentsBuilder);

    Page<DatosListadoClientes> listarClientes(Pageable paginacion);

    DatosRespuestaCliente listarUnCliente(Long id);

    DatosRespuestaCliente modificarCliente(DatosActualizarCliente datosActualizarCliente);

    boolean eliminarCliente(Long id);

    boolean desactivarCliente(Long id);
    boolean activarCliente(Long id);
}
