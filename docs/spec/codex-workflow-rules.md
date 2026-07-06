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
- For Qin fullstack startup or rebuild latency, use the opt-in profile mode
  first: `--profile`, `-Dqin.profile=true`, or `QIN_PROFILE=1`. Read the
  phase timings to identify the smallest slow boundary, then reproduce that
  boundary with a focused smoke/probe before widening to package and app
  validation.
## Knowledge Capture During Qin Work

- User-stated durable engineering rules, corrections, and preferences are implementation requirements, not chat-only context. Capture them in the relevant global skill, Qin project skill, and Qin repo docs during the same work turn.
- Normalize durable rules to one canonical source. Keep full wording in the owning global skill, project skill reference, or project spec; other files should link or briefly point to that source instead of copying the same rule.
- For Qin parser, syntax, compiler, runtime, generated-code, OVS/CSSTS, UI render, or dev-server failures, debug from the smallest reproducible unit outward. If one syntax form is suspect, first run a single-file focused test/probe for that exact syntax, then expand to the owning package/plugin smoke, and only then to the user-facing app/browser/server flow. This reduces compile cost without redefining final success; the original failing boundary remains the acceptance test.
- For parser/compiler syntax failures, use a fixed five-stage pipeline probe before rerunning full apps: token stream, CST shape, AST shape, emitted ESM, then integration/runtime behavior. Bisect by layer: token/CST failure belongs to parser grammar or lexer; CST-correct/AST-wrong belongs to CST-to-AST; AST-correct/emit-wrong belongs to generator; emitted ESM-correct/runtime-wrong belongs to runtime/plugin/app integration.
- Pipeline probes should reuse the active artifacts produced by earlier stages. If the probe already has tokens, CST, and AST for the same source, the emitted ESM stage should generate from those artifacts instead of invoking a second full transform that repeats parser and CST-to-AST work, unless the explicit goal is to compare that full transform boundary.
- For OVS, the canonical reusable diagnostic entry is `packages/qin-runtime-core/src/java/com/qin/runtime/core/QinOvsParserPipelineProbeMain.java`. Run it on the smallest `.ovs` source first, then run `QinOvsParserPipelineProbeSmokeTestMain`, then the owning OVS compiler smoke, and only then the browser/dev-server boundary.
- Parser diagnostics are not compatibility behavior and not fallback logic. They must exercise the active standard parser/compiler path and expose the failing layer directly.
- For Qin/OVS/CSSTS parser, compiler, runtime, and generated-code debugging, standard ESM syntax is the canonical frontend module target. If generated code is not valid browser-parseable ESM, fix the owning compiler/runtime layer instead of changing business files around it.
- The legacy handwritten TypeScript `slime-parser` may be used as a reference oracle when diagnosing generated parser or CST-to-AST regressions. Compare grammar, CST shape, variable/export handling, call arguments, and object literal conversion against it, then apply the fix to the active generated Java/TypeScript parser or Qin runtime path.
- Do not add generated-empty fallback or dual parser/AST extraction logic. If the active generated parser, CST-to-AST, normalizer, OVS compiler, or emitter returns an empty/malformed result, that active path is the bug. The old handwritten TypeScript parser is only an oracle for comparison.
- Do not introduce compatibility behavior, compatibility syntax, alternate accepted forms, or "both are valid" docs by default. Qin work has one correct standard path; non-standard input should fail clearly. If compatibility is truly unavoidable for an external boundary or migration, first report why the owning standard path cannot solve it and ask the user to confirm before implementing or documenting the exception.
- OVS syntax details belong in the canonical parser grammar document `D:\project\qkyproject\qinall\ovsjs\ovs\ovs-compiler\docs\OvsParser需求文档.md`. Qin docs and skills should point there rather than restating OVS props grammar.
- Before creating another Qin doc, search the existing docs and skills and update the canonical file when possible. If docs are redundant or oversized, propose a consolidation plan first; do not delete or move canonical docs without explicit confirmation.
