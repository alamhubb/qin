# qin-parser

Qin language parser frontend package.

## Position

`qin-parser` is the Qin-owned parser boundary.

It exists to keep these concerns separate:

- `java-slime`: shared JS/TS parser infrastructure
- `qin-parser`: Qin language syntax entry, Qin syntax extensions, Qin parse diagnostics
- `qin-lang-frontend-adapter`: Qin AST / normalized frontend model -> Qin IR

## Java Chevrotain build

The production build uses JDK 25 and the existing Java author grammars.

```text
Slime Java author grammar → AuthoringCompiler → GrammarIr
→ JavaParserSourceGenerator → generated Java Chevrotain parser
→ SubhutiCst → Qin AST / existing OVS emitter
```

Run `python -X utf8 build-chevrotain-parser.py` in this package. The normal Qin
dependency compiler honors this package's `type=library` and `scripts.build`.
The build generates QinParser, CssTsParser and OvsParser with precomputed
lookahead, runs the existing smoke tests, and publishes `build/classes` and
`build/qin-parser.jar`. Runtime facades preserve the inheritance chain
`SlimeParser → QinParser → CssTsParser → OvsParser`. Author sources and runtime
facades share public names in distinct build source roots.

The input fingerprint includes grammar, generator, compiler, IR, JDK and the
verified Slime public-package fingerprint. Cache admission checks every output
hash and the exact output file set. Input changes during generation reject
publication. Deployment contains generated parsers and the Java runtime.
The public base package consumes its pinned Step 19 receipt, while the three
Qin dialects are generated from the current author sources.

`QinParserFacade` is the normal Qin parse entry. `QinOvsCompiler` parses OVS with
the generated Java OvsParser and passes its CST and tokens to the existing
`ovs-compiler` AST/emitter through `parsedInput`. Transform cache identities
include the generated Java parser fingerprint. Default parse calls have no
application-defined quotas for source length, token count, CST size, nesting,
elapsed time, or total lookahead reads. The local Chevrotain decision tables
use LL(3), and the generated predicates can inspect longer prefixes.

## Java and Node OVS performance diagnosis

Measurements use the same 2,105-byte `BalanceRow.ovs` source, JDK 25 and Node
24.14. The generated Java `OvsParser` initializes its shared parser backend in
the constructor, matching Node's phase placement. Across five fresh processes,
the median Java constructor plus first `Program()` call is 587 ms, compared
with 642 ms for Node. The process-launch cost is excluded. Java construction
is about 490 ms and its first parse about 97 ms. Node construction is about
636 ms and its first parse about 6 ms.

After 1,000 warm parses, the Java public `Program()` median is 4.46-4.79 ms
across two 100-sample runs, compared with 1.88 ms for Node lexing plus CST
parsing. Java phase timings are
about 0.299 ms for lexing, 0.005 ms for native token-session adaptation, and
3.143 ms for recognition plus CST construction. The Java public entry adds
roughly 1.0-1.3 ms for its additional validation and result accounting.

In the same seven-case syntax matrix with 200 warmups and 24 measured parses,
Java public parse medians range from 1.92 ms for identifiers to 10.31 ms for
conditional object expressions. Node lexing plus parsing ranges from 0.83 ms
to 4.10 ms. The Java/Node timing ratios are 2.3x for identifiers, 1.8x for
binary addition, 1.3x for a precedence chain, 2.0x for calls and arrows, 4.3x
for conditional object expressions, 2.4x for nested OVS render, and 2.7x for
control flow with render. The Java CST contains 2,727 rule nodes for
`BalanceRow.ovs`, compared with 1,329 in Node. In the matrix, Java produces
1.8-2.7x as many rule nodes. The comparison Node grammar has 84 explicit
`RULE` definitions and represents binary operators with one flat
`BinaryExpression`; the generated Java OVS parser has 408 `parseRule` methods
and retains separate operator-precedence rules. The two parsers therefore do
different amounts of grammar and CST work on the same text.

A 500-parse JFR sample attributes 22.8% of Java execution samples to generated
alternative path matching, 4.7% to lookahead token access, 5.7% to immutable
map probing, and 2.0% to parser-parameter map reads. Allocation sampling
attributes 14.5% of allocation pressure to `SubhutiCst.projected` and 6.9% to
syntax validation. The parsing thread allocated about 1.9 GB across 500 parses
and incurred five GC pauses totaling 24 ms. These pauses are a small part of
the measured parse time. With `-Dslime.chevrotain.profile=true`, runtime
counters report 33,024 lookahead calls per parse, 32,616 cache hits and 408
materialized lookahead decisions. Profiling instrumentation raises measured
wall time, so its elapsed timings are used only as diagnostics.

The Java lexer examined 11,859 candidate definitions and attempted 9,298
pattern matches for the 418 non-EOF tokens. Node attempted 923 regex patterns.
The Java lexer is about six times slower in this sample, but adds only about
0.25 ms per parse. Its cost is smaller than the recognition/CST difference.

## OVS Node-shape grammar experiment

The current Java baseline was checkpointed in Slime commit `b39779df` before
changing grammar structure. A separate OVS-only experiment then changed the
expression path to the same broad shape as the Node reference parser:
`ConditionalExpression` calls one flat `BinaryExpression`, which repeats
`BinaryOperator` and `UnaryExpression`. The experiment keeps the full Slime
TypeScript and OVS extensions in place. It does not claim that all 84 Node rule
names or all Node CST names have been copied into the Java grammar.

The experiment passed the Java OVS smoke tests, the Qin parser smoke tests, and
all 11 balance-monitoring fixtures. On the 7-case matrix with 200 warmups and
24 measured parses, Java public medians changed as follows:

| Syntax case | Before | Node-shape experiment |
| --- | ---: | ---: |
| identifiers | 1.92 ms | 1.62 ms |
| binary addition | 2.38 ms | 2.16 ms |
| precedence chain | 3.75 ms | 3.57 ms |
| call and arrow chain | 7.23 ms | 5.39 ms |
| conditional object | 10.31 ms | 6.67 ms |
| nested OVS render | 8.65 ms | 7.19 ms |
| control flow and render | 10.98 ms | 9.36 ms |

For `BalanceRow.ovs`, Java rule nodes decreased from 2,727 to 1,999 and direct
recognition plus CST construction decreased from about 3.14 ms to 2.48 ms
after long warmup. The public entry remains slower than Node, which measured
about 1.88 ms on the same input. The largest improvement is in expressions that
repeat conditional, object, call, and OVS render structure. The remaining gap
comes from generated lookahead, Java CST/source-location allocation, the Java
lexer, and public validation work.

The opt-in `-Dslime.chevrotain.profile=true` runtime counters expose lookahead,
predicate, consume, rule and CST work. The phase and syntax probes are
`qin/build/chevrotain-runtime-budget-probe/OvsPhaseProfiler.java`,
`qin/build/chevrotain-runtime-budget-probe/OvsGrammarMatrixProfiler.java`,
`Chevrotain-test/tests/measure-ovs-phases.ts` and
`Chevrotain-test/tests/measure-ovs-grammar-matrix.ts`. Java Node-API timing
is not a pure recognizer comparison because the Java public entry also runs
nullish validation, syntax validation and CST counting.

`generate-static-parser.ps1` and `QinParserStaticEnhanced` document the historical
Subhuti static-generation route.

## Integration acceptance ledger

Updated 2026-11-26. The goal is to run balance-monitoring through its normal Qin
launcher using the generated Java Chevrotain Qin and OVS parsers.

| Milestone | Weight | Accepted evidence |
| --- | --- | --- |
| Java dialect generation and existing parser regressions | 30% | Three smoke mains passed and the balance-monitoring project parsed 18 source files |
| Normal dependency build and verified cache admission | 15% | `qin sync`, four library build checks and eight cache checks passed |
| Java OVS CST through existing AST/emitter | 25% | Pending integration regression |
| Balance application startup and HTTP modules | 25% | Pending normal launcher and module probes |
| Documentation and focused repository commits | 5% | In progress |

Accepted progress is 45%. Full completion requires all rows, including actual
OVS module output and an HTTP listener from the normal project command.

### Grammar integration regressions

- Ordered alternatives can accept a prefix and leave the rest behind. Qin export
  alternatives need explicit object-declaration predicates so ordinary
  `export async function` reaches the shared grammar.
- Nullish expressions require left factoring at ShortCircuitExpression. Parenthesized
  logical operands remain valid, and unparenthesized nullish/logical mixing is rejected.
- FormalParameterList repetition must stop before the comma that belongs to a rest
  parameter or a trailing comma. A real Java SDK module exposed this with
  `regionMatches(value, ...args)`.
- OVS render calls require OVS-aware prediction before the ordinary JavaScript call
  predictor. Parser smoke tests cover props, render expressions and class heritage.
- Export AST conversion must recognize the export node passed directly as well as
  its enclosing ModuleItem. Object declaration smoke tests exercise decorated exports.
- `QinParserDefaultLimitsTestMain` uses two independent cases to verify the
  default entry accepts a source over two million characters and a smaller source
  that needs more than 100,000 lookahead reads. The packaged parser also accepts
  the balance-monitoring dependency `SubhutiAdaptiveDecisionGraph.ts` with
  2,190,666 characters and 285,820 lookahead reads.

These cases are executable in `QinParserFacadeUnifiedEntrySmokeTestMain`,
`QinParserObjectDeclarationSmokeTestMain` and `OvsParserJavaSmokeTestMain`.

## Why This Package Exists

Before this package, parser-facing logic had started to accumulate inside
`qin-lang-frontend-adapter`, including:

- parser input preprocessing
- source rewrites/shims
- import extraction fallback
- direct Slime parser entry wiring

That made the adapter layer responsible for too many frontend concerns.

This package is the architectural correction:

- parser concerns move here
- lowering concerns stay in `qin-lang-frontend-adapter`

## Planned Responsibilities

`qin-parser` should own:

- `.qin` parsing entrypoints
- Stage 3 frontend `.java` parser integration boundaries once Java8 parsing is promoted from `slime-java`
- Qin parser configuration and parse options
- Qin-specific syntax extensions
- Qin parser diagnostics
- normalized parse results / Qin AST boundary
- migration of parser preprocess/rewrite logic out of the adapter
- import extraction fallback for parser-unfriendly top-level import forms during migration

`qin-parser` should not own:

- Qin IR lowering
- backend-specific logic
- JVM bytecode emission

## Migration Plan

Stage 1:

- introduce `qin-parser` package
- define `QinParser` and `QinParserFacade` entry boundaries
- keep implementation backed by `java-slime`

Stage 2:

- move parse/preprocess logic from `QinSlimeFrontendAdapter` into `qin-parser`
- keep adapter focused on AST -> IR

Stage 3:

- add Qin-specific syntax such as:
  - `async expr`
  - `async { ... }`
  - future Qin-only declarations

Stage 4:

- introduce a Qin-owned AST / normalized parse model if needed
- reduce direct dependence of downstream layers on raw Slime node shapes

Stage 5:

- integrate Qin/Slime Java8 `JavaParser` for frontend `.java`
- keep Java parsing in the Subhuti parser family rather than using ad hoc string lowering
- lower Java CST -> Java AST -> Java semantic model -> Qin IR before JS emission
