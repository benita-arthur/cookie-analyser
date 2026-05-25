# Engineering Self‑Report: Task Execution & Time Log

This document summarises my engineering workflow for designing and implementing the Cookie Analyser CLI tool. It reflects the real decisions, trade‑offs, and engineering practices applied during development.

---

## 5‑Hour Task Breakdown & Worklog Summary

| Phase | Duration | Description |
|-------|----------|-------------|
| **1. Technical Planning & Design** | 0.5 Hours | Defined functional requirements, designed class‑level architecture, selected tech stack (Java 17, Picocli), and planned extensibility via a `LogParser` interface. |
| **2. Implementation & Coding** | 1.5 Hours | Implemented streaming CSV parser, interface‑driven parsing layer, date converter, custom exceptions, structured logging, and the O(n) frequency‑tracking algorithm in `CookieAnalyser`. |
| **3. Testing & Validation** | 1.0 Hour | Wrote unit tests for parser, date converter, and analysis logic. Added edge‑case tests (malformed lines, empty files, ties, invalid timestamps). Ensured deterministic behaviour. |
| **4. Code Review & Refactoring** | 1.0 Hour | Performed self‑review, simplified logic, improved naming, removed unnecessary allocations, and used AI‑assisted suggestions (Gemini) to refine readability and error handling. |
| **5. Documentation & Handoff** | 1.0 Hour | Wrote README, USER guide, TECHNICAL documentation, and this self‑report. Added build/run instructions and clarified stdout vs stderr behaviour. |

---

## Key Engineering Decisions

### **Interface‑Driven Extensibility**
I introduced a `LogParser` interface so future formats (JSON) can be added without modifying the analysis logic.

### **Streaming Parser for Performance**
`CSVLogParser` uses `Files.lines()` to process the file lazily, ensuring low memory usage even for large logs.

### **Robust Error Handling**
Malformed lines increment a counter and are skipped. Invalid dates, missing files, and CLI errors produce safe, user‑friendly messages.

### **O(n) Analysis Algorithm**
`CookieAnalyser` tracks frequencies and maximum counts in a single pass, avoiding sorting or grouping overhead.

### **Testability**
All components are pure, stateless, and independently testable. Tests mirror the production package structure.

---

## Future Improvements
- Add support for multiple log formats via parser factory.
- Add `--json` output mode for automation.
- Add performance benchmarks for large files.
- Add integration tests for CLI behaviour.