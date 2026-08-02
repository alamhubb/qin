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
- When a user asks for visible task progress, define three `100%` scales
  immediately: overall progress, major-item progress, and small-item progress.
  Overall progress is the whole project or current long-running goal; major-item
  progress is the active phase or milestone that unlocks the next phase at
  `100%`; small-item progress is the active concrete implementation or
  validation item.
- Small-item progress must also be decomposed into visible weighted checkpoints,
  normally 3-7 steps such as reproduce, locate owner, design fix, implement,
  focused validation, broad validation, and cleanup/report. Do not use a bare
  `0% -> 100%` small item unless the task is genuinely atomic and finishes
  within one short command.
- Every update must show all three percentages, the `100%` acceptance gate for
  each layer, the current action, the next task, and the expected percentage
  change after the next accepted small-item checkpoint.
- After each accepted checkpoint, report small-item progress in `before -> after`
  form and state whether that changes major-item or overall progress. If the
  work only gathers evidence or exposes the next blocker, keep the relevant
  percentages unchanged and say which gate is still pending. Do not enter or
  claim the next phase until the current major-item progress reaches a real
  accepted `100%`.
- If a previous ledger, milestone, route, or phase is complete, frozen,
  superseded, or already in post-100% cleanup, do not reuse that terminal scale
  for a new active integration goal. Define or select a fresh current-goal
  denominator with its own `100%` acceptance gate before reporting progress.
  Historical percentages may be cited as evidence only, not as the active
  denominator.
- Do not keep one small-item scale pinned at `99.9%` across multiple independent
  blockers. When broad validation proves one blocker fixed but exposes another
  owning-layer blocker, record the old small item as evidence and open a fresh
  small item with its own weighted checkpoints. The major item can stay capped
  until its gate passes, but the active small item must show honest intermediate
  movement.
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
- Keep git cleanliness as an active gate before continuing work. Before
  starting, resuming, or switching to another coherent unit, inspect the
  relevant repository status and clean the current unit first.
- After the unit is implemented and practical validation has run, inspect the
  diff, stage only the intended files, commit, and push when the configured
  remote is usable.
- Do not move on to unrelated work while a completed coherent unit remains
  uncommitted.
- Keep unrelated dirty files out of the commit. Assume they are user work unless
  explicitly told otherwise.
- If a repository is already dirty, classify dirty paths before editing:
  current-agent completed work to commit, current-agent in-progress work to
  finish, unrelated user/historical work to leave untouched, or irrelevant
  generated/cache output. Do not use `git add .` to hide that distinction.
- Clean worktree means clean for the paths Codex owns in the current unit. When
  unrelated dirty files already exist, leave them untouched, keep current-unit
  paths committed/pushed, and report the remaining unrelated status.
- If validation is blocked or a push fails, keep the local commit when safe and
  report the exact blocker.

## Encoding Discipline

- Qin work uses UTF-8 without BOM for source, docs, configs, tests, fixtures,
  generated text artifacts, skills, Java argfiles, and validation artifacts.
- Do not rely on Windows ANSI code pages such as GBK for reading, writing,
  validating, or generating project text. If a command depends on the current
  locale, the result is not a valid Qin validation result.
- Do not use GBK, ANSI, OEM, `-Encoding Default`, `encoding="gbk"`, `cp936`,
  `chcp 936`, or locale-default decoding as a repair path, validation path, or
  way to make tooling pass. Fix the owning command or script so it reads and
  writes UTF-8 without BOM.
- If an external legacy input is truly not UTF-8, decode it explicitly at the
  boundary, convert the tracked/working artifact to UTF-8 without BOM, and keep
  that legacy encoding out of skills, repo docs, source, generated files, and
  validation commands.
- Python scripts must use explicit `encoding="utf-8"` for text file IO. When
  running Python tools from Windows or another locale-sensitive shell, set
  `PYTHONUTF8=1` or use `python -X utf8`.
- PowerShell writes to tracked text must use UTF-8 without BOM. Avoid
  `Out-File` defaults, `Set-Content`/`Add-Content` without explicit UTF-8
  behavior, `-Encoding Default`, and other implicit encodings.
- Java/Javac text validation should pass `-encoding UTF-8` and runtime UTF-8
  properties where applicable.
- If a tool exposes a GBK/default-locale failure, fix the owning script or
  command rule so future runs force UTF-8 instead of treating the failure as a
  content problem. Do not report a GBK-based rerun as successful validation.

## Cache And Instant Compilation

- If Qin cache behavior, module-class disk cache, dependency classpath refresh,
  generated parser materialization, hot rebuild, or instant compilation is
  stale or incorrect, treat it as a toolchain defect.
- Fix the owning cache/compiler/build layer directly and add a focused smoke
  test when practical.
- Manual cache deletion, forced full rebuilds, larger timeouts, or source
  rewrites may be used only as diagnostics. They are not acceptable final
  fixes for stale cache or instant compilation defects.
- Before declaring a module-class disk cache invalidation bug, prove the active
  wrapper/source content actually changed at the boundary being compiled:
  compare the generated wrapper source, its content-derived wrapper identity,
  the Java probe class on the active classpath, and whether the runner logged
  `module-class compile start` or a cache hit. An old wrapper or stale probe
  class can look like stale disk cache; do not change cache keys until the
  source digest contract is disproved by same-source-path evidence.
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
- If a focused AST probe proves a node is correct, such as
  `UpdateExpression(operator="++", prefix=false, argument=MemberExpression)`,
  but emitted ESM drops the semantic operator, the owning fix is the active
  generator/emitter's abstract AST dispatch for that node type. Do not patch
  the source sample, add parser token checks, or broaden dynamic JS runtime
  support.
- Pipeline probes should reuse the active artifacts produced by earlier stages. If the probe already has tokens, CST, and AST for the same source, the emitted ESM stage should generate from those artifacts instead of invoking a second full transform that repeats parser and CST-to-AST work, unless the explicit goal is to compare that full transform boundary.
- For OVS, the canonical reusable diagnostic entry is `packages/qin-runtime-core/src/java/com/qin/runtime/core/QinOvsParserPipelineProbeMain.java`. Run it on the smallest `.ovs` source first, then run `QinOvsParserPipelineProbeSmokeTestMain`, then the owning OVS compiler smoke, and only then the browser/dev-server boundary.
- Parser diagnostics are not compatibility behavior and not fallback logic. They must exercise the active standard parser/compiler path and expose the failing layer directly.
- For parser/compiler probes, an apparently successful run with zero tokens, zero CST children, zero AST body items, or empty generated output is a failure in the owning layer, not a passing diagnostic. Do not let empty output count as success; tighten the probe so the empty case surfaces as an explicit parse/transform defect.
- For Qin/OVS/CSSTS parser, compiler, runtime, and generated-code debugging, standard ESM syntax is the canonical frontend module target. If generated code is not valid browser-parseable ESM, fix the owning compiler/runtime layer instead of changing business files around it.
- Parser and generated-wrapper fixes must be phrased in abstract grammar or dispatch facts, not sample-specific token/word checks. For Subhuti-generated TypeScript, a `@SubhutiRule` wrapper owns the raw body declared on that wrapper class; `super.Rule(...)` must execute that declaring-class raw body rather than dynamically redispatching through a subclass `this.__qin_subhuti_raw_*`. Extension syntax should enter as a static grammar/rule alternative or owning bridge, not as a growing list of first-letter, token-name, or word special cases.
- Preserve accepted Qin-owned TS/JS source API shapes on the `.class` path. The canonical language rule is `packages/qin-runtime-core/QIN_JS_COMPATIBILITY_MODEL.md`: static source calls should keep the same Qin-visible import/export surface, member or method name, argument order, admitted arity, and default-argument meaning through generated facades, overload/default-argument lowering, typed helpers, or bridge fixes rather than forcing callers to use lower-level Java implementation signatures.
- Qin Java/runtime diagnostics should fail early instead of hiding defects behind long hangs. Focused parser/compiler/runtime probes should use a timeout of 30 seconds or less unless a specific cold-start benchmark explicitly declares a longer budget. The Qin JS-on-JVM runtime default run timeout is 30 seconds; if it is exceeded, report the timeout and captured stack as the next debugging boundary instead of rerunning broader flows.
- The legacy handwritten TypeScript `slime-parser` may be used as a reference oracle when diagnosing generated parser or CST-to-AST regressions. Compare grammar, CST shape, variable/export handling, call arguments, and object literal conversion against it, then apply the fix to the active generated Java/TypeScript parser or Qin runtime path.
- Do not add generated-empty fallback or dual parser/AST extraction logic. If the active generated parser, CST-to-AST, normalizer, OVS compiler, or emitter returns an empty/malformed result, that active path is the bug. The old handwritten TypeScript parser is only an oracle for comparison.
- Do not introduce compatibility behavior, compatibility syntax, alternate accepted forms, or "both are valid" docs by default. Qin work has one correct standard path; non-standard input should fail clearly. If compatibility is truly unavoidable for an external boundary or migration, first report why the owning standard path cannot solve it and ask the user to confirm before implementing or documenting the exception.
- OVS syntax details belong in the canonical parser grammar document `D:\project\qkyproject\qinall\ovsjs\ovs\ovs-compiler\docs\OvsParser需求文档.md`. Qin docs and skills should point there rather than restating OVS props grammar.
- Before creating another Qin doc, search the existing docs and skills and update the canonical file when possible. If docs are redundant or oversized, propose a consolidation plan first; do not delete or move canonical docs without explicit confirmation.
