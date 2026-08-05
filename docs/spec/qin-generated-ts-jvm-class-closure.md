# Qin Generated TS JVM Class Closure Ledger

Last updated: **2026-08-06 00:22 +08:00**.

## Goal

Prove the standard Qin path for generated Java/Slime/Subhuti TypeScript:

```text
Java/Qin static source
  -> generated TypeScript ESM package
  -> Qin/Slime AST
  -> Qin IR
  -> JVM module/declaration `.class`
  -> strict OVS/CSSTS runtime transform
```

This is a fresh H5 denominator. H3 generated-parser transform hot-path closure
and H4 static-admission hard gates are historical evidence only. Their terminal
`100.0%` values must not be reused for H5 progress.

## Product Direction

Qin is moving toward a Kotlin-like compiler experience: static semantics first,
target-aware, JVM `.class` capable, fullstack-aware, and cache/incremental
friendly. Qin is not required to support all ESM/TypeScript/npm dynamic syntax.
Generated TypeScript from Java/Slime/Subhuti must preserve Java's static intent.
If generated output looks dynamic, the compiler must prove fixed receiver,
member, arity, owner, and lowering facts, or fail at the owning layer.

The active route is abstract grammar/AST/IR/compiler facts. Do not implement
language support with first-letter checks, word checks, source-text allowlists,
sample-specific scanners, fallback parsers, legacy handwritten parser
execution, dynamic runtime compatibility, or caller-side rewrites.

Generated AST/CST normalization adapters are part of the static compiler
boundary. They must be idempotent and driven by static node/field facts. Do not
normalize generated nodes by repeatedly blind-scanning every possible public
field on every visit; repeated normalization must return the already-normalized
node, and child traversal should use a fixed AST field table for the node kind.
If a required generated field is missing from the table, add that field to the
static table or generated accessor contract instead of falling back to dynamic
property enumeration in the hot path.
The adapter shape should be table-driven: field reads belong in a fixed
field-reader registry whose entries use direct generated-field accessors, and
node traversal belongs in a fixed node-type child-field table. Do not grow a
long per-token/per-word/per-field if-else chain or route generated class
instances through string-selected bracket reads.

The 2026-08-04 H5-2bt broad validation corrected one wrong diagnostic route:
the generated `SubhutiLookaheadPlan.NO_MATCH` interface constant was not null.
The real prior NPE was nullable conditional lowering that inferred a primitive
numeric target for `unknown ? null : boxedNumeric`, then unboxed the null branch.
The accepted rule is that conditional expressions with a null branch and a
reference/boxed numeric branch keep a nullable reference target until an
explicit non-null primitive target owns the coercion. The same validation also
confirmed that generated Qin/Java class values on the JVM module-class path
must preserve JavaScript class `typeof` semantics: a source-visible generated
class value is a static class value and `typeof ClassName` must return
`"function"`, not `"object"`.

## Acceptance Conditions

H5 reaches `100.0%` only when current evidence proves all of these:

1. Generated Slime/Subhuti/Qin TypeScript packages parse through the standard
   Qin/Slime parser into AST and lower into Qin IR without legacy parser
   fallback.
2. Real generated-parser-adjacent module graphs compile through the standard
   JVM module-class and declaration-class backend.
3. Export slots, default exports, re-export aliases, class literals,
   constructors, and generated-local class metadata execute through the JVM
   class/module path.
4. Generated TypeScript static admission remains active with all
   `.call/.apply/.bind` occurrences admitted only by owner contracts and with
   zero legacy dynamic-wrapper admissions.
5. OVS/CSSTS generated-parser runtime transforms pass under
   `-Dqin.dynamicSemanticMode=error` through the same standard path.
6. Unsupported third-party or generated dynamic shapes stop with structured
   static-admission diagnostics and are reported for user choice instead of
   broadening runtime compatibility.
7. Windows Qin/JVM validations run sequentially.
8. Current-unit code/docs/tests are committed and pushed when practical, with
   unrelated dirty files excluded.

## Three-Layer Progress Scales

Overall active goal: **78.0%** for **H5 generated TS -> Qin IR -> JVM class
closure**. Its `100.0%` gate is all H5 acceptance conditions passing and the
current-unit git hygiene gate closed.

Completed major item: **100.0%** for **H5-1 new denominator, scope, and baseline
evidence**. Its `100.0%` gate is this ledger committed or otherwise recorded,
the H5 smoke/probe sequence selected, and the first sequential baseline command
started from the fresh denominator.

Completed small item: **100.0%** for **H5-1a ledger and existing evidence
classification**. Its checkpoints are: read required skills 20%, inspect H3/H4
and older generated TS/module-class ledgers 40%, locate existing H5 smoke/probe
owners 60%, write this fresh ledger 80%, run the first focused H5 baseline or
record the exact blocker 100%.

Active major item: **40.0%** for **H5-2 generated TS ESM packages parse/lower
with owner-contract admission**. Its `100.0%` gate is current sequential
evidence that generated Slime and Qin parser TS ESM packages pass static
admission with contract wrappers and zero legacy admissions.

Blocked small item: **55.0%** for **H5-2a generated Qin Parser TS ESM baseline**.
Its checkpoints are: select the standard Qin parser TS smoke 25%, run it under
strict dynamic semantic mode 55%, confirm owner-contract/legacy counts 75%,
record the evidence in this ledger 90%, and classify the next H5-2 gate 100%.
It is now blocked at the standard JVM module-class emit boundary by a varargs
lowering failure in generated `JavaParserStaticEnhanced.ts`.

Completed small item: **100.0%** for **H5-2b generated JavaParserStaticEnhanced
varargs `.class` backend fix**. Its checkpoints are: reproduce and isolate the
failing generated call/IR owner 20%, locate the backend/lowerer type owner 40%,
implement the varargs type-preserving fix 65%, focused validation of that shape
80%, rerun `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` 95%, and ledger/git
capture 100%.

Completed small item: **100.0%** for **H5-2c generated `SourceLocation`
receiver/static type closure**. Its checkpoints were: reproduce and isolate the
failed `location.type()` receiver/type owner 20%, locate the omitted
`slime-ast` source-root closure that widened `SourceLocation` to `Object`/`any`
40%, implement source-root/static metadata closure 65%, focused validation of
generated `SourceLocation` return/local/accessor types 80%, rerun
`QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the former
`location.type()` blocker is gone 95%, and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2d Java `Object.getClass()` static
declaration-class intrinsic**. Its checkpoints were: reproduce/isolate the
`getClass/0` receiver owner 20%, locate the JVM declaration-class backend
intrinsic owner 40%, implement fixed `Object.getClass()` bytecode emission 65%,
focused backend smoke proving parameter and field/property receivers 80%, rerun
`QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the former
`getClass/0` blocker is gone 95%, and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2e Java `java.lang.reflect.Method`
static type/reflection method path**. Its checkpoints were: reproduce/isolate
the generated `java.lang.reflect.Method` return/local widening to `any` 20%,
locate the Java AST semantic/lowerer/TS backend owner 40%, implement a
Qin-owned static type/facade preservation path for `java.lang.reflect.Method`
65%, focused generator/lowering smoke proving `findMethod()` and locals are not
`any` 80%, rerun `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the
former `setAccessible/1` blocker is gone 95%, and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2g generated Qin Parser TS
string-to-double argument coercion**. Its checkpoints were: reproduce/isolate
the `Unsupported declaration argument coercion: STRING -> DOUBLE` failure with
owner/method/argument context 20%, locate whether the owner is Java-to-TS
typing, Qin TS lowering, overload selection, or JVM argument coercion 40%,
implement the owning static type/overload/coercion fix without dynamic fallback
65%, focused smoke proving the exact call shape compiles statically 80%, rerun
`QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the former
`STRING -> DOUBLE` blocker is gone 95%, and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2h generated static-context `this`
overload bridge**. Its checkpoints were: reproduce/isolate the
`SubhutiCreateToken.createRegToken` dynamic helper blocker with exact generated
method body 20%, locate the owner in generated wrapper/static method lowering
and JVM overload resolution 40%, implement static method/overload bridge fix
without dynamic fallback 65%, focused smoke proving static `this.__qin_overload`
compiles as `invokestatic` 80%, rerun
`QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the former
`createRegToken` blocker is gone 95%, and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2i generated boxed numeric local
argument matching**. Its checkpoints were: reproduce/isolate the generated
`Builder.index(Integer)` dynamic global failure 20%, locate boxed numeric
argument matching/coercion in `QinJvmDeclarationClassEmitter` 40%, implement
target-specific boxed numeric matching and boxing 65%, focused smoke
`QinJvmLocalBoxedNumericArgumentSmokeTestMain OK` 80%, rerun
`QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the former `index/1`
blocker is gone 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2j generated boxed Boolean local
argument matching**. Its checkpoints were: reproduce/isolate
`Builder.hasLineBreakBefore(Boolean)` 20%, locate boxed Boolean argument
matching 40%, implement boxed Boolean admission 65%, focused smoke
`QinJvmLocalBoxedBooleanArgumentSmokeTestMain OK` 80%, rerun the broad smoke
until the former blocker is gone 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2k target-aware
`String.charAt(...)` numeric argument matching**. Its checkpoints were:
reproduce/isolate `isIdentifierPart(charAt(...))` 20%, locate pre-target
overload matching as the owner 40%, implement expression-aware static matching
for `__QinJavaLangString.charAt(...)` 65%, focused smoke
`QinJvmStaticCharAtNumericArgumentSmokeTestMain OK` 80%, rerun the broad smoke
until the former blocker is gone 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2l null literal reference parameter
matching**. Its checkpoints were: reproduce/isolate `SubhutiStaticGrammar.node`
with `null` reference arguments 20%, locate local static argument matching 40%,
admit `QinIrNullLiteral` only for reference-like target parameters 65%,
focused smoke `QinJvmStaticNullReferenceArgumentSmokeTestMain OK` 80%, rerun
the broad smoke until the former blocker is gone 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2m boxed Boolean to primitive
boolean matching**. Its checkpoints were: reproduce/isolate
`Fact.pending(boolean)` with `getOrDefault(..., false)` 20%, locate primitive
boolean target matching 40%, admit Boolean-like arguments to primitive boolean
65%, focused smoke `QinJvmLocalBoxedBooleanToPrimitiveSmokeTestMain OK` 80%,
rerun the broad smoke until the former blocker is gone 95%, and ledger capture
100%.

Completed small item: **100.0%** for **H5-2n target-aware collection-get and
JDK assignability argument matching**. Its checkpoints were:
reproduce/isolate `analyzePaths(grammar, __qin_collection_get__(...), ...,
new HashSet(), context)` 20%, locate target parameter and constructor/interface
assignability owners 40%, implement expression-aware matching with
`declarationIndex` plus reflected Java assignability 65%, focused smoke
`QinJvmStaticCollectionGetTargetArgumentSmokeTestMain OK` 80%, rerun
`QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the former
`analyzePaths(...)` blocker is gone 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2o generated conditional static
method return-type inference**. Its checkpoints were: reproduce/isolate
`PathAnalysis.dynamicResult` static method type failure 20%, locate static
method return inference and generated overload implementation resolution 40%,
implement the owning static return-type fix without dynamic fallback 65%,
focused smoke `QinJvmStaticGeneratedOverloadReturnTypeSmokeTestMain OK` 80%,
rerun `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the former
`dynamicResult` blocker is gone 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2p erased generic local static
argument matching**. Its checkpoints were: reproduce/isolate
`CandidateSet.from(...)` failing on same raw local class with incompatible
generated type arguments 20%, locate local generic argument matching as the
owner 40%, model JVM-erased generic admission for same raw local class 65%,
focused smoke `QinJvmStaticErasedGenericArgumentSmokeTestMain OK` 80%, rerun
the broad smoke until `CandidateSet.from` is gone 95%, and ledger capture
100%.

Completed small item: **100.0%** for **H5-2q dotted Java owner to generated
local owner alias matching**. Its checkpoints were: reproduce/isolate
`RuleTransducerIndex.get/1` with argument owner
`com.subhuti.parser.SubhutiAdaptiveDecisionGraph$RuleKey` against generated
owner `com_subhuti_parser_SubhutiAdaptiveDecisionGraph$RuleKey` 20%, locate
declaration-index-aware local instance matching as the owner 40%, implement
local argument matching through declaration index aliases 65%, focused smoke
`QinJvmLocalDottedAliasArgumentMatchSmokeTestMain OK` 80%, rerun the broad
smoke until `RuleTransducerIndex.get/1` is gone 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2r local static Java functional
interface argument matching**. Its checkpoints were: reproduce/isolate
`decisionItemSccFiniteEpsilonClosure(...)` failing on
`JavaFunctionalObject -> java.util.Comparator` 20%, locate local static
argument matching as the owner 40%, admit generated Java functional adapters to
Java functional-interface parameters 65%, focused smoke
`QinJvmStaticFunctionalObjectToLocalFunctionalInterfaceSmokeTestMain OK` 80%,
rerun the broad smoke until `decisionItemSccFiniteEpsilonClosure` is gone 95%,
and ledger capture 100%.

Completed small item: **100.0%** for **H5-2s local static typed array argument
matching and emission**. Its checkpoints were: reproduce/isolate
`MutableNode.split(int[])` static method type failure 20%, locate target-aware
local static array matching/emission as the owner 40%, implement typed array
parameter emission for array literals and static zero/default `Array.from`
factories plus `int[]` array identity 65%, focused smoke
`QinJvmStaticLocalIntArrayArgumentSmokeTestMain OK` 80%, rerun the broad smoke
until `MutableNode.split` is gone 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2t generated finite decision compiler
static method matching**. Its checkpoints are: reproduce/isolate
`SubhutiFiniteDecisionCompiler.addEdge(...)` static method failure 20%, locate
whether the owner is method name aliasing, argument type matching, array/list
targeting, or declaration index lookup 40%, implement the owning static backend
rule without dynamic fallback 65%, focused smoke proving the abstract call
shape 80%, rerun `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the
former `addEdge` blocker is gone 95%, and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2ac generated static argument
effective type evidence**. Its checkpoints were: reproduce the static argument
evidence gap 20%, locate `as Pattern` and instance method return evidence
owners 40%, lower `TSAsExpression` to `QinIrCastExpression` and preserve
effective argument typing 65%, focused smoke
`QinJvmStaticCallEffectiveArgumentTypeSmokeTestMain OK` 85%, and broad smoke
crossing the old argument-type blocker before exposing the next switch-case
owner 100%.

Completed small item: **100.0%** for **H5-2ad nested switch `CaseBlock`
ownership**. Its checkpoints were: reproduce the broad multiple-default
failure in `JavaParserStaticEnhanced.executeStaticGate` 20%, locate the owner
in CST-to-AST case collection rather than backend switch semantics 40%,
implement direct `CaseBlock/CaseClauses`-scoped case collection 65%, focused
AST validation `SlimeNestedSwitchCaseBlockAstSmokeTestMain OK` 85%, and broad
strict validation crossing the old `Declaration switch statement cannot contain
multiple defaults` blocker before exposing the next static receiver type
blocker 100%.

Completed small item: **100.0%** for **H5-2ae generated `SubhutiCst.getName()`
receiver static type preservation**. Its checkpoints are: reproduce/isolate
`CustomCstToAst.createPrimaryExpressionAst` dynamic `getName/0` receiver
failure 20%, locate whether the owner is Java-to-TS local typing, Qin Slime AST
lowering, method parameter metadata, or declaration-class local type propagation
40%, implement the owning static type fix without dynamic helper fallback 65%,
focused smoke proving `SubhutiCst` receivers retain fixed `getName/0` member
typing 85%, rerun `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the
former `createPrimaryExpressionAst/getName` blocker is gone 95%, and
ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2af primitive array argument emitted
against reference array descriptor**. Its checkpoints are: reproduce/isolate
the `SubhutiAdaptiveDecisionGraph$Builder.compileBareNode` `double[]` versus
`Object[]` VerifyError 20%, locate whether the owner is argument matching,
varargs array packing, static call descriptor selection, or primitive array
coercion 40%, design the owning static emission rule without widening through a
dynamic helper 55%, implement the backend/lowerer fix 70%, focused smoke proving
the abstract primitive-array/reference-array call shape loads under JVM
verification 85%, rerun `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until
the former `compileBareNode` VerifyError is gone 95%, and ledger/git capture
100%.

Accepted small item: **95.0%** for **H5-2ag generated/native enum identity at
dependency-session runtime**. Its checkpoints are: reproduce/isolate the
`SubhutiStaticGrammarPlan$Kind` generated enum versus Java enum
`ClassCastException` 20%, locate whether the owner is generated Java facade
aliasing, metadata decode return typing, enum field/static access emission, or
dependency-session classloader identity 40%, design the owning static enum
identity rule without fallback conversion 55%, implement the fix 70%, focused
smoke proving generated/native enum values cannot cross-cast incorrectly 85%,
rerun `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` until the former enum
ClassCastException is gone 95%, and ledger/git capture 100%.

Accepted small item: **95.0%** for **H5-2ah generated constructor boxed numeric
Object argument coercion**. Its checkpoints are: reproduce/isolate `Integer ->
Double` in `BranchDefinition.<init>` 20%, locate numeric boxing/storage owner
40%, design the static numeric rule 55%, implement the backend coercion fix 70%,
focused and adjacent smokes 85%, broad strict smoke crosses the blocker 95%, and
ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2as generated Java SDK
`IntStream.toArray()` real `int[]` closure**. Its checkpoints were:
reproduce/isolate the `ArrayStoreException`/dynamic-global/static-array blocker
20%, locate Java SDK source/runtime/lowerer/JVM builtin owners 40%, implement
source-owner `__qin_java_new_array__("[I", length)` plus runtime host binding
65%, register the helper as a static builtin and infer descriptor-literal array
types 80%, focused smoke proving direct `int[]` writes and
`mapToInt(...).toArray()` return real `int[]` 92%, broad round-trip crossing
the former helper/global/member-set/stack-shape blockers 98%, and ledger capture
100%.

Completed small item: **100.0%** for **H5-2at generated Java primitive local
default closure in `strongConnect`**. Its checkpoints were: reproduce/isolate
the runtime `NullPointerException` in `SubhutiDecisionProgram.strongConnect`
20%, locate Java definite-assignment `int member;` emitted as
`let member: number = null` 40%, design the source-owned Java primitive local
default rule 55%, implement `QinJavaAstIrLowerer` primitive defaults 70%,
focused smoke `QinJsBackendJavaPrimitiveLocalDefaultSmokeTestMain OK` 85%,
regenerate parser TS and broad strict round-trip crosses the former
`strongConnect` NPE 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2au generated static metadata
typed-array store VerifyError**. Its checkpoints were: reproduce/isolate the
wrapper `QinModule50.__qin_chunk_4` verifier failure 20%, locate typed
generated static object-slot argument casts as the owner 40%, design the fixed
declaration-index descriptor/cast rule 55%, implement 70%, focused smoke
`QinCfaGeneratedStaticObjectSlotArgumentCastSmokeTestMain OK` 85%, broad strict
round-trip crossed the former verifier blocker 95%, and ledger/git capture
100%.

Completed small item: **100.0%** for **H5-2av Java array constructor method
reference emission**. Its checkpoints were: reproduce/isolate generated
`new null` for Java `int[][]::new` 20%, locate the Java-to-TS backend array
constructor method-reference owner 40%, confirm the source owner already had
the correct `emitJavaArrayConstructorMethodReference(...)` logic and stale
classes needed recompilation 55%, recompile/regenerate through the standard
source-owned path 70%, focused smokes
`QinJsBackendJavaArrayCreationSmokeTestMain OK` and
`QinJsBackendJavaArrayConstructorMethodReferenceSourceSmokeTestMain OK` 85%,
generated parser TS contains `__qin_java_new_array__("[[I", ...)` and no
`new null` 95%, and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2aw generated-local owner
descriptors and constructor owners**. Its checkpoints were: reproduce/isolate
the generated `CandidateSet` being cast to original Java
`com.subhuti.parser.SubhutiLookaheadPlan$CandidateSet` 20%, locate the owner in
JVM descriptor/constructor-owner resolution rather than token or generated TS
text 40%, implement generated-local owner resolution for declaration-class
descriptors/constructors and CFA static parameter descriptors 65%, focused
smoke `QinCfaGeneratedLocalOwnerReferenceSmokeTestMain OK` 80%, broad strict
round-trip `QinGeneratedTsSubhutiStaticMetadataRoundTripSmokeTestMain OK` 95%,
and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2bg generated `CandidateSet`
constructor static array production closure**. Its checkpoints were:
reproduce/isolate the module 72 static-field constructor failure 20%, locate
the real CFA argument shape and constructor parameter type 35%, implement
static array-production lowering for `Array.from`, Java `Arrays.copyOf`, local
array assignment, and contextual generated constructor emission 55%, focused
smoke coverage for direct and lowered static array factory/copy shapes 75%,
focused coverage for lowered `Global.__qin_call_method__(Array,"from",...)`
into `java.lang.Object[]` generated constructors 82%, broad strict module 72
emit crosses the blocker 95%, and ledger/git capture 100%.

Completed small item: **100.0%** for **H5-2bs nullable conditional numeric
lowering closure**. Its checkpoints were: disprove the incorrect interface
constant-null hypothesis 20%, locate the actual null-branch unbox bytecode in
conditional expression lowering 40%, design the nullable reference conditional
target rule 55%, implement the declaration-class branch coercion fix 70%,
focused smoke `QinJvmInterfaceConstantPrimitiveLocalSelectionSmokeTestMain OK`
85%, broad strict run crossing the former NPE 95%, and ledger capture 100%.

Completed small item: **100.0%** for **H5-2bt generated class `typeof`
function semantics**. Its checkpoints were: reproduce the package export
assertion where `typeof SlimeCstToAstUtils === "function"` was false 20%,
locate the owner in `JavaEsmGlobal.typeOf(...)` for JVM module-class class
values 40%, implement `Class<?>` as JS-like `"function"` 60%, focused smoke
`QinJsClassTypeofFunctionSmokeTestMain OK` 75%, sequential strict broad
`QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK` with static admission
wrappers `1526`, contract wrappers `1526`, legacy wrappers `0` 95%, and
ledger/skill capture 100%.

Completed small item: **100.0%** for **H5-2bu durable capture and next-phase
classification**. Its checkpoints were: broad result captured 30%, root-cause
lessons classified 50%, ledger/compatibility model/skill updated 70%, git
hygiene classification recorded 85%, and next validation cursor selected 100%.

Completed major item: **100.0%** for **H5-3 generated-parser-adjacent
module/declaration class graph compilation**. Its `100.0%` gate is current
sequential evidence that a real generated-parser-adjacent graph such as the
CSSTS parser entry compiles through module classes and declaration classes
under strict dynamic semantic mode.

Accepted small item: **95.0%** for **H5-3a CSSTS parser declaration-class probe
admission**. Its checkpoints were: select the standard
`QinModuleDeclarationClassCompileProbeMain` entry 20%, reproduce the first
module graph blocker 30%, locate Qin package resolution as the owner 40%,
implement project `packageOverrides` resolver support 55%, add scoped `file:`
dependency resolver support with focused smoke 75%, correct the demo package
override for generated Qin parser TS 85%, pass runtime-feature static admission
95%, and declaration-class probe success plus ledger/git capture 100%. It
crossed the Static Admission Gate after the Qin-owned static `subhuti` facade
and Java SDK static-shape cleanup, then exposed the next declaration-class
blocker in `CssTsTokenConsumer.ts`.

Completed small item: **100.0%** for **H5-3b module const object property
static type closure**. Its checkpoints were: reproduce the strict
`CssTsContextualKeywordTypes.Css` dynamic member-get failure 20%, locate the
owner in module binding/object-literal static type propagation 40%, design the
generic non-computed fixed object literal property rule 55%, implement the
frontend declaration lowerer fix 70%, focused smoke
`QinJvmModuleConstObjectPropertyStaticSmokeTestMain OK` 85%, sequential broad
`QinModuleDeclarationClassCompileProbeMain OK modules=174 declarations=742`
crossing module 171 `CssTsTokenConsumer.ts` 95%, and ledger/skill/git hygiene
capture 100%.

Completed major item: **100.0%** for **H5-4 JVM export slots, aliases, class
literals, and generated-local metadata execution**. Its `100.0%` gate is the
current strict backend smoke group proving export-slot member access, type
aliases, Java class literal aliases, typed receivers, and generated-local class
metadata execute on the JVM class/module path.

Completed small item: **100.0%** for **H5-4b generated/native enum alias
execution closure**. Its checkpoints were: reproduce native enum alias
selection under the standard Slime/Subhuti classpath 20%, locate the owner in
declaration-class static owner resolution 40%, prove reflection fallback was
selected before generated enum helpers 55%, implement the generic
generated-local enum owner rule 75%, focused
`QinJvmGeneratedEnumLocalAliasValuesSmokeTestMain OK` 95%, and strict backend
smoke group success plus durable capture 100%.

Completed major item: **100.0%** for **H5-5 OVS/CSSTS generated-parser transform
execution under strict static mode**. Its `100.0%` gate is focused transform
probes and the broader generated TS Slime CSSTS compiler smoke passing through
the standard path under `-Dqin.dynamicSemanticMode=error`.
Its checkpoints were: H5-5a strict transform baseline and ASI closure 25%,
H5-5b broad strict generated TS Slime CSSTS compiler smoke and Java SDK alias
closure 55%, H5-5e no-arg varargs bridge owner closure 65%, H5-5f relative
ESM named helper import/export slot closure 72%, H5-5g generated AST
normalization/runtime budget closure 95%, and ledger/skill capture 100%.

Completed small item: **100.0%** for **H5-5a strict OVS/CSSTS transform
baseline**. Its checkpoints were: run the focused strict probe 20%, locate the
owning layer 40%, design the owning grammar/runtime fix 55%, implement the
focused fix 70%, rerun `twoConstDirect` and `importTwoConstDirect` focused
validation 85%, run the broader generated TS Slime CSSTS transform smoke 95%,
and update ledger/skill/git hygiene 100%.

Completed small item: **100.0%** for **H5-5b generated TS Boolean/Token method
static closure**. Its checkpoints were: reproduce the generated Boolean/token
method divergence 20%, locate the owning lowerer/backend layer 40%, design the
Java SDK alias static-facade rule 55%, implement the lowerer fix 70%, focused
Boolean/token validation 85%, broad transform/compiler validation 95%, and
ledger/skill/git hygiene 100%.

Completed small item: **100.0%** for **H5-5e generated no-arg varargs bridge
selection**. Its checkpoints were: reproduce the no-arg generated parser call
failure 20%, locate reflection candidate ordering 40%, prove synthetic bridge
methods were selected ahead of the real source method 55%, implement source
method priority 75%, focused and adjacent smokes 95%, and durable capture 100%.

Completed small item: **100.0%** for **H5-5f relative ESM named helper import
slot resolution**. Its checkpoints were: reproduce unresolved helper access
20%, locate declaration lowering/module binding ownership 40%, preserve static
export slot gets for imported named helpers 70%, focused relative import smoke
95%, and durable capture 100%.

Completed small item: **100.0%** for **H5-5g generated AST normalization
runtime budget closure**. Its checkpoints were: reproduce default 30 second
timeout 20%, locate the hot runtime boundary 40%, identify repeated
`normalizeGeneratedAstChildren -> readGeneratedField` as the owner 55%,
implement idempotent normalization and a static node field table 80%, rerun the
strict broad smoke under the default 30 second budget 95%, and ledger/skill
capture 100%.

Active major item: **60.0%** for **H5-6 git hygiene, durable capture, and
handoff readiness**. Its `100.0%` gate is the current coherent unit staged,
committed, and pushed when practical, with unrelated dirty files left excluded
and validation evidence recorded.
Its checkpoints are: classify dirty paths 20%, verify intended diffs and
abstract static-table shape 35%, rerun focused build and default strict smoke
45%, durable ledger/skill capture 60%, stage intended files only 75%, commit
the coherent unit 90%, and push/report or record the exact push blocker 100%.

Active small item: **85.0%** for **H5-6a current-unit validation and git
hygiene**. Its checkpoints are: classify dirty paths 20%, verify intended diffs
40%, refactor generated AST/CST normalization to table-driven field/node rules
55%, rerun package build and default strict OVS smoke 75%, update ledger/skills
85%, stage intended files 90%, commit 95%, and push/report 100%.

## H5 Weighted Plan

| ID | Gate | Weight | Accepted | State | Evidence |
|---|---|---:|---:|---|---|
| H5-1 | Fresh H5 denominator and baseline sequence selected | 10% | 100% | Accepted | Required skills were read; H3/H4 and older generated TS/module-class ledgers were inspected; existing smoke owners include `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain`, `QinJavaProjectQinParserTsEsmFilesSmokeTestMain`, `QinModuleDeclarationClassCompileProbeMain`, `QinGeneratedTsSlimeCsstsCompilerSmokeTestMain`, and JVM export/class-literal smokes. First sequential baseline passed: generated Slime Parser TS ESM static admission reports 1264 contract wrappers and 0 legacy wrappers. |
| H5-2 | Generated TS ESM packages parse/lower with owner-contract admission | 20% | 40% | In progress | Generated Qin Parser TS ESM smoke now runs the same static-admission audit and advances into real JVM module/declaration class compilation. Earlier generated static backend/type blockers through H5-2bt are closed by focused smokes, including `QinJvmStaticCallEffectiveArgumentTypeSmokeTestMain OK`, `SlimeNestedSwitchCaseBlockAstSmokeTestMain OK`, `QinJvmInheritedOverrideParameterStaticSmokeTestMain OK`, `QinJvmObjectArrayPrimitiveSemanticArgumentSmokeTestMain OK`, `QinJvmGeneratedConstructorNumericObjectArgumentSmokeTestMain OK`, `QinJvmJavaStreamToArrayIntFunctionArrayFactorySmokeTestMain OK`, `QinJsBackendJavaPrimitiveLocalDefaultSmokeTestMain OK`, `QinCfaGeneratedStaticObjectSlotArgumentCastSmokeTestMain OK`, `QinJsBackendJavaArrayConstructorMethodReferenceSourceSmokeTestMain OK`, `QinCfaGeneratedLocalOwnerReferenceSmokeTestMain OK`, `QinJvmInterfaceConstantPrimitiveLocalSelectionSmokeTestMain OK`, and `QinJsClassTypeofFunctionSmokeTestMain OK`. Sequential strict broad `QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK` now reports generated Qin parser TS static admission wrappers `1526`, contract wrappers `1526`, legacy wrappers `0`. |
| H5-3 | Generated-parser-adjacent module/declaration class graph compiles | 25% | 100% | Accepted | Sequential `QinModuleDeclarationClassCompileProbeMain` against the CSSTS parser entry now passes under `-Dqin.dynamicSemanticMode=error`: `OK modules=174 declarations=742`. Evidence includes project package override precedence, Qin-owned static `subhuti` facade, Java SDK static-shape cleanup, fixed module/export const object literal property lowering, and module 171 `CssTsTokenConsumer.ts` compiling after the former `CssTsContextualKeywordTypes.Css` dynamic member-get blocker. |
| H5-4 | JVM export slots, aliases, class literals, and generated-local metadata execute | 20% | 100% | Accepted | Sequential strict backend smoke group passed: `QinJvmStaticExportSlotMemberSmokeTestMain OK`, `QinJvmModuleExportSlotTypeAliasSmokeTestMain OK`, `QinJvmJavaClassLiteralAliasSmokeTestMain passed`, `QinJvmClassLiteralSimpleNameStaticMethodSmokeTestMain OK`, `QinJvmJavaClassInfoGeneratedLocalInterfaceSmokeTestMain OK`, `QinJvmGeneratedInterfaceMetadataSmokeTestMain passed`, `QinJvmGeneratedEnumMetadataStaticSmokeTestMain OK`, `QinJvmGeneratedEnumLocalAliasValuesSmokeTestMain OK`, and `QinJvmGeneratedEnumValueOfSmokeTestMain passed`. Generated-local enum owners now keep `values()/valueOf()` on Qin's static generated enum helper path instead of falling through to native Java enum reflection. |
| H5-5 | OVS/CSSTS generated-parser transform executes under strict static mode | 15% | 100% | Accepted | H5-5a accepted the strict transform baseline and adjacent top-level declaration ASI closure. H5-5b accepted the generated Java SDK alias static-facade closure. H5-5e/H5-5f accepted no-arg varargs bridge ordering and relative ESM named helper slot resolution. H5-5g accepted the OVS generated AST normalization budget fix: default strict `QinGeneratedTsSlimeOvsTransformSmokeTestMain OK` now runs with module-class disk cache hit, dependency session ready at `+4088ms`, run batch done at `+7814ms`, and wrapper complete at `+9289ms`. |
| H5-6 | Git hygiene, durable capture, and handoff readiness | 10% | 60% | In progress | Dirty paths were classified across `qin` and `ovsjs`; the current H5-5g/H5-6a intended diff was narrowed to the generated runtime adapter, strict OVS smoke diagnostic harness, and ledger/skill updates. The adapter was further refactored to table-driven field-reader and node-child-field registries, then revalidated with `qin-runtime-core` build and default strict `QinGeneratedTsSlimeOvsTransformSmokeTestMain OK` with module-class disk cache hit and run batch done at `+9213ms`. Staging, commit, push/report remain. |
| **H5 Total** |  | **100%** | **84.0%** | In progress | H5-1, H5-3, H5-4, and H5-5 are accepted; H5-2 has accepted 40% of its 20% weight; H5-6 has accepted 60% of its 10% hygiene/capture gate. |

## Progress History

| Time | Step | Status | Evidence | Progress |
|---|---|---|---|---|
| 2026-08-02 16:36 +08:00 | H5 denominator opened | Accepted checkpoint | Required skills were read. H4 `qin-static-admission-hard-gates.md` is complete and explicitly points to a fresh H5 denominator. Historical H3/H4/older generated-TS ledgers were inspected and treated as evidence only. Existing standard-path smokes and probes were located for generated TS static admission, module/declaration class compilation, JVM export/class-literal behavior, and OVS/CSSTS transform execution. | H5-1a small 0.0% -> 60.0%; H5-1 major 0.0% -> 60.0%; H5 overall 0.0% -> 6.0% |
| 2026-08-02 16:39 +08:00 | H5-1 baseline selected and Slime Parser TS static admission passed | Accepted checkpoint | Sequential standard-path validation passed with `java -Dfile.encoding=UTF-8 -Dqin.dynamicSemanticMode=error ... QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain`: generated ESM package `@qin/generated-slime-parser-ts`, static admission wrappers `1264`, contract wrappers `1264`, legacy wrappers `0`, legacy reasons `{}`, and `QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK`. The initial unquoted PowerShell `-Dfile.encoding=UTF-8` attempt failed before Java launched the smoke correctly and is not semantic evidence. | H5-1a small 60.0% -> 100.0%; H5-1 major 60.0% -> 100.0%; H5 overall 6.0% -> 10.0% |
| 2026-08-02 16:43 +08:00 | H5-2a generated Qin Parser TS ESM baseline exposed backend varargs blocker | Blocked, new small item opened | Added the same `QinGeneratedTsStaticAdmissionAudit` assertion/printing to `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` and recompiled that smoke. Sequential strict run advanced into real `QinJsPackageRunner` module-class compilation for 282 modules, then failed while emitting `@qin/generated-qin-parser-ts/com/slime/java/JavaParserStaticEnhanced.ts` with `Varargs parameter is not an array: java.lang.Object` from `QinCfaJvmClassFileBackend.emitArgumentsForVarArgs(...)`. This is an owning backend/lowerer type blocker, not a parser fallback or source workaround request. | H5-2a small 0.0% -> 55.0%; H5-2 major remains 0.0%; H5 overall remains 10.0%; opened H5-2b small 0.0% |
| 2026-08-02 16:50 +08:00 | H5-2b generated static varargs backend fix accepted; next receiver-owner blocker exposed | Accepted blocker removal, new small item opened | `QinCfaJvmClassFileBackend.runtimeClassForType(...)` now preserves `QinIrTypeRef.CLASS` array binary names such as `java.lang.Object[]` as JVM array classes instead of widening them to `Object.class`. Added and passed `QinCfaGeneratedStaticVarargsSmokeTestMain OK`, which constructs a generated declaration-index static `Object...` method and compiles a `.class` caller through the standard backend. Sequential rerun of `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` advanced past the varargs failure and emitted all 282 module classes, then hit a new strict declaration-class blocker: `SlimeAstCreateUtils.__qin_overload_createSyntaxToken_3_2` calls `type()` on a receiver lowered as `java.lang.Object`. | H5-2b small 0.0% -> 100.0%; H5-2 major 0.0% -> 20.0%; H5 overall 10.0% -> 14.0%; opened H5-2c small 0.0% |
| 2026-08-02 17:12 +08:00 | H5-2c SourceLocation static type closure accepted; Object.getClass blocker opened | Accepted blocker removal, new small item opened | Added focused `QinJavaProjectGeneratedSourceLocationTypeSmokeTestMain`, which proves wildcard-imported Java `SourceLocation` stays precise through Java AST -> IR -> TS as method return `demo.ast.SourceLocation`, local declaration `demo.ast.SourceLocation`, record accessor owner `demo.ast.SourceLocation`, and no `let location: any =`. `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` now includes `slime/java-slime/slime-ast/src/main/java` in its source closure and asserts generated `SlimeAstCreateUtils.ts` uses `com_slime_ast_SourceLocation`. Sequential strict rerun advanced through the old `location.type()` dynamic blocker and reached generated module-class emit completion before exposing the next strict blocker: `JavaTokenConsumer.consumeIdentifierValue` calls `parser.getClass()` and the backend would emit `JavaEsmGlobal.__qin_call_method_array__`. | H5-2c small 95.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2d small 0.0% |
| 2026-08-02 17:23 +08:00 | H5-2d Object.getClass intrinsic accepted; java.lang.reflect.Method blocker opened | Accepted blocker removal, new small item opened | `QinJvmDeclarationClassEmitter.emitInstanceMethodCall(...)` now recognizes statically typed reference receivers calling `getClass/0` and emits fixed `Object.getClass(): Class` bytecode instead of dynamic helper calls. Focused `QinJvmJavaObjectGetClassIntrinsicSmokeTestMain OK` proves both parameter receivers and `this.field` property receivers return the real declaration class under strict dynamic mode. Sequential strict rerun of `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` crossed the former `parser.getClass()` blocker and emitted 376 module classes before exposing the next root blocker: Java source declares `java.lang.reflect.Method`, but generated `JavaTokenConsumer.ts` emits `findMethod(...): any` and `let laMethod: any`, causing `setAccessible/1` on a receiver typed as `java.lang.Object`. This must be fixed by preserving the Java reflection method type/facade through the generator/lowerer/backend, not by allowing dynamic calls on `Object`. | H5-2d small 95.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2e small 0.0% |
| 2026-08-02 17:43 +08:00 | H5-2e java.lang.reflect.Method static type path accepted; compile-only DSL blocker opened | Accepted blocker removal, new small item opened | `QinJsBackend.tsClassTypeName(...)` now maps `java.lang.reflect.Method` to the Qin Java SDK facade `__QinJavaLangReflectMethod`, `QinJavaSdkAliasSupport` resolves that facade back to `java.lang.reflect.Method`, and the Java SDK runtime exports a fail-hard reflective method object with fixed `setAccessible`/`invoke` members. Focused `QinJsBackendJavaMethodReturnTypeSmokeTestMain OK` proves Method return/parameter types are not `any`; focused `QinJavaProjectGeneratedReflectMethodTypeSmokeTestMain OK` proves Java source `Method findMethod(...)` and local `Method method` generate typed TS; focused `QinJvmJavaClassInfoStaticReflectionSmokeTestMain OK` proves JVM emission for `Class.getDeclaredMethod(name, ...Class[])`. Sequential strict rerun of `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` crossed the former `setAccessible/1` and `getDeclaredMethod` blockers, completed 376 module emits, and exposed the next dynamic-global blocker: `__QinSubhutiCompileOnlyDsl` in `JavaIdentifierParser.__qin_subhuti_raw_identifier`. | H5-2e small 95.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2f small 0.0% |
| 2026-08-02 17:58 +08:00 | H5-2f Subhuti compile-only DSL static owner accepted; string-to-double blocker opened | Accepted blocker removal, new small item opened | `QinJavaSdkAliasSupport` now maps generated facade `__QinSubhutiCompileOnlyDsl` to the real Java owner `com.subhuti.parser.SubhutiCompileOnlyDsl`, allowing the existing top-level alias/import lookup and static-call lowering path to preserve owner identity without parser-specific branches. Added and passed `QinSubhutiCompileOnlyDslStaticOwnerSmokeTestMain OK`, covering generated TS facade declarations plus `SubhutiCompileOnlyDsl.Or/Option/Many/AtLeastOne/gate` under strict dynamic mode. Sequential strict rerun of `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` crossed the former `__QinSubhutiCompileOnlyDsl` dynamic-global blocker, completed 376 module emits, and exposed the next root blocker: `Unsupported declaration argument coercion: STRING -> DOUBLE` from `QinJvmDeclarationClassEmitter` while compiling generated Qin Parser TS declaration classes. | H5-2f small 95.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2g small 0.0% |
| 2026-08-02 18:08 +08:00 | H5-2g local overload argument type resolution accepted; static-context overload bridge blocker opened | Accepted blocker removal, new small item opened | `QinJvmDeclarationClassEmitter` now selects local and inherited declaration overloads by type-aware argument score before choosing a same-arity method. Focused `QinJvmLocalOverloadArgumentTypeSmokeTestMain OK` proves a leaf call `lookahead("MUL", 2)` selects the inherited `(String,double)` overload instead of the nearer incompatible `(double,String)` overload. Sequential strict rerun of `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` crossed the former `STRING -> DOUBLE` blocker, completed 376 module emits, and exposed the next root blocker: generated `SubhutiCreateToken.createRegToken` called `this.__qin_overload_createRegToken_2_0(...)` from a static wrapper and would emit `JavaEsmGlobal.__qin_call_method_array__`. | H5-2g small 0.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2h small 0.0% |
| 2026-08-02 18:19 +08:00 | H5-2h static-context generated overload bridge accepted; dynamic global lookup blocker opened | Accepted blocker removal, new small item opened | `QinJvmDeclarationClassEmitter.emitInstanceMethodCall(...)` now treats static-method `this.method(...)` as a current-class static receiver before emitting any receiver bytecode, and declaration resolvers admit exact generated overload implementation names such as `__qin_overload_*` only when the local owner declares the same fixed name and arity. Focused `QinJvmStaticThisGeneratedOverloadBridgeSmokeTestMain OK` proves a generated static wrapper with `Object[] __qin_args` compiles `this.__qin_overload_createRegToken_2_0(__qin_args[0], __qin_args[1])` to a fixed static call and returns the expected value. Sequential strict rerun of `QinJavaProjectQinParserTsEsmFilesSmokeTestMain` crossed the former `createRegToken` blocker, completed 376 module emits, and exposed the next root blocker: `QinJvmDeclarationClassEmitter` would emit `JavaEsmGlobal.__qin_global__` for a generated global-looking owner lookup. | H5-2h small 0.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2i small 0.0% |
| 2026-08-02 19:18 +08:00 | H5-2i through H5-2n generated static argument matching blockers accepted; conditional return-type blocker opened | Accepted blocker removals, new small item opened | `QinJvmDeclarationClassEmitter` now performs target-aware local/static argument matching for boxed numeric arguments, boxed Boolean arguments, primitive boolean targets, null literals to reference targets, `__QinJavaLangString.charAt(...)` to numeric/char-like targets, `__qin_collection_get__(...)` to reference targets, and reflected Java assignability such as `HashSet -> Set`. Focused smokes passed: `QinJvmLocalBoxedNumericArgumentSmokeTestMain OK`, `QinJvmLocalBoxedBooleanArgumentSmokeTestMain OK`, `QinJvmStaticCharAtNumericArgumentSmokeTestMain OK`, `QinJvmStaticNullReferenceArgumentSmokeTestMain OK`, `QinJvmLocalBoxedBooleanToPrimitiveSmokeTestMain OK`, and `QinJvmStaticCollectionGetTargetArgumentSmokeTestMain OK`. Sequential strict broad smoke crossed the former `analyzePaths(...)` blocker and exposed the next root blocker: `Unknown declaration static method type: com_subhuti_parser_SubhutiGastCallsiteAnalysis$PathAnalysis.dynamicResult` while inferring a nested conditional expression result. | H5-2i small 0.0% -> 100.0%; H5-2j small 0.0% -> 100.0%; H5-2k small 0.0% -> 100.0%; H5-2l small 0.0% -> 100.0%; H5-2m small 0.0% -> 100.0%; H5-2n small 0.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2o small 0.0% |
| 2026-08-02 21:33 +08:00 | H5-2o through H5-2s generated Qin Parser TS static blockers accepted; finite decision compiler blocker opened | Accepted blocker removals, new small item opened | The declaration-class backend crossed `PathAnalysis.dynamicResult`, `CandidateSet.from`, `RuleTransducerIndex.get/1`, `decisionItemSccFiniteEpsilonClosure`, and `MutableNode.split`. Focused smokes passed: `QinJvmStaticGeneratedOverloadReturnTypeSmokeTestMain OK`, `QinJvmStaticErasedGenericArgumentSmokeTestMain OK`, `QinJvmLocalDottedAliasArgumentMatchSmokeTestMain OK`, `QinJvmStaticFunctionalObjectToLocalFunctionalInterfaceSmokeTestMain OK`, and `QinJvmStaticLocalIntArrayArgumentSmokeTestMain OK`. The latest sequential strict broad smoke now exposes `Unknown declaration static method: com_subhuti_parser_SubhutiFiniteDecisionCompiler.addEdge`. | H5-2o small 0.0% -> 100.0%; H5-2p small 0.0% -> 100.0%; H5-2q small 0.0% -> 100.0%; H5-2r small 0.0% -> 100.0%; H5-2s small 0.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2t small 0.0% |
| 2026-08-04 00:17 +08:00 | H5-2ad nested switch case ownership accepted; `SubhutiCst.getName()` receiver blocker opened | Accepted blocker removal, new small item opened | `SlimeSwitchStatementCstToAst.collectCasesFromCaseBlock(...)` now recurses only through `CaseBlock` and `CaseClauses`, so nested `SwitchStatement` nodes are lowered as consequent statements instead of leaking their inner `CaseClause`/`DefaultClause` into the parent switch. Focused smoke `SlimeNestedSwitchCaseBlockAstSmokeTestMain OK` proves the outer switch has two direct cases with one default and the nested switch keeps its own default. Sequential strict broad smoke with conservative JVM flags crossed the former multiple-default blocker, emitted 377 module classes, and exposed the next strict blocker: `CustomCstToAst.createPrimaryExpressionAst` calls `getName/0` on a receiver lowered as `java.lang.Object`. | H5-2ad small 95.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2ae small 0.0% -> 20.0% |
| 2026-08-04 00:49 +08:00 | H5-2ae inherited override parameter static typing accepted; primitive array descriptor blocker opened | Accepted blocker removal, new small item opened | `QinDeclarationIrLowerer` now propagates inherited override parameter types when same-name/arity superclass methods resolve statically, and `QinJvmDeclarationClassEmitter` completes placeholder override parameter types from the full declaration index before class compilation. Focused smoke `QinJvmInheritedOverrideParameterStaticSmokeTestMain OK` proves both same-module and external declaration-index override cases retain `SubhutiCst` receiver typing for `getName/0`. Sequential strict broad smoke crossed the former `CustomCstToAst.createPrimaryExpressionAst/getName` blocker and exposed the next root blocker: JVM `VerifyError` in `SubhutiAdaptiveDecisionGraph$Builder.compileBareNode(SubhutiGastNode,double)` because a primitive `double[]` is passed where the selected static call descriptor expects `Object[]`. | H5-2ae small 20.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2af small 0.0% -> 20.0% |
| 2026-08-04 01:17 +08:00 | H5-2af Object-array storage descriptor accepted; generated/native enum identity blocker opened | Accepted blocker removal, new small item opened | `QinJvmDeclarationClassEmitter` now separates semantic array element type from JVM storage component type: `java.lang.Object[]` keeps an `Object` storage component even when its type argument is primitive-like, while true primitive array binary names such as `int[]`, `double[]`, `[I`, or `[D` still emit primitive arrays. Focused smoke `QinJvmObjectArrayPrimitiveSemanticArgumentSmokeTestMain OK` proves `Object[]<double>` array literals and `Array.from` factories load and invoke without verifier failure. Sequential strict broad smoke with reduced heap/code-cache pressure crossed the former `SubhutiAdaptiveDecisionGraph$Builder.compileBareNode` VerifyError, completed 377 module-class emits, then exposed the next root blocker during dependency-session runtime: Java-native `com.subhuti.parser.SubhutiStaticGrammarPlan$Kind` cannot be cast to generated `com_subhuti_parser_SubhutiStaticGrammarPlan$Kind` in `SubhutiStaticGrammarPlan.readDecisions`. | H5-2af small 20.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2ag small 0.0% -> 20.0% |
| 2026-08-04 02:06 +08:00 | H5-2ag generated/native enum identity accepted; boxed numeric constructor blocker opened | Accepted blocker removal, new small item opened | `QinJvmDeclarationClassEmitter` now resolves generated-local enum class literals and static `values()/valueOf()` through the effective local declaration owner before falling back to native Java enum owners. Focused smokes `QinJvmGeneratedEnumLocalAliasValuesSmokeTestMain OK` and `QinJvmGeneratedEnumValueOfSmokeTestMain passed` prove isolated generated enum alias behavior. Sequential strict broad smoke crossed the former `SubhutiStaticGrammarPlan$Kind` generated/native enum `ClassCastException` and exposed the next root blocker: `java.lang.Integer cannot be cast to java.lang.Double` in `SubhutiStaticGrammarPlan$BranchDefinition.<init>` while `readDecisions` decodes metadata. | H5-2ag small 20.0% -> 95.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2ah small 0.0% -> 20.0% |
| 2026-08-04 02:26 +08:00 | H5-2ah generated constructor boxed numeric coercion accepted; compiled LL(1) metadata blocker opened | Accepted blocker removal, new small item opened | Dumped `BranchDefinition.<init>` proved the generated constructor dispatcher loaded `DataInputStream.readInt()` as boxed `Integer` in `Object[]`, then directly `checkcast java.lang.Double` before calling the typed constructor helper. `QinJvmDeclarationClassEmitter.coerceValueForTargetType(...)` now treats `Object -> boxed numeric target` as a static `Number.*Value()` plus target `valueOf` coercion, not a dynamic helper. Focused and adjacent smokes passed: `QinJvmGeneratedConstructorNumericObjectArgumentSmokeTestMain OK`, `QinJvmGeneratedConstructorHelperAbiSmokeTestMain OK`, `QinJvmLocalBoxedNumericArgumentSmokeTestMain OK`, plus isolated enum smokes. Sequential strict broad smoke recompiled 377 modules and crossed the former `Integer -> Double` blocker, then exposed the next root blocker: `invalid compiled value-aware LL(1) definition` in `SubhutiStaticGrammarPlan.validateCompiledLookahead` while decoding metadata. | H5-2ah small 20.0% -> 95.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2ai small 0.0% -> 20.0% |
| 2026-08-04 08:19 +08:00 | H5-2as generated Java SDK `IntStream.toArray()` `int[]` closure accepted; `strongConnect` metadata null blocker opened | Accepted blocker removal, new small item opened | Java SDK source now emits `__qin_java_new_array__("[I", length)` for `IntStream.toArray()`, the runtime host binds `globalThis.__qin_java_new_array__`, `QinBuiltinRegistry` admits `Global.__qin_java_new_array__/2`, and `QinDeclarationIrLowerer` lowers direct calls as static builtins instead of `__qin_call__`. `QinJvmDeclarationClassEmitter` infers descriptor-literal array return types and preserves primitive array assignment expression stack results before `IASTORE`. Focused smoke `QinJvmJavaStreamToArrayIntFunctionArrayFactorySmokeTestMain OK` proves direct `int[]` writes and generated Java SDK-style `mapToInt(...).toArray()` return real `int[]`. Sequential strict round-trip crossed the former helper/global/member-set/stack-shape blockers and now fails later at runtime with `NullPointerException` in `SubhutiDecisionProgram.strongConnect` while constructing `SubhutiStaticGrammarPlan`. | H5-2as small 70.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2at small 0.0% -> 20.0% |
| 2026-08-04 08:36 +08:00 | H5-2at Java primitive local default closure accepted; static metadata typed-array store blocker opened | Accepted blocker removal, new small item opened | Bytecode dump proved the `strongConnect` NPE came from generated Java source `int member; do { member = stack.pop(); ... }` becoming `let member: number = null`, then the JVM backend unboxing null via `Number.doubleValue()`. `QinJavaAstIrLowerer` now lowers Java local declarations with no initializer to primitive defaults for primitive declared types (`0.0` for numeric, `false` for boolean) and keeps references as null. Focused smoke `QinJsBackendJavaPrimitiveLocalDefaultSmokeTestMain OK` proves `int member; boolean seen; do { ... }` emits `let member: number = 0.0` and `let seen: boolean = false`, not null. Regeneration passed `QinLanguageGenerateParserSmokeTestMain OK 355`, and `SubhutiDecisionProgram.ts` now has `let member: number = 0.0`. Sequential strict round-trip crossed the former `strongConnect` NPE and exposed a new JVM verifier blocker in wrapper `QinModule50.__qin_chunk_4`: `java.lang.Object` is not assignable to generated `SubhutiStaticGrammarPlan$StaticMetadata` during typed array storage. | H5-2at small 20.0% -> 100.0%; H5-2 major remains 20.0%; H5 overall remains 14.0%; opened H5-2au small 0.0% -> 20.0% |
| 2026-08-04 09:26 +08:00 | H5-2au through H5-2aw generated Subhuti metadata JVM class closure accepted | Accepted blocker removals, next small item opened | H5-2au crossed the static metadata typed-array store verifier blocker with `QinCfaGeneratedStaticObjectSlotArgumentCastSmokeTestMain OK`. H5-2av crossed Java array constructor method-reference emission by recompiling the existing source-owned `emitJavaArrayConstructorMethodReference(...)` path, adding `QinJsBackendJavaArrayConstructorMethodReferenceSourceSmokeTestMain`, regenerating parser TS with `QinLanguageGenerateParserSmokeTestMain OK 355`, and confirming `SubhutiStaticGrammarPlan.ts` emits `__qin_java_new_array__("[[I", ...)` with no `new null`. H5-2aw crossed the generated-local owner blocker by resolving original Java owner names to generated-local declaration owners in declaration-class descriptors/constructors and CFA static descriptors; focused smoke `QinCfaGeneratedLocalOwnerReferenceSmokeTestMain OK` proves `com.example.Owner$Inner` resolves to `com_example_Owner$Inner`. Sequential strict round-trip passed: `QinGeneratedTsSubhutiStaticMetadataRoundTripSmokeTestMain OK {"sharedPrefixDefault":1.0,"hasAdaptiveGraph":true,"branchIndexesSize":2,"candidateGroupDefault":1.0,"hasSharedPrefix":true,"hasFiniteProgram":true,"terminalCandidate0":0,"branchIndex1":1,"branchIndex0":0}`. | H5-2au small 20.0% -> 100.0%; H5-2av small 0.0% -> 100.0%; H5-2aw small 20.0% -> 100.0%; H5-2 major 20.0% -> 30.0%; H5 overall 14.0% -> 16.0%; opened H5-2ax small 0.0% |
| 2026-08-04 14:51 +08:00 | H5-2bg generated `CandidateSet` constructor static array production focused closure | Focused checkpoint accepted; broad gate pending | Broad strict smoke reproduced the module 72 emit failure at `SubhutiLookaheadPlan.ts`: `No compatible generated constructor: com_subhuti_parser_SubhutiLookaheadPlan$CandidateSet args=[BuiltinCall(Global.__qin_call_method__/4)] candidates=[constructor[java.lang.Object[]]]`. `QinCfaJvmClassFileBackend` now treats static array production as a semantic CFA shape: direct and lowered `Array.from({ length }, factory)` emit target arrays, `__QinJavaUtilArrays.copyOf/copyOfRange` emit typed `java.util.Arrays` calls for array parameters and static local-array assignment, local bindings carry inferred static array type, generated `JavaNewExpression` preserves local context, and array casts use descriptors through `emitCheckcastRuntimeClass`. Focused `QinCfaGeneratedConstructorResolutionSmokeTestMain OK` now covers direct and lowered `Array.from`, local `copyOf`, static-field constructor initialization, and lowered `Global.__qin_call_method__(Array,"from",...)` into a generated constructor whose parameter is `java.lang.Object[]`. Two attempted broad reruns after this focused fix produced no first-stage log while unrelated `llmweb` Maven/Surefire Java work was active; those interrupted runs are validation-environment evidence only, not H5 compiler pass/fail evidence. | H5-2bg small 0.0% -> 82.0%; H5-2 major remains 30.0%; H5 overall remains 16.0%; broad strict module 72 emit gate pending |
| 2026-08-04 23:47 +08:00 | H5-2bs/H5-2bt nullable conditional and generated class `typeof` closure | Accepted blocker removals; durable capture in progress | The interface static field null hypothesis for `SubhutiLookaheadPlan.NO_MATCH` was disproved by interface/static-field smokes. The actual NPE was conditional lowering of `unknown ? null : Plan.__qin_field_NO_MATCH` as primitive `double`; null was checkcast to `Number` and unboxed. `QinJvmDeclarationClassEmitter` now keeps null+boxed/reference conditional branches nullable/reference-owned. `JavaEsmGlobal.typeOf(...)` now treats `Class<?>` as `"function"` so generated class exports preserve JS class `typeof` semantics on the JVM module-class path. Focused validations passed: `QinJvmGeneratedInterfaceStaticFieldInitializerSmokeTestMain`, `QinJvmInterfaceConstantPrimitiveLocalSelectionSmokeTestMain`, `QinModuleClassGeneratedInterfaceStaticFieldInitializerSmokeTestMain`, and `QinJsClassTypeofFunctionSmokeTestMain`. Sequential strict broad `QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK` completed 377 module classes and reported static admission wrappers `1526`, contract wrappers `1526`, legacy wrappers `0`. | H5-2bs small 70.0% -> 100.0%; H5-2bt small 70.0% -> 100.0%; H5-2 major 36.0% -> 40.0%; H5 overall 17.2% -> 18.0%; opened H5-2bu small 0.0% -> 70.0% |
| 2026-08-05 00:04 +08:00 | H5-3a CSSTS parser declaration-class probe crossed resolver and hit Static Admission Gate | Blocked pending operator package decision | Added Qin ESM resolver support for project `packageOverrides` and source package `file:` dependencies, with focused `QinEsmPackageOverridesResolverSmokeTestMain OK` proving both unscoped and scoped package paths. Added the missing demo `@qin/generated-qin-parser-ts` package override. The same sequential CSSTS parser probe now crosses bare-module resolution, builds `modules=200`, links `8,576,112` chars, and validates `imports=960`, then stops at runtime-feature static admission: `subhuti/src/logutil.ts` uses `import.meta`, `subhuti/node_modules/lru-cache/dist/esm/index.min.js` uses dynamic import and advanced `Symbol`, `subhuti/src/validation/analyzers/SubhutiRuleCollector.ts` uses `Proxy` and `Reflect`, and generated Java SDK files use `WeakRef`/`arguments`. The third-party `lru-cache` diagnostic already reports package root, unsupported shape, static lowering reason, and approved choices. | H5-2bu small 70.0% -> 100.0%; H5-3a small 0.0% -> 85.0%; H5-3 major remains 0.0%; H5 overall remains 18.0%; blocker requires operator decision for third-party/static package route |
| 2026-08-05 00:30 +08:00 | H5-3a static facade route admitted; module const object blocker opened | Accepted blocker removal, new small item opened | Added Qin-owned `subhuti` static facade package and project override so the CSSTS graph uses generated static Subhuti classes instead of the dynamic `subhuti` package path. Cleaned generated Java SDK static shapes by replacing unlowerable `WeakMap`/`WeakSet`/`arguments.length` usage with statically admitted forms. Resolver priority now applies project `packageOverrides` before nested package `file:` dependencies, with `QinEsmPackageOverridesResolverSmokeTestMain OK`. Sequential CSSTS parser probe then passed the runtime-feature Static Admission Gate, passed sema with `modules=174` and `imports=867`, entered declaration-class compilation, and exposed the next root blocker in module 171 `CssTsTokenConsumer.ts`: `CssTsContextualKeywordTypes.Css` was lowered as module-ref `Object` plus dynamic member-get. | H5-3a small 85.0% -> 95.0%; H5-3 major remains 0.0%; H5 overall remains 18.0%; opened H5-3b small 0.0% -> 20.0% |
| 2026-08-05 00:42 +08:00 | H5-3b module const object property closure accepted; H5-3 graph compile complete | Accepted major gate | `QinDeclarationIrLowerer` now applies a generic static AST/IR rule for non-computed fixed object literal property access: if a receiver is a local or module/export const binding whose IR initializer is a `QinIrObjectLiteral`, the property lowers to the fixed property value instead of `Global.__qin_module_ref_get__(...)` followed by dynamic member lookup. Focused `QinJvmModuleConstObjectPropertyStaticSmokeTestMain OK` proves `export const CssTsContextualKeywordTypes = { Css: "css" } as const` used inside a class method lowers to a static string literal and compiles under `-Dqin.dynamicSemanticMode=error`. Sequential broad `QinModuleDeclarationClassCompileProbeMain OK modules=174 declarations=742` crossed module 171 `CssTsTokenConsumer.ts` and module 173 `CssTsParser.ts` under strict mode. | H5-3b small 20.0% -> 100.0%; H5-3 major 0.0% -> 100.0%; H5 overall 18.0% -> 43.0%; opened H5-4 major 0.0% |
| 2026-08-05 01:04 +08:00 | H5-4 generated-local metadata execution accepted; H5-5 opened | Accepted major gate | The stale source-tree `.class` artifact first reproduced an old dynamic-helper path and was recompiled from current UTF-8 source. With the standard Slime/Subhuti classpath, `QinJvmGeneratedEnumLocalAliasValuesSmokeTestMain` exposed the real blocker: generated-local enum `values()` resolved through native Java enum reflection before Qin's generated enum helper. `QinJvmDeclarationClassEmitter` now resolves generated enum-like types through `resolveIndexedDeclaration(...)` and stops generated-local enum synthetic `values()/valueOf()` from falling through to reflected native owners after no declared local method is found. Focused validation passed, then the sequential strict backend smoke group passed all H5-4 entries: export slot member, module export slot type alias, Java class literal alias, simple-name class literal static method, generated-local class info/interface metadata, generated interface metadata, generated enum metadata, generated/native enum alias values, and generated enum valueOf. | H5-4b small 20.0% -> 100.0%; H5-4 major 0.0% -> 100.0%; H5 overall 43.0% -> 63.0%; opened H5-5 major 0.0% and H5-5a small 0.0% |
| 2026-08-05 07:51 +08:00 | H5-5a ASI owner implemented, validation pending | Accepted focused implementation checkpoint | Strict `QinGeneratedTsSlimeCsstsTransformProbeMain importTwoConstDirect` still failed semantically, but focused evidence narrowed the owner: single `constDirect` parses and emits `const count = ref(0);`, while existing `twoConstDirect` diagnostics show `ModuleItemList`, `ModuleItem`, `StatementListItem`, `Declaration`, and `LexicalDeclaration` stop at the second `Const` after consuming the first declaration. The owner is `SemicolonASI` for newline ASI between adjacent top-level declarations, not the `ref(0)` call expression or a token-name special case. The demo `SlimeStatementParser.ts` and generated bundle now mirror the accepted generated parser shape by returning after hard ASI failure and calling `setParseSuccess()` when `canAutoInsertSemicolon()` succeeds without consuming a real semicolon. | H5-5a small 0.0% -> 70.0%; H5-5 major remains 0.0%; H5 overall remains 63.0% |
| 2026-08-05 07:57 +08:00 | H5-5a ASI validation advanced through focused and broad probes | Accepted validation checkpoint | `QinGeneratedTsSlimeCsstsTransformProbeMain twoConstDirect` and `importTwoConstDirect` both now finish `OK` after the `SemicolonASI` owner change. The two focused cases confirm the second top-level declaration is no longer blocked by newline ASI, and the broad transform probe now reaches the end of the same generated TS Slime CSSTS path without the old adjacent-declaration failure. Ledger evidence is updated; skill/git hygiene remains the final checkpoint. | H5-5a small 70.0% -> 95.0%; H5-5 major remains 0.0%; H5 overall remains 63.0% |
| 2026-08-05 08:01 +08:00 | H5-5a ledger, skill, and git hygiene completed | Accepted checkpoint | The `SemicolonASI` rule note was captured in `C:\Users\qinky\.codex\skills\qin-runtime-direct-fixes\SKILL.md`, the ledger was updated to reflect the passing `twoConstDirect` / `importTwoConstDirect` probes, and the current-unit repo files were committed locally as `fix generated ts ASI closure`. Unrelated dirty files remain excluded. | H5-5a small 95.0% -> 100.0%; H5-5 major 0.0% -> 25.0%; H5 overall 63.0% -> 66.8%; opened H5-5b small 0.0% |
| 2026-08-05 09:05 +08:00 | H5-5b broad strict smoke replay reproduced Map-backed Slime node type blocker | Progress-neutral blocker reproduction | `QinGeneratedTsSlimeCsstsTransformProbeMain importTwoConstDirect` reran under `-Dqin.dynamicSemanticMode=error`, reached full module-class lower/emit for 356 modules, and then failed in `QinJvmDeclarationClassEmitter.inferDeclarationExpressionType(...)` with `Unknown declaration instance method type: java.util.Map.type`. The owning source was corrected in `CssTsCstToAstUtils.ts` by replacing remaining `stmt.type().name()`, `actualSpec.type().name()`, `initExpression.type().name()`, `calleeExpression.type().name()`, and `expr.type().name()` checks with a single map-aware Slime node type helper. No acceptance gate moved yet; H5-5b remains the active small item. | H5-5b small 0.0% -> 0.0%; H5-5 major remains 25.0%; H5 overall remains 66.8% |
| 2026-08-05 10:38 +08:00 | H5-5b Slime node type helper boundary narrowed to fixed property normalization | Progress-neutral implementation checkpoint | `CssTsCstToAstUtils.ts` no longer models Slime nodes with a `{ type(): any }` structural method shape that the JVM backend lowers as `java.util.Map.type`. The helper now imports `normalizeGeneratedAst`, keeps Map wrappers on the explicit `get('type')` path, and normalizes other nodes before reading the fixed `type` property. This is still a diagnosis/implementation checkpoint only; the next strict probe must prove whether `java.util.Map.type` is gone or expose the next owning blocker. | H5-5b small 0.0% -> 0.0%; H5-5 major remains 25.0%; H5 overall remains 66.8% |
| 2026-08-05 21:22 +08:00 | H5-5b Java SDK alias static-facade closure accepted | Accepted checkpoint | The generated token failure was narrowed to declaration lowering: `SubhutiMatchToken.hasLineBreakBefore()` compiled `__QinJavaLangBoolean.TRUE.equals(this.__qin_field_hasLineBreakBefore)` through the imported Java SDK object literal, so `TRUE` became an object-freeze wrapper and the `.equals` call was emitted with dynamic/static-mismatched semantics. `QinDeclarationIrLowerer.fixedObjectPropertyAccessOrNull(...)` now gives every known `QinJavaSdkAliasSupport` alias priority over fixed object-literal export expansion, preserving `__QinJavaLangBoolean.TRUE` as `java.lang.Boolean.TRUE`. Validation passed sequentially: package builds for `qin-lang-frontend-adapter` and `qin-runtime-core`; `QinGeneratedTsBooleanTokenMethodSmokeTestMain OK`; `QinGeneratedTsSlimeCsstsTransformProbeMain twoConstParseOnly`, `twoConstDirect`, `importTwoConstParseOnly`, `importTwoConstDirect`, and `importCssConst` all exit 0; and `QinGeneratedTsSlimeCsstsCompilerSmokeTestMain OK` under `-Dqin.dynamicSemanticMode=error`. A prior chat progress line incorrectly reported H5-5 as 35.0% / overall 67.8%; the ledger correction is H5-5b's accepted gate at 55.0%, making H5 overall 71.3%. | H5-5b small 0.0% -> 100.0%; H5-5 major 25.0% -> 55.0%; H5 overall 66.8% -> 71.3%; opened H5-5c small 0.0% |
| 2026-08-05 23:33 +08:00 | H5-5e/H5-5f strict OVS transform blockers removed | Accepted blocker removals | H5-5e fixed generated parser `Program()` no-arg invocation by prioritizing real source methods over synthetic/bridge methods in `JavaEsmGlobal` reflection candidate ordering; focused `QinModuleClassSyntheticBridgeNoArgVarargsSmokeTestMain OK` and adjacent `QinModuleClassGeneratedJsNoArgVarargsSmokeTestMain OK` passed. H5-5f fixed relative ESM named helper imports by preserving static export slot gets in `QinDeclarationIrLowerer.moduleBindingReferenceExpression(...)`; focused `QinModuleClassRelativeNamedFunctionImportSmokeTestMain OK` passed. A long diagnostic strict OVS run with `-Dqin.runtime.jsRunTimeoutMs=120000` then passed semantically, proving the remaining blocker was default runtime budget rather than transform correctness. | H5-5e small 0.0% -> 100.0%; H5-5f small 0.0% -> 100.0%; H5-5 major 55.0% -> 72.0%; H5 overall 71.3% -> 73.8%; opened H5-5g small 0.0% |
| 2026-08-06 00:08 +08:00 | H5-5g generated AST normalization runtime budget accepted | Accepted major gate | The default strict OVS transform smoke initially exceeded the 30 second Qin JS-on-JVM run budget even after semantic success. `-Dqin.runtime.interpretedCallCountLimit=10000` localized the hot path to `ovsTransformBase -> normalizeGeneratedAst -> normalizeGeneratedAstChildren -> readGeneratedField -> readStaticMemberValue`. `generated-runtime-adapter.ts` now makes AST normalization idempotent and uses a static node-kind child-field table instead of repeatedly blind-scanning every possible AST field. The default strict rerun passed without raising the timeout: `module-class disk cache hit`, dependency session ready at `+4088ms`, run batch done at `+7814ms`, wrapper complete at `+9289ms`, and `QinGeneratedTsSlimeOvsTransformSmokeTestMain OK` with `tokenCount=25`, `hasAst=true`, `codeLength=680`, `hasBalancePanel=true`, `hasLoadingLine=true`. | H5-5g small 0.0% -> 100.0%; H5-5 major 72.0% -> 100.0%; H5 overall 73.8% -> 78.0%; opened H5-6 major 0.0% |
| 2026-08-06 00:22 +08:00 | H5-6a abstraction audit and default strict smoke validation | Accepted checkpoint | Current-unit dirty paths were classified before widening. The generated AST/CST adapter no longer expresses the static field contract as a long `fieldName === ...` chain or a `switch (nodeType)` block; it uses `generatedFieldReaders` with direct field/accessor reads and `generatedAstChildFieldTable` for node-kind child traversal. `rg` found no remaining `fieldName ===`, `switch (nodeType)`, `node[key]`, or `value[fieldName]` shapes in the adapter. Validation passed sequentially: `.\..\..\qin.bat build` reported `BUILD SUCCESS`; after one cold module-class rebuild caused by the source hash change, the cache-hit default strict `QinGeneratedTsSlimeOvsTransformSmokeTestMain` exited 0 in 12.1s with module-class disk cache hit and run batch done at `+9213ms`. | H5-6a small 0.0% -> 85.0%; H5-6 major 0.0% -> 60.0%; H5 overall 78.0% -> 84.0% |
