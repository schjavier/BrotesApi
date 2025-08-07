package com.brotes.api.validations;

import com.brotes.api.exceptions.*;
import com.brotes.api.modelo.categoria.Categoria;
import com.brotes.api.modelo.producto.Producto;
import com.brotes.api.modelo.producto.ProductoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;

@Component
public class ProductValidations {

    private final ProductoRepository productoRepository;


    //Inyeccion de dependecias
    public ProductValidations(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    /**
     * Verifica que el producto sea unico, basandose en el nombre y la categoria que recibe por parametro.
     * Verifica si ya existe el producto.
     *
     * @param nombre del producto
     * @param categoria del producto
     */

    public void productUniqueValidation(String nombre, Categoria categoria) throws DuplicateProductException{
        boolean exist = productoRepository.existsByNombreAndCategoria(nombre, categoria);

        if (exist){
            throw new DuplicateProductException("El producto ya se encuentra registrado");
        }
    }

    public void existValidation (Long id) throws ProductNotExistException {

        if (!productoRepository.existsById(id)){
            throw new ProductNotExistException("El producto no existe");
        }

    }

    public void activeProductValidation(Producto producto){

        if (!producto.isActivo()){
            throw new ProductoDesactivadoException("El producto ya se encuentra desactivado");
        }

    }

    public void inactiveProductValidation(Producto producto){
        if(producto.isActivo()){
            throw new ProductoActivoException("El producto ya se encuentra activado");
        }


    }

    public void existValidationByName(String nombre) {
        if(!productoRepository.existsByNombreContaining(nombre)){
            throw new ProductNotExistException("El producto no Existe");
        }

    }

    public void existValidationByNameAndStatus(String nombre, Boolean status){
        if(!productoRepository.existsByNombreContaining(nombre)){
            throw new ProductNotExistException("El producto no existe");
        }
        if(!productoRepository.existsByNombreContainingAndActivo(nombre, status)){
            switch (status.toString()){
                case "false" -> throw new ProductoActivoException("El producto esta activado");
                case "true" -> throw new ProductoDesactivadoException("El producto se encuentra desactivado");
            }
        }

    }
}
