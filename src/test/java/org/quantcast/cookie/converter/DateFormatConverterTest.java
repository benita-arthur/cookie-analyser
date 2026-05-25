package org.quantcast.cookie.converter;

import org.junit.jupiter.api.Test;
import org.quantcast.cookie.exception.CookieAnalyserInputException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DateFormatConverterTest {

    private final DateFormatConverter converter = new DateFormatConverter();

    @Test
    void testValidIsoDate() {
        LocalDate result = converter.convert("2026-05-21");
        assertEquals(LocalDate.of(2026, 5, 21), result);
    }

    @Test
    void testInvalidDateFormat() {
        assertThrows(CookieAnalyserInputException.class,
                () -> converter.convert("21-05-2026"));
    }

    @Test
    void testCompletelyInvalidString() {
        assertThrows(CookieAnalyserInputException.class,
                () -> converter.convert("not-a-date"));
    }
}
