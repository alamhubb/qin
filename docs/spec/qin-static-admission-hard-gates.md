# Qin Static Admission Hard Gates Ledger

Last updated: **2026-08-02 16:33 +08:00**.

## Goal

Turn Qin's Kotlin-like static language rules from documentation and partial
runtime warnings into executable compiler gates. H4 starts after the generated
parser transform hot-path closure reached the overall active goal at `90.0%`.

This is a fresh denominator. Previous H1-H3 ledgers are historical evidence
only; they do not count as active H4 progress.

## Acceptance Conditions

H4 reaches `100%` only when current evidence proves:

1. Generated TypeScript static admission is owner-aware and is not a
   line/string-pattern allowlist for `.call`, `.apply`, or `.bind`.
2. Unknown receiver/member calls on the JVM `.class` path fail at the compiler
   or backend boundary instead of falling through to dynamic runtime helpers.
3. Unknown `obj[key]` access fails unless the base has a Qin-visible
   Map/Dict/indexed-collection type or an explicitly modeled compiler-owned
   indexed table type.
4. Dynamic-looking generated wrappers are admitted only when the receiver,
   member, and arity contract is tied to Java/Qin source facts.
5. Third-party package shapes that cannot pass static admission stop with a
   reportable diagnostic instead of silently broadening runtime compatibility.
6. Focused and broad validations run sequentially on Windows under
   `-Dqin.dynamicSemanticMode=error`.
7. Current-unit changes are committed and pushed when practical, with unrelated
   dirty files excluded.

## Progress Scales

Overall active goal: **100.0%**. H4 is weighted as the next 10 overall
percentage points. Each 10% accepted H4 major progress advances the overall
goal by 1.0%.

Completed major item: **100.0%** for **H4 static admission hard gates**. Its `100%`
gate is the static subset producing hard, owner-aware compiler/backend
diagnostics for generated TS dynamic wrappers, unknown receivers/members,
unknown index access, and non-static package shapes, with strict JVM validation
passing through the standard Qin path.

Completed small item: **100.0%** for **H4-1 current gate audit and route
selection**. Its checkpoints were: inspect current ledgers/docs 25%, inspect
active audit/validator/backend owners 50%, record fresh H4 ledger 75%, select
the first executable gate slice 100%.

Completed small item: **100.0%** for **H4-4a unknown indexed access IR/backend
focused gate**. Its checkpoints were: select IR/backend owner 25%, identify
conflicting old dynamic smoke 40%, add unknown `Object` receiver hard-gate smoke
70%, run unknown-fail/static-Map-pass/dynamic-helper strict smokes 90%, ledger
capture 100%.

Completed small item: **100.0%** for **H4-4b indexed access source-to-IR and git
cleanup**. Its checkpoints are: classify existing backend dirty changes 25%,
separate current gate implementation from unrelated historical edits 45%,
prove source-to-IR unknown index rejection 65%, rerun strict indexed-access
group 85%, commit/push intended paths or record exact blocker 100%.

Completed small item: **100.0%** for **H4-3a unknown member source-to-IR hard
gate**. Its checkpoints are: confirm the existing property-access owner and
strict failure surface 20%, add source-to-IR unknown property smoke 45%, verify
the lowered node is `QinIrPropertyAccessExpression` instead of a string scan
55%, run source unknown-property rejection 70%, rerun static member pass and
dynamic strict group 90%, ledger/git capture 100%.

Completed small item: **100.0%** for **H4-3b unknown receiver call source-to-IR hard
gate**. Its checkpoints are: confirm the instance-call owner and strict dynamic
call failure surface 20%, add source-to-IR unknown call smoke 45%, verify the
lowered node is `QinIrInstanceMethodCallExpression` instead of a string scan
55%, run source unknown-call rejection 70%, rerun typed receiver/static member
and dynamic strict group 90%, ledger/git capture 100%.

Completed small item: **100.0%** for **H4-2a generated TS dynamic-call AST
occurrence detection**. Its checkpoints were: identify current line/source
scanner owner 20%, replace `.call/.apply/.bind` occurrence discovery with
Qin/Slime AST `CallExpression -> MemberExpression` traversal 55%, compile the
audit and run full generated Slime Parser TS ESM smoke 80%, ledger/git capture
100%. This does not finish H4-2 because several allow rules still need to move
from line/source text predicates to generated owner contracts.

Completed small item: **100.0%** for **H4-2b generated TS owner-contract
admission**. Its checkpoints were: inventory remaining allowed-wrapper
predicates 20%, define the generated owner contract record/span model 40%, emit
or attach contract facts from Java-to-TS generation 60%, make audit match AST
occurrences against contracts 80%, rerun generated TS rejection and full Slime
Parser TS ESM smoke with zero generated legacy admissions 100%. Final broad
evidence proves 1264/1264 dynamic-looking occurrences admitted by generated
owner contracts and `legacyAllowedDynamicWrapperCount() == 0`; the legacy
allow branch has been removed from the audit.

Completed small item: **100.0%** for **H4-5a third-party package
static-admission report path**. Its checkpoints were: locate current
npm/package admission owner 20%, define diagnostic payload for
package/file/unsupported shape/choices 40%, add focused non-static package
smoke 60%, wire the report path into the standard admission failure 80%, run
focused and relevant package smoke sequentially 100%.

Completed small item: **100.0%** for **H4-6a cleanup, durable capture, and git
hygiene boundary classification**. Its checkpoints were: inspect current-unit
diff 20%, run focused diff/format hygiene 40%, classify unrelated dirty files
60%, identify practical commit boundary or blocker 80%, record final H4 cleanup
evidence 100%.

Completed small item: **100.0%** for **H4-6b commit core H4 static-admission
unit**. Its checkpoints were: stage only H4-2/H4-5 intended files 20%, verify
staged diff names 40%, create local commit 60%, attempt push when a safe remote
is available 80%, record post-commit status and remaining dirty exclusions
100%.

Completed small item: **100.0%** for **H4-6c split backend-jvm hard-gate
remainder**. Its checkpoints were: list backend-jvm H4 files versus unrelated
backend smokes 20%, inspect `QinJvmDeclarationClassEmitter`/dynamic-warning
diff ownership 40%, rerun H4-3/H4-4 strict backend validations 60%, stage or
explicitly block the backend-jvm commit boundary 80%, record final H4-6
remainder evidence 100%.

Next active denominator: open a fresh H5 ledger for generated TS -> Qin IR ->
JVM `.class` closure. H4 is now historical evidence only and must not be reused
as the active denominator for the next phase.

## H4 Weighted Plan

| ID | Gate | Weight | Accepted | State | Evidence |
|---|---|---:|---:|---|---|
| H4-1 | Fresh H4 denominator and first owner-aware gate slice selected | 10% | 100% | Accepted | Current audit found `QinGeneratedTsStaticAdmissionAudit` and `QinEsmRuntimeFeatureValidator` still rely on source scanning/line allowlists for some static-gate decisions; the first executable slice is the IR/JVM indexed-access hard gate. |
| H4-2 | Generated TS dynamic wrapper admission uses AST/IR/owner facts | 25% | 100% | Accepted | Occurrence discovery uses Qin/Slime AST `CallExpression -> MemberExpression`; owner contracts are emitted from Java-to-TS/IR method-call facts; Java target-typed lambda, record accessor, enhanced-for `var`, and `Map.Entry` generic propagation now preserve owner facts. The broad generated Slime Parser TS ESM smoke passes with 1264 contract-admitted wrappers and 0 legacy admissions after the legacy allow branch was removed. |
| H4-3 | Unknown receiver/member hard failures are covered on JVM `.class` path | 20% | 100% | Accepted | Source-to-IR unknown member access and unknown receiver method calls now hard-fail at the JVM backend, while typed receiver/static member smokes still pass. Backend hard-gate files were committed and pushed as `77c160a3`. |
| H4-4 | Unknown indexed access gate distinguishes Map/Dict from dynamic object lookup | 20% | 100% | Accepted | Source-to-IR and IR/backend validation passed: `QinJvmSourceUnknownElementAccessHardGateSmokeTestMain OK`, `QinJvmUnknownElementAccessHardGateSmokeTestMain OK`, `QinJvmStaticMapElementAccessSmokeTestMain OK`, and `QinJvmDynamicSemanticWarningSmokeTestMain passed.` Backend hard-gate files were committed and pushed as `77c160a3`. |
| H4-5 | Third-party package static admission report path is executable | 15% | 100% | Accepted | `QinEsmStaticAdmissionReport` now attaches package name, package root, source file, unsupported shape, static-lowering reason, and approved choices to third-party `node_modules` hard dynamic feature diagnostics. `QinEsmThirdPartyStaticAdmissionReportSmokeTestMain OK` proves `new Proxy(...)` in `@vendor/dynamic-kit` reports the package decision path; `QinEsmRuntimeFeatureParserScanSmokeTestMain passed` keeps existing parser-scan behavior stable. |
| H4-6 | Cleanup, durable capture, and git hygiene | 10% | 100% | Accepted | H4-2/H4-5 core static-admission unit was committed as `34fa2c7f` and pushed to `origin/master`; final push evidence was recorded as `b0717360`; backend-jvm H4-3/H4-4 hard-gate unit was committed and pushed as `77c160a3`; unrelated historical dirty files remain excluded. |
| **H4 Total** |  | **100%** | **100.0%** | Accepted | H4-1 through H4-6 accepted. Qin static admission hard gates now have executable owner-aware generated TS audit, JVM hard failures for unknown receiver/member/indexed access, third-party package static-admission reports, sequential validation evidence, and committed/pushed current-unit changes. |

## Progress History

| Time | Step | Status | Evidence | Progress |
|---|---|---|---|---|
| 2026-08-02 14:46 +08:00 | H4 denominator opened | Accepted checkpoint | H3 closed at overall `90.0%`; H4 now tracks static admission hard gates separately. Current inspection found static rules documented in `QIN_JS_COMPATIBILITY_MODEL.md`, a generated TS audit gate in `QinGeneratedTsStaticAdmissionAudit`, runtime feature scanning in `QinEsmRuntimeFeatureValidator`, and dynamic helper hard-failure support in `QinJvmDynamicSemanticWarnings`. The first route is to replace source-scan/line-allowlist admission with owner-aware AST/IR/static facts. | H4 small 50.0% -> 75.0%; H4 major 0.0% -> 5.0%; overall 90.0% -> 90.5% |
| 2026-08-02 14:52 +08:00 | H4-1 route selected and H4-4a indexed hard gate validated | Accepted checkpoint | The first executable slice is the IR/JVM indexed-access gate, because it checks `QinIrElementAccessExpression` receiver type rather than source words. Added `QinJvmUnknownElementAccessHardGateSmokeTestMain`, which constructs `values: Object` plus `values[1]` and expects `[QinDynamicSemanticError]`. Sequential strict validation passed for unknown indexed access rejection, explicit `Map` indexed access acceptance, and dynamic helper strict-mode failure. A historical untracked `QinJvmDynamicElementAccessSmokeTestMain` still proves the old dynamic behavior and is rejected as stale evidence. `QinJvmDeclarationClassEmitter.java` already contains related large dirty changes, so this slice is not yet a clean commit unit. | H4-1 small 75.0% -> 100.0%; H4-4a small 0.0% -> 100.0%; H4 major 5.0% -> 20.0%; overall 90.5% -> 92.0% |
| 2026-08-02 14:58 +08:00 | H4-4b source-to-IR indexed hard gate validated | Accepted validation, git capture blocked | Added `QinJvmSourceUnknownElementAccessHardGateSmokeTestMain`, which lowers real source `values: any` plus `values[1]` through `QinFrontendLowerer`, asserts the lowered return is a `QinIrElementAccessExpression`, and then verifies the JVM backend fails with `[QinDynamicSemanticError]` under `-Dqin.dynamicSemanticMode=error`. Sequential validation passed for source unknown index rejection, hand-built IR unknown index rejection, explicit `Map` indexed access acceptance, and dynamic helper strict-mode failure. `QinJvmDeclarationClassEmitter.java` remains a large pre-existing dirty diff; staging it wholesale would mix unrelated historical work, while committing only the new tests/docs would not be a clean reproducible gate from a clean checkout. | H4-4b small 0.0% -> 100.0%; H4-4 accepted 50.0% -> 85.0%; H4 major 20.0% -> 27.0%; overall 92.0% -> 92.7% |
| 2026-08-02 15:02 +08:00 | H4-3a source-to-IR unknown member hard gate validated | Accepted validation, git capture blocked | Added `QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain`, which lowers real source `this.payload.missing` through `QinFrontendLowerer`, asserts the lowered return is a `QinIrPropertyAccessExpression`, and verifies the JVM backend fails with `[QinDynamicSemanticError]` under `-Dqin.dynamicSemanticMode=error` because the receiver is `java.lang.Object`. Sequential validation also passed `QinJvmGeneratedTypedReceiverAbstractSlotSmokeTestMain OK` and `QinJvmDynamicSemanticWarningSmokeTestMain passed.`, proving known typed receiver/member slots remain admitted. The same large pre-existing `QinJvmDeclarationClassEmitter.java` diff blocks a clean reproducible commit. | H4-3a small 0.0% -> 100.0%; H4-3 accepted 0.0% -> 50.0%; H4 major 27.0% -> 37.0%; overall 92.7% -> 93.7% |
| 2026-08-02 15:06 +08:00 | H4-3b source-to-IR unknown receiver call hard gate validated | Accepted validation, git capture blocked | Added `QinJvmSourceUnknownMethodCallHardGateSmokeTestMain`, which lowers real source `this.payload.missing("qin")` through `QinFrontendLowerer`, asserts the lowered return is a `QinIrInstanceMethodCallExpression`, and verifies the JVM backend fails with `[QinDynamicSemanticError]` for `__qin_call_method_array__` under `-Dqin.dynamicSemanticMode=error` because the receiver type is `java.lang.Object`. Sequential validation also reran source unknown member rejection, typed receiver/static member acceptance, and dynamic strict failure. The same pre-existing `QinJvmDeclarationClassEmitter.java` diff blocks a clean reproducible commit. | H4-3b small 20.0% -> 100.0%; H4-3 accepted 50.0% -> 85.0%; H4 major 37.0% -> 44.0%; overall 93.7% -> 94.4% |
| 2026-08-02 15:13 +08:00 | H4-2a generated TS dynamic-call occurrence detection moved to AST | Accepted checkpoint | Updated `QinGeneratedTsStaticAdmissionAudit` so `.call/.apply/.bind` occurrence discovery parses generated TS through `QinParserFacade` and traverses Slime AST `CallExpression -> MemberExpression` nodes. This removes the first-letter/member-name source scanner from occurrence discovery. Validation passed: `javac` for the audit and `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK`, with `Generated TS static admission wrappers: 1264`. Remaining H4-2 work is to replace the still-existing allowed-wrapper predicates that inspect line/source text with owner-contract metadata tied to generated Java/Qin facts. | H4-2a small 0.0% -> 100.0%; H4-2 accepted 0.0% -> 30.0%; H4 major 44.0% -> 51.5%; overall 94.4% -> 95.2% |
| 2026-08-02 15:56 +08:00 | H4-2b generated TS owner contracts cover almost all dynamic-looking wrappers | Accepted checkpoint, final blocker open | Added generated static-admission contracts to Java-to-TS runtime helper and backend IR method-call emission, split audit counts into contract vs legacy admissions, and added `QinJavaAstIrLowererCollectorLambdaOwnerSmokeTestMain` for target-typed collector lambda owner propagation. Sequential validation passed for audit/backend/lowerer compilation and full `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK`. Contract coverage improved from 592/1264 to 1261/1264; legacy admissions fell from 672 to 3. The remaining 3 are `SubhutiFiniteDecisionCompiler` enhanced-for `var` over `byCall.entrySet()` followed by `entry.getValue().stream().map(contributor -> contributor.call()...)`, where one focused smoke still reports `missing=1/4` owner facts. | H4-2b small 0.0% -> 96.0%; H4-2 accepted 30.0% -> 85.0%; H4 major 51.5% -> 65.3%; overall 95.2% -> 96.5% |
| 2026-08-02 16:08 +08:00 | H4-2b generated TS owner-contract admission closed | Accepted | Fixed the remaining enhanced-for expression-path owner propagation and `Map.Entry.getKey/getValue` generic return typing. `QinJavaAstIrLowererCollectorLambdaOwnerSmokeTestMain OK` now proves collector lambda `call()` accessors retain static owners through both statement and expression lowering paths. Removed the legacy dynamic-wrapper allow branch from `QinGeneratedTsStaticAdmissionAudit` and added a broad-smoke assertion that `legacyAllowedDynamicWrapperCount() == 0`. Sequential validation passed: focused collector lambda owner smoke and full `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK`, with `Generated TS static admission wrappers: 1264`, `contract wrappers: 1264`, `legacy wrappers: 0`. | H4-2b small 96.0% -> 100.0%; H4-2 accepted 85.0% -> 100.0%; H4 major 65.3% -> 69.0%; overall 96.5% -> 96.9% |
| 2026-08-02 16:18 +08:00 | H4-5a third-party package static-admission report path closed | Accepted | Added `QinEsmStaticAdmissionReport` and attached it to `QinEsmDiagnostic` for third-party `node_modules` modules that hit hard dynamic JVM feature diagnostics. The report carries package name, package root, source file, unsupported shape, static-lowering reason, and approved choices: reject package, write a Qin-owned facade, select a different static package entry, or change project source with approval. Sequential validation passed: `QinEsmThirdPartyStaticAdmissionReportSmokeTestMain OK` and `QinEsmRuntimeFeatureParserScanSmokeTestMain passed`. This keeps the existing validator path visible without broadening runtime compatibility. | H4-5a small 0.0% -> 100.0%; H4-5 accepted 0.0% -> 100.0%; H4 major 69.0% -> 84.0%; overall 96.9% -> 98.4% |
| 2026-08-02 16:20 +08:00 | H4-6a cleanup boundary classified | Blocked before final commit | `git diff --check` passed for the current H4-5/H4-6 paths, with only CRLF replacement warnings on existing Java files. Targeted status shows current report files plus the H4 ledger, but `QinEsmRuntimeFeatureValidator.java` and `QinEsmRuntimeFeatureParserScanSmokeTestMain.java` already contained pre-existing uncommitted changes before this step. A whole-file commit would mix current structured package-report work with earlier edits, so git hygiene is recorded as blocked until the existing H4 dirty set is reviewed/staged as one intended unit or split interactively. | H4-6a small 0.0% -> 80.0%; H4-6 accepted 0.0% -> 0.0%; H4 major remains 84.0%; overall remains 98.4% |
| 2026-08-02 16:26 +08:00 | H4-6a cleanup boundary split into core commit unit and backend-jvm remainder | Accepted checkpoint | Full working tree still contains a large unrelated/historical dirty set, but targeted inspection found a coherent H4-2/H4-5 commit boundary: `QinJsBackend`, Java semantic/lowerer owner propagation, generated TS static admission audit/smoke, ESM static-admission report diagnostics/smoke, and this ledger. Sequential validation passed immediately before staging: `QinJavaAstIrLowererCollectorLambdaOwnerSmokeTestMain OK`, `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK` with `1264` contract wrappers and `0` legacy wrappers, `QinEsmThirdPartyStaticAdmissionReportSmokeTestMain OK`, and `QinEsmRuntimeFeatureParserScanSmokeTestMain passed`. H4-6 remains open because backend-jvm H4-3/H4-4 changes are still mixed with many older backend smokes and generated/runtime files. | H4-6a small 80.0% -> 100.0%; H4-6 accepted 0.0% -> 40.0%; H4 major 84.0% -> 88.0%; overall 98.4% -> 98.8% |
| 2026-08-02 16:28 +08:00 | H4-6b core static-admission unit committed and pushed | Accepted checkpoint | Explicitly staged only the 13 H4-2/H4-5/H4-ledger files, verified the staged names, committed `34fa2c7f Harden Qin static admission gates`, confirmed those paths were clean afterward, and pushed `HEAD:master` to `origin` with command output suppressed to avoid exposing the remote URL. Remaining dirty files are intentionally excluded, including backend-jvm H4-3/H4-4 files that still need their own split plus many generated/parser/runtime historical artifacts. | H4-6b small 0.0% -> 100.0%; H4-6 accepted 40.0% -> 60.0%; H4 major 88.0% -> 90.0%; overall 98.8% -> 99.0% |
| 2026-08-02 16:33 +08:00 | H4-6c backend-jvm hard-gate unit committed and pushed | Accepted | Targeted backend validation passed: `QinJvmSourceUnknownElementAccessHardGateSmokeTestMain OK`, `QinJvmUnknownElementAccessHardGateSmokeTestMain OK`, `QinJvmStaticMapElementAccessSmokeTestMain OK`, `QinJvmSourceUnknownMemberAccessHardGateSmokeTestMain OK`, `QinJvmSourceUnknownMethodCallHardGateSmokeTestMain OK`, `QinJvmGeneratedTypedReceiverAbstractSlotSmokeTestMain OK`, and `QinJvmDynamicSemanticWarningSmokeTestMain passed.` `git diff --check` passed for the backend H4 candidate files. Explicitly staged the backend H4 files, committed `77c160a3 Harden JVM static semantic gates`, and pushed `HEAD:master` to `origin` with output suppressed to avoid exposing the remote URL. | H4-6c small 0.0% -> 100.0%; H4-3 accepted 85.0% -> 100.0%; H4-4 accepted 85.0% -> 100.0%; H4-6 accepted 60.0% -> 100.0%; H4 major 90.0% -> 100.0%; overall 99.0% -> 100.0% |
