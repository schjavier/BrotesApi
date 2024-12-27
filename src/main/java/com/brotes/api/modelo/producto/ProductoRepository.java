package com.brotes.api.modelo.producto;

import com.brotes.api.modelo.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existByNombreAndCategoria(String nombre, Categoria categoria);
}
