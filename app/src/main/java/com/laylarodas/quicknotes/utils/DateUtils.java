package com.laylarodas.quicknotes.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    
    /**
     * Formatea un timestamp a un formato amigable como "Hace 5 minutos"
     * @param timestamp El timestamp en milisegundos
     * @return String formateado de forma amigable
     */
    public static String getTimeAgo(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = days / 365;
        
        if (seconds < 60) {
            return "Ahora mismo";
        } else if (minutes < 60) {
            return minutes == 1 ? "Hace 1 minuto" : "Hace " + minutes + " minutos";
        } else if (hours < 24) {
            return hours == 1 ? "Hace 1 hora" : "Hace " + hours + " horas";
        } else if (days < 30) {
            return days == 1 ? "Hace 1 día" : "Hace " + days + " días";
        } else if (months < 12) {
            return months == 1 ? "Hace 1 mes" : "Hace " + months + " meses";
        } else {
            return years == 1 ? "Hace 1 año" : "Hace " + years + " años";
        }
    }
    
    /**
     * Formatea un timestamp a formato de fecha completa
     * @param timestamp El timestamp en milisegundos
     * @return String con la fecha en formato "dd MMM yyyy, HH:mm"
     */
    public static String getFormattedDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", new Locale("es", "ES"));
        return sdf.format(new Date(timestamp));
    }
}

