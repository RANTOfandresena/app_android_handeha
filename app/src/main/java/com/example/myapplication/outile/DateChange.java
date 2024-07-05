package com.example.myapplication.outile;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.Locale;

public class DateChange {
    public static String changerLaDate(String isoDate) {
        // Parser la date en ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDate);

        // Formater la date avec le modèle personnalisé
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'à' HH:mm", Locale.FRENCH)
                .withZone(ZoneId.of("UTC"));

        // Formater et retourner la date
        return formatter.format(zonedDateTime);
    }
}
