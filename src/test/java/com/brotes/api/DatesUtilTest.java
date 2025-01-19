package com.brotes.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DatesUtilTest {

    @Test
    void devolverNumeroSemana_debeRetornarNroDeSemana(){
        LocalDateTime fecha = LocalDateTime.of(2025, 1,1, 14, 50);

        int resultado = DatesUtil.devolverNumeroSemana(fecha);

        assertEquals(1, resultado);

    }

    @Test
    void devolverNumeroSemanaActual_debeRetornarNroSemanaActual(){
        LocalDate hoy = LocalDate.now();
        int semanaActual = hoy.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

        int resultado = DatesUtil.devolverNumeroSemanaActual();

        assertEquals(semanaActual, resultado);

    }





}
