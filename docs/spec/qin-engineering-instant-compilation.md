# Qin Engineering And Instant Compilation Design

This document records the intended Qin engineering direction for cache,
incremental compilation, package materialization, generated parser execution,
and developer feedback loops.

## Design Position

Qin should not become a generic JavaScript bundler with a JVM fallback. Qin's
engineering model is a target-aware, ESM-style language toolchain whose dev
loop is fast because the compiler understands stable source identities, target
zones, dependency fingerprints, and JVM `.class` output boundaries.

The best current design is to combine proven ideas from modern toolchains:

- Bazel-style hermeticity: cache keys must describe real declared inputs, not
  incidental temp paths, timestamps, or local machine layout.
- Bazel/Gradle-style build cache: expensive compiler outputs should be reused
  locally first and later be eligible for remote reuse when inputs match.
- Buck2/Bazel-style action graphs: compiler work should be represented as
  explicit, target-partitioned actions whose inputs and outputs can be reasoned
  about, cached, and invalidated independently.
- Kotlin/Gradle-style incremental compilation: classpath and source changes
  should invalidate only affected work, not the entire graph.
- Turborepo-style declared task boundaries: cacheable work must name the inputs
  and outputs it actually owns, including logs where they are useful for
  replaying or debugging a cached result.
- Vite-style dev ergonomics: cold start should precompute heavy dependencies,
  and hot updates should touch only changed application modules.
- esbuild-style persistent rebuild context: long-lived dev processes should
  keep parser/compiler/module graph state warm instead of rebuilding from
  scratch on every request.
- Deno/Bun-style integrated tooling: dependency locking, local dependency
  caching, transpilation, test, and run loops should feel cohesive to users,
  while Qin still keeps stricter source portability and JVM `.class` rules.

These are design influences, not permission to copy another tool's semantics.
Qin must keep its own language and target-zone boundaries.

## Design Maturity Assessment

Qin's engineering direction is reasonable, modern, and ambitious if it is
implemented as a compiler/build-system architecture rather than as a generic
JavaScript dev-server wrapper. The strongest version of the design is:

- compiler-first: parse, lower, type, transform, and emit through Qin-owned
  layers before any runtime shortcut is considered;
- target-aware: every cache and transform is partitioned by `app/`, `main/`,
  `shared/`, frontend JS, backend JVM, and dual-target output constraints;
- content-addressed: reusable work is keyed by stable content, config,
  dependency, compiler-version, and target identity, not by incidental paths or
  wall-clock state;
- hot-stateful: long-running dev processes keep parser, dependency graph,
  materialized package, and module-class state warm across edits;
- observable: every cache hit, miss, invalidation, and conservative rebuild is
  visible enough to debug without deleting caches;
- strict: fast paths must preserve language semantics and may not become
  fallback paths that hide parser, compiler, runtime, or resolver defects.

This is the right modern direction for Qin because it combines the best ideas
from hermetic build systems, incremental JVM compilers, and fast frontend dev
servers while keeping Qin's own source and `.class` constraints. It should be
treated as a high-standard product architecture, not a temporary performance
patch.

The design should be described as "frontier-quality for Qin" only when it is
more than a copy of mainstream tools. Qin should borrow their proven mechanics
but specialize them around Qin's language promise: ESM-style source, JVM
`.class` output, JS output, and `.qin`-only shared source in the MVP. A design
that is excellent for a generic JavaScript monorepo is not automatically
excellent for Qin.

The design is not "most advanced" merely because it is fast. It becomes
advanced only when it gives developers all three properties at once:

- correctness: stale output, wrong target reuse, and hidden fallback success are
  considered bugs;
- speed: unchanged parser/runtime/package/compiler work is reused aggressively;
- simplicity: project authors do not manage cache directories, generated
  packages, or rebuild knobs during normal development.

## Qin-Specific Engineering Standard

Qin should prefer a unified compiler service model:

- frontend transforms, backend `.class` emission, OVS/CSSTS compilation, npm
  materialization, generated parser execution, and shared-module compilation
  should be coordinated by one Qin dev/build service;
- external tools may be used as implementation references, but Qin must own the
  target-zone decision, cache key, invalidation, and error surface;
- app-level changes should be cheap, while toolchain-level changes should
  invalidate only the packages and compiler outputs that actually depend on
  them;
- `shared/` reuse must be proven by portable Qin source rules, not by the fact
  that a package or file happens to run in both Node and the browser.

The ideal developer experience is:

1. first run warms stable compiler and package caches;
2. subsequent runs reuse generated parser, OVS, CSSTS, Vue, npm, and JVM module
   outputs by stable fingerprint;
3. editing one application file recompiles only the affected frontend/backend
   module and any directly dependent output;
4. broken cache invalidation is fixed in the owning cache/compiler layer instead
   of being hidden by `clean`, timeout increases, broad rebuilds, or source
   rewrites.

## Decision Checklist

Before accepting a new instant-compilation or cache behavior, verify:

- Is the cache key based on declared Qin inputs and target identity?
- Does the behavior preserve `app/`, `main/`, and `shared/` boundaries?
- Can stale output be reproduced by a focused smoke test?
- Does a source/config/dependency change invalidate the smallest correct unit?
- Does a generated parser/package/runtime update invalidate its consumers?
- Are cache hits and misses observable in logs?
- Does the fast path preserve the same semantics as the slow path?
- Would the system still work without manual cache deletion?

If any answer is no, the design is not yet Qin-quality.

## Core Principles

### Stable Inputs Over Incidental Paths

Cache identity should be based on:

- normalized source content;
- explicit compiler/runtime version;
- target zone and target backend;
- relevant `qin.config.js` content;
- package manifest and lock/stamp data;
- generated parser/runtime artifact version;
- explicit compiler flags and feature gates.

Cache identity should avoid:

- temp project roots created by tests;
- materialized `node_modules` absolute paths;
- wrapper file names generated from random or transient paths;
- wall-clock timestamps except inside content fingerprints that intentionally
  represent dependency freshness;
- broad directory mtimes as the only invalidation signal.

### Layered Caches

Qin should use separate cache layers instead of one coarse cache:

- package materialization cache: copies or shims external/workspace packages
  into the Qin runtime host and records a content-aware stamp;
- workspace package index cache: discovers monorepo package names once per
  long-lived runner process, then reuses the name-to-path index while
  materialization stamps still decide whether package content must be recopied;
- dependency fingerprint cache: fingerprints manifests, selected entries, lock
  data, and Qin package stamps without walking every dependency file on every
  invocation;
- frontend transform cache: stores OVS/Vue/CSSTS transform outputs keyed by
  source, config, module id, target zone, and toolchain version;
- frontend semantic cache: stores validated ESM import/export semantic models
  keyed by frontend graph module sources, resolved imports, entry identity,
  semantic analyzer/parser/runtime validator class resources, Java version, and
  project root;
- Java source compile cache: skips repeated `javac` work only when the
  normalized Java source set, source content, classpath, compiler identity,
  output directory, and compile options match and the recorded class outputs
  still exist;
- module-class cache: stores JVM module-class bytecode keyed by stable source
  identity, dependency fingerprint, compiler version, and target options;
- fullstack backend module cache: skips repeated `.qin`/`.ts`/`.js` backend
  module-class compilation only when `qin.config.js`, the backend source,
  `main`/`src/main`, `shared`/`src/shared`, classpath, compiler class
  resources, output directory, generated backend class name, and recorded
  `.class` outputs match;
- in-process hot cache: keeps parsed module graphs, lowered IR, and compiled
  classes warm inside the dev server.

Each layer must have a focused smoke test for hit/miss behavior.

Hot in-process caches are checked before disk caches. Disk caches are the
cross-process persistence layer; once a dev server has loaded or produced a
module-class result, the same process should not repeatedly deserialize the
same large cache artifact for later requests with the same stable identity.

For JVM module-class execution, a hot process should treat the stable
dependency module graph like an ESM module graph: initializer and dependency
modules are instantiated once per stable module-class cache identity, while the
entry wrapper remains the invocation boundary and runs on every call. This is
not a fallback and it must not skip unproven side effects. Add focused smoke
coverage that proves dependency side effects run once, entry side effects run
per invocation, and live exports remain readable before applying the behavior to
broader OVS/CSSTS/dev-server flows.

Request data must not be embedded into stable compiler/tool wrapper source when
the wrapper's module graph is otherwise identical. OVS/CSSTS/Vite-style
compiler wrappers should keep plugin/container imports and helper code stable,
then pass per-request source text, module ids, and options through a scoped hot
runtime input boundary. This follows the Vite plugin-container lesson: the
container and dependency graph stay warm while each transform invocation still
receives the current request data. Add focused smokes that compile two distinct
sources through the same hot wrapper and prove the second result is fresh, not a
stale cache hit.

Java JIT and module-class caching only remove wrapper/module graph compilation,
class loading, and dependency-session setup from the hot path. They must not be
treated as proof that per-request OVS source work is free or skipped: each fresh
OVS input still runs the standard parser/transform path unless a strict
transform cache proves the same input/config/dependency identity. Use focused
hot-session probes to separate these timings: wrapper compile hit, dependency
session hit, entry run time, and inner parser/transform elapsed time.

The standard Qin OVS wrapper path is `ovs-compiler`, not a runtime
`vite-plugin-ovs` fallback. Project options such as CSSTS `classPrefix` must
enter that standard transform as runtime input, so the wrapper source and module
graph stay stable while each project still receives its own configuration.
Toolchain fingerprints must include only packages that affect that standard
path; unused plugin packages should not invalidate OVS transform cache keys or
force expensive directory hashing. A focused smoke should prove both the
configured output and the absence of default-config leakage.

The same rule applies to batch transforms. A `compileAll` or multi-file
transform wrapper must not embed the source/id array into wrapper source. Bind
the whole batch through a scoped runtime input boundary and keep the wrapper
source stable so the hot module-class graph, plugin container, and dependency
session can be reused across batches. A correct batch smoke must prove three
things together: changed batch inputs produce fresh output, the second batch is
not served from the transform disk cache, and the stable wrapper hits the
module-class compile cache plus dependency session.
Production frontend emit must use that same batch boundary for graph OVS
modules: collect the `.ovs` modules in the frontend graph, prewarm them through
`QinOvsCompiler.compileAll`, then write each output from the service cache.
Per-module production transforms are a performance defect unless the graph has
only one OVS module or the transform cache already proves no transform work is
needed.

### Active Dependency Fingerprints

The Qin runtime npm host must fingerprint the active dependency closure for the
current wrapper/module graph, not every directory that happens to remain under
`.qin/runtime/npm-host/node_modules`.

This follows the Vite module-graph lesson: dev-server work should be scoped to
the modules that can affect the requested transform. Stale or inactive runtime
packages must not be part of every request's cache key, because that turns one
old large dependency into global latency.

When a module-class cache hit is already proven but startup is still slow,
profile the next boundary separately: dependency session setup, entry wrapper
execution, and per-input transform time. A cache hit that still spends tens of
seconds inside the transform run is not a cache-key problem; it means the active
OVS/CSSTS transform execution path needs its own focused probe and optimization.

The same active-scope rule applies one layer earlier: a long-lived dev runner
should not rebuild the workspace package name index on every package invocation.
The index is process-local discovery state. Content freshness still belongs to
package materialization stamps and active dependency fingerprints, so caching
the index must not hide changed package source files.

It also follows the Kotlin/Gradle classpath snapshot lesson: dependency cache
identity should come from stable package metadata and content-aware stamps,
similar to classpath/ABI snapshots, rather than scanning every implementation
file on every compile request. A package that is active in the runtime host
must have `.qin-package-sync.json`; missing stamps are cache-system defects to
repair in materialization or shim generation, not a reason to silently fall
back to full tree hashing in the hot path.

Fullstack Java helper/backend compilation should follow the same principle at
the `javac` boundary. Re-running a dev build with an unchanged Java source set
must be able to skip `javac` using a content-aware stamp. Any Java source
content change, source-set change, classpath/options change, compiler identity
change, output directory change, or missing recorded `.class` output must
invalidate the stamp and run standard `javac`. This is a strict incremental
compile cache, not a fallback compiler.

OVS/CSSTS toolchain fingerprints should use the same snapshot principle in a
long-lived compiler process. The first fingerprint for a tool package computes
the real content digest; later fingerprints for the same unchanged directory
may reuse that digest when the sorted file metadata snapshot still matches.
Source, generated parser, package, config, or override changes must invalidate
the snapshot and recompute the digest. This is an incremental compiler cache,
not a fallback path, and focused smokes must prove both cache hit and
invalidation behavior.

### Fast Path Is Not Fallback

A fast path is correct only when it preserves the owning semantic model. For
example, a generated Java `HashMap` fast path must honor Java `hashCode` and
`equals`; otherwise it is a bug, not an optimization.

Do not add degraded parser/runtime/compiler shortcuts that silently accept
wrong behavior to make startup look faster. Performance work must preserve
strict semantics.

### Lightweight Module Binding Semantics

ESM module binding discovery is an early, frequently executed compiler phase.
Qin should follow the mainstream layering used by TypeScript, esbuild, SWC,
oxc, Vite, and similar toolchains: only pay for the information required by the
current phase.

For Qin module graph and ESM link validation, the required facts are top-level
`import`/`export` bindings, resolved specifiers, and source locations. The
standard semantic path should collect those facts with a lightweight static ESM
binding scanner. It should not build the full Qin/Slime CST/AST or run lowering
just to validate module linkage. Full parser, CST/AST, IR, type/lowering, and
`.class` work belong to later phases that actually need those artifacts.

This is not fallback behavior. The lightweight scanner is the standard module
binding path. If a Qin-standard ESM or TypeScript-like import/export form is
missing, repair the scanner and add focused smoke coverage. Non-standard forms
should fail visibly instead of falling through to a second parser path.

The CSSTS toolchain benchmark on 2026-07-08 compared the same 272-module,
about 2.92MB source graph against mainstream tools:

- TypeScript `createSourceFile`: about `554ms`;
- Babel parser: about `480ms`;
- esbuild transform: about `397ms`;
- SWC parser: about `467ms`;
- oxc parser: about `416ms`;
- old Qin AST-first ESM semantic binding: about `26515ms`;
- Qin lightweight ESM static binding: about `474ms`.

Treat this as the target pattern for future early compiler phases: match the
cost model of the information being collected, then cache or precompile stable
toolchain work at later boundaries.

### Incremental Compilation Contract

Incremental compilation should be graph-based:

- changing one app OVS/Qin module should not recompile generated parser and
  runtime packages;
- changing a package manifest or package stamp should invalidate imports from
  that package only;
- changing `qin.config.js` should invalidate transforms that read that config;
- changing generated parser artifacts should invalidate parser consumers;
- changing backend runtime code should invalidate affected JVM class outputs.
- unchanged frontend module graphs may skip expensive ESM semantic analysis
  only when graph sources, resolved imports, parser/semantic validator class
  resources, Java version, entry identity, and project root still match; any
  module source or import graph change must invalidate and run the standard
  semantic analyzer path.
- unchanged fullstack Java helper/backend sources may skip `javac` only when
  their content-aware compile stamp and recorded class outputs still match;
  changed Java source content or classpath/options must invalidate the stamp.
- unchanged fullstack Qin/TS/JS backend module-class builds may skip
  `QinBuildCoordinator` only when their strict backend module stamp and
  recorded class outputs still match; backend source, config, main/shared
  source, classpath, compiler class resource, output-directory, or generated
  class-name changes must invalidate the stamp and run the standard coordinator
  path.

When Qin cannot prove the affected set, it may rebuild conservatively, but that
case should be observable in logs and covered by an explicit test before it is
accepted as the permanent design.

### Dev Server Shape

The Qin dev server should be one orchestrator for frontend and backend work:

- keep a persistent runtime/compiler service for the current project;
- run ordinary Qin fullstack `dev` and `build` through the CLI process by
  invoking `QinFullstackMain` in-process; do not add a routine second JVM,
  Jite wrapper, Node/Vite server, or child-process path for the same standard
  fullstack boundary;
- pre-materialize heavy compiler dependencies once per dependency fingerprint;
- precompile stable tool packages such as generated parser, OVS, CSSTS, and
  Vue compiler shims into reusable module classes;
- compile changed app/main/shared modules on demand;
- serve cached frontend transform output until a relevant source/config input
  changes;
- restart or reload only the affected backend/frontend slice when possible.

The end-user expectation is "first run may warm caches; subsequent runs should
be fast without manual cleanup."

The CLI dependency layer is part of startup performance. `.qin/classpath.json`
should cache not only the classpath but also the resolved local dependency
project list. When the current project config and cached local project configs
are unchanged, Qin may use that list directly and avoid a full workspace scan.
If a cached local project config is newer than the cache, or the cached
classpath no longer contains the required local class outputs, Qin must return
to the normal dependency resolver and refresh the cache. Local source freshness
is still checked by the standard local dependency compilation guard; the cached
project list is an index, not a stale-output permission.

The unchanged classpath cache hit path should read and validate
`.qin/classpath.json` once per CLI invocation. Re-reading the same JSON to
separately validate classpath files, parse local project metadata, and fetch the
cached classpath is startup overhead, not a correctness requirement. Cache
freshness still depends on `qin.config.js`, cached local project configs,
classpath entry existence, and the local dependency readiness guard.

CLI startup/cache changes must keep focused regression coverage before moving
to full apps. Use `QinCliLocalDependencyCacheSmokeTestMain` for local
dependency metadata cache hit/invalidation, and
`QinCliFullstackInProcessProfileSmokeTestMain` for the in-process fullstack
`dev --build-only --profile` path.

Dev-server startup should not synchronously compile every OVS module before the
HTTP listener is available. Like Vite, Qin should start the server after the
minimal backend/frontend graph is ready, then transform OVS modules on demand
as the browser requests them. Optional warming is acceptable only when it does
not block the listener and uses the same standard transform/cache path.

The first dev-server OVS request may compile the current frontend graph's OVS
modules as one on-demand batch through `QinOvsCompiler.compileAll`. This keeps
startup non-blocking while avoiding one heavy OVS/CSSTS toolchain dependency
session per browser module request. The batch path is the standard compiler
path: it must use the same transform disk cache, module-class cache, runtime
input boundary, and error surface as individual transforms. It is not a
fallback and must not hide parser/compiler/runtime failures.

Dev-server file changes should first try the smallest correct update. If a
changed file is already in the current frontend graph, belongs to the pure
frontend transform surface such as OVS, Vue, CSSTS, CSS, or assets, and keeps
the same resolved import graph, Qin should refresh that module inside the
existing `QinFrontendEsmService`, invalidate only that module's transform and
virtual modules, bump the dev version, and let HMR/client requests fetch the
new output. This preserves the long-lived OVS/Vue/CSSTS compiler state in the
same way Vite keeps the plugin/module graph warm and esbuild keeps a rebuild
context alive.

If the changed file is a backend source, unified `src/app.qin`, config file,
entry file, new/deleted graph module, or a frontend edit that changes resolved
imports, Qin must use the standard full rebuild path. That is not a fallback or
compatibility mode; it is the conservative invalidation boundary when the
current hot frontend graph can no longer prove correctness.

Hot refresh must decline only for structural ineligibility such as graph,
target, entry, deletion, or import-set changes. Parser, compiler, transform,
runtime, or HMR-message errors are defects to surface through the normal dev
error path; they must not be hidden by automatically converting the edit into a
successful full rebuild.

## Target Zones

The engineering cache model must respect Qin zones:

- `app/`: frontend-specific JS/TS/Qin/Vue/OVS may use frontend transforms and
  browser-target package entries.
- `main/`: backend Qin/Java/controlled JS/TS must compile cleanly to JVM
  `.class` or run through explicit backend runtime support.
- `shared/`: MVP shared code is `.qin` only and must compile portably to both
  JVM and JS targets.

Do not let cache reuse cross incompatible target zones.

## Required Observability

Cache and instant compilation logs should name:

- whether a layer hit or missed;
- the layer name;
- the stable identity or a short hash;
- the invalidating input category when known;
- elapsed time.

Required examples:

- `module-class disk cache hit`;
- `module-class compile cache hit`;
- `module-class dependency session hit`;
- `javac cache hit`;
- `module-class run batch start` / `module-class run batch done`;
- `workspace package index built` / `workspace package index cache hit`;
- `frontend transform disk cache hit`;
- `package materialization fresh`;
- `dependency fingerprint changed`;
- `hot module cache invalidated`.

Default dev-server logs should be concise enough not to become a performance
cost. Per-module module-class run tracing is diagnostic output, not normal
progress output, and should be enabled only with `QIN_MODULE_CLASS_TRACE=1` or
`-Dqin.moduleClass.trace=true`. This follows the same principle as Vite-style
debug logging: keep cache/phase summaries visible, and make very detailed
module traces opt-in.

Fullstack build profiling is likewise opt-in. Use `qin dev --profile`,
`qin build --profile`, `-Dqin.profile=true`, or `QIN_PROFILE=1` when diagnosing
startup or rebuild latency. The profile output must show phase timings for the
current standard path, including project layout resolution, dependency
materialization, source selection, backend compilation, frontend service
creation, frontend production emit, `javac`, and Qin build-coordinator stages.
This follows Kotlin/Gradle build-scan and Vite debug-mode practice: first expose
the slow boundary with cheap structured timings, then reproduce that boundary
with a focused smoke or probe before broadening validation.

The CLI layer is part of that standard path. `qin dev --profile` and
`qin build --profile` must also time the outer CLI phases before the fullstack
runtime starts: config loading, script dispatch checks, Qin entry/source
resolution, dependency sync/cache use, runtime classpath construction, and the
in-process `QinFullstackMain` invocation. If the runtime profile is fast but the
process still starts slowly, use these CLI timings to choose the smallest owning
layer instead of guessing from total wall time. Keep
`QinCliFullstackInProcessProfileSmokeTestMain` as the focused guard for this
profile surface.

Because fullstack `dev` and `build` now invoke `QinFullstackMain` in the same
JVM, their CLI environment phase must not run the legacy all-tools check that
spawns coursier, javac, and java version commands. The in-process fullstack
path may confirm the current JVM identity, then let dependency sync and
fullstack compilation perform their own specific checks. Reintroducing a broad
external `checkAll()` on this path is a startup regression unless a focused
profile proves it is the smallest necessary owning boundary.

Bundled/local Qin runtime classpath construction must also avoid broad
workspace recursion. When a source checkout needs sibling workspace classes,
the declared discovery boundary is each known sibling root's own
`build/classes` plus direct child package `build/classes` directories. Do not
walk into `node_modules`, hidden directories, `dist`, or arbitrary nested trees
to find runtime classes; those are incidental filesystem shape, not declared
Qin inputs. `QinCliSiblingWorkspaceClasspathSmokeTestMain` guards this direct
package-boundary rule.

Profiling is evidence, not a workaround. A slow phase found by `--profile`
should lead to an owning-layer cache, graph, parser, compiler, or runtime fix
with focused hit/invalidation coverage. Do not solve a slow phase by hiding
errors, forcing broad rebuilds, or asking project authors to clean caches.

If a smoke test uses a temp project root, it must still prove stable cache
identity or use a stable test root when the test is about cache reuse.

## Anti-Patterns

Do not treat these as fixes:

- deleting `.qin/cache`;
- forcing full rebuilds as the default;
- increasing timeouts or call-count limits to hide runaway compilation;
- embedding absolute temp paths in stable cache keys;
- compiling generated parser packages on every OVS transform;
- bypassing OVS/CSSTS/parser code with ad hoc string transforms;
- using Node/Vite external dev servers as Qin validation unless explicitly
  requested as a comparison.

## MVP Implementation Standard

For MVP, Qin should aim for:

- content-aware package stamps;
- lightweight dependency fingerprints;
- stable module-class cache identities for path-independent wrappers;
- OVS/CSSTS transform disk cache;
- focused smokes for cache hits and stale invalidation;
- dev server logs that explain cache behavior;
- no manual cache deletion in normal use.

This is the engineering equivalent of Qin's language design rule: elegant
source, explicit boundaries, predictable output, and fast feedback from a
toolchain that understands its own model.
## Parser And ESM Parity Rule

Instant compilation caches are only correct when they preserve the active language semantics. Qin/OVS/CSSTS frontend transforms must emit valid ESM, and cache hits must not preserve malformed AST debug output, stale token objects, or dropped properties.

When a parser/compiler cache hit exposes malformed ESM, reproduce the failing module boundary and compare the active generated parser/CST-to-AST/emitter path with the legacy handwritten TypeScript `slime-parser` for the same syntax. Use that comparison to repair the owning active layer and add a focused smoke for both fresh compile and cache-hit reuse.

Do not make cache reuse, parser conversion, or emitter normalization depend on a fallback that reads the legacy handwritten TS `slime-parser` CST shape when the generated result is empty. That creates two semantic paths and can cache a masked defect. The only acceptable permanent fix is a single active generated/parser/compiler path that emits standard ESM for both fresh and cached transforms.

## Subhuti Parser Framework Optimization

Parser performance should be improved first in the Subhuti parser framework,
not by requiring each grammar author to hand-optimize every hot syntax rule.
Grammar-local first-token dispatch may be used as a focused diagnostic or a
temporary proof of the slow boundary, but the durable Qin direction is a
framework-level prediction layer.

Qin parser cold-start work follows the same rule. Runtime ByteBuddy generation
is a historical Java-only diagnostic/reference mechanism, not a current Qin
standard path. The Qin-owned parser path should use a static enhanced parser
`.class` when the source grammar is known. A valid
static enhanced parser preserves the same `@SubhutiRule` wrapper semantics,
packrat keys, CST shape, AST output, and error behavior as the dynamic enhanced
class; it is not a raw-parser fallback. Parser construction must also avoid
framework-level repeated work such as recompiling token regexes that are already
matched with `Matcher.region(...).lookingAt()`. Parser hot classes must not
eagerly initialize heavyweight logging frameworks just to emit rare diagnostics;
diagnostics that only apply to non-standard raw parser construction should stay
on a lightweight path so the standard static parser can start quickly.
Focused parser performance probes must measure that same standard static path.
Do not use `SubhutiParser.create(...)`, `ByteBuddyParserFactory.createRaw(...)`,
or another runtime ByteBuddy route as the main Qin parser benchmark path. Those
routes are historical or diagnostic references only, and any performance fix
must be proven on the static enhanced `.class` wrapper and its `cst(false)`
recognizer mode when parser-only timing is required.

Decorator handling follows the canonical Qin JS compatibility rule in
`packages/qin-runtime-core/QIN_JS_COMPATIBILITY_MODEL.md`: Qin-owned decorators
are lowered at compile time into static `.class` wrappers, metadata,
initializer calls, JVM annotations, or rule tables. Runtime ByteBuddy,
reflection, or JavaScript descriptor emulation must not become the standard
fallback for a missing decorator lowerer.

When Qin emits or transforms JVM bytecode directly, prefer the JDK Class-File
API as the standard implementation path when it covers the required classfile
features. ASM is a mature third-party bytecode engineering reference and may be
studied or evaluated for gaps, but it should not replace the current Qin
Class-File API route merely because it is popular. ByteBuddy remains outside
the standard static `.class` path; it is not the mechanism for parser wrappers,
decorator lowering, or Qin-owned source-to-class compilation.

Subhuti should learn from mature parser frameworks by recording enough grammar
metadata to predict alternatives before executing them. The framework should
prefer first-set and lookahead based alternative selection, ambiguity reporting,
predicate or gate support for genuinely contextual choices, and optional
commit/cut semantics after a branch has consumed a decisive prefix. These
features must preserve the single active grammar path: they are parser
prediction and pruning, not fallback parsers, compatibility syntax, or degraded
success.

The target behavior is:

- an `Or(...)` with alternatives that start with distinct tokens should select
  the matching alternative without executing the others;
- an `Or(...)` with overlapping starts should either use declared lookahead or
  report an ambiguity during grammar analysis/profile mode;
- rules should expose profile counters for tried alternatives, skipped
  alternatives, ambiguous alternatives, memo hits/misses, and commit failures;
- parser generated Java and generated TypeScript should share the same
  prediction semantics;
- grammar authors should express syntax normally, adding explicit predicates
  only for real language ambiguity that the framework cannot infer safely.

The first implementation steps may keep the grammar surface unchanged, including
calls such as `Alternative.of(() -> ImportDeclaration())`. Subhuti may execute
those lambdas in a recording mode where rule wrappers do not build CST/cache
entries and token consumption records prediction tokens. The safe progression is
FIRST(1) first, then conservative LL(2) only for alternatives whose first token
is duplicated and whose recording data is complete for the whole `Or(...)`.
If any alternative needs contextual lookahead, nested `Or(...)`, side effects,
or otherwise has an unknown prediction, that `Or(...)` must not be partially
pruned. Unknown means "do not optimize this choice yet", not "try a second parser
path" and not "accept a fallback syntax".

Chevrotain-level LL(k) should be treated as the next architecture step, not as
"run every alternative on real parser state". Full LL(k) requires a grammar/GAST
recording model that records terminals, nonterminals, repetitions, options,
predicates/gates, and action boundaries separately from semantic actions. Until
that exists, deeper lookahead must stay conservative and covered by focused
smokes that prove both skipped alternatives and unchanged parse results.

A simplified grammar tree is useful because it turns "guess a first token by
executing lambdas" into analyzable parser structure. A node such as
`Alternative -> NonTerminal ImportDeclaration -> Terminal ImportTok` lets
Subhuti compute FIRST, nullable, follow, and LL(k) lookahead facts once per
parser class/callsite, cache the resulting branch-selection plan across parser
instances, and diagnose dynamic or ambiguous choices before runtime. Recording
only a first token can skip a few branches, but it cannot explain where the
token came from, compute deeper common-prefix lookahead reliably, or report why
an `A` versus `A B` choice must stay PEG-ordered. The tree is therefore the
framework-layer bridge toward Chevrotain-style self-analysis, not a second
parser path and not compatibility behavior.

The framework cannot honestly promise perfect automatic LL(k) for arbitrary Java
code inside grammar lambdas. It can be made complete for an analyzable grammar
subset: terminals, nonterminals, alternatives, options, repetitions,
nullable/follow sets, explicit predicates or gates, and semantic action
boundaries. If an alternative depends on arbitrary side effects, contextual host
code, unbounded lookahead, or unanalyzable predicates, the framework must report
that choice as dynamic or ambiguous instead of pretending it has a perfect
prediction. That is a grammar diagnostic, not a fallback parser.

The Chevrotain-style refactor should be a structural split, not another
runtime hot-path patch. Subhuti must separate at least four concerns:

- `SubhutiGrammarGraph` remains a grammar-fact and lookahead summary surface for
  partially modeled grammars. It may contain FIRST/lookahead approximations and
  must not be treated as an executable grammar body.
- a new exact GAST layer should represent complete grammar structure:
  `Rule`, `Alternative`, `Terminal`, `TerminalValue`, `NonTerminal`, `Option`,
  `Many`, `AtLeastOne`, `Gate`, and explicit `Action` boundaries. Only this
  layer may drive direct execution or generated runtime plans.
- a self-analysis layer should run once per parser class, resolve rule
  references, compute nullable/FIRST/FOLLOW/LL(k), detect duplicate or prefix
  ambiguity, and mark dynamic sections. Its output is a diagnostic report plus
  immutable lookahead/execution plans.
- runtime parser execution should consume those plans through stable callsite
  ids or generated wrapper code. It should not rediscover first tokens, rebuild
  alternative graphs, or query incomplete graph nodes on every rule call.

The migration order should be:

1. Introduce the exact GAST model beside the existing graph. Do not delete or
   reinterpret current `SubhutiGrammarGraph` entries; current graph data has
   proven to mix complete and FIRST-only facts.
2. Teach explicit grammar APIs such as `Alternative.rule/token/tokenValue`,
   `Option(Alternative...)`, and `Many(Alternative...)` to record exact GAST
   nodes when their bodies are analyzable. Plain `Alternative.of(...)` stays
   supported but records an `Action` or `Dynamic` node unless recording proves a
   complete structure without side effects.
3. Add a parser-class `SubhutiGrammarSelfAnalysis` pass that produces immutable
   per-callsite plans and warnings. Ambiguous, prefix-conflicting, or dynamic
   callsites stay on the ordinary PEG path with visible diagnostics.
4. Change static enhanced wrapper generation to embed stable callsite ids and,
   where analysis proves it, direct plan calls. This is the correct place for
   Chevrotain-like performance wins because the decision is made once, not by
   per-wrapper runtime graph lookup.
5. Only after exact GAST coverage exists, add successful-path execution plans:
   direct terminal/value consume for exact terminal rules, pass-through rule
   chains, token-set `MANY`/`OPTION`, and later compact expression-precedence
   plans. Each plan must preserve CST mode, recognizer `cst(false)` mode,
   packrat semantics where still needed, and PEG prefix order.

Rejected shortcuts define the boundary of this refactor. Do not use
`singleTerminalForRule(...)` as executable grammar; it may be only FIRST data.
Do not add runtime graph queries to every wrapper to discover small direct
execution cases. Do not globally disable packrat, loop detection, or state
snapshots because a tiny probe got faster. The accepted path is exact GAST plus
class/callsite self-analysis plus generated or cached immutable runtime plans.

Focused performance probes must measure both structural work avoided and real
wall-clock time. FIRST-token prediction that only skips cheap token mismatches
may be slower than ordinary PEG retry because it pays an extra lookahead cost;
common-prefix LL(2+) prediction can still be much faster when it avoids deeper
failed branches. Treat skipped-alternative counters as necessary evidence, but
not sufficient evidence, for parser speed work.

Runtime pruning must therefore be cost-gated by evidence, not by a permanent
ban on FIRST(1). A recorded `Or(...)` whose alternatives are complete,
non-dynamic, non-duplicated, and not a strict prefix conflict may enter the
runtime pruning hot path even when the decisive lookahead is a single token.
If focused probes show that a particular FIRST(1) callsite is slower than
ordinary PEG retry, keep that callsite analysis-only or optimize its plan cost;
do not turn the whole parser back into branch retry. A recorded `Or(...)` with
duplicated prefixes may expand conservatively to LL(k) up to the framework
limit and enable pruning only when the recorded token sequences are complete and
non-dynamic. The current Subhuti Java prototype uses the unchanged
`Alternative.of(...)` surface, records lambda callsite identities as the
prediction cache key instead of stack traces, keeps a hot in-process prediction
plan for repeated `Or` calls, and enables pruning for unambiguous LL(1) through
LL(k) choices while preserving PEG prefix ambiguity.

The runtime LL(k) matcher should be plan-like, not alternative-retry-like. When
all predicted alternatives share the same lexer-mode sequence for the relevant
lookahead depth, Subhuti reads the current token sequence once and matches
alternatives against that in-memory sequence. This is the Chevrotain-style
direction: prediction does cheap lookahead and branch selection before running
the selected grammar branch. Mixed lexer-mode predictions still use exact
mode-aware matching for each recorded sequence, but this remains the same active
parser semantics; it is not a compatibility path or alternate syntax.

LL(k) pruning must preserve PEG branch order. If one alternative prediction is
a strict prefix of another, such as `A` versus `A B`, the choice is not safely
LL(k)-selectable because the shorter earlier branch may be the correct PEG
result. Subhuti must keep that callsite analysis-only, report the ambiguity
through stats/diagnostics, and execute the normal grammar path. This is not a
fallback; it is the single standard parser semantics refusing an unsafe
optimization.

After the shared-lookahead matcher and analysis-only hot-call skip changes, the
older 20,000-item focused benchmark measured `FIRST1_DISTINCT` as slightly
slower when pruned. On 2026-07-10, the generated Slime TypeScript corpus
changed the accepted evidence: enabling unambiguous FIRST(1) runtime pruning
reduced `SlimeParser.ts` recognizer warm average from about `751.835ms` to
`611.999ms`, and reduced `SlimeAstCreateUtils.ts` rule core executions from
`30517` to `30338` while increasing skipped alternatives from `366997` to
`367199`. Keep the rule corpus-driven: measure the target generated parser
files, structural counters, and wall-clock together before retaining or
reverting a pruning policy.

The follow-up Chevrotain-style LL(1) cleanup keeps those same semantics but
uses a cheaper dispatch path for non-value-aware single-token plans. Instead of
building a `CurrentTokenNames` array for a plan whose lookahead depth is one,
Subhuti reads the current token once and looks up the candidate through the
same first-token plan table. This does not change branch selection, duplicate
checks, prefix ambiguity handling, or parser errors. On the same generated
Slime TypeScript corpus, `SlimeParser.ts` recognizer warm average moved from
about `611.999ms` to `460.789ms`, and `SlimeAstCreateUtils.ts` measured about
`927.578ms` with unchanged structural counters. Treat this as an accepted
hot-path implementation detail for LL(1) plans: it lowers dispatch overhead, but
it is still subordinate to the broader GAST/self-analysis goal.

The next framework step is parser-class/callsite prediction-plan reuse, closer
to Chevrotain self-analysis than per-instance probing. Subhuti keeps the
`Alternative.of(...)` grammar surface, records an analyzable plan once, stores
it in a parser-class scoped global prediction cache, and lets later parser
instances reuse that plan. Focused 5,000-item measurements on the Java runtime
showed the intended shape for common-prefix plans: `LL2_COMMON_PREFIX` uses
runtime pruning with a global plan hit and measured `72.996ms` vs `202.965ms`,
or `2.78x` faster; and `LL3_COMMON_PREFIX` measured `168.838ms` vs
`502.695ms`, or `2.98x` faster. FIRST(1) plans are no longer globally excluded:
they may prune when duplicate/prefix checks prove the choice safe and focused
corpus measurements prove the plan cost is worthwhile.
The global cache hit must be observable in stats such as
`orPredictionGlobalCacheHits=1`, while dynamic or incomplete choices remain
analysis-only rather than falling back to another parser path.

When comparing `parser-source-snapshot` with the current Qin/OVS stack, treat
the Node snapshot parser as a behavior oracle and a performance reference, not
as a runtime fallback. The two stacks share the PEG/Subhuti design goal, so token
streams, CST/AST shape, emitted ESM, and parser profiling counters should be
compared on the same smallest input and then widened to package and app flows.
If the current Java-generated TypeScript or Qin JVM path is materially slower
than the Node snapshot, first identify whether the extra cost is grammar-local
branch retry, generated Java-to-TS runtime overhead, missing framework cache
reuse, or Qin JS interpreter overhead. Fix the owning active layer directly.
For example, a generated TypeScript method that loses Java `synchronized` block
contents and turns the Subhuti global prediction cache into `return null` is a
Java CST/AST/lowering/generator defect; restore the single generated parser path
so `orPredictionGlobalCacheHits` proves cross-instance plan reuse.

Focused OVS measurements on `BalanceRow.ovs` and the 9-file balance-monitoring
fixture showed that current generated Java-to-TypeScript parser overhead is
dominated by generated runtime calls, not by a different OVS grammar. The Node
snapshot stayed around tens of milliseconds for the same single file, while the
current generated path remained hundreds of milliseconds after correctness fixes.
Framework-level fixes that proved useful were single-mode `LA(offset)` execution
that avoids constructing a one-item Java list for every ordinary lookahead, and
direct CST child appends instead of rebuilding parent CST nodes through builders
on every successful rule. A later focused probe also proved that packrat keys
should stay semantic but be cheap in the generated TypeScript runtime: the active
key still contains `ruleName`, `cacheKeyExtra`, `tokenIndex`, `currentMode`, and
`lastTokenName`, but the generated path should avoid allocating a Java-style key
object and re-entering Java `hashCode`/`equals` shims for every rule invocation
when a stable lightweight key preserves the same cache identity. After this
change, `BalanceRow.ovs` hot parsing moved into roughly the `199-231ms` range,
and the 9-file balance fixture measured around `526-567ms` on the default path
after warmup. A speculative last-token-entry microcache was rejected after
focused measurement because the generated TypeScript property checks added more
cost than the repeated map hit saved. Keep this as the pattern: compare with the
snapshot, change one active framework layer at a time, measure wall-clock and
profile counters, and discard negative optimizations instead of documenting them
as architecture.

2026-07-07 parser-only correction: the accepted comparison boundary is the same
`.ovs` source set at `new OvsParser(source) + Program/OvsProgram()`, with OVS
transform disk cache excluded and the Qin JVM toolchain/module classes warmed
before judging parser speed. On the balance-monitoring 9-file fixture, the Node
TypeScript snapshot measured about `125ms` warm parser-only while the current
Qin JVM active path measured about `26s` warm parser-only. Disabling Subhuti
OR prediction did not materially change the 9-file result, and focused probes
showed millions of `JavaEsmGlobal.__qin_binary__` calls per run. Small runtime
micro-optimizations to binary helpers, runtime function-definition caching, or
bound-this caching did not materially close the gap and should not be treated
as the solution. The owning bottleneck is that generated parser methods are
still executing through Qin's JavaScript runtime/interpreter path rather than
compiled JVM method bodies. The next durable fix should move an analyzable
generated parser method subset into direct JVM lowering, starting with the
Subhuti/OVS hot method shapes proven by the smallest parser-only probes.

2026-07-08 follow-up: the first direct-class probe proved a local single-file
subset with constructor, class field, `this.field = value`, and `extends` can be
lowered to JVM classes without using a fallback parser. The broader
`CssTsParser -> QinParser -> SubhutiParser` and `OvsParser -> CssTsParser`
chains still do not direct-lower because the module-class source rewrites
cross-module imports into local `__qin_export_get__` aliases, while declaration
class lowering only understands local same-module class names or `java:` class
imports. The owning fix is a cross-module JS/TS declaration-class index that
maps imported class values such as `QinParser` and `CssTsParser` to the emitted
JVM declaration binary names, then lets constructor and superclass emission use
that index. Do not treat Node `parser-source-snapshot`, legacy handwritten TS
parsers, or interpreted Qin JS class execution as a fallback for this gap; keep
them as oracle/profile inputs and repair the active Java/Qin lowering path.

After adding the prefix-ambiguity guard, the focused smoke
`SubhutiOrFirstTokenPredictionSmokeTestMain` proves `A | A B` remains
analysis-only and keeps PEG order. A 5,000-item benchmark on the same path
measured `FIRST1_DISTINCT` as analysis-only (`0.85x` in that run, so still not
runtime-pruned), `LL2_COMMON_PREFIX` at `79.384ms` versus `223.805ms`
(`2.82x`, `64.5%` wall-clock reduction), and `LL3_COMMON_PREFIX` at
`187.142ms` versus `508.727ms` (`2.72x`, `63.2%` reduction).

2026-07-09 Qin parser-only measurements on the same Windows machine compared
the static enhanced Qin/Subhuti parser with the Chevrotain structural parser
benchmark. Chevrotain measured `OvsConsumer.ts` at about `0.534ms` warm average
and `OvsParser.ts` at about `17.985ms`. The current Qin/Subhuti static CST-only
path, with a larger packrat cache and structured rule cache keys, measured
`OvsConsumer.ts` at about `6.309ms` and `OvsParser.ts` at about `525.116ms`.
A temporary FIRST(1)-all-runtime-pruning experiment measured about `5.894ms`
and `510.967ms`, so it was only a small OVS improvement and does not close the
architecture gap. Treat this as evidence that the next real step is grammar-tree
and callsite-plan self-analysis, not more isolated FIRST-token micro-tuning.
This OVS result is not a ban on FIRST(1) pruning. The 2026-07-10 generated
Slime TypeScript parser-only corpus showed a measurable positive result after
safe LL(1) pruning was enabled, so FIRST(1) pruning remains an accepted
Chevrotain-alignment primitive when it is protected by duplicate/prefix checks
and proven on the target corpus.

The same-day nested grammar-tree experiment showed why this step must be
bounded. Letting recording mode recursively expand nested `Or(...)` nodes can
prove a small smoke such as an outer alternative whose first rule contains
`A | B`, but real TypeScript expression grammar recursion made cold analysis
too expensive and at depth 8 caused stack overflow before a depth guard was
added. Depth 2 produced occasional warm improvements but the averages were not
stable enough to accept as a performance fix. The accepted small correction is
to keep analysis-only `SubhutiOrPrediction` objects available for diagnostics
and grammar recording instead of returning `null`; runtime pruning still stays
limited to already-proven LL(2+)/LL(3+) token-sequence plans. Future grammar
tree work should build an explicit bounded GAST/callsite analysis pass with
cycle detection and a cost budget, not recursively execute arbitrary grammar
lambdas deeper and deeper during ordinary parsing.

The recommended implementation path is Java-native Subhuti GAST, not embedding
Chevrotain or replacing Subhuti with ANTLR/JavaCC/Parboiled. Mature parser
projects are references for architecture: Chevrotain's `@chevrotain/gast`,
`performSelfAnalysis()`, and lookahead strategy show the desired layering, but
Qin needs a small JVM-owned model that preserves Subhuti's existing grammar
surface. The first accepted step is explicit rule-reference metadata such as
`Alternative.rule("ImportDeclaration", this::ImportDeclaration)` plus a
`SubhutiGrammarGraph` that resolves `RULE_REF` nodes with a visited set. This
proves the key distinction from failed deep recording: analyze a graph of rule
references with cycle detection, do not recursively expand arbitrary grammar
lambdas. Rule references remain dynamic for runtime pruning until a callsite
plan is built and validated, so this foundation must not be counted as a parser
speedup by itself.

The next accepted step is an explicit callsite lookahead plan. When a parser
opts in by exposing a `SubhutiGrammarGraph`, and every `Or(...)` alternative is
an `Alternative.rule(...)` reference that resolves to non-nullable,
non-dynamic FIRST tokens, Subhuti may build a
`SubhutiOrLookaheadPlan` without executing the alternatives in recording mode.
That plan maps current token names to PEG-ordered candidate alternative indexes
and is then wrapped in the normal `SubhutiOrPrediction` runtime pruning path.
When FIRST tokens are distinct, the candidate list has one alternative. When
FIRST tokens overlap, the plan may still prune alternatives whose FIRST set
cannot match the current token, but it must keep every overlapping candidate in
the original PEG order and report the ambiguity diagnostic. Recursive rules,
missing rule definitions, nullable alternatives, empty FIRST sets, or
alternatives that are not explicit rule references must keep the callsite
analysis-only or return to the normal PEG path; they are diagnostics, not
fallback parser behavior. The focused acceptance smoke is
`SubhutiGraphLookaheadRuntimeSmokeTestMain`: it proves
`orPredictionCandidateAlternatives=0`, `orPredictionSkippedAlternatives=1`, and
`runtimePruningEnabled=true` for a two-rule choice, meaning Subhuti selected the
branch from the graph plan instead of running grammar lambdas to discover FIRST.
The first real Slime parser rule connected to this path is
`SlimeModuleParser.ModuleExportName`, expressed as
`Alternative.rule("IdentifierName", this::IdentifierName)` versus
`Alternative.rule("StringLiteral", this::StringLiteral)`. The focused
`SlimeModuleGraphLookaheadSmokeTestMain` proves the same counters on a real
parser rule. This is still a narrow structural milestone: `OvsConsumer.ts`
parser-only measurements may show skipped alternatives from this rule, but the
overall OVS/TypeScript gap remains until larger callsites such as `ModuleItem`
and declaration/expression choices can expose complete graph facts.
The following step extended graph plans to support multiple FIRST tokens for
one alternative. `SubhutiOrPrediction` may now receive an explicit candidate
map, so a rule such as `ModuleItem` can map `Import -> alt0`, `Export -> alt1`,
and many statement-start tokens such as `Class`, `Function`, `IdentifierName`,
`LParen`, or `StringLiteral` to the same statement alternative. The focused
`SlimeModuleGraphLookaheadSmokeTestMain` proves `ModuleItem` skips import and
export for a `class` input with `orPredictionSkippedAlternatives=2` and
`orPredictionCandidateAlternatives=0`. This is still a structural milestone:
on 2026-07-09 the `OvsConsumer.ts` parser-only probe measured about `7.572ms`
warm average with `orPredictionSkippedAlternatives=20`, while `OvsParser.ts`
measured about `607ms` over 10 rounds. The skip count improved, but larger
declaration/expression callsites still dominate the Chevrotain gap.
The next focused step kept overlapping FIRST tokens useful instead of disabling
the whole graph plan. `SubhutiOrLookaheadPlan` now stores token -> candidate
list mappings, so a choice such as `ImportClause` can map `IdentifierName` to
the three default-import alternatives while mapping `Asterisk` directly to the
namespace alternative and `LBrace` directly to named imports. The focused
`SlimeModuleGraphLookaheadSmokeTestMain` proves `ImportClause` skips 4 branches
for `*` and skips 2 impossible branches for `IdentifierName`, with
`orPredictionCandidateAlternatives=0`. The same 2026-07-09 parser-only probe
then measured `OvsConsumer.ts` at about `8.225ms` over 100 warm rounds and
`OvsParser.ts` at about `573ms` over 10 warm rounds, with
`orPredictionSkippedAlternatives` rising to 32 and 95 respectively. This is a
valid framework capability and a modest large-file improvement, not the final
Chevrotain-level solution; remaining work is still declaration/expression graph
coverage plus packrat/CST cost reduction.
The next accepted Slime integration connected `StatementListItem` and
`Declaration` to the same graph path. `StatementListItem` may now skip
declaration parsing for clear statement-start tokens such as `Return`, while
`Declaration` may skip hoistable/class branches for `Const`. Because JavaScript
`let` is a soft keyword represented as `IdentifierName` in this lexer, lexical
declaration FIRST must include both `Const`/`Let` and `IdentifierName`; otherwise
class bodies containing `let hash = 17` can incorrectly fail after graph
pruning. The focused smoke covers `return`, `const`, and `let` separately before
the larger OVS probes. After this change, the 2026-07-09 parser-only probe
measured `OvsConsumer.ts` at about `7.696ms` over 100 warm rounds and
`OvsParser.ts` at about `499.887ms` over 10 warm rounds, with
`orPredictionSkippedAlternatives` rising to 40 and 622 respectively.
The next accepted expression-layer step connected `PrimaryExpression` to the
same graph path. Clear tokens such as `Class`, `This`, `Function`, `LBracket`,
`LBrace`, `LParen`, literal tokens, and template starts can now skip the other
primary-expression alternatives. Soft-keyword `async` remains represented by
`IdentifierName`, so the graph keeps the PEG-ordered candidate list
`AsyncGeneratorExpression`, `AsyncFunctionExpression`, and
`IdentifierReference` instead of choosing a single branch. The focused smoke
proves `class` skips 12 alternatives, while `foo` preserves the three
IdentifierName candidates and skips 10 unrelated branches. The 2026-07-09
parser-only probe then measured `OvsConsumer.ts` at about `7.729ms` over 100
warm rounds and `OvsParser.ts` at about `491.578ms` over 10 warm rounds, with
`orPredictionSkippedAlternatives` rising to 204 and 13352 respectively. The
large skip increase but small wall-clock change is evidence that the remaining
gap is no longer only FIRST(1) retry; the next bottleneck investigation should
look at packrat key/object churn, CST construction, and Java method/lambda
dispatch costs.

The next framework-alignment step changed explicit graph alternatives to use
the rule reference name, not the Java lambda class name, as their prediction
identity. This moves Subhuti cache keys closer to Chevrotain's structural
callsite model: two `Alternative.rule("A", ...) | Alternative.rule("B", ...)`
sites under the same parser class and rule scope reuse the same graph
lookahead plan even when their lambda bodies are different Java callsites. The
focused `SubhutiGraphLookaheadRuntimeSmokeTestMain` now proves the second
callsite receives `orPredictionGlobalCacheHits=1` and still skips the
impossible branch. This is correctness/architecture progress, not a large speed
claim by itself; the 2026-07-09 OVS probes still showed the same packrat/core
counts (`OvsParser.ts` around 90k rule core executions).

The same unit began expression hotspot graph migration by connecting
`UnaryExpression` and `MemberExpression` top-level choices to explicit
`Alternative.rule(...)` entries and adding the required graph FIRST summaries
for `PrimaryExpression`, `LeftHandSideExpression`, unary operator expressions,
`SuperProperty`, `MetaProperty`, and `NewMemberExpression`. The focused
`SlimeModuleGraphLookaheadSmokeTestMain` proves `super.x` now reaches
`MemberExpression` through graph pruning and skips three non-`Super`
alternatives. The broader `OvsConsumer.ts` and `OvsParser.ts` probes remained
near the previous wall-clock/counter range (`OvsConsumer.ts` about `10.139ms`,
`OvsParser.ts` about `578.067ms` in that run), which means these callsites are
valid graph coverage but not yet the main Chevrotain gap closer. Keep the
change because it removes more lambda-shape dependency and broadens GAST
coverage; continue with the remaining bottleneck at `LeftHandSideExpression`,
`AssignmentExpression`, packrat key churn, and CST/object allocation.

The next accepted framework primitive is a Chevrotain-style alternative gate:
`Alternative.rule(ruleName, gate, body)`. Subhuti evaluates the gate before
executing the alternative body, so a false gate does not enter the Java lambda,
does not run rule wrappers, and does not mutate parser state. This is not
fallback behavior; it is explicit grammar metadata equivalent to Chevrotain's
`GATE` predicate. `SubhutiGraphLookaheadRuntimeSmokeTestMain` proves two
same-FIRST alternatives can be selected by graph lookahead, then the first
alternative can be skipped by a false gate while the second succeeds, with
`orPredictionGateSkippedAlternatives=1`.

A direct attempt to use that gate by restoring bounded top-level assignment
operator scanning in `AssignmentExpression` was rejected. On `OvsConsumer.ts`
it reduced wrapper counts (`LeftHandSideExpression` 203 -> 123 and
`AssignmentExpression` 191 -> 111), but after the correctness bug around
`new Set([...])` was fixed, `OvsParser.ts` slowed to about `1483ms` warm
average because the bounded scan forced too much token lookahead. The accepted
state is therefore framework gate support only; `AssignmentExpression` still
needs a stronger structural solution, likely a common-prefix or Pratt-style
expression strategy, not repeated deep token scanning.

The next accepted Chevrotain-alignment step is parser-class self-analysis for
declared graph alternations. `SubhutiGrammarGraph` now carries declared
`ruleScope -> alternative rule names` metadata, and
`SubhutiGrammarSelfAnalysis` prebuilds `SubhutiOrPrediction` entries for those
callsites once per parser class before the first runtime `Or(...)` needs them.
This moves graph plan construction from lazy runtime recording toward
Chevrotain's `performSelfAnalysis()` shape while preserving the same active PEG
execution path for dynamic or undeclared callsites. The focused
`SlimeModuleGraphLookaheadSmokeTestMain` proves `orPredictionCacheBuilds=0`,
`orPredictionGlobalCacheHits=1`, and `orPredictionSelfAnalysisHits=1` for
declared callsites such as `ModuleExportName`, `ModuleItem`, `ImportClause`,
`StatementListItem`, `Declaration`, and `PrimaryExpression`. Same-day Qin
parser-only probes measured `OvsConsumer.ts` at about `9.475ms` warm average
and `OvsParser.ts` at about `551.531ms`; this confirms the self-analysis step
does not repeat the rejected deep-recording regression, but it is an
architecture milestone rather than the final speed closer. The remaining
Chevrotain gap is still dominated by expression/common-prefix structure,
packrat key/object churn, rule wrapper count, and CST allocation.

The next accepted expression-layer step is the first common-prefix factoring in
`AssignmentExpression`. Instead of parsing `LeftHandSideExpression` separately
for `=`, compound assignment, `&&=`, `||=`, and `??=`, Slime now parses one
shared `LeftHandSideExpression` prefix followed by a unified
`AssignmentOperatorAny` tail. This follows the Chevrotain direction of
factoring common grammar structure instead of retrying one branch per operator;
it is still normal parser grammar, not a fallback. The focused
`SlimeAssignmentExpressionCommonPrefixSmokeTestMain` covers plain identifiers,
`x = y`, `x += y`, `x &&= y`, `new Set([...items])`, and `(x) => x`, including
AST operator assertions for `+=` and `&&=`. The same-day Qin parser-only probes
measured `OvsConsumer.ts` between about `7-10ms` over 300 warm rounds and
`OvsParser.ts` between about `514-548ms` over 20 warm rounds. Structural counters
improved from the prior same-machine measurement:
`OvsParser.ts` `ruleWrapperCalls` moved from about `200447` to `189613`, and
`ruleCacheKeyBuilds` moved from about `113798` to `108576`. This is an accepted
incremental move toward a Pratt/common-prefix expression strategy; it does not
close the full Chevrotain gap by itself.

The next framework-level GAST step is explicit terminal alternatives.
`Alternative.token("Assign", ...)` lets a Subhuti `Or(...)` expose a Chevrotain-
style `Terminal` grammar node without executing the alternative body in
recording mode. `SubhutiGraphLookaheadRuntimeSmokeTestMain` proves a token-only
choice can runtime-prune and skip the impossible branch, and
`AssignmentOperatorAny` now uses mixed `Alternative.token(...)` and
`Alternative.rule(...)` entries. This is accepted as architecture alignment
because terminal/rule choices can enter graph lookahead directly. The same
`OvsConsumer.ts` and `OvsParser.ts` probes stayed near `9.132ms` and
`548.908ms`, so this particular connection is performance-neutral for the
current OVS files; the value is removing another lambda-recording dependency
before broader expression/operator graph coverage.

`AssignmentOperator` then entered the Slime grammar graph so the mixed
`AssignmentOperatorAny` choice is fully analyzable for compound assignment
tokens such as `+=`. The focused smoke asserts `orPredictionSkippedAlternatives=4`
and `orPredictionUnknownAlternatives=0` for `+=`. On the same OVS parser probe,
`OvsParser.ts` skipped alternatives rose modestly from about `13352` to `13412`
while rule wrapper/cache counts stayed unchanged (`ruleWrapperCalls=189613`,
`ruleCacheKeyBuilds=108576`). This is a useful graph-coverage increment, but
not a wall-clock improvement; the next speed work should keep moving toward
larger expression structure rather than treating operator token choices as the
main bottleneck.

The next accepted Chevrotain-alignment unit adds negative lookahead pruning and
lexer-mode-aware graph terminals. When a complete `SubhutiOrPrediction` has no
candidate for the current token, Subhuti may now fail the `Or(...)` immediately
instead of executing every branch once just to prove failure. This required
carrying `LexerMode` on grammar terminals and preserving the mapping from
multiple FIRST tokens back to their owning alternative, matching Chevrotain's
GAST alternative identity model. `RegularExpressionLiteral` is now recorded
with `LexerMode.REGEXP`; the focused `SlimeRegexGraphLookaheadSmokeTestMain`
covers `export const x = /#/` so default-mode `/` cannot be mistaken for a
missing candidate. The same unit moved the active monolithic `SlimeParser`
`MemberExpression` and `CallExpression` suffix choices to explicit
`Alternative.rule(...)` graph nodes, added decorator `At` to module/declaration
FIRST sets, and factored `UpdateExpression` so ordinary expressions parse one
`LeftHandSideExpression` prefix before postfix/TypeScript tail selection. The
2026-07-09 probes measured `OvsConsumer.ts` at about `9.263ms` warm average
over 300 rounds and `OvsParser.ts` at about `475.471ms` warm average over 10
rounds without rule profiling. Structural counters improved materially:
`OvsParser.ts` `ruleWrapperCalls` moved to `118034`, `ruleCoreExecutions` to
`63301`, and `ruleCacheKeyBuilds` to `71051`; `LeftHandSideExpression` wrapper
calls dropped from about `9773` to `2738` after the `UpdateExpression`
common-prefix step. This is accepted progress but not completion: the target is
still Chevrotain-like performance within roughly a 5-10x range, so the next
work should attack remaining `AssignmentExpression`, `TSType`, operator
precedence chain, packrat key churn, and CST allocation costs.

The following focused expression cleanup factored `ConditionalExpression` in the
same common-prefix direction. Slime now parses one shared
`ShortCircuitExpression` prefix and then an optional `? AssignmentExpression :
AssignmentExpression` tail, instead of trying a conditional branch and then
re-parsing the same short-circuit expression for the non-conditional case. The
focused `SlimeAssignmentExpressionCommonPrefixSmokeTestMain` covers
`flag ? yes : no` in addition to assignment-operator cases. The 2026-07-09
parser-only probes measured `OvsConsumer.ts` at about `9.665ms` warm average
over 100 rounds and `OvsParser.ts` at about `420.105ms` warm average over 10
rounds, with `OvsParser.ts` structural counters at `ruleWrapperCalls=116422`,
`ruleCoreExecutions=63301`, and `ruleCacheKeyBuilds=69893`. This is a valid
small Chevrotain-aligned cleanup, but the remaining gap is still dominated by
large-scale rule wrapper/cache/CST cost and expression/type grammar structure.

The next retained GAST migration connected TypeScript type grammar hotspots to
explicit `Alternative.rule(...)` metadata. `TSType`, `TSPrimaryType`,
`TSPrefixTypeOrPrimary`, and `TSKeywordType` now expose rule references, and
the Slime grammar graph carries their conservative FIRST sets. This is
Chevrotain-aligned because the parser framework can analyze the type choices as
grammar data instead of recording opaque Java lambdas at runtime. A focused
`SlimeModuleGraphLookaheadSmokeTestMain` case proves `TSPrimaryType` can choose
the `this` type through graph pruning. On 2026-07-09, `OvsParser.ts`
parser-only structural counters improved from `ruleWrapperCalls=116422`,
`ruleCoreExecutions=63301`, and `ruleCacheKeyBuilds=69893` to
`ruleWrapperCalls=110991`, `ruleCoreExecutions=59757`, and
`ruleCacheKeyBuilds=66322`; `TSType` wrapper calls dropped from `6688` to
`5585`. Wall-clock timings remained noisy, so treat this as a real structural
improvement but not the final speed closer. A key remaining architecture gap is
that many TypeScript contextual keywords still share the lexer token
`IdentifierName`, while Chevrotain-style grammars often use distinct keyword
token types or categories; future framework work may need value-aware terminals
or a stricter token model to prune those choices without hand-coded grammar
special cases.

The next framework step begins Chevrotain-style optional-production prediction.
Subhuti now has an `Option(Alternative.rule/token(...))` and
`Many(Alternative.rule/token(...))` overload that can inspect an analyzable
FIRST set before executing the optional/repeated body. When the current token
cannot start the alternative, the parser skips the optional body without
entering a rule wrapper, building a packrat key, or relying on failure and
restore. The overload is strict: if the parser is already in a failed state it
returns without changing that state, so an optional fast path cannot mask a
failed outer alternative. `TSTypeAnnotation` is the first production connected
to this path through a conservative `Colon` FIRST set and the
`OptionalTSTypeAnnotation()` helper. The focused smokes include arrow-function
cases to prove that failed alternatives remain visible. On 2026-07-09,
`OvsParser.ts` parser-only counters moved from `ruleWrapperCalls=110991`,
`ruleCoreExecutions=59757`, and `ruleCacheKeyBuilds=66322` to
`ruleWrapperCalls=110460`, `ruleCoreExecutions=59537`, and
`ruleCacheKeyBuilds=66011`; `TSType` wrappers dropped from `5585` to `5365`.
This is a small but important architecture step because Chevrotain optimizes
`OPTION`/`MANY` as well as `OR`; further gains require connecting more optional
hotspots and making contextual keyword lookahead more precise.

The next retained `OPTION`/`MANY` step adds `Alternative.tokens(...)` for a
single grammar body that may start with one of several terminals. This matches
Chevrotain's repetition lookahead shape for operator tails such as additive,
shift, equality, and logical expression continuations: the parser checks whether
the current token can start the repeated body before entering the body, instead
of executing the body once just to fail and restore. `SlimeBinaryExpressionParser`
now uses this path for binary operator repetitions and the short-circuit tail.
On 2026-07-09, the same `OvsParser.ts` parser-only profile probe moved from
`cstWarmAvgMs=591.323`, `ruleWrapperCalls=110460`,
`ruleCoreExecutions=59537`, and `ruleCacheKeyBuilds=66011` to
`cstWarmAvgMs=512.586`, `ruleWrapperCalls=95311`,
`ruleCoreExecutions=55628`, and `ruleCacheKeyBuilds=62102`. A 10-round
non-profile probe after the change measured `OvsParser.ts` at
`cstWarmAvgMs=425.222`, `cstBestMs=329.173`, and `OvsConsumer.ts` at
`cstWarmAvgMs=9.076`, `cstBestMs=3.193`. This is accepted as a real framework
alignment step, but it is still a partial bridge: the remaining Chevrotain gap is
dominated by expression/common-prefix structure and contextual-token choices that
still need fuller GAST coverage.

The follow-up retained step extends the same `Alternative.token(s)` discipline to
expression punctuation tails that were still executed as ordinary lambda bodies:
the conditional `? :` tail, comma expression tails, argument-list comma tails,
and optional-chain suffix repetitions. The parser now checks `Question`,
`Comma`, `QuestionDot`, or the optional-chain suffix start set before entering
those bodies. On 2026-07-09, after the binary-tail change above, the focused
`OvsParser.ts` profile probe moved from `ruleWrapperCalls=95311` to
`ruleWrapperCalls=93037`, with `AssignmentExpression` wrapper calls dropping
from `7519` to `5249`; `ruleCoreExecutions` stayed at `55628`, which is expected
because the optimization avoids wrapper/body entry on skipped optional/repeated
tails rather than changing successful core rules. The non-profile 10-round probe
measured `OvsParser.ts` at `cstWarmAvgMs=391.470`, `cstBestMs=330.935`, while
`OvsConsumer.ts` remained essentially flat at `cstWarmAvgMs=9.024`,
`cstBestMs=3.304`. This is another accepted small Chevrotain-style step, not the
main remaining gap closer.

The next retained framework step adds value-aware terminals for contextual
keywords represented by the lexer as `IdentifierName`. Chevrotain-style grammars
often use distinct keyword token types or token categories; Subhuti now exposes
the same information in its graph model through `SubhutiGrammarNode.terminalValue`
and the standard `Alternative.tokenValue(tokenName, tokenValue, body)` API. This
is not a second parser path: it is the single active graph/lookahead path learning
that `IdentifierName("number")`, `IdentifierName("readonly")`, and a generic
`IdentifierName` are different prediction facts. Focused smoke coverage proves
`TSKeywordType` parses `number` by executing only the number branch, preserves
custom type references through the standard `TSTypeReference` path, and proves
`TSTypeOperator` can skip non-`readonly` operator branches through
`Alternative.tokenValue`.

On 2026-07-09, after value-aware terminal support, the `OvsParser.ts` parser-only
probe improved structural counters from the previous expression-punctuation
step's `ruleWrapperCalls=93037`, `ruleCoreExecutions=55628` to
`ruleWrapperCalls=90957`, `ruleCoreExecutions=54136`, with skipped alternatives
at `51088`. The 10-round wall-clock probe measured `cstWarmAvgMs=432.037`,
`cstBestMs=361.166` for `OvsParser.ts`, and `cstWarmAvgMs=15.304`,
`cstBestMs=4.830` for `OvsConsumer.ts`. The counter improvement is real, but the
wall-clock gap to Chevrotain remains; the next work should reduce prediction/key
overhead and continue moving high-frequency expression/type callsites into
complete analyzable graph plans.

The immediate follow-up removed avoidable allocation from the value-aware
FIRST(1) prediction path. Most contextual-keyword graph choices only need the
current token, so Subhuti now queries `candidateIndexesForFirstToken(tokenName,
tokenValue)` directly instead of building a two-dimensional token-key matrix and
merging combinations. This keeps the same branch-selection semantics while
reducing prediction overhead. On the same 2026-07-09 probes, structural counters
remained `ruleWrapperCalls=90957`, `ruleCoreExecutions=54136`, but wall-clock
improved to `OvsParser.ts cstWarmAvgMs=411.762`, `cstBestMs=333.247`, and
`OvsConsumer.ts cstWarmAvgMs=12.907`, `cstBestMs=5.317`. The fact that counters
stayed flat while time improved is evidence that the bottleneck at this step was
prediction runtime cost, not additional grammar coverage.

The next retained framework step caches `Option(Alternative...)` and
`Many(Alternative...)` start predictions at the parser-class level. This moves
those optional/repetition checks closer to Chevrotain's precomputed
OPTION/MANY lookahead shape: analyzable graph terminals or rule references are
analyzed once, then later parser instances reuse the same start-token plan
instead of calling `grammarGraph().analyze(...)` on every hot optional or
repetition check. `SubhutiAlternativeStartPredictionSmokeTestMain` proves a
first parser builds one start prediction, a second parser reuses it through
`alternativeStartPredictionGlobalCacheHits=1`, and an impossible optional body
is skipped before execution. The 2026-07-09 OVS probes measured
`OvsConsumer.ts cstWarmAvgMs=9.100`, `cstBestMs=3.408`, and
`OvsParser.ts cstWarmAvgMs=424.816`, `cstBestMs=330.498`, with unchanged
`OvsParser.ts` structural counters (`ruleWrapperCalls=90957`,
`ruleCoreExecutions=54136`, `ruleCacheKeyBuilds=60609`). Treat this as a
small architecture cleanup rather than the main speed closer; the dominant
remaining gap is still expression/type structure, packrat key churn, and CST
allocation after branch selection.

The follow-up retained packrat-key cleanup replaced `Objects.hash(...)` in
`SubhutiRuleCacheKey` with a manual stable hash calculation. This preserves the
same equality fields and cache semantics while avoiding a varargs/object-array
allocation on every rule cache key construction. The 2026-07-09 focused smokes
still passed, and the same OVS parser-only probes measured
`OvsParser.ts cstWarmAvgMs=395.500`, `cstBestMs=319.977`, while structural
counters stayed unchanged (`ruleCacheKeyBuilds=60609`). Treat this as a real
framework overhead reduction: it does not make the grammar more Chevrotain-like
by itself, but it reduces the packrat machinery cost that remains while
Subhuti still needs packrat during the migration to stronger GAST/lookahead.

The next retained grammar-graph connection converts `TSTypeReference`'s
optional type-argument tail from `Option(() -> TSTypeParameterInstantiation())`
to `Option(Alternative.rule("TSTypeParameterInstantiation", ...))`, with
`TSTypeParameterInstantiation` declared in the graph as starting with `Less`.
This is the Chevrotain-style OPTION path applied to a hot TypeScript rule:
when a bare type reference such as `Custom` has no `<`, Subhuti can skip the
type-argument rule before entering its wrapper or building a packrat key.
`SlimeModuleGraphLookaheadSmokeTestMain` now proves the no-`<` path does not
execute `TSTypeParameterInstantiation` and uses an alternative start
prediction. On 2026-07-09, the `OvsParser.ts` 20-round parser-only probe
measured `cstWarmAvgMs=361.610`, `cstBestMs=310.156`, with structural counters
at `ruleWrapperCalls=90765`, `ruleCoreExecutions=54040`, and
`ruleCacheKeyBuilds=60513`. A profile run showed `TSType` wrapper calls moving
from `5365` to `5269`, and `TSTypeParameterInstantiation` no longer appeared
in the top 12 core execution list. This is still a small GAST coverage step,
but it is aligned with the desired self-analysis direction because it removes
failure-driven optional parsing from a real hot path.

The next retained framework step adds bounded LL(k) path analysis to
`SubhutiOrLookaheadPlan`. Subhuti can now expand analyzable grammar graph
sequences such as `IdentifierName LParen` versus `IdentifierName LBracket`
without executing alternatives in recording mode, matching Chevrotain's
practice of deriving lookahead paths from GAST rather than from failed runtime
branches. Prefix ambiguity remains conservative: if one alternative path is a
strict prefix of another, Subhuti keeps PEG-ordered first-token candidates
instead of pruning to the longer path. `SubhutiOrLookaheadPlanSmokeTestMain`
and `SubhutiGraphLookaheadRuntimeSmokeTestMain` prove both sides: non-prefix
common-first alternatives prune to the matching deeper path, while incomplete
rule graphs fall back to the standard recording diagnostic path rather than
pretending to have a complete plan. On 2026-07-09, focused smokes and
slime-parser smokes passed. The OVS parser-only probes measured
`OvsParser.ts cstWarmAvgMs=357.536`, `cstBestMs=290.502`, with unchanged
structural counters at `ruleWrapperCalls=90765`, `ruleCoreExecutions=54040`,
and `ruleCacheKeyBuilds=60513`; `OvsConsumer.ts` measured
`cstWarmAvgMs=5.945`, `cstBestMs=2.665`. Treat this as a necessary
Chevrotain-alignment capability, not a completed speed closer: current OVS hot
paths still need more non-prefix LL(k) graph coverage, expression/type
structure factoring, and CST/allocation reductions to approach the 5-10x
target.

The next retained Slime grammar step moves more TypeScript expression hot paths
from runnable PEG branches into analyzable graph facts. `TSTypeAssertion` is
now a real grammar graph rule starting with `<` and is part of the
`UnaryExpression` alternation, so ordinary identifiers no longer enter the
type-assertion branch just to fail. The `as` and `satisfies` expression tails
are also value-aware `IdentifierName` graph terminals, and the repeated tail
checks skip the body unless an identifier token can start one of those
standard tails. Focused smokes prove that identifier unary expressions skip
`TSTypeAssertion`, while bare, `as`, and `satisfies` assignment expressions
execute only the needed tail. On 2026-07-09, the same OVS parser-only probes
measured `OvsParser.ts cstWarmAvgMs=382.150`, `cstBestMs=302.177`, with
structural counters down to `ruleWrapperCalls=83489`, `ruleCoreExecutions=51815`,
and `ruleCacheKeyBuilds=56706`; `TSType` wrappers dropped to `4014`, and
`TSTypeAssertion` was no longer a top core hot rule. `OvsConsumer.ts` measured
`cstWarmAvgMs=8.833`, `cstBestMs=2.888`, with `ruleWrapperCalls=1359`,
`ruleCoreExecutions=759`, and `ruleCacheKeyBuilds=876`. Keep this direction:
contextual TypeScript syntax should enter the GAST/lookahead model as precise
token or token-value facts instead of remaining opaque runtime lambda probes.

The next retained common-prefix step factors `Arguments` from three `(`-prefixed
alternatives into one prefix plus analyzable optional `ArgumentList` and optional
trailing `Comma`. Subhuti's optional start prediction also now preserves
per-FIRST-token lexer modes instead of disabling prediction whenever the FIRST
set mixes default-mode and regexp-mode tokens. This matters because
`ArgumentList` can start with normal expression tokens or a
`RegularExpressionLiteral` in `LexerMode.REGEXP`; empty `()` arguments should be
skipped by lookahead instead of executing `ArgumentList` once to fail. Focused
smokes prove `()` does not call `ArgumentList`, while `(value)` and `(value,)`
call it exactly once. On 2026-07-09, the same parser-only probes measured
`OvsParser.ts cstWarmAvgMs=360.993`, `cstBestMs=301.649`, with structural
counters at `ruleWrapperCalls=81647`, `ruleCoreExecutions=51815`, and
`ruleCacheKeyBuilds=56432`; `OvsConsumer.ts` measured
`cstWarmAvgMs=5.943`, `cstBestMs=2.621`, with `ruleWrapperCalls=1331`,
`ruleCoreExecutions=759`, and `ruleCacheKeyBuilds=872`. This is a small but
real Chevrotain-style improvement: it removes another failure-driven optional
parse and keeps lexer-mode-aware prediction in the framework rather than in a
grammar-local workaround.

The 2026-07-09 follow-up makes `ArgumentList` itself more GAST-shaped without
changing accepted syntax: `ArgumentListItem` is now an explicit rule whose
alternatives are `SpreadArgument` and `AssignmentExpression`, and
`SubhutiGrammarGraph` declares that alternation. This removes one remaining
opaque runnable `OR` inside call arguments and increases parser-class
self-analysis coverage from 16 to 17 declared alternations. Focused smokes show
the intended structural effect on trailing-comma arguments: the inner
`ArgumentListItem` choice no longer needs runtime candidate/unknown recording
for the `value,` case. The larger `OvsParser.ts` parser-only counters stayed
flat (`ruleWrapperCalls=65861`, `ruleCoreExecutions=44511`,
`ruleCacheKeyBuilds=47262`), so this is retained as a small correctness-neutral
Chevrotain-alignment step, not claimed as a main performance closer. Two broader
framework experiments were rejected in the same session: generic
`Many(Runnable)` start prediction and rule-wrapper-level graph start prediction
both broke the focused `<number>foo` TypeScript assertion smoke. Until
`SubhutiGrammarGraph` is complete enough to be trusted as full GAST, such global
pruning must remain experimental and be discarded when focused syntax smokes
show legal paths are pruned.

A tiny follow-up applies the same `MANY` start-prediction rule to `TSTypeName`
dotted suffixes: `Identifier ('.' Identifier)*` now checks `Dot` before entering
the repeated body. On 2026-07-09 this moved `OvsParser.ts` wrapper calls from
`81647` to `81550` and `Identifier` wrapper calls from `2051` to `1954`, while
wall-clock remained within probe noise (`cstWarmAvgMs=365.241`,
`cstBestMs=285.029`). Keep it only as a minor structural cleanup; the remaining
gap is still dominated by expression-chain wrappers, `TSType`, and CST/packrat
costs.

The next retained expression factoring step changes `ExponentiationExpression`
from a failure-driven `UpdateExpression '**' ExponentiationExpression |
UnaryExpression` `OR` into a shared `UpdateExpression` prefix with an optional
`**` tail, falling through to `UnaryExpression` only when the update-expression
prefix cannot start. This is the same common-prefix transformation Chevrotain's
GAST/lookahead model rewards: ordinary identifiers no longer parse
`UpdateExpression`, fail on missing `**`, restore, then parse `UnaryExpression`
which re-enters `UpdateExpression`. The graph also records
`ExponentiationExpression` as an analyzable alternation, and focused smokes prove
`value` enters `UpdateExpression` once while `value ** other` accounts for the
right-hand side separately. On 2026-07-09, `OvsParser.ts` moved to
`cstWarmAvgMs=354.336`, `cstBestMs=270.783`, with `ruleWrapperCalls=78068`,
`ruleCoreExecutions=50746`, and `ruleCacheKeyBuilds=54294`; `OvsConsumer.ts`
measured `cstWarmAvgMs=5.351`, `cstBestMs=2.538`, with
`ruleWrapperCalls=1286`, `ruleCoreExecutions=744`, and `ruleCacheKeyBuilds=842`.
The profile shows the intended structural effect: `UnaryExpression` wrapper
calls dropped from `2563` to `1494`, and `UpdateExpression` cache hits dropped
out of the top hot list because the duplicate failure path was removed.

The follow-up retained step migrates `AssignmentExpression` from opaque runnable
alternatives to explicit `Alternative.rule(...)` entries for arrow, async arrow,
yield, assignment-tail, and conditional branches. The branch order and gates stay
unchanged, but the framework can now see the alternatives as grammar data and
record `AssignmentExpression` in self-analysis. On 2026-07-09 the OVS
parser-only probe measured `OvsParser.ts cstWarmAvgMs=347.819`,
`cstBestMs=287.513`, with `ruleWrapperCalls=74358`,
`ruleCoreExecutions=47303`, and `ruleCacheKeyBuilds=50850`; `OvsConsumer.ts`
stayed structurally flat at `ruleWrapperCalls=1286`, `ruleCoreExecutions=744`,
and `ruleCacheKeyBuilds=842`. The profile showed `AssignmentExpression` wrapper
calls dropping from `5249` to `4983`. This is still not a complete Chevrotain
self-analysis model, but it converts another hot opaque OR into named grammar
data and reduces failure-driven runtime work.

The next retained optional-tail step connects more `TSTypeParameterInstantiation`
sites to the same analyzable graph path. `NewMemberExpression`, `ClassHeritage`,
`TSImportType`, `TSExpressionWithTypeArguments`, and the cover call head now use
`Option(Alternative.rule("TSTypeParameterInstantiation", ...))`; the cover call
head is no longer expressed as an OR that tries `typeArguments + Arguments` and
then retries plain `Arguments`. Focused smokes prove `fn()` skips the optional
type-argument rule while `fn<T>()` executes it exactly once. On 2026-07-09, the
same OVS parser-only probe measured `OvsParser.ts cstWarmAvgMs=354.020`,
`cstBestMs=293.250`, with structural counters down to
`ruleWrapperCalls=70886`, `ruleCoreExecutions=46143`, and
`ruleCacheKeyBuilds=49690`; `OvsConsumer.ts` measured `cstWarmAvgMs=5.842`,
`cstBestMs=2.682`, with `ruleWrapperCalls=1237`, `ruleCoreExecutions=728`, and
`ruleCacheKeyBuilds=825`. The profile confirms the intended effect:
`TSType` wrapper calls dropped to `2855`, `Arguments` wrappers dropped to `1992`,
and `CoverCallExpressionAndAsyncArrowHead` remains a successful single standard
path instead of a fallback parse.

The next retained TypeScript optional-rule step applies the same graph start
prediction to `TSTypeParameterDeclaration`. A standard
`OptionalTSTypeParameterDeclaration()` helper now wraps
`Option(Alternative.rule("TSTypeParameterDeclaration", ...))`, and the grammar
graph records `TSTypeParameterDeclaration` as starting with `Less`. This removes
failure-driven entry into generic parameter declarations at class, function,
method, interface, and TS signature sites when the source is not generic. The
focused smoke proves ordinary input skips the rule and `<T>` executes it once.
On 2026-07-09, `OvsParser.ts` moved from `ruleWrapperCalls=70886`,
`ruleCoreExecutions=46143`, and `ruleCacheKeyBuilds=49690` to
`ruleWrapperCalls=70357`, `ruleCoreExecutions=45920`, and
`ruleCacheKeyBuilds=49384`; `OvsConsumer.ts` moved to
`ruleWrapperCalls=1229`, `ruleCoreExecutions=725`, and
`ruleCacheKeyBuilds=820`.

The next retained TypeScript class-member step applies the same graph-aware
start prediction to decorators and class modifiers. `TSDecorators`,
`TSAbstractModifier`, and `TSAccessibilityModifier` are now represented in the
grammar graph by `At` or value-aware `IdentifierName` terminals, and the class
member optional/repeated modifier sites use `Alternative.rule(...)` rather than
opaque lambdas. `TSAccessibilityModifier` itself also uses
`Alternative.tokenValue(...)` branches for `public`, `private`, `protected`,
`readonly`, `override`, `declare`, and `accessor`, so a matching modifier does
not retry the other contextual keyword branches. Focused smoke coverage proves
plain class member identifiers skip decorator/abstract/accessibility starts,
decorator input executes `TSDecorators` once, and a matching accessibility
modifier executes the modifier rule once. On 2026-07-09, the same parser-only
probes moved `OvsParser.ts` to `ruleWrapperCalls=69326`,
`ruleCoreExecutions=45666`, and `ruleCacheKeyBuilds=48417`, with
`cstWarmAvgMs=346.729` and `cstBestMs=279.116`. `OvsConsumer.ts` moved to
`ruleWrapperCalls=1188`, `ruleCoreExecutions=716`, and
`ruleCacheKeyBuilds=782`, with `cstWarmAvgMs=5.443` and `cstBestMs=2.550`.
The profile run showed `TSAccessibilityModifier` cache hits down to `146` and
`TSDecorators` at `47`; the remaining dominant gap is still expression-chain
wrappers, `TSType`, packrat key churn, and CST allocation rather than this
modifier/decorator entry path.

The next retained expression step removes another failure-driven TypeScript
branch from `PrimaryExpression`. The Slime TypeScript override now models the
choice as `Alternative.rule("TSTypeAssertion", ...)` versus the standard
`Alternative.rule("PrimaryExpression", ...)` path. Because the graph already
knows `TSTypeAssertion` starts with `<` while the standard primary expression
starts with normal expression tokens, ordinary identifiers and literals no
longer execute the type-assertion rule just to fail. Focused smoke coverage
proves `foo` skips `TSTypeAssertion`, while `<number>foo` still executes the
type assertion once and consumes the input. On 2026-07-09, the parser-only
probes moved `OvsParser.ts` from `ruleWrapperCalls=69326`,
`ruleCoreExecutions=45666`, and `ruleCacheKeyBuilds=48417` to
`ruleWrapperCalls=65861`, `ruleCoreExecutions=44511`, and
`ruleCacheKeyBuilds=47262`; `OvsConsumer.ts` moved from
`ruleWrapperCalls=1188`, `ruleCoreExecutions=716`, and
`ruleCacheKeyBuilds=782` to `ruleWrapperCalls=1140`,
`ruleCoreExecutions=700`, and `ruleCacheKeyBuilds=766`. The profile confirmed
`TSTypeAssertion` left the top core/cache-put hot list and `TSType` wrappers
dropped from `2855` to `1700`. Wall-clock remained noisy on this small run
(`OvsParser.ts cstWarmAvgMs=352.721`, `cstBestMs=304.850`), so keep judging
this step primarily by the structural counters and hotspot removal.

The next retained framework/cache step hydrates parser-class global
`SubhutiOrPrediction` entries into each new parser instance once per parser
class. This moves reused graph/lookahead plans closer to Chevrotain's
parser-class self-analysis model: later parser instances no longer repeatedly
probe the global cache for every callsite; they preload the known prediction
keys for that parser class into the instance cache. The focused
`SubhutiGraphLookaheadRuntimeSmokeTestMain` now accepts either the old direct
`orPredictionGlobalCacheHits=1` proof or the stronger
`orPredictionGlobalHydratedPredictions=1` proof for duplicate callsites.

The same retained grammar cleanup converts pure binary and assignment operator
choices from opaque token-consumer lambdas into explicit
`Alternative.token(...)` terminals. This does not change the accepted grammar;
it makes those `OR` sites visible as terminal alternatives to the Subhuti graph
model. The focused `SlimeAssignmentExpressionCommonPrefixSmokeTestMain` now
proves `AssignmentOperatorAny` on `+=` skips four impossible top-level
alternatives plus eleven impossible `AssignmentOperator` token alternatives,
for `orPredictionSkippedAlternatives=15`. On 2026-07-09, the same
`OvsParser.ts` 20-round parser-only probe measured
`cstWarmAvgMs=371.768`, `cstBestMs=293.831`, and `cstColdMs=1835.130`, with
`orPredictionGlobalHydratedPredictions=4971`,
`orPredictionSkippedAlternatives=54820`, `ruleWrapperCalls=65839`,
`ruleCoreExecutions=44500`, and `ruleCacheKeyBuilds=47251`. Compared with the
prior same-path result, this adds small structural skip coverage but does not
move wall-clock outside normal probe noise. Keep it as Chevrotain-alignment and
cache-hydration groundwork; the remaining 5-10x target still requires broader
expression/common-prefix GAST coverage plus packrat/CST allocation reduction.

The next retained TypeScript tail cleanup converts `TSAsExpressionTail` from an
opaque runnable `OR` into `Alternative.token("Const", ...)` versus
`Alternative.rule("TSType", ...)`. Focused smoke coverage proves both
`as const` and `as number`: the `as const` path stays analysis-only because
`const` can overlap with TypeScript type syntax, while the `as number` path can
runtime-prune impossible branches and still executes `TSType` exactly once. The
same `OvsParser.ts` 20-round parser-only probe measured
`cstWarmAvgMs=361.064`, `cstBestMs=305.066`, and `cstColdMs=2000.663`, with
the same broad structural counters as the previous run
(`ruleWrapperCalls=65839`, `ruleCoreExecutions=44500`,
`ruleCacheKeyBuilds=47251`). Keep this as a safe GAST visibility step, not a
wall-clock closer.

A rejected experiment in the same area tried to make `NewExpression` parse the
`new` branch before `MemberExpression` and to wrap its optional arguments in a
graph-aware `Option(Alternative.rule("Arguments", ...))`. A focused
`new Target` smoke passed, but the real `OvsParser.ts` probe failed with
unconsumed input at a later `const`, proving the branch order changed real
expression consumption. That shape must not be retained as a performance
shortcut; future `new` optimization needs a faithful common-prefix or lookahead
plan that preserves the existing successful `MemberExpression`/`NewExpression`
boundary on full files.

The 2026-07-09 framework follow-up adds multi-alternative
`Many(Alternative...)` and `Option(Alternative...)` containers to Subhuti. This
keeps repeated or optional suffix choices visible as grammar data instead of
forcing grammar authors back into `Many(() -> Or(...))` / `Option(() -> Or(...))`
opaque lambdas. Focused smokes prove member suffixes and optional postfix tails
skip impossible alternatives through graph runtime pruning. On the same
`OvsParser.ts` parser-only probe, this increased pruning visibility
(`orPredictionSkippedAlternatives=57532`) but did not reduce true rule work
(`ruleCoreExecutions=44500`, `ruleCacheKeyBuilds=47251`), so it is retained as
Chevrotain-style grammar-shape groundwork rather than claimed as a wall-clock
closer. A follow-up candidate-direct-execution experiment was rejected: it kept
the same structural counters and worsened the 20-round warm average in probe
noise, proving that the next real optimization must reduce rule-core entry, not
micro-adjust already selected suffix candidates.

The same session retained a parser-core fail-fast fix: in normal parsing,
`executeRuleWrapper` now returns immediately when `parseSuccess` is already
false, before wrapper profiling, top-level checks, loop keys, or cache work.
This matches the Chevrotain-style direction where once a sequence element fails,
the following rule calls in that failed branch should not pay wrapper overhead.
Focused Subhuti and Slime smokes passed. The `OvsParser.ts` parser-only probe
moved `ruleWrapperCalls` from `65839` to `47250` while leaving
`ruleCoreExecutions=44500` and `ruleCacheKeyBuilds=47251`; wall-clock stayed in
the same noisy band. Treat this as a valid framework overhead reduction and a
diagnostic milestone: the remaining gap is dominated by real expression/type
rule-core executions, CST allocation, and packrat/cache-key work.

The next profiling step splits rule-core counts into success and failure hot
lists when `-Dsubhuti.profile.rules=true` is enabled. This is diagnostic-only
instrumentation: normal parsing behavior is unchanged, but OVS parser probes
can now distinguish unavoidable successful precedence rules from failed PEG
probes that should become lookahead/factoring targets. The first use of that
failure profile showed the hottest avoidable chain was
`CallExpression -> CoverCallExpressionAndAsyncArrowHead -> Arguments` on plain
left-hand-side expressions. `hasCallExpressionPostfixAhead()` now performs a
shallow, conservative token gate for simple primary heads: it skips the
`CallExpression` branch only when the next token cannot start call, type
argument, member, optional-chain, or tagged-template suffix syntax. Focused
smokes prove both `foo` and `foo()` still parse as `LeftHandSideExpression`.
On `OvsParser.ts`, this reduced real work from `ruleCoreExecutions=44500` to
`41600`, `ruleCacheKeyBuilds=47251` to `43769`, and the 20-round warm average
to `345.675ms`. The failure hot list moved to TypeScript and statement probes,
so the next Chevrotain-style target should be another measured failed core
cluster, not further call-expression micro-tuning.

The next retained step applies the same principle to `PrimaryExpression`'s
contextual async function branches. `async function` and `async function *`
share the `IdentifierName` first token with ordinary identifiers, so pure
FIRST(1) graph pruning must keep all three candidates. Subhuti now adds
explicit gates for `AsyncFunctionExpression` and `AsyncGeneratorExpression`
that inspect only the shallow standard shape: `async` followed without a line
terminator by `function`, and then optionally `*` for generators. This keeps
legal async function expressions on the standard path while preventing ordinary
identifiers from entering both async function rules just to fail. Focused
smokes prove plain `foo` skips the async candidates, while `async function(){}`
and `async function*(){}` still parse fully. On `OvsParser.ts`, this moved
`ruleCoreExecutions=41600` to `40470`, `ruleCacheKeyBuilds=43769` to `42639`,
and removed `AsyncFunctionExpression` / `AsyncGeneratorExpression` from the
failure hot list.

The next Chevrotain-alignment step moves `Statement` from opaque runnable
branches to explicit `Alternative.rule(...)` branches backed by grammar-graph
FIRST summaries. Clear statement starts such as `{`, `var`, `;`, `if`, loops,
`continue`, `break`, `throw`, `try`, `debugger`, and `with` can now reach only
their owning statement branch. Contextual gates preserve the standard semantics:
`return` is skipped before execution when `Return` is not allowed, and
`foo:;` gates `ExpressionStatement` so `LabelledStatement` owns the label form.
Focused `SlimeModuleGraphLookaheadSmokeTestMain` cases cover expression,
labelled, block, variable, empty, and return-disallowed statements. On the same
`OvsParser.ts` parser-only probe, this moved the counters to
`ruleWrapperCalls=39236`, `ruleCoreExecutions=37068`, and
`ruleCacheKeyBuilds=39237`, with `orPredictionSkippedAlternatives=61520` and
`orPredictionGateSkippedAlternatives=3786`. The 20-round warm average measured
`378.992ms`, so this is retained as a real rule-core reduction while the
remaining wall-clock gap still points at expression/type precedence work,
packrat/cache-key cost, CST allocation, and Java dispatch overhead.

The follow-up type-focused step adds shallow grammar gates and tighter graph
FIRST data for TypeScript type prefixes. `TSType` now gates
`TSTypePredicate`, `TSFunctionType`, and `TSConstructorType` before entering
their rule wrappers; `TSPrefixTypeOrPrimary` similarly gates `TSTypeQuery`,
`TSTypeOperator`, and `TSInferType`. The grammar graph records predicate forms
such as `asserts`, `this is`, and `IdentifierName is`, and constructor type
starts as `new` or `abstract new`, instead of treating every `IdentifierName`
as a possible predicate or constructor. Focused smokes prove plain `Custom`
types skip predicate/constructor/query/operator/infer, while `value is Custom`,
`abstract new () => Custom`, `keyof Custom`, and `infer T` still route to their
own standard branches. On `OvsParser.ts`, this moved the counters to
`ruleWrapperCalls=37492`, `ruleCoreExecutions=35416`, and
`ruleCacheKeyBuilds=37493`, with `orPredictionGateSkippedAlternatives=4380`
and a 20-round warm average of `332.764ms`. The previous type failure cluster
(`TSInferType`, `TSTypeOperator`, `TSTypeQuery`, `TSTypePredicate`, and
`TSConstructorType`) disappeared from the failure hot list; the next hot
avoidable cluster is now decorators and call-expression probes.

The next retained decorator cleanup removes the remaining opaque decorator
entry points from `FormalParameter`, `ClassDeclaration`, and repeated
decorator tails. Those sites now use `OptionalTSDecorators()` or
`Many(Alternative.rule("TSDecorator", ...))`, and the grammar graph records
`TSDecorator` as starting with `At`. Focused smokes prove ordinary parameters
and undecorated class declarations skip `TSDecorators`, while decorated input
still enters the standard decorator branch. On `OvsParser.ts`, this moved
`ruleCoreExecutions=35416` to `35255`, `ruleCacheKeyBuilds=37493` to `37331`,
and `ruleWrapperCalls=37492` to `37330`; `TSDecorators` disappeared from the
failure hot list and `TSDecorator` failures dropped from `204` to `112`. The
20-round warm average was noisy at `346.379ms`, so keep this step for structural
Chevrotain alignment rather than as a wall-clock claim.

The next call-expression gate step tightens the existing shallow
`hasCallExpressionPostfixAhead()` predicate for simple primary heads. Instead
of treating every member suffix as proof that a `CallExpression` should run, it
scans a bounded simple member chain and enters the call branch only when it sees
real call/type-argument-call syntax such as `foo.bar()` or `foo[bar]()`.
Unknown complex shapes remain conservative. Focused smokes prove `foo.bar` and
`foo[bar]` parse as left-hand-side expressions without entering
`CallExpression`, while `foo.bar()` and `foo[bar]()` still execute the standard
call branch. On `OvsParser.ts`, this moved `ruleCoreExecutions=35255` to
`34670`, `ruleCacheKeyBuilds=37331` to `36628`, `ruleWrapperCalls=37330` to
`36627`, and the 20-round warm average to `304.294ms`. The previous
call-expression failure cluster (`Arguments`,
`CoverCallExpressionAndAsyncArrowHead`, `CallExpression`, `ImportCall`, and
`SuperCall`) disappeared from the failure hot list.

The next retained TS declaration graph step moves `SlimeParser.Declaration`
from opaque TS declaration lambdas to explicit `Alternative.rule(...)`
branches for `TSInterfaceDeclaration`, `TSTypeAliasDeclaration`,
`TSEnumDeclaration`, `TSModuleDeclaration`, `TSDeclareStatement`, and a
`StandardDeclaration` rule reference. The grammar graph now records those
declaration starts directly, so declaration choices can use parser-class
self-analysis instead of runtime branch probing. Focused smokes prove standard
`const` skips all TS declaration branches, `interface` and `declare` route only
to their owning TS declarations, and class method bodies still accept
contextual `let` declarations. The contextual `let` case is important: the
gate must match the existing `LexicalDeclaration` grammar (`Let` token or
`IdentifierName("let")`) instead of pruning a valid standard declaration. On
2026-07-09, the same `OvsParser.ts` 20-round parser-only probe measured
`cstWarmAvgMs=302.425`, `cstBestMs=245.709`, and `cstColdMs=1732.191`, with
`ruleWrapperCalls=35951`, `ruleCoreExecutions=33994`, and
`ruleCacheKeyBuilds=35952`. Compared with the previous call-expression gate
baseline, this reduces another small declaration failure cluster while keeping
the single standard parser path.

The follow-up QinParser override step applies the same principle to Qin's
`object` declaration extension. `QinParser.Declaration` no longer uses opaque
two-branch lambdas for `QinObjectDeclaration` versus the inherited Slime
declaration path. The Qin object branch now exposes its start tokens explicitly
(`IdentifierName` for contextual `object`, plus `At` for decorated objects),
while the non-Qin branch gates through the inherited Slime/TypeScript
declaration starts. A focused `QinParserDeclarationGateProbeMain` proves
`const` and `type` route through the non-Qin standard path, while `object`
routes through the Qin object branch. The existing
`QinParserObjectDeclarationSmokeTestMain` still proves object declarations,
decorated object declarations, exports, type-keyword `object`, and ordinary
class declarations. On the same `OvsParser.ts` parser-only probe, this moved
`ruleWrapperCalls=35951` to `35503`, `ruleCoreExecutions=33994` to `33546`,
and `ruleCacheKeyBuilds=35952` to `35504`, with `cstWarmAvgMs=289.051` and
`cstBestMs=253.218`.

The next retained grammar-visibility step keeps moving failed class/member
and literal probes into analyzable alternatives. `UpdateExpression` now parses
prefix `++`/`--` through token alternatives and shares one
`LeftHandSideExpression` prefix for postfix and bare forms. `Literal`,
`BooleanLiteral`, `TSLiteralType`, and `BindingPattern` expose their token or
rule starts directly. `SlimeParser.MethodDefinition` now gates
`AsyncGeneratorMethod`, `AsyncMethod`, `GeneratorMethod`, getter, setter, and
plain class-element-name branches instead of forcing ordinary methods through
async/generator failures. Focused smokes prove bare/postfix/prefix update
expressions plus `value(){}`, `async value(){}`, `async(){}`, and `*value(){}`
stay on the standard parser path. The same `OvsParser.ts` 20-round
parser-only probe moved `ruleWrapperCalls=35158` to `34886`,
`ruleCoreExecutions=33201` to `32932`, `ruleCacheKeyBuilds=35159` to `34887`,
and `orPredictionGateSkippedAlternatives=4616` to `5062`; the async/generator
method failure cluster disappeared from the hot failure list. The probe also
exposed a framework follow-up: a pure terminal alternative such as `Asterisk`
still needed an explicit gate in a hydrated prediction plan to avoid one failed
runtime branch, so Subhuti terminal-only plans should be tightened at the
framework layer rather than relying on grammar authors to add redundant gates.

The follow-up framework step fixes that issue at the Subhuti layer. Graph
lookahead is no longer all-or-nothing when an `OR` mixes analyzable terminal or
rule alternatives with unknown/dynamic alternatives. Dynamic alternatives stay
in the candidate set to preserve PEG order and correctness, while known
alternatives whose lookahead does not match the current token are skipped. This
is not fallback behavior; it is the standard callsite lookahead plan becoming
closer to Chevrotain's self-analysis model while still accepting partially
modeled Subhuti grammar. Focused `SubhutiGraphLookaheadRuntimeSmokeTestMain`
coverage proves a known `Asterisk` branch is skipped on `IdentifierName` even
when another dynamic branch remains. The broader
`SlimeModuleGraphLookaheadSmokeTestMain` also proves the redundant
`GeneratorMethod` gate can be removed from `SlimeParser.MethodDefinition`.
On `OvsParser.ts`, the same 20-round parser-only probe measured
`ruleWrapperCalls=34830`, `ruleCoreExecutions=32876`,
`ruleCacheKeyBuilds=34831`, `orPredictionSkippedAlternatives=64479`,
and `cstWarmAvgMs=299.156`. This is a small but real framework-level
reduction from the prior `32932` core executions; the remaining gap to the
Chevrotain-test baseline (`warmAvg=5.67ms` for the 9 OVS fixtures in the same
session) is now dominated by successful expression/type precedence chains,
packrat/cache-key work, and CST allocation rather than this all-or-nothing
lookahead defect.

The 2026-07-09 follow-up focused probes confirmed that diagnosis more directly.
Minimal expression files such as `foo;`, `foo.bar;`, `foo();`, and `foo + bar;`
each have only 2-4 tokens, but still execute roughly 30-40 parser rules and
build one CST rule node for almost every successful expression-precedence layer.
For the real `OvsParser.ts` probe, `orPredictionUnknownAlternatives=0` and
`orPredictionSkippedAlternatives=64479`, while `ruleWrapperCalls=34830`,
`ruleCoreExecutions=32876`, and `ruleCacheKeyBuilds=34831` remain. That means
the important remaining Chevrotain gap is no longer missing FIRST-token pruning;
it is Subhuti's executable-rule stack still building rule wrappers, packrat
entries, and CST nodes for deterministic successful pass-through rules.

A tested but rejected framework experiment was to skip packrat writes for all
rules executed inside a lookahead-unique `OR` candidate. The focused smoke showed
cache-key count falling on tiny inputs, but real `OvsParser.ts` exceeded the
30-second focused-probe timeout because large files lost useful packrat reuse
inside the selected branch. Do not revive this broad skip. The next viable
Chevrotain-style step must be more precise: either CST pass-through for
analysis-proven transparent rules, grammar-graph level precedence factoring, or
another optimization that reduces successful rule/CST overhead without removing
packrat reuse from nested ambiguous work.

The first accepted Chevrotain-style successful-path primitive is explicit CST
output control in Subhuti. `SubhutiParserCore.cst(false)` turns a parser instance
into a recognizer/parser-only mode: token consumption, rule wrappers, loop
detection, packrat behavior, and parse errors remain active, but token/rule CST
nodes are not allocated. This is not a fallback parser and it does not change the
grammar. It separates parser execution depth from the CST stack through
`ruleExecutionDepth`, because a Chevrotain-like parser cannot use CST construction
as the source of truth for nested rule execution. The focused
`SubhutiCstOutputModeSmokeTestMain` proves the boundary: the same three-rule
successful chain stays at `ruleWrapperCalls=3`, `ruleCoreExecutions=3`, and
`ruleCacheKeyBuilds=3`, while CST work drops from `ruleCstNodes=3` and
`tokenCstNodes=1` to zero. This confirms the remaining gap is separable into CST
allocation versus wrapper/cache overhead; follow-up work should next reduce
wrapper/cache cost or add analysis-proven CST pass-through for CST-producing
paths.

The next accepted recognizer-path reduction removes parsed-token list storage
from ordinary `cst(false)` parsing. Subhuti now keeps an independent
`parsedTokenCount` for rule-cache keys, loop detection, state save/restore, and
cache-hit recovery, while storing `parsedTokens` only when CST/debug/error
recovery paths need concrete token objects. The focused
`SubhutiCstOutputModeSmokeTestMain` proves default CST parsing still reports
`parsedTokenCount=1` and `parsedTokenListSize=1`, while recognizer mode reports
`parsedTokenCount=1` and `parsedTokenListSize=0`. This is another Chevrotain-like
separation: recognizer execution no longer treats token history objects as the
source of truth for parser position. It does not remove packrat itself; wrapper
calls, rule-core executions, and cache-key creation remain the next measured
successful-path costs.

Performance work must be measured after each coherent local state advance. Do
not pile several parser optimizations together and infer their effect afterward.
For the CST/token-list recognizer work, `SubhutiRecognizerPerformanceProbeMain`
uses a focused 10-rule successful chain and clears rule cache on each round so it
measures real successful-path execution instead of top-level cache hits. On
2026-07-09, 50,000 rounds measured default CST at `totalMs=318.178`,
`avgUs=6.364`, and recognizer mode at `totalMs=188.310`, `avgUs=3.766`, about a
40.8% local improvement. Structural counters stayed equal for wrapper/core/cache
work (`ruleWrapperCalls=11`, `ruleCoreExecutions=11`,
`ruleCacheKeyBuilds=11`, `ruleCachePuts=11`), proving that the accepted change
only removed CST/token-history overhead and that rule-cache/wrapper overhead is
the next target.
The same probe also runs `recognizer-no-cache` as a diagnostic comparison, not
as a production fallback. On the same 50,000-round run, recognizer mode with
packrat enabled measured `totalMs=171.206`, `avgUs=3.424`, while
`recognizer-no-cache` measured `totalMs=86.813`, `avgUs=1.736` with
`ruleCacheKeyBuilds=0` and `ruleCachePuts=0`. This confirms that successful-path
packrat key/result/map overhead is a major remaining cost, but disabling cache
globally is not the standard fix. The framework should only use a Chevrotain-like
light path when grammar analysis proves the callsite can avoid packrat safely.
The first attempt to check pass-through rules by querying the grammar graph from
each rule wrapper reduced cache counters but regressed wall-clock time, so it was
discarded. The accepted direction is a precomputed parser-class grammar plan:
when a rule is proven to be a non-recursive single-rule pass-through, parser-only
`cst(false)` mode may skip that parent rule's packrat cache while still executing
the standard child rule. On 2026-07-09, the 200,000-round focused probe showed
plan-off recognizer at `avgUs=2.476` and plan-on recognizer at `avgUs=2.285`
while reducing `ruleCacheKeyBuilds` and `ruleCachePuts` from 11 to 1
(`ruleCachePassThroughSkips=10`). This is a valid Chevrotain-style initialization
plan step, but the remaining gap is still wrapper/core rule execution, not OR
prediction or CST allocation alone.
The next accepted step is pass-through wrapper/core fusion for the same proven
rules: in parser-only mode, a non-top-level non-recursive pass-through rule may
execute its child directly without creating a rule wrapper/cache boundary. On the
same 200,000-round probe, recognizer mode measured `avgUs=1.175` with
`ruleWrapperCalls=2`, `ruleCoreExecutions=2`, `ruleCacheKeyBuilds=1`, and
`ruleWrapperPassThroughSkips=9`; `recognizer-no-cache` measured `avgUs=0.884`.
This confirms the main remaining Subhuti/Chevrotain gap was successful-path rule
stack overhead. The optimization is still analysis-gated: CST output, error
recovery, top-level parse setup/EOF checks, debug tracing, and non-pass-through
rules stay on the standard wrapper path.

Static enhanced parser wrappers should also avoid avoidable argument-packaging
work on the hot path. For a single-argument generated wrapper, pass that
argument directly as `cacheKeyExtra` instead of generating
`cacheKeyExtra(new Object[] {arg0})`; the helper returns the same value for one
argument, but the temporary array still adds allocation churn before every
wrapped rule call. This is a static-wrapper generation rule, not a grammar
fallback. On 2026-07-10, applying it to `SlimeParserStaticEnhanced` kept the
same structural parser counters and moved the generated
`SlimeAstCreateUtils.ts` recognizer benchmark from about `525ms` average /
`499ms` best to about `469ms` average / `424ms` best with warmup=3 and
rounds=5. The broader Chevrotain gap remains successful-path rule wrapper,
packrat, and grammar-plan cost; this wrapper-shape cleanup is a retained small
step because it removes pure generated-code overhead.

The follow-up successful-path cache policy removes two more unnecessary packrat
costs without changing the grammar path. Top-level rules no longer build or
write packrat entries, because there is no parent callsite that can reuse that
entry. In parser-only `cst(false)` mode, grammar-graph-proven terminal leaf
rules also skip packrat cache while still executing their normal rule body. The
focused smoke suite proves CST mode, recognizer mode, grammar graph terminal
leaf discovery, and nested cached rules separately. On 2026-07-09, the
100,000-round focused probe measured `default-cst avgUs=3.232`, recognizer
`avgUs=0.926`, and diagnostic `recognizer-no-cache avgUs=0.642`, with
`ruleCacheKeyBuilds=0`, `ruleCachePuts=0`,
`ruleCacheTerminalLeafSkips=1`, and `ruleWrapperPassThroughSkips=9` on the
recognizer path. The remaining gap is now mostly the two still-real rule
executions plus parser-state bookkeeping, not OR prediction or packrat key
allocation on this focused chain.

The next boundary probe split that remaining recognizer cost into reset, raw
token consumption, top-level rule execution, and pass-through chain execution.
On 2026-07-09, the 1,000,000-round focused probe showed reset at about
`0.023us`, raw recognizer token consumption at about `0.218us`, top leaf rule
execution at about `0.198us`, and the 10-rule pass-through chain recognizer at
about `0.445us`. The accepted code change keeps `_consumeTokenMatch` on the
same standard path but avoids calling CST and parse-record helpers when CST
output and error recovery are both disabled. This is a parser-only hot-path
guard, not a second parser. A separate experiment that precomputed a combined
recognizer non-cache rule set was not retained because the focused wall-clock
result was not stable enough across boundary probes.

The same probe then exposed token cache key construction as a cost on
recognizer paths that have no prior lookahead. Subhuti now lets
`_consumeTokenMatch` read the current token directly only when CST output is
disabled, error recovery is disabled, and the token cache is empty. If a prior
`LA(1)` populated the cache, the standard cache path is still used. The focused
probe covers both `raw-consume` and `lookahead-consume`; on the retained run,
the 10-rule recognizer chain measured about `0.455us` versus the earlier
post-cache-policy baseline of about `0.531us`, while `recognizer-no-cache`
measured about `0.318us`. The remaining gap is now dominated by pass-through
method/lambda dispatch and the terminal rule's memoization-policy check.

On 2026-07-10, Subhuti removed that terminal-leaf memoization-policy check from
the recognizer successful path when the grammar graph proves a non-top-level rule
is a terminal leaf. In parser-only `cst(false)` mode, and only when debugger and
error recovery are disabled, a terminal leaf rule is now inlined the same way as
a graph-proven pass-through rule. The focused `SubhutiCstOutputModeSmokeTestMain`
proves CST mode is unchanged while graph recognizer mode drops to only the top
rule wrapper. On the 200,000-round `SubhutiRecognizerPerformanceProbeMain`,
`recognizer` moved from `avgUs=0.984` to `avgUs=0.803`; on
`SubhutiRecognizerBoundaryProbeMain`, `chain-top` moved from `avgUs=1.077` to
`avgUs=0.757`, with `ruleWrapperCalls=1`, `ruleCoreExecutions=1`, and
`ruleWrapperPassThroughSkips=10`. This is a Chevrotain-style self-analysis step:
the framework executes the pre-analyzed terminal leaf directly instead of paying
the full rule wrapper on the successful path.

On 2026-07-10, Subhuti widened the recognizer-only inline plan from
single-rule pass-through and terminal leaves to grammar-graph-proven
non-recursive rules. In `cst(false)` mode, with debugger and error recovery
disabled, a non-top-level rule whose grammar graph cannot reach itself may skip
the outer rule wrapper and execute the standard rule body directly. Packrat
memoization is also restricted to speculative `Or` branches that may be
backtracked; deterministic recognizer rule calls do not write cache entries just
in case. This remains analysis-gated, not a grammar-local shortcut: CST output
and recovery modes keep the normal wrapper/cache path. The parser-only proof
used the generated slime parser TypeScript files as the benchmark corpus: all
61 files under `generated/qin-parser-ts/com/slime/parser` parsed successfully in
Qin recognizer mode, while `SlimeParser.ts` moved to about `336ms` warm average
and `SlimeAstCreateUtils.ts` to about `842ms`. Structural counters showed the
intended direction: `SlimeParser.ts` dropped from roughly 54k wrapper calls to
42k, and `SlimeAstCreateUtils.ts` from roughly 176k to 129k. Chevrotain remains
faster on the same two files, so the next target is still the remaining
successful-path rule execution cost, not OVS-specific grammar patches.

The next retained Chevrotain-alignment step applies the same idea to `Or(...)`:
after lookahead/runtime pruning has selected a single candidate, Subhuti no
longer saves the full parser state before executing that branch. State snapshots
are still required for multi-candidate PEG fallback, gates that may skip all
candidates, and error-recovery/tolerant collection. The structural proof is the
`orPredictionStateSaves` / `orPredictionStateSaveSkips` pair in
`getOrPredictionStats()`. On the focused `SubhutiOrPredictionBenchmarkMain`
run with `-Dsubhuti.benchmark.items=100000`, the LL(2) and LL(3) runtime-pruned
scenarios both showed `orPredictionStateSaves=0` and
`orPredictionStateSaveSkips=100000`, with LL(2) at about `157ms` vs `191ms`
without pruning and LL(3) at about `183ms` vs `285ms` without pruning. This is
not fallback behavior; it is the standard predicted `Or` path avoiding a
PEG-era state copy that Chevrotain-style lookahead has already made unnecessary.

The same single-candidate rule also applies inside `Many(Alternative...)`.
When graph/lookahead prediction has already selected exactly one candidate and
the parser is not in error recovery or tolerant collection mode, Subhuti executes
that alternative directly for the current iteration instead of wrapping it in
`tryAndRestore(() -> OrPlanned(...))`. The iteration still saves the starting
state so a failed candidate or zero progress cleanly terminates the repetition;
it simply avoids the extra planned-OR layer that Chevrotain-style lookahead has
made redundant. The focused proof is
`SubhutiManyAlternativesLookaheadSmokeTestMain`, which checks member-style
suffix parsing and requires the single suffix iteration to report
`orPredictionStateSaves=0` and `orPredictionStateSaveSkips=1`. On 2026-07-10,
the generated Slime TS parser-only probe showed a small wall-clock win on
`SlimeAstCreateUtils.ts` while leaving parser semantics and CST-capable paths
unchanged.

On 2026-07-10, Subhuti also narrowed the recognizer-mode state snapshot cost.
When `cst(false)` is active and the parsed-token object list is empty, `saveState`
and `restoreState` no longer inspect the CST stack or trim the parsed-token list;
they restore only the cursor, source position, last token name, and token count.
This preserves the same PEG backtracking state but removes CST/token-list work
from parser-only probes. `SubhutiCstOutputModeSmokeTestMain` still proves CST
mode unchanged. On the focused generated Slime TypeScript corpus,
`SlimeParser.ts` measured about `388ms` warm recognizer average,
`SlimeAstCreateUtils.ts` about `819ms`, and
`SlimePrimaryExpressionCstToAst.ts` about `151ms`, with structural counters
unchanged. Treat this as a successful-path state-cost cleanup, not an OR
prediction or grammar-local syntax change.

On 2026-07-09, `readonly string[]` exposed a correctness bug in Subhuti's hot
`Or` prediction shortcut: matching only Java lambda classes can confuse different
`Alternative.rule(..., Runnable)` callsites that have the same arity and wrapper
class shape. Hot alternative predictions must match both the supplier class and
the structural prediction identity such as `rule:TSTypeOperator` or
`rule:TSPrimaryType`. This keeps the optimization Chevrotain-like: a reused
lookahead plan is tied to grammar identity, not incidental JVM lambda class
shape. The focused Slime smoke now proves that top-level `TSType` and nested
`TSPrefixTypeOrPrimary` no longer share an incompatible hot prediction.

On 2026-07-10, JFR sampling on the generated Slime TypeScript parser-only
benchmark showed `predictionSequenceMatchesCurrent(...)` and lexer regex
matching as hot methods. Subhuti now caches current lookahead token keys by
lexer-mode sequence within one mixed-mode `Or(...)` prediction, so alternatives
with the same mode sequence reuse the same lookahead instead of reparsing it per
candidate. This is a Chevrotain-alignment step: a callsite reads lookahead once
per required mode sequence, then matches alternatives against that in-memory
view. It is not fallback behavior and it does not change branch order, CST
shape, packrat keys, or parse errors. Focused validation covered
`SubhutiCstOutputModeSmokeTestMain`, `SubhutiGraphLookaheadRuntimeSmokeTestMain`,
and generated Slime TS parser-only probes. On the same machine,
`SlimeParser.ts` moved from about `357ms` to `282ms` warm recognizer average,
and `SlimeAstCreateUtils.ts` from about `796ms` to `744ms`.

The same parser-only pass then removed string concatenation from Subhuti token
cache keys. `_getOrParseTokenEntry(...)` now uses a structured key containing
the source index, lexer mode, and previous token name instead of building
`"index:mode:lastToken"` strings for every lookahead read. This keeps the same
lexer dependency on `lastTokenName` while reducing allocation, string hashing,
and `StringBuilder` samples seen in JFR. Focused Subhuti smokes stayed green;
with the mixed-mode lookahead reuse plus structured token keys, `SlimeParser.ts`
measured about `259ms` warm recognizer average in a 12-round probe and
`SlimeAstCreateUtils.ts` about `680ms` in an 8-round probe.

The follow-up kept that structured cache identity but made lookup allocation
lighter: a parser instance reuses a mutable lookup key for `HashMap.get(...)`
and creates the immutable `SubhutiTokenCacheKey` only on cache miss before
`put(...)`. This preserves the stored cache key contract while avoiding key
allocation on cache hits. Focused Subhuti smokes stayed green. On generated
Slime TS parser-only probes, `SlimeParser.ts` measured about `265ms` warm
recognizer average and `SlimeAstCreateUtils.ts` about `587ms`.

The next Chevrotain-style step reused Subhuti's graph-aware
`SubhutiOrLookaheadPlan` from ordinary recording-mode `Or(...)` prediction when
a parser exposes `grammarGraph()`. This lets explicit `Alternative.rule(...)`
references and recorded grammar nodes move from analysis-only metadata to
runtime pruning without changing grammar source syntax. Focused smokes prove
that explicit rule references skip the non-matching branch at runtime. In
generated Slime TS parser-only probes, the change reduced `SlimeParser.ts`
`orPredictionStateSaves` from about `13,661` to `11,877`, and
`SlimeAstCreateUtils.ts` from about `34,965` to `28,153`; wall-clock stayed
roughly comparable, with `SlimeParser.ts` slightly faster and the larger file
near baseline variance.

The follow-up observability step profiles candidate counts after Subhuti OR
prediction. This is diagnostic data, not a grammar change. It confirms that the
remaining Chevrotain gap is concentrated in a few analyzable call shapes rather
than in unknown alternatives: generated Slime TS parser-only probes reported
`unknownAlternatives=0` while `IdentifierName` still averaged `5.00`
candidates, `FormalParameters` averaged `4.00`, and
`CoverParenthesizedExpressionAndArrowParameterList` averaged `8.00` on
`SlimeAstCreateUtils.ts`. This makes the next useful framework work more
specific: improve value-aware token/category prediction and common-prefix
lookahead for these successful-path hotspots, then prove the result with the
same generated Slime TS parser-only probes before widening to OVS or CSSTS.

The first `IdentifierName` experiment proved what not to do: expanding the
`IdentifierName` graph rule into every hard keyword token made upper grammar
plans noisier and broke `SlimeModuleGraphLookaheadSmokeTestMain`. The
Chevrotain-style model is token categories, not graph expansion. Subhuti should
grow a framework-level token category concept: token definitions may declare
that a concrete token such as `Class`, `Const`, or `Await` belongs to the
`IdentifierName` category; `SubhutiMatchToken` and prediction matching should
expose both the concrete token and category keys. Grammar graph rules can then
refer to the category without losing concrete-token precision or inflating
alternatives. This is the correct next architecture step for `IdentifierName`
and keyword-like prediction; do not replace it with local parser special cases
or a larger hand-written graph.

Framework optimization must be proven with focused parser probes before it is
trusted by OVS, CSSTS, Qin, or Java parser users. A correct performance fix must
preserve token -> CST -> AST -> emitted ESM -> integration behavior while
reducing unnecessary rule execution.

On 2026-07-10, the focused generated Slime TS parser-only probe confirmed that
token categories are not the largest remaining Subhuti/Chevrotain gap. They
remain the right model for keyword-like `IdentifierName` prediction, but the
largest measured win is Chevrotain-style token-stream parser input. On
`SlimeAstCreateUtils.ts`, recognizer mode with rule profiling moved from
`avgMs=492.051 bestMs=437.058` to `avgMs=289.321 bestMs=265.864` when the parser
used an explicit pre-tokenized default-mode stream; with rule profiling disabled
it moved from `avgMs=455.756 bestMs=406.563` to
`avgMs=251.337 bestMs=214.991`. The same run reduced `tokenCacheGets` from about
`721k` to about `58k` and added about `663k` token-stream hits. Therefore the
next maximum-return framework work is to make token-stream input a standard,
mode-safe parser-core primitive, then reduce the remaining successful-path
wrapper/cache costs. Token category work should follow as a targeted lookahead
model improvement, not be treated as the primary wall-clock closer.

The next accepted token-stream step on the same date moved pre-tokenized
recognizer access closer to Chevrotain's token-index cursor. Subhuti now uses
the parsed token ordinal to read the current token and default-mode lookahead
sequences directly from the pre-tokenized token array when CST and recovery are
off. On `SlimeAstCreateUtils.ts`, the same static enhanced parser-only probe
improved from `avgMs=373.488 bestMs=289.573` at the start of the session to
`avgMs=275.659 bestMs=222.191`; a two-file generated Slime TS probe measured
`SlimeParser.ts avgMs=171.830 bestMs=120.743` and
`SlimeAstCreateUtils.ts avgMs=258.512 bestMs=240.658`. The retained lesson is
that token-stream input should be a first-class parser-core model, not just a
lexer cache shortcut. Continue reducing the remaining gap through
token-array/cursor execution and successful-path wrapper/cache reduction before
treating token categories as the maximum-return item.

The follow-up current-token entry cache keeps that same Chevrotain-style cursor
direction. In parser-only `cst(false)` mode, non-recovery pre-tokenized default
mode parsing may cache the current `TokenCacheEntry` for the exact parser state
`currentIndex`, line, column, parsed token count, lexer mode, and previous token
name. Repeated `LA(1)`, `isEof()`, or immediate consume calls at the same state
then reuse the same entry instead of touching the token array again. This is not
a syntax fallback and it is not enabled for CST or recovery paths. The focused
`SubhutiCurrentTokenEntryCacheSmokeTestMain` proves one token-array read and two
cache hits for `LA(1), LA(1), consume`. On `SlimeAstCreateUtils.ts`, the
parser-only probe reduced `tokenStreamGets` from roughly `333k` to about `200k`
with about `132k` `currentTokenEntryCacheHits`; a three-round retained run
measured `avgMs=379.819 bestMs=329.542`. Treat the structural reduction as
accepted, while continuing to judge wall-clock movement with repeated focused
probes because single-run parser timings are noisy.

A follow-up `SubhutiPackratCache.getNullable(...)` experiment was rejected. It
removed `Optional` allocation from the hot rule-cache lookup path, but
`SlimeAstCreateUtils.ts` regressed from the retained token-cursor result around
`avgMs=275.659 bestMs=222.191` to roughly `avgMs=299-324ms`. Do not prioritize
small allocation cleanups in the packrat API unless a focused probe shows
wall-clock improvement; the remaining larger gap is still token-array/cursor
execution and successful-path rule wrapper/cache structure, not this `Optional`
surface.

A static low-yield memoization expansion was also rejected. Adding
`BindingIdentifier`, `StatementListItem`, and several TypeScript type pass-through
rules to `recognizerLowYieldMemoRules()` lowered `ruleCacheKeyBuilds` from
`45871` to `39271` and `ruleCachePuts` from `32106` to `25507`, but
`SlimeAstCreateUtils.ts` regressed from about `avgMs=259.305 bestMs=215.383` to
about `avgMs=316.705 bestMs=253.855`. Do not treat lower packrat counters as
proof of a better Chevrotain-style runtime path. Keep low-yield memo policy
changes only when the same focused parser-only benchmark improves wall-clock
time.

A terminal-leaf direct-execution experiment was rejected because it exposed an
architecture boundary: the current `SubhutiGrammarGraph` often stores FIRST-token
approximations, not complete rule bodies. For example, graph data can say
`ImportDeclaration -> Import`, but the real rule must also consume the import
clause, `from`, string literal, and ASI. Treating that FIRST-token graph as a
complete Chevrotain-style GAST made parsing stop at the `{` after `import`.
Therefore, any future lightweight execution plan must first distinguish
FIRST/lookahead graph facts from exact grammar-tree nodes. Do not execute a rule
directly from `singleTerminalForRule(...)` unless the graph node is proven to be
the complete rule body, not merely its start token.

The Chevrotain-style self-analysis cache must also be keyed by the exact GAST
identity, not only by parser class and callsite. A parser class/callsite pair
can only reuse a direct lookahead/execution plan when the resolved grammar body
that produced the plan is the same grammar revision. If a `SubhutiGastGrammar`
instance gains or changes rule bodies, its analysis revision changes and cached
plans for the old revision must not be reused. This keeps the architectural
split honest: `SubhutiGrammarGraph` may remain a partial FIRST/lookahead fact
surface, while `SubhutiGastGrammar` owns exact rule-body planning. Missing GAST,
dynamic lambda alternatives, recursive unresolved rules, or stale grammar
revisions must stay analysis-only or rebuild the plan; they must not silently
execute a plan derived from different rule bodies.

The next accepted Chevrotain-style step is declared exact GAST alternations.
`SubhutiGastGrammar` may declare OR callsites such as `ModuleItem ->
ImportDeclaration | ExportDeclaration` beside exact rule bodies. Then
`SubhutiGastSelfAnalysis` can build `SubhutiOrPrediction` entries directly from
that grammar, without executing the alternatives in recording mode. This differs
from `SubhutiGrammarGraph.putAlternation(...)`: graph alternations may still be
FIRST/lookahead summaries, but GAST alternations are eligible only when all
referenced rule bodies resolve through exact `SubhutiGastNode` structure. The
runtime may hydrate these plans through the normal parser-class self-analysis
cache; if a referenced rule is dynamic, missing, recursive in an unsupported
way, or only represented by graph facts, no executable GAST prediction is
created. Focused smoke coverage must prove that an import-like rule keeps an
LL(k) prefix derived from the complete body rather than collapsing to the single
FIRST token.

The runtime lookup order should prefer declared exact GAST self-analysis plans
before per-callsite runtime GAST analysis. When `gastGrammar().alternations()`
is non-empty, `getOrPrediction(...)` first builds the standard callsite key,
hydrates the parser-class self-analysis cache, and reuses that plan if present.
Only if no declared plan exists should it fall back to runtime
`SubhutiGastCallsiteAnalysis.fromAlternatives(...)`, graph lookahead, or
recording-mode prediction. This moves Subhuti closer to Chevrotain's model:
known grammar callsites are analyzed once per parser class, while non-declared
or dynamic callsites still use the conservative standard path. The focused
`SubhutiGastRuntimePredictionSmokeTestMain` proves the boundary by requiring a
declared `Choice -> A | B` parser to hit self-analysis, skip the runtime
GAST/graph/recording cache build, and still prune the unchosen branch.

The next retained exact-GAST migration applies the same rule to real Slime
syntax. `MetaProperty` is now declared as a complete exact body:
`MetaProperty -> NewTarget | ImportMeta`, where `NewTarget` is `New Dot
IdentifierName("target")` and `ImportMeta` is `Import Dot
IdentifierName("meta")`. The parser rule uses `Alternative.rule(...)` with the
same rule names, allowing the declared GAST self-analysis plan to select
`import.meta` without executing the `NewTarget` branch. The focused
`SlimeMetaPropertyGastSelfAnalysisSmokeTestMain` proves that both `import.meta`
and `new.target` consume the full source, that `orPredictionSelfAnalysisHits`
is used, and that the unchosen branch is not executed. Treat this as the model
for future Chevrotain-style migrations: first declare complete GAST rule bodies,
then attach the real `Or(...)` callsite to those rule names, then prove the
chosen branch and unchosen branch behavior with a smallest focused smoke before
measuring broad parser speed.

The follow-up `ModuleExportName -> IdentifierName | StringLiteral` retained the
same exact-GAST pattern on another real Slime rule whose complete body was
already available. Adding `gast.putAlternation("ModuleExportName",
"IdentifierName", "StringLiteral")` lets the declared self-analysis plan choose
a string literal export name without first executing the `IdentifierName`
branch. `SlimeModuleExportNameGastSelfAnalysisSmokeTestMain` proves both
identifier and string-literal export names consume the full source, hit
`orPredictionSelfAnalysisHits`, and skip the unchosen identifier branch for the
string-literal case. The broader `SlimeAstCreateUtils.ts` parser-only probe did
not show structural-count movement because that file does not exercise this
callsite heavily; keep this as exact-GAST coverage, not as a claimed main speed
closer. The current main speed closer remains the successful-path cost around
`MemberExpression`, `AssignmentExpression`, token cursor access, wrapper/cache
work, and state snapshots.

A `currentTokenForPrediction()` pretokenized miss fast path was also rejected.
It tried to read the current token directly from the parsed ordinal on cache
misses, but `SlimeAstCreateUtils.ts` regressed from about `avgMs=259.305` to
roughly `avgMs=336-344ms`. The retained lesson is that the token cursor win came
from replacing repeated source-index token lookup at broader lookahead points;
adding extra branches to an already cached current-token path is not currently a
maximum-return optimization.

A follow-up adaptive low-yield memoization experiment on the same file was
rejected. It made recognizer speculative rules stop memoizing after `256` puts
with zero hits. Structural counters improved (`ruleCacheKeyBuilds` dropped from
about `45.9k` to `38.6k`, `ruleCachePuts` from about `32.1k` to `24.8k`, with
`7322` adaptive skips), but wall-clock regressed to `avgMs=259.826
bestMs=235.282` versus the retained token-stream baseline of about
`avgMs=251.337 bestMs=214.991`. Do not reintroduce a per-rule adaptive
memoization Map on the hot path just because cache counters fall; future
wrapper/cache reductions need a lower-overhead static or self-analysis-derived
plan.

A second static-plan experiment tried to skip loop detection for graph-proven
non-recursive, non-dynamic recognizer rules. The graph smoke passed, but on
`SlimeAstCreateUtils.ts` the parser reported `ruleLoopDetectionSkips=0`, so the
current generated Slime graph did not expose useful eligible rules beyond paths
already handled by recognizer pass-through or terminal-leaf inlining. Do not
treat generic loop-detection skipping as the next main closer until the graph
model exposes concrete hot rules that can actually hit this path.

On 2026-07-10, three more successful-path experiments were rejected on the same
`SlimeAstCreateUtils.ts` static enhanced parser-only probe. Replacing the active
loop-detection `HashSet<SubhutiRuleCacheKey>` with a structured stack-array scan
preserved the loop key semantics but regressed from `avgMs=272.645
bestMs=223.764` to `avgMs=294.740 bestMs=243.707`; stack scanning is not a
better generic loop-detection model for the current rule depth. Changing
`SubhutiPackratCache` from access-order `LinkedHashMap` to insertion-order
eviction also failed to improve wall-clock (`avgMs=290.361 bestMs=216.658`),
so LRU hit mutation is not the maximum-return packrat issue. Finally, a runtime
`exactSingleTerminalForRule(...)` direct-recognizer experiment regressed to
`avgMs=303.004 bestMs=254.903` because every wrapper paid a dynamic graph lookup
while only a few explicitly exact terminal rules could benefit. The retained
architecture lesson is that exact grammar-body execution is still the right
Chevrotain-style direction, but it must be generated or preplanned for proven
hot rules from a real GAST; do not add per-wrapper runtime graph queries to
discover tiny direct-execution cases.

The next retained token-stream step applies the same principle to assignment
lookahead. Source-mode `hasTopLevelAssignmentOperatorAhead()` stays conservative
because deep scanning can force regexp-sensitive lexing far ahead of the parser,
but pre-tokenized default-mode recognizers may scan the already materialized
token array until an expression boundary. This is a Chevrotain-style token cursor
optimization: it avoids entering the `LeftHandSideExpression ->
AssignmentOperatorAny` branch when the current expression plainly has no
top-level assignment operator, while preserving the normal source-mode path. The
focused `SlimePreTokenizedAssignmentLookaheadSmokeTestMain` proves that `value`
skips `AssignmentOperatorAny` and `value = next` still consumes the assignment.
On `SlimeAstCreateUtils.ts` recognizer with pre-tokenized input, structural
counters improved from about `ruleWrapperCalls=51745`,
`ruleCoreExecutions=37981`, and `ruleCacheKeyBuilds=45870` to
`ruleWrapperCalls=39201`, `ruleCoreExecutions=32466`, and
`ruleCacheKeyBuilds=38841`; the same two-round probe moved from about
`avgMs=476 bestMs=403` to `avgMs=407 bestMs=304`. Treat this as a targeted
token-stream/grammar-gate optimization, not as a fallback parser or permission
to reintroduce broad source-mode deep scans.

The next retained OPTION/GAST coverage step connects optional initializers to
the grammar graph. `Initializer` is now declared as starting with `Assign`, and
optional initializer sites use `Option(Alternative.rule("Initializer", ...))`
instead of entering the rule and failing whenever `=` is absent. This is the
same Chevrotain-style optional-production direction as earlier type-argument
and expression-tail work: the grammar metadata decides whether the optional
production can start before the wrapper and packrat cache are paid. The focused
`SlimeInitializerOptionLookaheadSmokeTestMain` proves both sides: `const value;`
skips the `Initializer` rule, while `const value = 1;` still executes it. On
`SlimeAstCreateUtils.ts` pre-tokenized recognizer, structural counters moved
from about `ruleWrapperCalls=39201`, `ruleCoreExecutions=32466`, and
`ruleCacheKeyBuilds=38841` to `ruleWrapperCalls=38544`,
`ruleCoreExecutions=31809`, and `ruleCacheKeyBuilds=38184`; the same two-round
probe measured about `avgMs=322 bestMs=262`. Keep this as a small but real
Chevrotain-alignment step: it removes failure-driven optional parsing from a hot
standard path without changing accepted syntax.

On 2026-07-07, the focused `com.qin.parser.QinParser` Java-to-TypeScript
generation probe succeeded after adding standard `java.util.IdentityHashMap`
and `Collections.newSetFromMap` support to the Qin Java SDK JS runtime and JS
backend. The probe generated 266 files in `16559ms`, so parser package
generation was not the current OVS fresh-transform bottleneck.

The same-day balance-monitoring comparison measured the old Node TypeScript
snapshot at best `118.19ms` and warm average `124.74ms` for the 9 OVS files.
The current Qin JVM path with transform disk-cache hits measured `292ms`, but a
fresh 9-file transform with profiling enabled measured `133025ms`: about `85s`
was module-class compilation of the 286-module OVS/CSSTS/generated-parser
toolchain, about `1.1s` was dependency-session setup, and about `43.4s` was
runtime transform execution. Inside the runtime transform, parser `parse+cst`
accounted for about `33.6s`, CST-to-AST for about `9.8s`, and code generation
only for tens of milliseconds. A tiny hot-session parse of
`export const X = () => { return div { "x" } }` measured `633ms` on the second
Qin JVM run, while the Node snapshot measured about `3.7-5.1ms` after warmup
for the same 14-token input. Treat this as a current active-path performance
regression to narrow at the generated Java-to-TS/Subhuti runtime layer; do not
replace it with the Node snapshot or another fallback parser.

## Pipeline Probe Artifact Reuse

Five-stage parser/compiler probes are diagnostic accelerators. They should expose token -> CST -> AST -> emitted ESM -> integration boundaries without paying for the same lower layers twice.

When a probe has already produced active tokens, CST, and AST for a source, the emitted ESM stage should reuse those exact artifacts and call the active emitter directly. It should not call a second full transform that reparses the same source and reruns CST-to-AST just to get generated code.

This is not fallback behavior. The probe still uses the single active parser, CST-to-AST, and emitter path. It only avoids duplicate diagnostic work inside one probe. If the goal is specifically to compare the public full-transform API with the staged artifacts, add that as an explicit parity smoke instead of making every ordinary probe pay that cost.

## Dev Launcher And Watcher Memory

Qin launchers must not hard-code a tiny JVM heap for `qin dev` or other compiler-heavy flows. The launcher should expose `QIN_JAVA_OPTS` as the standard override surface and keep the default heap large enough for OVS/CSSTS/parser package compilation. A command-line `-Xmx128m` default is invalid for modern Qin dev because OVS batch compilation and generated parser toolchains can legitimately need far more memory.

The dev watcher must prune ignored directories while walking the tree, not after collecting every file. Directories such as `.git`, `.qin`, `@qin-mod`, `build`, `dist`, `logs`, `node_modules`, `out`, `target`, and temporary `tmp-*` directories should be skipped before descending. Traversing generated packages, caches, logs, and temp trees competes with OVS/CSSTS compilation for heap and can turn a valid focused compile into a dev-server `OutOfMemoryError`.

When diagnosing dev-server memory failures, first separate the boundaries: run the focused OVS/CSSTS/compiler probe with the same heap, then run the dev server with browser requests paused. If the focused probe passes but the dev server fails, repair the launcher, watcher, concurrency, or cache/session owning layer rather than changing application syntax or deleting caches as the fix.
