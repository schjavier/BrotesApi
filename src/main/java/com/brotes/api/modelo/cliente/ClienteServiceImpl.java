package com.brotes.api.modelo.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    @Override
    public DatosRespuestaClienteConUrl registrarCliente(DatosRegistroCliente datosRegistroCliente, UriComponentsBuilder uriComponentsBuilder) {

        Cliente cliente = clienteRepository.save(new Cliente(datosRegistroCliente));

        URI url = uriComponentsBuilder.path("/clientes{id}").buildAndExpand(cliente.getId()).toUri();

        return new DatosRespuestaClienteConUrl(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                url.toString());


    }

    @Override
    public Page<DatosListadoClientes> listarClientes(Pageable paginacion) {
        return clienteRepository.findAll(paginacion).map(DatosListadoClientes::new);
    }

    @Override
    public DatosRespuestaCliente listarUnCliente(Long id) {

        Cliente cliente = clienteRepository.getReferenceById(id);

        return new DatosRespuestaCliente(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.getTelefono());

    }

    @Override
    public DatosRespuestaCliente modificarCliente(DatosActualizarCliente datosActualizarCliente) {
        Cliente cliente = clienteRepository.getReferenceById(datosActualizarCliente.id());
        cliente.actualizarDatos(datosActualizarCliente);
        clienteRepository.save(cliente);

        return new DatosRespuestaCliente(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.getTelefono());

    }

    @Override
    public boolean eliminarCliente(Long id) {
        boolean respuesta = false;

        if(clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public boolean desactivarCliente(Long id) {
        boolean respuesta = false;

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        if(clienteOptional.isPresent()){
            Cliente cliente = clienteRepository.getReferenceById(id);
            cliente.desactivar();
            clienteRepository.save(cliente);
            respuesta = true;
        }

        return respuesta;
    }
}
