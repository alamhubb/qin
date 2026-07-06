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
- When a conversation teaches a durable rule, preference, architectural
  decision, workflow reminder, or engineering standard, update both the local
  project documentation and the relevant global/project skill documentation in
  the same turn. Treat this as a required knowledge-capture step, not an
  optional summary.
- If the durable rule applies beyond one repository, update the global skill
  first, then mirror the stricter project-specific wording into Qin docs and
  relevant Qin skills.

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
- Qin engineering should follow the target-aware instant compilation model in
  `docs/spec/qin-engineering-instant-compilation.md`: stable cache identities,
  layered caches, content-aware package stamps, lightweight dependency
  fingerprints, hot in-process state, and no incidental temp-path invalidation.
- When evaluating whether Qin's engineering direction is "advanced" or
  "optimal", use the same standard: correctness, speed, and simplicity must be
  achieved together. A cache or dev-server path is not acceptable if it is fast
  but hides stale output, crosses target zones, depends on temp paths, or
  weakens Qin's `.class`/dual-target language boundaries.
- Treat modern systems such as Bazel, Gradle/Kotlin, Vite, and esbuild as
  references for hermetic inputs, incremental graphs, warm compiler state, and
  fast feedback loops. Do not copy their semantics when those semantics would
  make Qin a generic JS bundler or weaken `shared/` portability.
- Also learn from Buck2/Bazel remote execution style explicit action graphs,
  Turborepo-style declared task inputs/outputs, and Deno/Bun-style cohesive
  developer tooling where they help Qin stay fast and simple. These references
  are acceptable only when they preserve Qin's target-aware `.class`, JS, and
  `shared/` boundaries.
## Knowledge Capture During Qin Work

- User-stated durable engineering rules, corrections, and preferences are implementation requirements, not chat-only context. Capture them in the relevant global skill, Qin project skill, and Qin repo docs during the same work turn.
- For Qin/OVS/CSSTS parser, compiler, runtime, and generated-code debugging, standard ESM syntax is the canonical frontend module target. If generated code is not valid browser-parseable ESM, fix the owning compiler/runtime layer instead of changing business files around it.
- The legacy handwritten TypeScript `slime-parser` may be used as a reference oracle when diagnosing generated parser or CST-to-AST regressions. Compare grammar, CST shape, variable/export handling, call arguments, and object literal conversion against it, then apply the fix to the active generated Java/TypeScript parser or Qin runtime path.
- Do not add generated-empty fallback or dual parser/AST extraction logic. If the active generated parser, CST-to-AST, normalizer, OVS compiler, or emitter returns an empty/malformed result, that active path is the bug. The old handwritten TypeScript parser is only an oracle for comparison.
- Do not introduce compatibility behavior, compatibility syntax, alternate accepted forms, or "both are valid" docs by default. Qin work has one correct standard path; non-standard input should fail clearly. If compatibility is truly unavoidable for an external boundary or migration, first report why the owning standard path cannot solve it and ask the user to confirm before implementing or documenting the exception.
- OVS canonical props syntax is declaration-list style inside `tag(...)`, for example `div(class = "a", style = "color:red", onClick() { console.log(123) }) { div { 123 } }`. Entries may be comma-separated and include `name = expression`, boolean shorthand, and method-like handlers. JS object props such as `div({ class: "a" }) { ... }` are wrong OVS source syntax, not a supported alternate form and not a workaround for OVS parser/compiler defects.
- Before creating another Qin doc, search the existing docs and skills and update the canonical file when possible. If docs are redundant or oversized, propose a consolidation plan first; do not delete or move canonical docs without explicit confirmation.
