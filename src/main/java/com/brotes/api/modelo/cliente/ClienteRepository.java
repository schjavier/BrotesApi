package com.brotes.api.modelo.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByNombre(String nombre);
    boolean existsByNombreContaining(String nombre);
    boolean existsByNombreAndDireccion(String nombre, String direccion);

    List<Cliente> findByNombreContaining(String nombre);
}