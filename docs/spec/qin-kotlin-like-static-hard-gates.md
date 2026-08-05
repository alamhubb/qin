# Qin Kotlin-Like Static Hard Gates Ledger

Last updated: **2026-08-06 03:00 +08:00**.

## Goal

Turn Qin's Kotlin-like direction into executable compiler gates:

```text
Java/Qin static source
  -> generated TypeScript ESM
  -> static-admission audit
  -> Qin/Slime AST
  -> Qin IR
  -> JVM .class
  -> strict fullstack runtime smoke
```

H5 proved the generated Java/Slime/Subhuti TS to JVM `.class` closure. H6 is a
fresh denominator: H5's `100.0%` is historical evidence only and must not be
reported as active progress for this phase.

## Product Direction

Qin is a Kotlin-like modern JVM/fullstack language direction, not a Kotlin
implementation and not a Kotlin syntax clone. The core product standard is:
static semantics first, fixed owners and members, JVM `.class` as a first-class
target, ESM-shaped source where statically admitted, structured errors for
unsupported dynamic JavaScript, and no fallback parser/runtime compatibility
lane.

Generated TypeScript from Java/Slime/Subhuti must preserve Java's static
intent. Dynamic-looking wrappers such as `.call`, `.apply`, and `.bind` are
admitted only when a compiler-emitted contract proves the fixed member, owner,
method, receiver, and arity shape. A nearby comment without valid contract
fields is not proof.

## Acceptance Conditions

H6 reaches `100.0%` only when current evidence proves all of these:

1. Java -> generated TS emits static-admission contracts only for fixed owner,
   method, receiver, and arity shapes; malformed contracts fail audit.
2. Generated TS static audit rejects unproven `.call`, `.apply`, `.bind`, and
   unknown computed/member/callee shapes that cannot be mapped to static Qin IR.
3. Qin frontend/lowerer/backend hard gates stop unknown receiver, unknown
   member, dynamic global, dynamic member get/set, and dynamic call helpers
   under strict JVM mode at the owning compile boundary.
4. Third-party packages that fail the static subset produce structured reports
   with package root, unsupported shape, static lowering reason, and approved
   choices; they are not silently admitted by runtime compatibility.
5. The generated Slime parser TS, generated Qin parser TS, CSSTS/OVS transform,
   and representative fullstack path pass sequential strict validation.
6. Current-unit code/docs/tests are committed and pushed when practical, with
   unrelated dirty files excluded.

## Three-Layer Progress Scales

Overall active goal: **75.0%** for **H6 Kotlin-like static hard gates**. Its
`100.0%` gate is all H6 acceptance conditions passing and the current-unit git
hygiene gate closed.

Active major item: **100.0%** for **H6-3 third-party static package admission
reports**. Its `100.0%` gate is current validation proving unsupported
third-party package shapes stop with structured package-root diagnostics and
approved operator choices.

Active small item: **100.0%** for **H6-3a third-party report validation**. Its
checkpoints are: inspect owning validator/report smoke 40%, run third-party
report smoke 70%, run parser-scan/runtime-feature companion smoke 95%, and
ledger/git hygiene 100%.

## Weighted Plan

| ID | Gate | Weight | Accepted | State | Evidence |
|---|---|---:|---:|---|---|
| H6-1 | Generated TS static-admission audit hardening | 25% | 100% | Accepted | `QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain OK`, `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK`, and `QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK` prove malformed contracts are rejected while generated Slime/Qin parser TS packages keep contract wrappers and zero legacy dynamic admissions. |
| H6-2 | Compiler/lowerer/backend strict dynamic helper hard gates | 35% | 100% | Accepted | `QinJvmDynamicSemanticWarningSmokeTestMain`, `QinJvmUnknownElementAccessHardGateSmokeTestMain`, `QinJvmSourceUnknownElementAccessHardGateSmokeTestMain`, `QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain`, and `QinJvmSourceUnknownMethodCallHardGateSmokeTestMain` prove strict hard failures for dynamic global/call/member helpers, unknown member get/set, unknown receiver method calls, and unknown computed access. |
| H6-3 | Third-party static package admission reports | 15% | 100% | Accepted | `QinEsmThirdPartyStaticAdmissionReportSmokeTestMain OK` proves scoped third-party packages report package name, package root, source file, unsupported shape, static-lowering reason, and approved choices; `QinEsmRuntimeFeatureParserScanSmokeTestMain passed` keeps the runtime-feature scanner boundary stable. |
| H6-4 | Sequential standard-path regression suite | 15% | 0% | Pending | Needs generated Slime parser TS, generated Qin parser TS, CSSTS/OVS transform, and representative fullstack smoke under strict JVM mode after H6 hardening. |
| H6-5 | Durable capture and git hygiene | 10% | 0% | Pending | Current-unit docs/skills/code must be staged, committed, and pushed with unrelated dirty files excluded. |
| **H6 Total** |  | **100%** | **75.0%** | In progress | H6-1 through H6-3 are accepted. H6-4 and H6-5 remain pending. |

## Progress History

| Time | Step | Status | Evidence | Progress |
|---|---|---|---|---|
| 2026-08-06 02:30 +08:00 | H6 denominator opened and H6-1a focused hardening implemented | Accepted focused checkpoint; broad gate pending | H5 closure was treated as historical evidence only. Existing `QinGeneratedTsStaticAdmissionAudit` and broad generated parser smoke integration were inspected. The first weak gate was contract shape validation: a nearby `@qin-static-admission` comment could admit `.call/.apply/.bind` if fields were non-empty, without validating field grammar or `.call` arity. The audit now validates fixed owner, method, receiver, and arity shapes, allows only numeric/`bound`/`spread` arity, and requires `.call(receiver, ...)` declared arity to match the real argument count after the receiver. Focused `QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain OK` and `..\..\qin.bat build` in `packages/qin-runtime-core` passed. | H6-1a small 0.0% -> 75.0%; H6-1 major 0.0% -> 30.0%; H6 overall 0.0% -> 7.5% |
| 2026-08-06 02:47 +08:00 | H6-1a generated TS contract hardening accepted | Accepted | A focused broad rerun exposed that Java methods whose real source name is `call` must not be treated as JS `Function.prototype.call(receiver, ...)`; the static contract now treats `method=call` as a fixed Java method and validates arity against the actual argument count. The Slime broad smoke also exposed that `findQinRoot()` could misidentify nested package roots when launched from `packages/qin-runtime-core`; the smoke now requires the real Qin repo marker `packages/qin-parser`. Sequential validation passed: `..\..\qin.bat build`, `QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain OK`, `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK` with 1264 contract wrappers and 0 legacy wrappers, and `QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK` with 1526 contract wrappers, 0 legacy wrappers, and 377 module-class outputs. | H6-1a small 75.0% -> 100.0%; H6-1 major 30.0% -> 100.0%; H6 overall 7.5% -> 25.0% |
| 2026-08-06 02:57 +08:00 | H6-2a strict JVM dynamic helper matrix accepted | Accepted | Existing strict-mode hard gates were inventoried and run, then two gaps were closed: the helper-policy smoke now checks `__qin_global__`, `__qin_call__`, `__qin_call_method_array__`, `__qin_member_get__`, and `__qin_member_set__` individually under `qin.dynamicSemanticMode=error`; the source unknown-member smoke now also lowers `this.payload.missing = "qin"` and proves `__qin_member_set__` fails at declaration-class compile time. Sequential validation passed: `..\..\qin.bat build`, `QinJvmDynamicSemanticWarningSmokeTestMain passed`, `QinJvmUnknownElementAccessHardGateSmokeTestMain OK`, `QinJvmSourceUnknownElementAccessHardGateSmokeTestMain OK`, `QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain OK`, and `QinJvmSourceUnknownMethodCallHardGateSmokeTestMain OK`. | H6-2a small 0.0% -> 100.0%; H6-2 major 0.0% -> 100.0%; H6 overall 25.0% -> 60.0% |
| 2026-08-06 03:00 +08:00 | H6-3a third-party static package report accepted | Accepted | The owning report validator and smoke were inspected. Sequential validation passed: `QinEsmThirdPartyStaticAdmissionReportSmokeTestMain OK` for `@vendor/dynamic-kit` under `node_modules`, proving package name, package root, source file, unsupported shape `new Proxy(...)`, static lowering reason, and approved choices; `QinEsmRuntimeFeatureParserScanSmokeTestMain passed` keeps dynamic import/import.meta/arguments/top-level await parser-scan behavior stable without widening runtime compatibility. | H6-3a small 0.0% -> 100.0%; H6-3 major 0.0% -> 100.0%; H6 overall 60.0% -> 75.0% |
