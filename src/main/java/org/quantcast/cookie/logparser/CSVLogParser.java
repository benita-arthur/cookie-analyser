package org.quantcast.cookie.logparser;

import org.quantcast.cookie.model.CookieLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class CSVLogParser implements LogParser {

    private static final String CSV_DELIMITER = ",";
    private final AtomicInteger malformedLinesCount = new AtomicInteger(0);

    /**
     * Parses the log file into a Stream of CookieLog objects.
     */
    @Override
    public Stream<CookieLog> parseLogFile(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .skip(1)
                .map(this::parseLine)
                .filter(java.util.Objects::nonNull);
    }

    @Override
    public int getMalformedLinesCount() {
        return this.malformedLinesCount.get();
    }

    private CookieLog parseLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split(CSV_DELIMITER);
        if (parts.length != 2) {
            malformedLinesCount.incrementAndGet();
            return null; // Handle malformed rows missing a timestamp
        }

        String cookieId = parts[0].trim();
        String timestampStr = parts[1].trim();

        try {
            // Parses ISO 8601 strings (e.g., "2018-12-09T14:19:00+00:00")
            // and extracts just the local date component.
            if(cookieId.isBlank()) {
                malformedLinesCount.incrementAndGet();
                return null;
            }
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(timestampStr);
            return new CookieLog(cookieId, zonedDateTime.toLocalDate());
        } catch (DateTimeParseException e) {
            malformedLinesCount.incrementAndGet();
            return null;
        }
    }
}