# qin-conformance

Chrome-strict ESM conformance subsystem for Qin.

## Goals

- Freeze a Chrome baseline and compare Qin behavior against it.
- Run deterministic case suites and output machine-readable reports.
- Keep exclusions explicit via `allowed-exclusions.json`.

## Files

- `conformance-baseline.json` baseline metadata and case list
- `allowed-exclusions.json` approved mismatches

## Run

From any Qin project with this package on classpath:

```bash
qin conformance
```

Report is written to `.qin/conformance/report-*.json`.
