package com.brotes.api.modelo.cliente;

import com.brotes.api.validations.ClientValidations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;
    private final ClientValidations clientValidations;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClientValidations clientValidations){
        this.clienteRepository = clienteRepository;
        this.clientValidations = clientValidations;
    }

    @Override
    public DatosRespuestaClienteConUrl registrarCliente(DatosRegistroCliente datosRegistroCliente, UriComponentsBuilder uriComponentsBuilder) {

        clientValidations.validarClienteUnico(datosRegistroCliente.nombre(), datosRegistroCliente.direccion());

        Cliente cliente = clienteRepository.save(new Cliente(datosRegistroCliente));

        URI url = uriComponentsBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();

        return new DatosRespuestaClienteConUrl(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.isActivo(),
                url.toString());

    }

    @Override
    public Page<DatosListadoClientes> listarClientes(Pageable paginacion) {
        return clienteRepository.findAll(paginacion).map(DatosListadoClientes::new);
    }

    @Override
    public DatosRespuestaCliente listarUnCliente(Long id) {

        clientValidations.validarExistencia(id);

        Cliente cliente = clienteRepository.getReferenceById(id);

        return new DatosRespuestaCliente(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.isActivo(),
                cliente.getTelefono());

    }

    @Override
    public List<DatosRespuestaCliente> mostrarClientesPorNombre(String nombre) {
        clientValidations.validarExistenciaByNombre(nombre);

        List<Cliente> clienteList = clienteRepository.findByNombreContaining(nombre);

        return clienteList.stream().map(cliente -> new DatosRespuestaCliente(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.isActivo(),
                cliente.getTelefono()
        )).collect(Collectors.toList());
    }

    @Override
    public DatosRespuestaCliente modificarCliente(DatosActualizarCliente datosActualizarCliente) {

        clientValidations.validarExistencia(datosActualizarCliente.id());

        Cliente cliente = clienteRepository.getReferenceById(datosActualizarCliente.id());
        cliente.actualizarDatos(datosActualizarCliente);

        clienteRepository.save(cliente);

        return new DatosRespuestaCliente(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.isActivo(),
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
            clientValidations.validarClienteActivo(cliente);
            cliente.desactivar();
            clienteRepository.save(cliente);
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public boolean activarCliente(Long id) {
        boolean respuesta = false;

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        if(clienteOptional.isPresent()){
            Cliente cliente = clienteRepository.getReferenceById(id);
            clientValidations.validarClienteDesactivado(cliente);
            cliente.activar();
            clienteRepository.save(cliente);
            respuesta = true;
        }

        return respuesta;
    }

}
