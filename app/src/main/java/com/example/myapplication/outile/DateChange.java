package com.example.myapplication.outile;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

import java.util.Locale;

public class DateChange {
    public static String changerLaDate(String isoDate) {
        // Parser la date en ZonedDateTime
        ZonedDateTime zonedDateTime=null;

        try {
            zonedDateTime = ZonedDateTime.parse(isoDate);
        } catch (DateTimeParseException e) {
            // Si échec, essayer de parser le format 19/07/24 14:19
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm", Locale.FRENCH);
            zonedDateTime = ZonedDateTime.parse(isoDate, inputFormatter.withZone(ZoneId.of("UTC")));
        }

        // Formater la date avec le modèle personnalisé
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'à' HH:mm", Locale.FRENCH)
                .withZone(ZoneId.of("UTC"));

        // Formater et retourner la date
        return formatter.format(zonedDateTime);
    }
}
