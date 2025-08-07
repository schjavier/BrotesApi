package com.brotes.api.PedidoTests;

import com.brotes.api.modelo.pedidos.PedidoRepository;
import com.brotes.api.validations.PedidoValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PedidoValidationsTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoValidations validations;

    private static final String DIA_ENTREGA_VALIDO = "MARTES";
    private static final String DIA_ENTREGA_VALIDO_2 = "VIERNES";
    private static final String DIA_ENTREGA_INVALIDO = "LUNES";
    private static final String DIA_ENTREGA_INVALIDO_OTRO = "MIERCOLES";


    @Test
    public void isDiaDeEntregaValid_shouldDoNothing_whenDiaIsViernes(){

        assertDoesNotThrow(() -> validations.isDiaDeEntregaValid(DIA_ENTREGA_VALIDO_2));

    }

    @Test
    public void isDiaDeEntregaValid_shouldDoNothing_whenDiaIsMartes(){

        assertDoesNotThrow(() -> validations.isDiaDeEntregaValid(DIA_ENTREGA_VALIDO));

    }

    @Test
    public void isDiaDeEntregaValid_shouldThrownAnException_whenDiaIsLunes(){
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validations.isDiaDeEntregaValid(DIA_ENTREGA_INVALIDO));
    }

    @Test
    public void isDiaDeEntregaValid_shouldThrownAnException_whenDiaIsInvalid(){
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validations.isDiaDeEntregaValid(DIA_ENTREGA_INVALIDO_OTRO));
    }



}

