# Cookie Analyser
Cookie Analyser is a lightweight command‑line tool that reads a CSV cookie log and prints the most active cookie(s) for a given date. It uses a modular architecture with a streaming parser and a simple O(n) analysis algorithm. The project is structured for interview evaluation, code review, and demonstration of clean engineering practices.

## Key Features
**Input:** CSV with cookie,timestamp (ISO‑8601 with timezone)

**Output:** Most active cookie(s), one per line

**Behaviour:**

* Streams file lines for low memory usage

* Skips malformed rows with warnings to stderr

* Prints results to stdout for easy piping

## Documentation
- [TECHNICAL.md](TECHNICAL.md) — build, run, test, architecture

- [USER.md](USER.md) — quick start and usage examples

- [SELF_REPORT.md](SELF_REPORT.md) — design decisions, trade‑offs, and time breakdown