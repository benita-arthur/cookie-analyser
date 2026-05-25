package org.quantcast.cookie.logparser;

import org.quantcast.cookie.model.CookieLog;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface LogParser {
    public Stream<CookieLog> parseLogFile(String filepath) throws IOException;
    public int getMalformedLinesCount();
}
