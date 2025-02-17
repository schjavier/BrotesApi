package com.brotes.api.modelo.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndDireccion(String nombre, String direccion);
}
