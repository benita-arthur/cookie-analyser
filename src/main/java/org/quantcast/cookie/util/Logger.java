package org.quantcast.cookie.util;

public final class Logger {
    private Logger() {}

    private static String now() {
        return java.time.LocalDateTime.now().toString();
    }

    public static void warn(String message) {
        System.err.println(now() + "[WARN]  " + message);
    }

    public static void error(String message) {
        System.err.println(now() + "[ERROR] " + message);
    }

    public static void error(String message, Throwable t) {
        System.err.println(now() + "[ERROR] " + message);
        t.printStackTrace(System.err);
    }
}
