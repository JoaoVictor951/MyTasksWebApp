package com.mytasks.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DataUtil {

    // STRICT para recusar datas que não existem, como 31/02/2026.
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private DataUtil() {
    }

    // Retorna null se o texto não for uma data válida.
    public static LocalDate converter(String texto) {
        if (texto == null) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim(), FORMATO);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String formatar(LocalDate data) {
        return data == null ? null : data.format(FORMATO);
    }
}
