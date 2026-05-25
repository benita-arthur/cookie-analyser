package org.quantcast.cookie.service;

import org.junit.jupiter.api.Test;
import org.quantcast.cookie.exception.CookieAnalyserInputException;
import org.quantcast.cookie.model.CookieLog;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CookieAnalyserTest {

    private final CookieAnalyser analyser = new CookieAnalyser();

    @Test
    void testMostActiveCookieSingleWinner() {
        Stream<CookieLog> stream = Stream.of(
                new CookieLog("A", LocalDate.of(2020, 1, 1)),
                new CookieLog("A", LocalDate.of(2020, 1, 1)),
                new CookieLog("B", LocalDate.of(2020, 1, 1))
        );

        List<String> result = analyser.findMostActiveCookies(stream, LocalDate.of(2020, 1, 1));
        assertEquals(List.of("A"), result);
    }

    @Test
    void testMostActiveCookieTie() {
        Stream<CookieLog> stream = Stream.of(
                new CookieLog("A", LocalDate.of(2020, 1, 1)),
                new CookieLog("B", LocalDate.of(2020, 1, 1))
        );

        List<String> result = analyser.findMostActiveCookies(stream, LocalDate.of(2020, 1, 1));
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
        assertEquals(2, result.size());
    }

    @Test
    void testNoCookiesForDate() {
        Stream<CookieLog> stream = Stream.of(
                new CookieLog("A", LocalDate.of(2020, 1, 2))
        );

        List<String> result = analyser.findMostActiveCookies(stream, LocalDate.of(2020, 1, 1));
        assertTrue(result.isEmpty());
    }

}
