# Qin Kotlin-Like Static Hard Gates Ledger

Last updated: **2026-08-06 03:28 +08:00**.

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

Overall active goal: **100.0%** for **H6 Kotlin-like static hard gates**. Its
`100.0%` gate is all H6 acceptance conditions passing and the current-unit git
hygiene gate closed.

Completed major item: **100.0%** for **H6-5 durable capture and git hygiene**.
Its `100.0%` gate is H6 evidence captured in this ledger and the relevant Qin
skill, with current-unit repo changes committed and pushed while unrelated dirty
files remain excluded.

Completed small item: **100.0%** for **H6-5a closeout hygiene**. Its checkpoints
are: commit/push H6-1 through H6-4 units 60%, verify targeted touched paths are
clean 80%, record unrelated dirty count/exclusion 90%, and final ledger commit
100%.

Completed small item: **100.0%** for **Post-H6 completion audit**. Its
checkpoints were: re-open objective evidence map 40%, identify the still-dirty
generated Qin parser TS smoke as a relevant git-hygiene gap 70%, rerun the
strict generated Qin parser TS JVM smoke with a long enough sequential window
90%, and commit/push the smoke plus ledger evidence 100%.

## Weighted Plan

| ID | Gate | Weight | Accepted | State | Evidence |
|---|---|---:|---:|---|---|
| H6-1 | Generated TS static-admission audit hardening | 25% | 100% | Accepted | `QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain OK`, `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK`, and `QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK` prove malformed contracts are rejected while generated Slime/Qin parser TS packages keep contract wrappers and zero legacy dynamic admissions. |
| H6-2 | Compiler/lowerer/backend strict dynamic helper hard gates | 35% | 100% | Accepted | `QinJvmDynamicSemanticWarningSmokeTestMain`, `QinJvmUnknownElementAccessHardGateSmokeTestMain`, `QinJvmSourceUnknownElementAccessHardGateSmokeTestMain`, `QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain`, and `QinJvmSourceUnknownMethodCallHardGateSmokeTestMain` prove strict hard failures for dynamic global/call/member helpers, unknown member get/set, unknown receiver method calls, and unknown computed access. |
| H6-3 | Third-party static package admission reports | 15% | 100% | Accepted | `QinEsmThirdPartyStaticAdmissionReportSmokeTestMain OK` proves scoped third-party packages report package name, package root, source file, unsupported shape, static-lowering reason, and approved choices; `QinEsmRuntimeFeatureParserScanSmokeTestMain passed` keeps the runtime-feature scanner boundary stable. |
| H6-4 | Sequential standard-path regression suite | 15% | 100% | Accepted | Sequential strict validation passed: `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK`, `QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK`, `QinGeneratedTsSlimeCsstsCompilerSmokeTestMain`, `QinGeneratedTsSlimeOvsTransformSmokeTestMain`, and `QinFullstackJavaBackendSmokeTestMain OK`. |
| H6-5 | Durable capture and git hygiene | 10% | 100% | Accepted | H6 changes were split into focused commits `f8c4c973`, `8125c933`, `41e4a85b`, and `c075e5ae`, pushed to `master`, and this final ledger closeout records targeted touched paths clean with 2182 unrelated dirty entries intentionally excluded. The post-H6 completion audit also commits the generated Qin parser TS smoke assertions that prove the broad Qin parser package has 1526 contract wrappers and zero legacy dynamic-wrapper admissions. The Qin runtime skill was updated outside the repo. |
| **H6 Total** |  | **100%** | **100.0%** | Accepted | H6-1 through H6-5 are accepted. |

## Progress History

| Time | Step | Status | Evidence | Progress |
|---|---|---|---|---|
| 2026-08-06 02:30 +08:00 | H6 denominator opened and H6-1a focused hardening implemented | Accepted focused checkpoint; broad gate pending | H5 closure was treated as historical evidence only. Existing `QinGeneratedTsStaticAdmissionAudit` and broad generated parser smoke integration were inspected. The first weak gate was contract shape validation: a nearby `@qin-static-admission` comment could admit `.call/.apply/.bind` if fields were non-empty, without validating field grammar or `.call` arity. The audit now validates fixed owner, method, receiver, and arity shapes, allows only numeric/`bound`/`spread` arity, and requires `.call(receiver, ...)` declared arity to match the real argument count after the receiver. Focused `QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain OK` and `..\..\qin.bat build` in `packages/qin-runtime-core` passed. | H6-1a small 0.0% -> 75.0%; H6-1 major 0.0% -> 30.0%; H6 overall 0.0% -> 7.5% |
| 2026-08-06 02:47 +08:00 | H6-1a generated TS contract hardening accepted | Accepted | A focused broad rerun exposed that Java methods whose real source name is `call` must not be treated as JS `Function.prototype.call(receiver, ...)`; the static contract now treats `method=call` as a fixed Java method and validates arity against the actual argument count. The Slime broad smoke also exposed that `findQinRoot()` could misidentify nested package roots when launched from `packages/qin-runtime-core`; the smoke now requires the real Qin repo marker `packages/qin-parser`. Sequential validation passed: `..\..\qin.bat build`, `QinGeneratedTsStaticAdmissionAuditContractSmokeTestMain OK`, `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK` with 1264 contract wrappers and 0 legacy wrappers, and `QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK` with 1526 contract wrappers, 0 legacy wrappers, and 377 module-class outputs. | H6-1a small 75.0% -> 100.0%; H6-1 major 30.0% -> 100.0%; H6 overall 7.5% -> 25.0% |
| 2026-08-06 02:57 +08:00 | H6-2a strict JVM dynamic helper matrix accepted | Accepted | Existing strict-mode hard gates were inventoried and run, then two gaps were closed: the helper-policy smoke now checks `__qin_global__`, `__qin_call__`, `__qin_call_method_array__`, `__qin_member_get__`, and `__qin_member_set__` individually under `qin.dynamicSemanticMode=error`; the source unknown-member smoke now also lowers `this.payload.missing = "qin"` and proves `__qin_member_set__` fails at declaration-class compile time. Sequential validation passed: `..\..\qin.bat build`, `QinJvmDynamicSemanticWarningSmokeTestMain passed`, `QinJvmUnknownElementAccessHardGateSmokeTestMain OK`, `QinJvmSourceUnknownElementAccessHardGateSmokeTestMain OK`, `QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain OK`, and `QinJvmSourceUnknownMethodCallHardGateSmokeTestMain OK`. | H6-2a small 0.0% -> 100.0%; H6-2 major 0.0% -> 100.0%; H6 overall 25.0% -> 60.0% |
| 2026-08-06 03:00 +08:00 | H6-3a third-party static package report accepted | Accepted | The owning report validator and smoke were inspected. Sequential validation passed: `QinEsmThirdPartyStaticAdmissionReportSmokeTestMain OK` for `@vendor/dynamic-kit` under `node_modules`, proving package name, package root, source file, unsupported shape `new Proxy(...)`, static lowering reason, and approved choices; `QinEsmRuntimeFeatureParserScanSmokeTestMain passed` keeps dynamic import/import.meta/arguments/top-level await parser-scan behavior stable without widening runtime compatibility. | H6-3a small 0.0% -> 100.0%; H6-3 major 0.0% -> 100.0%; H6 overall 60.0% -> 75.0% |
| 2026-08-06 03:09 +08:00 | H6-4a strict standard-path regression suite accepted | Accepted | The selected suite ran sequentially under `-Dqin.dynamicSemanticMode=error`. Generated Slime parser TS passed with 1264 contract wrappers and 0 legacy wrappers. Generated Qin parser TS passed with 1526 contract wrappers, 0 legacy wrappers, and 377 module dependency session. CSSTS compiler strict smoke and OVS transform strict smoke both exited 0. Representative fullstack validation `QinFullstackJavaBackendSmokeTestMain OK` generated `demo/Main.class` and frontend `app.js` in build-only mode. | H6-4a small 0.0% -> 100.0%; H6-4 major 0.0% -> 100.0%; H6 overall 75.0% -> 90.0% |
| 2026-08-06 03:11 +08:00 | H6-5a durable capture and git hygiene accepted | Accepted | H6-1 through H6-4 were each committed and pushed as focused current-unit commits: `f8c4c973 harden generated ts static admission contracts`, `8125c933 prove strict dynamic helper hard gates`, `41e4a85b record third party static admission gate`, and `c075e5ae record strict standard path h6 suite`. Targeted status for all H6 touched repo paths was clean. Whole-repo status still has 2182 dirty entries from historical/user/generated work, intentionally excluded. `C:\Users\qinky\.codex\skills\qin-runtime-direct-fixes\SKILL.md` was updated with the H6 static contract, repo-root, and strict helper matrix lessons outside the Qin repo. | H6-5a small 0.0% -> 100.0%; H6-5 major 0.0% -> 100.0%; H6 overall 90.0% -> 100.0% |
| 2026-08-06 03:28 +08:00 | Post-H6 completion audit for generated Qin parser TS smoke | Accepted audit checkpoint | Completion audit found `QinJavaProjectQinParserTsEsmFilesSmokeTestMain.java` still dirty even though it is the broad smoke that proves generated Qin parser TS static admission and JVM module/declaration class closure. `git diff --check` passed for the file, `..\..\qin.bat build` in `packages/qin-runtime-core` passed, and a longer sequential `..\..\qin.bat run com.qin.runtime.core.QinJavaProjectQinParserTsEsmFilesSmokeTestMain` passed after compiling/running 377 modules. The smoke printed static admission wrappers `1526`, contract wrappers `1526`, legacy wrappers `0`, legacy reasons `{}`, and `QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK`. | Completion-audit small 90.0% -> 100.0%; completion-audit major 80.0% -> 100.0%; H6 overall remains 100.0% |
