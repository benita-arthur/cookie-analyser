package org.quantcast.cookie.logparser;

import org.junit.jupiter.api.Test;
import org.quantcast.cookie.exception.CookieAnalyserInputException;
import org.quantcast.cookie.model.CookieLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CSVLogParserTest {

    @Test
    void testValidCsvParsing() throws IOException {
        Path temp = Files.createTempFile("cookie", ".csv");
        Files.writeString(temp,
                "cookie,timestamp\n" +
                        "A,2018-12-09T10:00:00+00:00\n" +
                        "B,2018-12-09T11:00:00+00:00\n");

        CSVLogParser parser = new CSVLogParser();
        try (Stream<CookieLog> stream = parser.parseLogFile(temp.toString())) {
            List<CookieLog> logs = stream.toList();
            assertEquals(2, logs.size());
            assertEquals("A", logs.get(0).cookie());
            assertEquals(LocalDate.of(2018, 12, 9), logs.get(0).date());
        }
    }

    @Test
    void testMalformedLinesAreCounted() throws IOException {
        Path temp = Files.createTempFile("cookie", ".csv");
        Files.writeString(temp,
                "cookie,timestamp\n" +
                        "A,\n" +                     // missing timestamp
                        ",2018-12-09T10:00:00+00:00\n" + // missing cookie
                        "invalid\n"                 // not enough columns
        );

        CSVLogParser parser = new CSVLogParser();
        try (Stream<CookieLog> stream = parser.parseLogFile(temp.toString())) {
            List<CookieLog> logs = stream.toList();
            assertEquals(0, logs.size());
        }

        assertEquals(3, parser.getMalformedLinesCount());
    }

    @Test
    void testInvalidTimestamp() throws IOException {
        Path temp = Files.createTempFile("cookie", ".csv");
        Files.writeString(temp,
                "cookie,timestamp\n" +
                        "A,not-a-timestamp\n");

        CSVLogParser parser = new CSVLogParser();
        try (Stream<CookieLog> stream = parser.parseLogFile(temp.toString())) {
            assertEquals(0, stream.count());
        }

        assertEquals(1, parser.getMalformedLinesCount());
    }

    @Test
    void testFileNotFoundThrowsException() {
        CSVLogParser parser = new CSVLogParser();
        assertThrows(NoSuchFileException.class,
                () -> parser.parseLogFile("does-not-exist.csv"));
    }
}
