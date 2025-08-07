package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByNombreAndCategoria(String nombre, Categoria categoria);
    boolean existsByNombreContaining(String nombre);
    List<Producto> findByNombreContaining(String nombre);
    boolean existsByNombreContainingAndActivo(String nombre, Boolean activo);
    List<Producto> findByNombreContainingAndActivo(String nombre, Boolean activo);
}
