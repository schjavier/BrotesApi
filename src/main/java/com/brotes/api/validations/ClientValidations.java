package com.brotes.api.validations;


import com.brotes.api.exceptions.ClientNotExistException;
import com.brotes.api.exceptions.ClienteActivadoException;
import com.brotes.api.exceptions.ClienteDesactivadoException;
import com.brotes.api.exceptions.ClienteDuplicadoException;
import com.brotes.api.modelo.cliente.Cliente;
import com.brotes.api.modelo.cliente.ClienteRepository;
import org.springframework.stereotype.Component;

@Component
public class ClientValidations {

private final ClienteRepository clienteRepository;

    /**
     * Constructor para inyeccion de dependecias
     * @param clienteRepository la dependencia a inyectar.
     */
    public ClientValidations(ClienteRepository clienteRepository){
    this.clienteRepository = clienteRepository;
}

    /**
     * Metodo que verifica la existencia de un cliente basandose en el nombre y direccion.
     *
     * @param nombre del cliente
     * @param direccion del cliente
     * @throws ClienteDuplicadoException
     */

    public void validarClienteUnico(String nombre, String direccion) throws ClienteDuplicadoException {
        boolean existe = clienteRepository.existsByNombreAndDireccion(nombre, direccion);

        if (existe) {
            throw new ClienteDuplicadoException("El cliente ya se encuentra registrado");
        }
    }

    /**
     * Metodo que verifica que el cliente se encuentra activo
     * @param cliente
     * @throws ClienteDesactivadoException
     */

    public void validarClienteActivo(Cliente cliente) throws ClienteDesactivadoException {
        if (!cliente.isActivo()){
            throw new ClienteDesactivadoException("El cliente ya se encuentra desactivado");
        }
    }

    public void validarClienteDesactivado(Cliente cliente) throws ClienteActivadoException {
        if (cliente.isActivo()){
            throw new ClienteActivadoException("El cliente ya se encuentra activo");
        }
    }

    public void validarExistencia(Long id) throws ClientNotExistException {

        if (!clienteRepository.existsById(id)){
            throw new ClientNotExistException("El cliente no existe");
        }
    }



}
