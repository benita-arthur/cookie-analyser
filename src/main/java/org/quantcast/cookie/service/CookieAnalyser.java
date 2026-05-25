package org.quantcast.cookie.service;

import org.quantcast.cookie.model.CookieLog;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CookieAnalyser {

    /**
     * Finds the most active cookie(s) for a given date.
     * Takes a Stream to preserve lazy, low-memory evaluation up to this point.
     */
    public List<String> findMostActiveCookies(Stream<CookieLog> cookieStream, LocalDate targetDate) {

        if (cookieStream == null) {
            return Collections.emptyList();
        }

        // Accumulators to hold the running frequencies and the highest count seen so far
        final Map<String, Long> runningCounts = new HashMap<>();
        final List<String> maxCookies = new ArrayList<>();
        // Use an array wrapper to allow modification inside the stream's forEach lambda
        final long[] currentMaxCount = {0L};

        // Stream remains completely lazy up until this point.
        // We filter out unwanted dates IMMEDIATELY to prevent downstream processing overhead.
        cookieStream
                .filter(log -> log.date().equals(targetDate))
                .forEach(log -> {
                    String cookie = log.cookie();

                    // 1. Compute and update the running frequency for this cookie
                    long newCount = runningCounts.merge(cookie, 1L, Long::sum);

                    // 2. Track the maximum frequency on-the-fly
                    if (newCount > currentMaxCount[0]) {
                        // We found a new absolute maximum; wipe older ties and update the threshold
                        currentMaxCount[0] = newCount;
                        maxCookies.clear();
                        maxCookies.add(cookie);
                    } else if (newCount == currentMaxCount[0]) {
                        // It matches our current highest frequency, register it as a tie
                        maxCookies.add(cookie);
                    }
                });

        return maxCookies;
    }
}
