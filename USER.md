# User Guide

This short guide helps end users run the tool and interpret its output.

What the tool does
- Reads a CSV cookie log with columns `cookie,timestamp`.
- For a requested date (YYYY-MM-DD) it prints the cookie(s) that appeared most frequently on that date.

Quick run (PowerShell)

The repository includes a built "fat" jar at `build/libs/cookie-analyser-1.0-SNAPSHOT.jar` and an example input file `cookielog.csv` in the project root. Run the jar with the -f (file) and -d (date) options. Example commands to check the sample dates from the table below:

```powershell
# Run the program for each sample date (copy-paste the one you want to test)
java -jar .\build\libs\cookie-analyser-1.0-SNAPSHOT.jar -f "cookielog.csv" -d 2023-12-10
java -jar .\build\libs\cookie-analyser-1.0-SNAPSHOT.jar -f "cookielog.csv" -d 2023-12-09
java -jar .\build\libs\cookie-analyser-1.0-SNAPSHOT.jar -f "cookielog.csv" -d 2023-12-08
java -jar .\build\libs\cookie-analyser-1.0-SNAPSHOT.jar -f "cookielog.csv" -d 2023-12-07
java -jar .\build\libs\cookie-analyser-1.0-SNAPSHOT.jar -f "cookielog.csv" -d 2023-12-05
java -jar .\build\libs\cookie-analyser-1.0-SNAPSHOT.jar -f "cookielog.csv" -d 2023-12-01
java -jar .\build\libs\cookie-analyser-1.0-SNAPSHOT.jar -f "cookielog.csv" -d 2025-01-01
java -jar .\build\libs\cookie-analyser-1.0-SNAPSHOT.jar -f "cookielog.csv" -d 2023-11-30
```

The program prints the most frequent cookie(s) for the requested date to stdout. Any malformed lines are reported to stderr.

Input CSV format
- Header row is required: `cookie,timestamp`.
- Timestamp must be ISO 8601 with timezone (e.g. `2018-12-09T14:19:00+00:00`).
- Lines missing fields or with invalid timestamps are skipped and reported to stderr.

| Date       | Expected Output         | Notes                   |
|------------|-------------------------|-------------------------|
| 2023‑12‑10 | TIECOOKIE01<br>TIECOOKIE02 | Tie cookies (printed on separate lines) |
| 2023‑12‑09 | X9Y8Z7W6V5              | Max count = 3           |
| 2023‑12‑08 | SINGLECOOKIE            | Single entry            |
| 2023‑12‑07 | A1B2C3D4E5              | Malformed lines skipped |
| 2023‑12‑05 | *(empty)*               | No cookies              |
| 2023‑12‑01 | Z1Z1Z1Z1Z1              | Single entry            |
| 2025‑01‑01 | FUTURECOOKIE            | Future date             |
| 2023‑11‑30 | EARLYCOOKIE             | Oldest date             |
