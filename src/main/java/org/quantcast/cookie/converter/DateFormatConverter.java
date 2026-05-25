package org.quantcast.cookie.converter;

import org.quantcast.cookie.exception.CookieAnalyserInputException;
import picocli.CommandLine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatConverter implements CommandLine.ITypeConverter<LocalDate> {

    private static final DateTimeFormatter[] SUPPORTED_FORMATS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_DATE                     // 2026-05-21
    };

    @Override
    public LocalDate convert(String value) throws CookieAnalyserInputException {
        for (DateTimeFormatter dateformat : SUPPORTED_FORMATS) {
            try {
                return LocalDate.parse(value, dateformat);
            } catch (DateTimeParseException ignored) {}
        }
        throw new CookieAnalyserInputException("Invalid date argument : " + value);
    }
}
