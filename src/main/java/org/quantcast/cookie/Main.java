package org.quantcast.cookie;

import org.quantcast.cookie.converter.DateFormatConverter;
import org.quantcast.cookie.exception.CookieAnalyserInputException;
import org.quantcast.cookie.logparser.LogParser;
import org.quantcast.cookie.model.CookieLog;
import org.quantcast.cookie.service.CookieAnalyser;
import org.quantcast.cookie.logparser.CSVLogParser;
import org.quantcast.cookie.util.Logger;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;


public class Main implements Callable<Integer> {

    @CommandLine.Option(names = {"-f"}, required = true, description = "Path to the cookie log file.")
    private File logFile;

    @CommandLine.Option(names = {"-d"}, required = true, description = "Target analysis date in YYYY-MM-DD format.", converter = DateFormatConverter.class)
    private LocalDate targetDate;

    public static void main(String[] args) {
        CommandLine command = new CommandLine(new Main());

        command.setExecutionExceptionHandler((ex, commandLine, parseResult) -> {
            if (ex instanceof CookieAnalyserInputException) {
                Logger.warn(ex.getMessage());
                return 1;
            }
            // fallback for other exceptions
            System.err.println("Error: " + ex.getMessage());
            return 1;
        });
        int exitcode = command.execute(args);
        System.exit(exitcode);
    }

    @Override
    public Integer call() throws Exception {
        LogParser parser = new CSVLogParser();
        CookieAnalyser service = new CookieAnalyser();
        try (Stream<CookieLog> cookieStream = parser.parseLogFile(logFile.getAbsolutePath())) {

            List<String> activeCookies = service.findMostActiveCookies(cookieStream, targetDate);

            if(parser.getMalformedLinesCount() > 0) {
                Logger.warn("Warning: " + parser.getMalformedLinesCount() + " malformed lines were skipped during log parsing.");
            }
            if (activeCookies.isEmpty()) {
                System.out.println("No active cookies found for the specified date: " + targetDate);
            } else {
                // Print each top cookie on a new line as specified by standard CLI conventions
                activeCookies.forEach(System.out::println);
            }

        } catch (CookieAnalyserInputException | IOException logException) {
            Logger.warn(logException.getMessage());
            return 1;
        } catch (RuntimeException e) {
            Logger.error("System processing error" + e.getMessage(), e);
            return 1;
        }
        return 0;
    }
}