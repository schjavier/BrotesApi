package com.brotes.api.controller;

import com.brotes.api.modelo.cliente.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaCliente> registrarCliente(@RequestBody DatosRegistroCliente datosRegistroCliente,
                                                                  UriComponentsBuilder uriComponentsBuilder){
    Cliente cliente = clienteRepository.save(new Cliente(datosRegistroCliente));
    DatosRespuestaCliente datosRespuestaCliente = new DatosRespuestaCliente(cliente.getId(), cliente.getNombre(), cliente.getDireccion(), cliente.getTelefono());

        URI url = uriComponentsBuilder.path("/clientes{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaCliente);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoClientes>> mostrarClientes(@PageableDefault(size = 5) Pageable paginacion){
        return ResponseEntity.ok(clienteRepository.findAll(paginacion).map(DatosListadoClientes::new));
    }

    @PutMapping
    public ResponseEntity<DatosRespuestaCliente> actualizarCliente(@RequestBody @Valid DatosActualizarCliente datosActualizarCliente) {
        Cliente cliente = clienteRepository.getReferenceById(datosActualizarCliente.id());
        cliente.actualizarDatos(datosActualizarCliente);
        clienteRepository.save(cliente);
        return ResponseEntity.ok(
                new DatosRespuestaCliente(
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getDireccion(),
                        cliente.getTelefono()));

    }


}
