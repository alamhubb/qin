# Codex Workflow Rules

These rules capture durable workflow expectations for Codex work on Qin.

## Progress Reporting

- Every user-visible progress update must begin with the current system time
  from the machine when possible.
- The time must be fetched immediately before that update. Do not reuse a
  timestamp from a prior command, prior batch, summary, or chat memory.
- Do not split the protocol into "fetch time now, report later"; the timestamp
  must describe the update being sent.
- Then report the command or action, the result, and the next step.
- This applies to command execution, file edits, log reads, validation, service
  start/stop, status updates, and resumed goal turns.
- Do not rely on chat memory for this rule; keep it in project docs and relevant
  skills.

## Git Hygiene

- Treat every coherent code, test, documentation, config, or skill update as a
  commit unit.
- After the unit is implemented and practical validation has run, inspect the
  diff, stage only the intended files, commit, and push when the configured
  remote is usable.
- Do not move on to unrelated work while a completed coherent unit remains
  uncommitted.
- Keep unrelated dirty files out of the commit. Assume they are user work unless
  explicitly told otherwise.
- If validation is blocked or a push fails, keep the local commit when safe and
  report the exact blocker.

## Cache And Instant Compilation

- If Qin cache behavior, module-class disk cache, dependency classpath refresh,
  generated parser materialization, hot rebuild, or instant compilation is
  stale or incorrect, treat it as a toolchain defect.
- Fix the owning cache/compiler/build layer directly and add a focused smoke
  test when practical.
- Manual cache deletion, forced full rebuilds, larger timeouts, or source
  rewrites may be used only as diagnostics. They are not acceptable final
  fixes for stale cache or instant compilation defects.
