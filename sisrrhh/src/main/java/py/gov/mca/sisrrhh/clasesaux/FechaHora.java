/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.clasesaux;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author vinsfran
 */
public class FechaHora {

    static long milisegundos_dia = 24 * 60 * 60 * 1000;
    private final SimpleDateFormat dateFormatFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private long horas;
    private long minutos;
    private long segundos;

    public void calcularHorasMinutosMismoDia(String anio, String mes, String dia, String horaInicio, String horaFin) {
        /**
         * llamamos el metodo StringToDate para convertir la cadena en un objeto
         * de la clase date este metodo recive la fecha en cualquier formato
         */

        Date fechaInicial = FechaHora.StringToDate(anio + "/" + mes + "/" + dia + " " + horaInicio + ":00", "/", 0);//yyyy-MM-dd
        Date fechaFinal = FechaHora.StringToDate(anio + "/" + mes + "/" + dia + " " + horaFin + ":00", "/", 0);
        /**
         * Creamos una instancia de la clase calendar
         */
        Calendar calFechaInicial = Calendar.getInstance();
        Calendar calFechaFinal = Calendar.getInstance();

        /**
         * Le pasamos el objeto Date al metodo set time
         */
        calFechaInicial.setTime(fechaInicial);
        calFechaFinal.setTime(fechaFinal);
        //Numero total de minutos que hay entre las dos Fechas
//        System.out.println("Numero Total de Horas"
//                + " Entre las dos Fechas: "
//                + cantidadTotalHoras(calFechaInicial, calFechaFinal));
        //Numero total de horas que hay entre las dos Fechas
//        System.out.println("Numero Total de Minutos"
//                + " Entre las dos Fechas: "
//                + cantidadTotalMinutos(calFechaInicial, calFechaFinal));
        //Numero total de segundos que hay entre las dos Fechas
//        System.out.println("Numero Total de segundos"
//                + " Entre las dos Fechas: "
//                + cantidadTotalSegundos(calFechaInicial, calFechaFinal));
        /**
         * Llamamos el metodo diferenciaHorasDias y diferenciaHoras y retamos
         * para que nos de el total de horas
         */
        setHoras(diferenciaHorasDias(calFechaInicial, calFechaFinal) + diferenciaHoras(calFechaInicial, calFechaFinal));
        //setMinutos(diferenciaMinutos(calFechaInicial, calFechaFinal));

        setMinutos(cantidadTotalMinutos(calFechaInicial, calFechaFinal));

        /**
         * si los minutos son iguales menores a cero hay que restarle 1 hora al
         * total que dio las horas
         */
//        if (getMinutos() < 0) {
//            //System.out.println("Horas: " + (getHoras() - 1) + " Minutos: " + (getMinutos() + 60));
//            setHoras(getHoras() - 1);
//            setMinutos(getMinutos() + 60);
//        }
    }

    /*Metodo que calcula la diferencia de las horas que han pasado entre dos fechas en java
     */
    public static long diferenciaHorasDias(Calendar fechaInicial, Calendar fechaFinal) {
        //Milisegundos al día
        long diferenciaHoras = 0;
        //Restamos a la fecha final la fecha inicial y lo dividimos entre el numero de milisegundos al dia
        diferenciaHoras = (fechaFinal.getTimeInMillis() - fechaInicial.getTimeInMillis()) / milisegundos_dia;
        if (diferenciaHoras > 0) {
            // Lo Multiplicamos por 24 por que estamos utilizando el formato militar
            diferenciaHoras *= 24;
        }
        return diferenciaHoras;
    }

    /*Metodo que calcula la diferencia de las horas entre dos fechas*/
    public static long diferenciaHoras(Calendar fechaInicial, Calendar fechaFinal) {
        long diferenciaHoras = 0;
        diferenciaHoras = (fechaFinal.get(Calendar.HOUR_OF_DAY) - fechaInicial.get(Calendar.HOUR_OF_DAY));
        return diferenciaHoras;
    }

    /*Metodo que calcula la diferencia de los minutos entre dos fechas
     */
    public static long diferenciaMinutos(Calendar fechaInicial, Calendar fechaFinal) {

        long diferenciaHoras = 0;
        diferenciaHoras = (fechaFinal.get(Calendar.MINUTE) - fechaInicial.get(Calendar.MINUTE));
        return diferenciaHoras;
    }

    /*Metodo que devuelve el Numero total de minutos que hay entre las dos Fechas */
    public static long cantidadTotalMinutos(Calendar fechaInicial, Calendar fechaFinal) {

        long totalMinutos = 0;
        totalMinutos = ((fechaFinal.getTimeInMillis() - fechaInicial.getTimeInMillis()) / 1000 / 60);
        return totalMinutos;
    }

    /*Metodo que devuelve el Numero total de horas que hay entre las dos Fechas */
    public static long cantidadTotalHoras(Calendar fechaInicial, Calendar fechaFinal) {

        long totalMinutos = 0;
        totalMinutos = ((fechaFinal.getTimeInMillis() - fechaInicial.getTimeInMillis()) / 1000 / 60 / 60);
        return totalMinutos;
    }

    /*Metodo que devuelve el Numero total de Segundos que hay entre las dos Fechas */
    public static long cantidadTotalSegundos(Calendar fechaInicial, Calendar fechaFinal) {

        long totalMinutos = 0;
        totalMinutos = ((fechaFinal.getTimeInMillis() - fechaInicial.getTimeInMillis()) / 1000);
        return totalMinutos;
    }

    /* Convierte una fecha en date a string*/
    public static String DateToString(Date fecha, String caracter, int op) {
        String formatoHora = " HH:mm:ss";//Formato de hora
        //caracter hace referencia al separador / -
        String formato = "yyyy" + caracter + "MM" + caracter + "dd" + formatoHora;
        if (op == 1) //
        {
            formato = "yyyy" + caracter + "dd" + caracter + "MM" + formatoHora;
        } else if (op == 2) {
            formato = "MM" + caracter + "yyyy" + caracter + "dd" + formatoHora;
        } else if (op == 3) {
            formato = "MM" + caracter + "dd" + caracter + "yyyy" + formatoHora;
        } else if (op == 4) {
            formato = "dd" + caracter + "yyyy" + caracter + "MM" + formatoHora;
        } else if (op == 5) {
            formato = "dd" + caracter + "MM" + caracter + "yyyy" + formatoHora;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());
        String fechaFormato = null;

        sdf.setLenient(false);
        fechaFormato = sdf.format(fecha);

        return fechaFormato;
    }

    /*Convertir fecha date en string*/
    public static Date StringToDate(String fecha, String caracter, int op) {
        String formatoHora = " HH:mm:ss";
        String formato = "yyyy" + caracter + "MM" + caracter + "dd" + formatoHora;
        if (op == 1) //
        {
            formato = "yyyy" + caracter + "dd" + caracter + "MM" + formatoHora;
        } else if (op == 2) {
            formato = "MM" + caracter + "yyyy" + caracter + "dd" + formatoHora;
        } else if (op == 3) {
            formato = "MM" + caracter + "dd" + caracter + "yyyy" + formatoHora;
        } else if (op == 4) {
            formato = "dd" + caracter + "yyyy" + caracter + "MM" + formatoHora;
        } else if (op == 5) {
            formato = "dd" + caracter + "MM" + caracter + "yyyy" + formatoHora;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());
        Date fechaFormato = null;
        try {

            sdf.setLenient(false);
            fechaFormato = sdf.parse(fecha);
        } catch (ParseException ex) {

        }
        return fechaFormato;
    }

    /**
     * @return the horas
     */
    public long getHoras() {
        return horas;
    }

    /**
     * @param horas the horas to set
     */
    public void setHoras(long horas) {
        this.horas = horas;
    }

    /**
     * @return the minutos
     */
    public long getMinutos() {
        return minutos;
    }

    /**
     * @param minutos the minutos to set
     */
    public void setMinutos(long minutos) {
        this.minutos = minutos;
    }

    /**
     * @return the segundos
     */
    public long getSegundos() {
        return segundos;
    }

    /**
     * @param segundos the segundos to set
     */
    public void setSegundos(long segundos) {
        this.segundos = segundos;
    }
}
