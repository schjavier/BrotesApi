package com.brotes.api.controller;

import com.brotes.api.modelo.cliente.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteServiceImpl clienteService;

    public ClienteController(ClienteServiceImpl clienteService){
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<DatosRespuestaClienteConUrl> registrarCliente(@RequestBody DatosRegistroCliente datosRegistroCliente,
                                                                  UriComponentsBuilder uriComponentsBuilder){

        DatosRespuestaClienteConUrl respuesta = clienteService.registrarCliente(datosRegistroCliente, uriComponentsBuilder);

        return ResponseEntity.created(URI.create(respuesta.url())).body(respuesta);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoClientes>> mostrarClientes(@PageableDefault(size = 5) Pageable paginacion){
        return ResponseEntity.ok(clienteService.listarClientes(paginacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaCliente> mostrarUnCliente(@PathVariable Long id) {

        DatosRespuestaCliente respuesta = clienteService.listarUnCliente(id);

    return ResponseEntity.ok(respuesta);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaCliente> actualizarCliente(@RequestBody @Valid DatosActualizarCliente datosActualizarCliente) {

        DatosRespuestaCliente respuesta = clienteService.modificarCliente(datosActualizarCliente);
        return ResponseEntity.ok(respuesta);

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id){

        boolean eliminado = clienteService.eliminarCliente(id);

        if(eliminado){
            return ResponseEntity.ok("Cliente Eliminado");
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PatchMapping("/{id}/desactivar")
    @Transactional
    public ResponseEntity<?> desactivarCliente(@PathVariable Long id){
        boolean desactivado = clienteService.desactivarCliente(id);

        if(desactivado){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PatchMapping("/{id}/activar")
    @Transactional
    public ResponseEntity<?> activarCliente(@PathVariable Long id){
        boolean activado = clienteService.activarCliente(id);

        if(activado){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }


}
