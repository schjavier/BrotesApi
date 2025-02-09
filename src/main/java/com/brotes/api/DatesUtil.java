package com.brotes.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;


public class DatesUtil {

    public static int devolverNumeroSemana(LocalDateTime fecha) {

        return fecha.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

    }

    public static int devolverNumeroSemanaActual(){
        LocalDate hoy = LocalDate.now();

        return hoy.get(ChronoField.ALIGNED_WEEK_OF_YEAR);


    }

    public static boolean isSameWeekOfYear(LocalDate fechaUno, LocalDate fechaDos){

        return fechaUno.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == fechaDos.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    public static LocalDate devolverFechaActual(){
        return LocalDate.now();
    }

}
