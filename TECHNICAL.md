# Technical README — Build, Run, Test, Architecture

This project is a CLI tool that analyses a log file and prints the most active cookie(s) for a given date. It is designed with extensibility, performance, and clean separation of concerns in mind.

**1. Architecture Overview**
   The project follows a modular structure with clear responsibilities:

* Main (CLI Layer)  
Handles argument parsing using Picocli and delegates work to the service layer.

* LogParser Interface  
Defines the minimal contract required to support different log formats in the future.

* CSVLogParser (Current Implementation)  
A streaming CSV parser that processes the file line‑by‑line to avoid high memory usage.

* CookieAnalyser (Service Layer)  
Core logic to compute the most active cookie(s) for a given date.

This separation ensures testability, maintainability, and future extensibility.

**2. Extensibility**
   The LogParser interface allows the system to support additional formats (TSV, JSON, NDJSON, etc.) without modifying existing logic.

To add a new format: Implement LogParser. Register the implementation in the CLI or via a factory. Reuse CookieAnalyser without changes.

**3. Error Handling & Logging**
   Structured logging with configurable log levels. Malformed lines are skipped with a warning to stderr. Invalid file paths, unreadable files, or invalid dates produce clear error messages and safe exits. The program prints only the final result to stdout to support piping and automation.

**4. Performance & Scalability**
   CSV parsing is streamed, not loaded into memory. Processing is O(n) with a single pass through the file. Uses efficient data structures for counting and comparison. Suitable for large log files.

**5. Testing Strategy**
*    Unit tests cover: Core analysis logic. Edge cases (no matches, multiple matches, malformed lines). CSV parsing behaviour. Error scenarios

* Tests are written using JUnit and follow a clear arrange‑act‑assert structure.

**6. Build & Run**
   Build the JAR
```powershell
   ./gradlew clean build
```
   Run the CLI
```powershell
   java -jar build/libs/cookie-analyser-1.0-SNAPSHOT.jar -f cookielog.csv -d 2023-12-09
```
**7. Running Tests**
```powershell
   ./gradlew test
```
**8. Developer Notes (Optional)**
   Useful for debugging or capturing output:
```powershell
java -jar build/libs/cookie-analyser-1.0-SNAPSHOT.jar -f cookielog.csv -d 2018-12-09 > out.txt 2> err.txt
```


# Technical README — Build, Run, Test, Architecture

This project is a CLI tool that analyses a CSV cookie log and prints the most active cookie(s) for a given date. It is designed with extensibility, performance, and clean separation of concerns in mind.

---

## 1. Architecture Overview

The codebase follows a modular structure:

- **Main (CLI Layer)**  
  Uses Picocli for argument parsing, delegates work to parser + service layers, and handles user‑facing errors.

- **DateFormatConverter**  
  Converts CLI date strings into `LocalDate` using strict ISO‑8601 parsing.

- **LogParser Interface**  
  Defines the contract for parsing log files into a stream of `CookieLog` records.

- **CSVLogParser (Current Implementation)**
    - Streams file lines using `Files.lines()`
    - Skips malformed rows
    - Parses ISO‑8601 timestamps into `LocalDate`
    - Tracks malformed line count

- **CookieAnalyser (Service Layer)**  
  Implements an O(n) algorithm to compute the most active cookie(s) for a given date.

- **CookieLog (Model)**  
  A compact Java `record` representing a parsed log entry.

- **Logger (Utility)**  
  Minimal stderr logger with timestamps and severity levels.

This separation ensures testability, maintainability, and future extensibility.

---

## 2. Extensibility

The `LogParser` interface allows the system to support additional formats (JSON, etc.) without modifying the analysis logic.

To add a new format:
1. Implement `LogParser`.
2. Register the implementation in the CLI or a parser factory.
3. Reuse `CookieAnalyser` unchanged.

---

## 3. Error Handling & Logging

- Malformed lines increment a counter and are skipped.
- Invalid dates or missing files produce clear CLI errors.
- Warnings and diagnostics go to **stderr**.
- Final results print to **stdout** for easy piping.
- Unexpected exceptions are logged with stack traces.

---

## 4. Performance & Scalability

- CSV parsing is **streamed**, not loaded into memory.
- Processing is **O(n)** with a single pass.
- Efficient data structures (`HashMap`, primitive counters).
- Suitable for large log files.

---

## 5. Testing Strategy

Unit tests cover:
- CSV parsing
- Date conversion
- Analysis logic
- Edge cases (ties, empty files, malformed lines)
- Error scenarios

Tests mirror the production package structure for clarity.

---

## 6. Build & Run

### Build the JAR
```powershell
   ./gradlew clean build
```
Run the CLI
```powershell
   java -jar build/libs/cookie-analyser-1.0-SNAPSHOT.jar -f cookielog.csv -d 2023-12-09
```

## 7. Running Tests
```powershell
   ./gradlew test
```
## 8. Developer Notes (Optional)
Useful for debugging or capturing output:
```powershell
java -jar build/libs/cookie-analyser-1.0-SNAPSHOT.jar -f cookielog.csv -d 2018-12-09 > out.txt 2> err.txt
```