package com.brotes.api.productTests;

import com.brotes.api.modelo.categoria.Categoria;
import com.brotes.api.modelo.producto.*;
import com.brotes.api.validations.ProductValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductoRepository productoRepository;

    @Mock
    ProductValidations productValidations;

    @InjectMocks
    ProductoServiceImpl productoService;

    private static final Long ID_PRODUCTO = 1L;
    private static final String NOMBRE_PRODUCTO = "Brocoli";
    private static final Float PRECIO_PRODUCTO = 20F;
    private static final Categoria CATEGORIA_PRODUCTO = Categoria.BROTES;

    private Producto productoActivo;
    private Producto productoInactivo;
    private DatosRegistroProductos datosRegistro;
    private DatosActualizarProducto datosActualizarProducto;
    private DatosActualizarProducto datosActualizarProductoInexistente;

    private UriComponentsBuilder uriComponentsBuilder;

    @BeforeEach
    void setUp(){

    }

}
