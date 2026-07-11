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

Qin parser cold-start work follows the same rule. Runtime parser enhancement is
not a current Qin standard path. The Qin-owned parser path should use a static enhanced parser
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
Do not use `SubhutiParser.create(...)` or another runtime parser-enhancement
route as the main Qin parser benchmark path. Those
routes are historical or diagnostic references only, and any performance fix
must be proven on the static enhanced `.class` wrapper and its `cst(false)`
recognizer mode when parser-only timing is required.

Decorator handling follows the canonical Qin JS compatibility rule in
`packages/qin-runtime-core/QIN_JS_COMPATIBILITY_MODEL.md`: Qin-owned decorators
are lowered at compile time into static `.class` wrappers, metadata,
initializer calls, JVM annotations, or rule tables. Runtime enhancement,
reflection, or JavaScript descriptor emulation must not become the standard
fallback for a missing decorator lowerer.

When Qin emits or transforms JVM bytecode directly, prefer the JDK Class-File
API as the standard implementation path when it covers the required classfile
features. ASM is a mature third-party bytecode engineering reference and may be
studied or evaluated for gaps, but it should not replace the current Qin
Class-File API route merely because it is popular. Runtime enhancement remains
outside the standard static `.class` path; it is not the mechanism for parser
wrappers, decorator lowering, or Qin-owned source-to-class compilation.

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

Void `@SubhutiRule` methods in static enhanced parser wrappers should call
`executeVoidRuleWrapper(...)`, not `executeRuleWrapper(() -> { ...; return null;
})`. This keeps the source-level rule shape isomorphic while exposing the
standard void-rule direct-recognizer entry to generated/static parser classes.
It is not a fallback: exact GAST still authorizes any direct execution, while
normal CST, recovery, debug, returned-value rules, and unplanned rules stay on
the standard wrapper path. On 2026-07-11,
`SubhutiDirectTerminalSequenceSmokeTestMain` added a static-enhanced-style void
wrapper probe for an exact-GAST `OPTION` sequence and proved
`ruleWrapperCalls=0`, `ruleCoreExecutions=0`, and
`ruleWrapperDirectRecognizerPlanSkips=1`. The same change updates
`SlimeParserStaticEnhanced` and `QinParserStaticEnhanced` generation shape, and
retains the single-argument `cacheKeyExtra=arg0` rule.

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

The next retained refinement keeps the same cursor direction but avoids making
the current-token cache the first hop for the hottest ordinary lookahead path.
For pre-tokenized parser-only recognizers, `LA(1)` and `isEof()` now read the
parser-owned token array directly before entering `_getOrParseTokenEntry(...)`.
The current-token cache still exists for paths that need the cache-shaped entry,
but Chevrotain-style token-array input is the primary `LA(1)`/EOF path when CST
and recovery are off. `SubhutiCurrentTokenEntryCacheSmokeTestMain` now proves
that `LA(1), LA(1), consume` can complete with zero `tokenStreamGets` and zero
`currentTokenEntryCacheHits` on this focused pre-tokenized recognizer path. On
the 2026-07-11 generated TypeScript recognizer benchmark, the same two-file
pre-tokenized probe moved from `SlimeParser.ts avgMs=164.391 bestMs=142.075`
and `SlimeAstCreateUtils.ts avgMs=349.125 bestMs=288.986` to
`SlimeParser.ts avgMs=127.607 bestMs=94.030` and
`SlimeAstCreateUtils.ts avgMs=282.661 bestMs=243.411`. This is the current
retained framework-level token-array step.

The retained follow-up attacks the next measured cache-key hotspot without
hard-coding Slime expression rules. In recognizer speculative parsing, Subhuti
now tracks per-parse packrat hit/put yield and, after 256 zero-hit puts for a
rule, skips further memo writes for that rule during the same parse. This is a
performance-only packrat policy: it does not change grammar, prediction, token
consumption, or parse failures, and the normal loop-detection key remains
separate. `SubhutiAdaptiveLowYieldMemoSmokeTestMain` proves the focused pattern:
a speculative branch repeatedly memoizes a rule that is never hit, then moves to
`ruleCacheAdaptiveLowYieldSkips` after the threshold. On the UTF-8 2026-07-11
generated TypeScript recognizer benchmark, the two-file pre-tokenized probe
measured `SlimeParser.ts avgMs=117.096 bestMs=79.381` and
`SlimeAstCreateUtils.ts avgMs=271.327 bestMs=220.832`, while
`ruleCacheKeyBuilds` dropped to `18640` and `33006` respectively. A follow-up
A/B check on the same 2026-07-11 code temporarily disabled the adaptive threshold
with `Integer.MAX_VALUE`; `SlimeAstCreateUtils.ts` regressed from the current
`avgMs=222.557` run to `avgMs=280.288`, and `ruleCacheKeyBuilds` returned from
`33006` to `171007`. Keep the adaptive policy as part of the retained
token-array/cache-key combination, not as a standalone optimization claim.

The same 2026-07-11 baseline showed that the original 256 zero-hit threshold was
too late for many speculative rules that produce only about 258 cache puts in a
large generated TypeScript file. Lowering `ADAPTIVE_LOW_YIELD_MEMO_MIN_PUTS` to
64 is retained because it preserves the same parser semantics while making the
framework stop writing zero-yield speculative memo entries sooner. The focused
adaptive smoke still proves visible skip behavior, and the UTF-8 10-round
two-file recognizer probe moved `SlimeParser.ts` from `avgMs=96.579 bestMs=73.821`
to `avgMs=83.218 bestMs=60.197`, and `SlimeAstCreateUtils.ts` from
`avgMs=217.806 bestMs=175.328` to `avgMs=207.133 bestMs=173.438`.
`ruleCacheKeyBuilds` dropped from `18640/33006` to `9640/22086`. Treat this as a
measured framework packrat-policy refinement, not as the main Chevrotain gap
closer; the remaining dominant cost is still successful-path wrapper/core work
around expression/member rules.

A parser-state token-sequence cache for prediction was rejected on the same
2026-07-11 baseline. The focused `And(() -> Choice())` probe could produce a
cache hit for repeated lookahead at the exact same parser state, but the real
generated TypeScript corpus produced `predictionTokenSequenceCacheHits=0` and
large miss counts (`29093` on `SlimeParser.ts`, `116113` on
`SlimeAstCreateUtils.ts`). The two-file pre-tokenized recognizer benchmark was
mixed and regressed the larger file (`SlimeParser.ts avgMs=91.808 bestMs=62.279`,
`SlimeAstCreateUtils.ts avgMs=244.994 bestMs=187.626`). Do not retain a
state-sequence cache unless corpus counters show real reuse on the hot path.

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

The 2026-07-11 follow-up also rejected adding `MemberExpression` to
`recognizerLowYieldMemoRules()`. The rule looked tempting because the profile
showed only `651` cache hits for `7787` puts, but those hits saved large spans.
The focused 10-round two-file recognizer probe regressed to
`SlimeParser.ts avgMs=98.119 bestMs=74.854` and
`SlimeAstCreateUtils.ts avgMs=240.221 bestMs=199.196`. Keep `MemberExpression`
memoized until a structural exact-GAST/common-prefix plan can remove the repeated
work without losing those useful hits.

A later targeted low-yield memo point was retained for `ArgumentListItem`.
Unlike the rejected broad expansion, this single rule showed zero cache hits in
the hot generated-TS recognizer probe while contributing thousands of cache
key/put operations. Adding only `ArgumentListItem` to the existing recognizer
low-yield no-memo policy removed it from `cacheWork`; the focused
`SlimeArgumentListItemLowYieldMemoSmokeTestMain` proves argument parsing still
executes the rule while avoiding recognizer cache puts. On the two-file
pre-tokenized recognizer probe, `SlimeParser.ts` measured
`avgMs=152.134 bestMs=101.166`, and `SlimeAstCreateUtils.ts` measured
`avgMs=262.956 bestMs=234.088`, with the latter reducing
`ruleCacheKeyBuilds` from about `38005` to `35483` and `ruleCachePuts` from
about `31270` to `28748`. Treat this as a narrowly retained profile-derived
step, not the desired end state: future Chevrotain-style self-analysis should
derive such deterministic low-yield/no-memo decisions from complete GAST or an
execution plan instead of making grammar authors maintain a hand list.

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

On 2026-07-10 this lookup model was lifted into a parser-class runtime plan.
`SubhutiParserRuntimePlan` is the framework-level owner for Graph/GAST
self-analysis predictions plus recognizer pass-through and terminal-leaf facts.
It is keyed by parser class and grammar identity/revision, built once, and
reused by later parser instances. Runtime `Or(...)` should first consult this
plan for declared self-analysis predictions instead of rebuilding or hydrating
scattered caches. Recognizer wrapper inlining should also read from this plan
instead of recomputing graph rule sets per parser instance. The focused
`SubhutiParserRuntimePlanSmokeTestMain` proves the first parser builds the plan,
the second parser reuses it, declared `Choice -> A | B` hits self-analysis
without recording mode, and terminal-leaf recognizer execution comes from the
same plan. Treat this as architecture groundwork: it reduces repeated planning
and gives Subhuti a Chevrotain-style plan boundary, but broad parser speed still
depends on the next successful-path reductions for wrapper/cache/state/token
execution.

A same-day LL(k) depth experiment was rejected. Raising the framework lookahead
constants from `4` to `8` in `SubhutiGastCallsiteAnalysis`,
`SubhutiOrLookaheadPlan`, and `SubhutiParserCombinators` compiled and the
focused parser benchmark still succeeded, but `SlimeAstCreateUtils.ts` showed
no structural improvement: `orPredictionStateSaves`, `LeftHandSideExpression`
state saves, wrapper calls, cache-key builds, and token-stream gets stayed the
same. Do not treat a larger global `k` as the next performance closer unless a
focused callsite first proves that the extra depth changes candidate sets while
preserving PEG prefix ambiguity rules.

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

The next retained GAST-analysis step allows exact callsite analysis to keep a
known terminal prefix even when the rule body has a dynamic tail after that
prefix. This matches the Chevrotain-style distinction between "enough prefix to
plan lookahead" and "complete body can be executed": a rule such as
`BracketSuffix -> LBracket dynamic(Expression) RBracket` may contribute
`LBracket` to lookahead, while `dynamic(...) A` remains unplanned because the
first consumed token is unknown. `SubhutiGastCallsiteAnalysisSmokeTestMain`
proves both sides. Slime now declares member suffix GAST prefixes such as
`DotMemberSuffix -> Dot IdentifierName`, `DotPrivateIdentifierSuffix -> Dot
PrivateIdentifier`, `ComputedMemberSuffix -> LBracket dynamic(Expression)
RBracket`, `OptionalChain -> QuestionDot dynamic(...)`, and
`TSNonNullExpressionTail -> LogicalNot`. On `SlimeAstCreateUtils.ts` this did
not materially move wall-clock (`avgMs=282.813` local baseline to
`avgMs=282.866`), but it reduced the hot `MemberExpression` candidate average
from about `1.21` to `1.11` and increased skipped alternatives by about `995`.
Keep this as a structural Chevrotain-alignment step, not as the final speed
closer; the next performance work still needs to turn these better plans into
less state-save, wrapper, cache, and token-access work.

A `currentTokenForPrediction()` pretokenized miss fast path was also rejected.
It tried to read the current token directly from the parsed ordinal on cache
misses, but `SlimeAstCreateUtils.ts` regressed from about `avgMs=259.305` to
roughly `avgMs=336-344ms`. The retained lesson is that the token cursor win came
from replacing repeated source-index token lookup at broader lookahead points;
adding extra branches to an already cached current-token path is not currently a
maximum-return optimization.

An earlier adaptive low-yield memoization experiment on the 2026-07-10 baseline
was rejected. It made recognizer speculative rules stop memoizing after `256`
puts with zero hits. Structural counters improved (`ruleCacheKeyBuilds` dropped
from about `45.9k` to `38.6k`, `ruleCachePuts` from about `32.1k` to `24.8k`,
with `7322` adaptive skips), but wall-clock regressed to `avgMs=259.826
bestMs=235.282` versus that day's retained token-stream baseline of about
`avgMs=251.337 bestMs=214.991`. That negative conclusion applied to the old
baseline only and has been superseded by the retained 2026-07-11 token-array
cursor plus adaptive cache-key combination above. The durable lesson remains:
do not keep a cache-counter optimization unless same-baseline A/B timing proves
it.

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

Two further 2026-07-10 experiments were rejected before the retained
dynamic-tail GAST change. Reading prediction current-token misses directly from
the pretokenized ordinal stream passed focused smokes but regressed
`SlimeAstCreateUtils.ts` from `avgMs=282.813 bestMs=255.735` to
`avgMs=334.883 bestMs=299.250` and increased `tokenStreamGets`; do not add
extra direct-token branches inside the already cached current-token prediction
path. Moving recognizer pass-through and terminal-leaf classification into a
parser-class/grammar-revision execution-plan cache also passed its smoke but
regressed the same probe to `avgMs=340.628 bestMs=316.210` with no useful
structural-count improvement; per-instance graph classification is not the
current hot cost compared with successful-path wrapper/cache/token execution.

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

The next retained optional-production runtime step makes that behavior stricter
and cheaper. When `Option(Alternative...)` has graph-backed start prediction and
the current token proves the optional production is present, Subhuti now executes
the alternative directly instead of wrapping it in `tryAndRestore`. This matches
Chevrotain's OPTION shape more closely: lookahead decides whether the optional
body is entered; once entered, an inner parse error remains visible rather than
being treated as an absent optional production. The focused
`SubhutiAlternativeStartPredictionSmokeTestMain` proves all three cases: absent
optional input skips the body, present optional input executes directly, and a
partial inner failure is not restored away. On `SlimeAstCreateUtils.ts`
pre-tokenized recognizer, the same two-round focused probe moved from the local
baseline `avgMs=432.008 bestMs=341.595` to
`avgMs=403.660 bestMs=308.245`, with
`alternativeStartPredictionDirectOptionExecutions=2585`. Treat this as accepted
framework work because it improves correctness and modestly reduces successful
path overhead; continue judging later optional-production changes with the same
focused smoke plus benchmark discipline.

The matching `Many(Alternative...)` single-candidate step follows the same
strictness rule. When runtime lookahead leaves exactly one repetition
alternative, Subhuti executes that alternative directly instead of saving state
and treating an inner failure as "the repetition ended". This aligns with
Chevrotain's MANY behavior: lookahead decides whether the repeated production is
present, and once present its internal errors remain visible. The focused
`SubhutiManyAlternativesLookaheadSmokeTestMain` now covers `id.name`, `id()`,
no-suffix input, and `id.`; the last case proves that a suffix start token
followed by a missing required member name fails inside the suffix rather than
restoring to the pre-suffix state. On `SlimeAstCreateUtils.ts`, the focused
probe recorded `orPredictionDirectManyExecutions=1679`, but wall-clock did not
improve over the prior optional-production result (`avgMs=415.242
bestMs=370.156` in the retained three-round run). Treat this as accepted
Chevrotain-style error-surface alignment, not as a major parser-speed closer.
The remaining performance work should still target token cursor execution,
wrapper/cache structure, and hot expression rules.

The next retained `Many(Alternative...)` planning-gate step applies the same
Chevrotain-style `GATE` discipline to repetition alternatives. When runtime
lookahead has already narrowed a `MANY` candidate set, Subhuti now evaluates
`Alternative.lookaheadRule(...)` planning gates before deciding whether the
loop has one executable candidate or still needs a state-saving planned OR. This
is intentionally limited to non-null candidate sets: an inconclusive or EOF
prediction must still use the normal `tryAndRestore` loop-end behavior instead
of converting "no matched suffix" into a hard inner failure. The focused
`SubhutiManyAlternativesLookaheadSmokeTestMain` now covers a disabled recovery
suffix with the same start token as the real suffix, proving the recovery branch
is filtered before execution and the remaining suffix runs without a state
save. Slime member suffix recovery alternatives now use
`Alternative.lookaheadRule("IncompleteMemberAccessProperty", ...)`, so normal
non-recovery parses do not keep that error branch in the hot member-suffix
candidate set. On `SlimeAstCreateUtils.ts` pre-tokenized recognizer,
`MemberExpression` candidate average moved from about `1.11` to `1.02`,
`MemberExpression` state saves fell from `1174` to `179`,
`orPredictionStateSaves` fell from `7456` to `6461`, and
`orPredictionDirectManyExecutions` rose from `1679` to `2674`; the comparable
five-round probe measured `avgMs=252.498 bestMs=226.941` on this machine. Treat
this as retained framework architecture progress because it removes a proven
unreachable recovery candidate from the successful path without adding fallback
syntax or weakening errors.

A broader `LeftHandSideExpression` experiment was rejected immediately after
that retained step. It tried to make `NewExpression` a planning-gated fallback
behind `OptionalExpression` and `CallExpression` so simple call expressions
would not keep the `NewExpression -> MemberExpression` candidate. The focused
`SlimeAstCreateUtils.ts` parser probe failed with unconsumed `class` at source
position `13464`, proving the gate did not model the full JS/TS left-hand-side
boundary. Do not reintroduce this broad `NewExpression` gate without first
building smaller grammar-specific coverage for class expressions, call heads,
`new` chains, optional chains, and TypeScript postfix forms. The remaining
`LeftHandSideExpression` state saves should be attacked with narrower exact
GAST/factoring evidence, not by excluding `NewExpression` through an incomplete
deep lookahead predicate.

The next retained token-cursor step targets JavaScript's lexical-goal split for
regular expression literals. The pre-tokenized recognizer path already owns the
default-mode token array, but `RegularExpressionLiteral` checks use
`LexerMode.REGEXP` and were still falling back to the on-demand token cache tens
of thousands of times. Subhuti now uses the default-mode token stream as a
negative gate for REGEXP mode: if the current default token's source start is
not `/`, a regular expression literal cannot start there, so the parser returns
that default token as the mismatch instead of re-lexing in REGEXP mode. If the
default token does start with `/`, the standard REGEXP lexer path still runs.
The focused `SubhutiPreTokenizedRegexpNegativeGateSmokeTestMain` proves the
non-slash case fails without token-cache fallback. On `SlimeAstCreateUtils.ts`
pre-tokenized recognizer, `tokenCacheGets` fell from about `52k` to `2`, with
`preTokenizedRegexpNegativeHits=52708`; the same two-round focused probe measured
`avgMs=312.700 bestMs=257.931`. Treat this as the current strongest
Chevrotain-style token-array/cursor win: it removes repeated lexer-mode retries
without weakening the real `/.../` REGEXP path.

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

## Subhuti Runtime Plan Coverage

The Chevrotain-style runtime plan needs an observable coverage boundary, not
just faster-looking counters. `SubhutiParserRuntimePlan` should report which
facts came from `SubhutiGrammarGraph` and which came from exact
`SubhutiGastGrammar`: rule counts, alternation counts, planned predictions,
exact/dynamic GAST rule counts, consuming element counts, and recognizer
pass-through/terminal-leaf facts. This report is the standard way to decide
whether a future optimization is allowed to run on exact GAST evidence or is
still only working from FIRST/lookahead graph facts.

On 2026-07-10, Subhuti added `getRuntimePlanReport()` and a focused
`SubhutiParserRuntimePlanSmokeTestMain` GAST-only parser. The smoke proves that
an exact GAST grammar with no `SubhutiGrammarGraph` can still build one
self-analysis prediction and feed recognizer terminal-leaf/pass-through skips.
This is a small but important architecture step toward Chevrotain's
`performSelfAnalysis()` model: direct successful-path reductions should now be
guarded by runtime-plan coverage instead of assuming that a graph summary is a
complete executable grammar body.

The follow-up tightening makes that boundary executable: graph-derived
pass-through and terminal-leaf facts remain visible in the runtime-plan report
but no longer feed successful-path wrapper skips. Direct recognizer skips are
fed only by exact `SubhutiGastGrammar` facts. The updated smokes prove both
sides: a graph-only parser still reports graph coverage and uses predictions,
but has zero `ruleWrapperPassThroughSkips`; an exact-GAST chain parser reports
exact coverage and reduces the recognizer path from eleven wrappers to one.
This is the intended Chevrotain-style safety rule for future wrapper/cache/token
optimizations.

The next accepted exact-GAST execution unit is direct terminal recognizer
planning. When exact GAST proves that a void rule, possibly through a chain of
single rule references, is exactly one terminal with no token-value constraint,
`SubhutiParserRuntimePlan` records a direct terminal plan. In `cst(false)` mode,
with recovery and debugger disabled, the void rule wrapper may consume that
terminal directly and skip the successful-path wrapper/core/cache stack while
still preserving top-level initialization, EOF validation, parse failure, token
semantics, and CST-producing mode. A direct plan miss is not a fallback parser;
only exact GAST can authorize this path, and a token mismatch remains the
standard parse failure. Retained focused probes on 2026-07-10 moved the exact
GAST recognizer chain to about `avgUs=0.6-0.8` with `ruleWrapperCalls=0`,
`ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`, and
`ruleWrapperDirectTerminalSkips=1`. An instance-level direct-terminal lookup
cache experiment was rejected because focused wall-clock results were unstable;
future work should continue through generated or parser-class runtime plans, not
per-instance micro-caches.

The next retained exact-GAST successful-path step extends that rule from one
terminal to a terminal sequence. When exact GAST proves that a void rule body is
only a sequence of terminals or rule references that resolve to terminal
sequences, and none of those terminals require token-value matching, the runtime
plan records a direct terminal-sequence plan. In `cst(false)` mode, with recovery
and debugger disabled, the wrapper may consume the whole sequence directly. This
is still exact-GAST-only execution: `SubhutiGrammarGraph` FIRST facts do not
authorize it, `OPTION`/`MANY`/`OR`/dynamic nodes are not direct sequences, and a
token mismatch remains the normal parser failure instead of a fallback. The
focused `SubhutiDirectTerminalSequenceSmokeTestMain` proves success, visible
failure after a partial sequence, and an `Or(...)` branch selected by GAST
self-analysis executing the chosen sequence directly. On the 2026-07-10 focused
probe, a 10-token exact-GAST sequence ran with `ruleWrapperCalls=0`,
`ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`,
`ruleWrapperDirectTerminalSequenceSkips=1`, and about `avgUs=2.124` over
200,000 rounds.

The follow-up token-array cursor step specializes that exact-GAST sequence plan
for pre-tokenized recognizer input. When all terminals in the direct sequence use
the default lexer mode, Subhuti advances by parsed-token ordinal in the existing
token array instead of calling `_consumeTokenMatch(...)` for each terminal. This
keeps the same exact-GAST authorization and the same visible parse-failure
surface, but removes per-token `tokenStreamGets` and token-entry cache checks
from the sequence path. The pre-tokenized case in
`SubhutiDirectTerminalSequenceSmokeTestMain` asserts `tokenStreamGets=0`; the
  focused 2026-07-10 probe measured the 10-token pre-tokenized exact-GAST sequence
  at about `avgUs=0.293` with `ruleWrapperCalls=0`, `ruleCoreExecutions=0`,
  `ruleCacheKeyBuilds=0`, `tokenCacheGets=0`, and `tokenStreamGets=0`.

  The next retained exact-GAST execution unit extends direct recognizer plans to
  simple `OPTION` terminal sequences. When exact GAST proves that a void rule is
  a sequence of required terminals plus optional terminal subsequences, the
  runtime plan may decide optional
  presence from the optional sequence's first terminal and then consume the
  planned terminals directly. This preserves Chevrotain-style `OPTION` semantics:
  absent optional input does not fail, but once the optional start token is
  present, a missing inner required token remains a visible parse failure rather
  than being restored away as absence. The focused
  `SubhutiDirectTerminalSequenceSmokeTestMain` now proves absent, present,
  inner-failure, and pre-tokenized cursor cases. The 2026-07-11 focused probe
  measured the four-token optional plan at about `avgUs=1.569` from source and
  `avgUs=0.532` with pre-tokenized input, with `ruleWrapperCalls=0`,
  `ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`, `tokenCacheGets=0`,
  `tokenStreamGets=0`, and `ruleWrapperDirectRecognizerPlanSkips=1`.

  The matching exact-GAST repetition step extends the same direct recognizer plan
  to simple `MANY` terminal sequences. A repetition element may run only when
  exact GAST proves the repeated body is a terminal sequence with no gate,
  alternation, or dynamic boundary. The loop continues while the repeated
  sequence's first terminal is present; after that start token is present, an
  inner mismatch is a visible parse failure rather than being treated as the end
  of repetition. This is the same Chevrotain-style distinction as `OPTION`:
  lookahead decides whether the production is entered, and entered productions do
  not hide internal errors. The focused
  `SubhutiDirectTerminalSequenceSmokeTestMain` covers absent repetition,
  multiple repetitions, inner failure, and the pre-tokenized cursor path. The
  2026-07-11 focused probe measured a six-token two-iteration repetition plan at
  about `avgUs=2.332` from source and `avgUs=0.285` with pre-tokenized input,
  with `ruleWrapperCalls=0`, `ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`,
  `tokenCacheGets=0`, `tokenStreamGets=0`, and
  `ruleWrapperDirectRecognizerPlanSkips=1`.

  The same exact-GAST direct recognizer plan now covers simple `AT_LEAST_ONE`
  terminal sequences. Unlike `MANY`, the first sequence is consumed
  unconditionally after preceding required elements, so absence of the first
  item is a visible parse failure; after one item succeeds, additional items use
  the same first-terminal lookahead loop as `MANY`. This keeps Chevrotain-style
  one-or-more semantics precise: entered productions do not hide missing inner
  tokens, and the optimization is available only when exact GAST proves the
  repeated body is a terminal sequence with no gate, alternation, or dynamic
  boundary. On 2026-07-11,
  `SubhutiDirectTerminalSequenceSmokeTestMain` added absent-first-item,
  single-item, multi-item, inner-failure, and pre-tokenized cursor coverage. The
  focused `SubhutiRecognizerPerformanceProbeMain 200000` run measured the
  six-token two-iteration at-least-one plan at `avgUs=2.699` from source and
  `avgUs=0.241` with pre-tokenized input, with `ruleWrapperCalls=0`,
  `ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`, `tokenCacheGets=0`,
  `tokenStreamGets=0`, and `ruleWrapperDirectRecognizerPlanSkips=1`.

  The next exact-GAST execution tightening lets direct terminal, sequence, and
  container plans carry `TerminalValue` as well as token names. This is still a
  single standard parser path: only exact `SubhutiGastGrammar` terminal-value
  nodes authorize the skip, and the runtime cursor checks both `tokenName` and
  `tokenValue` before consuming. A wrong value remains a visible parse failure
  and is not consumed as a weaker token-name match. The focused
  `SubhutiDirectTerminalSequenceSmokeTestMain` covers matching terminal values,
  wrong-value failure without consumption, terminal-value sequences,
  pre-tokenized cursor matching, and value-aware optional presence. The
  2026-07-11 `SubhutiRecognizerPerformanceProbeMain 200000` run measured a
  two-token terminal-value sequence at `avgUs=1.139` from source and
  `avgUs=0.432` with pre-tokenized input, with `ruleWrapperCalls=0`,
  `ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`, `tokenCacheGets=0`,
  `tokenStreamGets=0`, and `ruleWrapperDirectTerminalSequenceSkips=1`.

  Simple exact-GAST `ALTERNATION` is now also a direct recognizer plan when every
  alternative is a terminal sequence and each alternative has a distinct first
  terminal key, including token value and lexer mode. Runtime execution chooses
  the matching alternative by that first terminal and then consumes the planned
  sequence directly. If no alternative can start, the rule fails without
  consuming input; if an entered alternative later misses an inner token, that
  failure remains visible. Prefix-ambiguous alternatives such as `A` versus
  `A B`, gated alternatives, nullable alternatives, dynamic bodies, or partial
  `SubhutiGrammarGraph` FIRST facts must stay analysis-only to preserve PEG
  branch order. The 2026-07-11 focused
  `SubhutiDirectTerminalSequenceSmokeTestMain` covers first-branch selection,
  second-branch selection, inner failure, no-start failure without consumption,
  and the pre-tokenized cursor path. `SubhutiRecognizerPerformanceProbeMain
  200000` measured a two-token second-branch OR plan at `avgUs=1.067` from
  source and `avgUs=0.234` with pre-tokenized input, with
  `ruleWrapperCalls=0`, `ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`,
  `tokenCacheGets=0`, `tokenStreamGets=0`, and
  `ruleWrapperDirectRecognizerPlanSkips=1`.

  A local OR execution experiment that reused the first token entry between
  branch selection and first-terminal consumption was rejected on 2026-07-11. It
  produced an unstable source-path result and regressed the pre-tokenized cursor
  path from the retained `avgUs=0.234` range to about `avgUs=0.427` in the same
  focused probe. Do not pursue ad hoc OR-entry reuse as the main Chevrotain
  alignment path; the durable direction is a unified token-array/parser-input
  model plus exact-GAST runtime plans.

  A 2026-07-11 `Many(Alternative...)` experiment hoisted the static
  `SubhutiOrPrediction` lookup outside the repetition loop. It correctly reduced
  one structural counter on the generated TypeScript recognizer profile
  (`orPredictionHotCacheHits` dropped from about `102709` to `100031` on
  `SlimeAstCreateUtils.ts`), but the same focused run regressed wall-clock timing
  from about `349.9ms` to `376.5ms` and increased current-token prediction misses.
  Do not keep plan-hoisting micro-optimizations merely because one cache counter
  improves. The next retained work should remove larger successful-path costs,
  especially wrapper/core/token work proven by both structural counters and
  wall-clock measurements.

  The retained follow-up specializes exact-GAST `ALTERNATION` plans by size.
  Small direct OR plans with at most two alternatives keep the already-proven
  linear recognizer path, avoiding fixed map-dispatch overhead on the common
  tiny-OR case. Larger simple OR plans precompute a first-token dispatch table in
  the runtime plan, grouped by lexer mode and token key, then consume the selected
  terminal sequence directly. A broad token-name first terminal and value-aware
  first terminals for the same token name/mode are treated as ambiguous and stay
  analysis-only, because the broad token would otherwise match the value-aware
  token and change PEG branch order. On 2026-07-11, the focused
  `SubhutiDirectTerminalSequenceSmokeTestMain` added three-branch dispatch
  coverage, including the pre-tokenized cursor path. `SubhutiRecognizerPerformanceProbeMain
  200000` measured the two-branch linear OR at `avgUs=0.966` from source and
  `avgUs=0.206` pre-tokenized, and the three-branch dispatch OR at `avgUs=0.915`
  from source and `avgUs=0.279` pre-tokenized, with `ruleWrapperCalls=0`,
  `ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`, `tokenCacheGets=0`, and
  `tokenStreamGets=0`.

  The next retained token-array/parser-input step makes the default-mode token
  array a parser-owned input mode instead of requiring callers to construct a
  separate `SubhutiLexer`. `useDefaultModeTokenArrayInput()` tokenizes the parser
  source once with the parser's own lexer and installs the same cursor-backed
  ordinal index used by direct recognizer plans. Direct terminal recognizer reads
  from that cursor without recording `tokenStreamGets`, matching the sequence and
  container cursor paths. This is not a second parser or fallback lexer; it is the
  standard Chevrotain-style token-array input model for default-mode recognizer
  runs, while mixed lexer-mode cases continue to fall back only when the
  token-array contract cannot represent the requested mode. On 2026-07-11,
  `SubhutiDirectTerminalSequenceSmokeTestMain` added parser-owned token-array
  direct-terminal coverage, and `SubhutiRecognizerPerformanceProbeMain 200000`
  measured exact-GAST single-token recognition at `avgUs=0.466` from source and
  `avgUs=0.258` through parser-owned token-array input, with
  `ruleWrapperCalls=0`, `ruleCoreExecutions=0`, `ruleCacheKeyBuilds=0`,
  `tokenCacheGets=0`, and `tokenStreamGets=0`.

  The retained follow-up makes parser-owned token-array input automatic for
  top-level exact-GAST direct recognizer plans whose terminals are all in
  default lexer mode. This moves the proven Chevrotain-style token-array model
  from a caller opt-in benchmark path into the standard direct recognizer entry,
  without using it as a fallback: the rule must still be exact GAST, `cst(false)`,
  no recovery/debug, at the top-level start state, and default-mode-only. Mixed
  lexer-mode or already-partial parses continue through the existing standard
  token path. On 2026-07-11, `SubhutiDirectTerminalSequenceSmokeTestMain`
  added coverage that a default-mode direct plan installs parser-owned
  token-array input automatically. The focused
  `SubhutiRecognizerPerformanceProbeMain 200000` run measured source-path
  improvements such as the 10-token sequence at `avgUs=0.454`, optional plan at
  `avgUs=0.664`, repetition plan at `avgUs=0.436`, at-least-one plan at
  `avgUs=0.476`, token-value sequence at `avgUs=0.462`, two-branch alternation
  at `avgUs=0.459`, and three-branch dispatch alternation at `avgUs=0.487`,
  all with `ruleWrapperCalls=0`, `ruleCoreExecutions=0`,
  `ruleCacheKeyBuilds=0`, `tokenCacheGets=0`, and `tokenStreamGets=0`.

  The follow-up retained token-consume cleanup shares the existing recognizer
  direct-read primitive between `_consumeToken(...)` and `_consumeTokenMatch(...)`.
In `cst(false)` mode, when recovery is disabled and no prior lookahead has filled
the source-index token cache, token matching reads the current token directly
instead of doing `HashMap` token-cache get/miss/put work. This keeps the same
token semantics and still uses the cache after an explicit `LA(...)` call. On the
focused 2026-07-10 probe, the ordinary recognizer chain removed its
`tokenCacheGets/Misses/Puts=1` counters and measured about `avgUs=1.181` at
200,000 rounds; the exact-GAST direct chain also kept
`tokenCacheGets/Misses/Puts=0` and measured about `avgUs=0.437` on that run.

The next retained Chevrotain-alignment step separates recording output as well as
runtime execution. `Alternative.of(...)` recording now writes both the legacy
`SubhutiGrammarNode` diagnostic tree and an exact `SubhutiGastNode` tree. When the
recorded GAST is complete and unambiguous, Subhuti builds the `Or(...)` prediction
through `SubhutiGastCallsiteAnalysis` instead of interpreting the legacy
FIRST/lookahead grammar tree. If the first recording pass sees duplicate
prefixes, the duplicate-prefix expansion must update both the diagnostic grammar
tree and the recorded GAST tree, then retry exact-GAST planning. This lets
ordinary AB/AC-style LL(k) choices move onto the GAST self-analysis path while
still preserving PEG prefix ambiguity such as `A` versus `A B`: that shape must
remain analysis-only and keep branch order because pruning into the longer branch
would change PEG semantics. Nullable or dynamic alternatives also remain
analysis-only. This is not a compatibility parser; it is the standard
Chevrotain-style route from recording to executable GAST plans. The focused
`SubhutiGrammarRecordingSmokeTestMain` and
`SubhutiOrFirstTokenPredictionSmokeTestMain` prove both sides: AB/AC records a
GAST LL(2) self-analysis prediction, while `A` versus `A B` still disables
runtime pruning.

The next structural API step adds `Alternative.structure(...)`: a grammar author
or future generator can attach an explicit diagnostic `SubhutiGrammarNode` and
exact/prefix `SubhutiGastNode` to a branch without executing that branch in
recording mode. `SubhutiGastCallsiteAnalysis` must let its path analysis decide
whether a dynamic tail is usable; a branch such as
`LBracket dynamic(Expression) RBracket` can still contribute the known
`LBracket` prefix, while a dynamic
prefix before the first consumed token stays unplanned. This is a Chevrotain-style
self-analysis surface, not a fallback parser and not a second grammar. The
focused `SubhutiGastCallsiteAnalysisSmokeTestMain` proves both the structured
Alternative path and the existing rule-reference path for known-prefix dynamic
tails.

The next retained real-parser GAST coverage step adds exact operator rules for
Slime's `MultiplicativeOperator`, `AssignmentOperator`, and
`AssignmentOperatorAny`. This is the preferred Chevrotain-style direction for
operator hot paths: expose the real grammar facts to `SubhutiGastGrammar` so the
framework can prebuild runtime-plan coverage, rather than adding caller fallback
or hand-written parsing shortcuts. `SlimeOperatorGastDirectPlanSmokeTestMain`
asserts the added exact-GAST coverage in the static enhanced parser. On the
focused generated TypeScript recognizer benchmark with pre-tokenized default-mode
input, the 2026-07-11 retained run measured `SlimeParser.ts` at
`recognizer avgMs=183.391 bestMs=100.920` and `SlimeAstCreateUtils.ts` at
`recognizer avgMs=285.466 bestMs=258.489`. The same investigation rejected two
negative experiments: broad manual low-yield memo lists and a stack-array
loop-detection replacement both reduced some counters but failed to improve the
real generated TypeScript corpus consistently, so they were not retained.

The next retained leaf-rule GAST step adds exact coverage for primary/literal
leaves: `ThisExpression`, `BooleanLiteral`, `NumericLiteral`,
`RegularExpressionLiteral`, `PrivateIdentifier`, `NoSubstitutionTemplate`, and
`Literal`. This is still the same architecture direction: expose complete rule
bodies to exact `SubhutiGastGrammar`, let `SubhutiParserRuntimePlan` decide safe
pass-through/direct recognizer coverage, and keep semantic rules such as
`Identifier` dynamic until their checks can be modeled honestly. The focused
`SlimeLiteralGastDirectPlanSmokeTestMain` asserts that the static enhanced parser
sees the added exact-GAST coverage. On the retained 2026-07-11 generated
TypeScript recognizer benchmark with pre-tokenized default-mode input,
`SlimeParser.ts` measured `recognizer avgMs=164.391 bestMs=142.075`, and
`SlimeAstCreateUtils.ts` measured `recognizer avgMs=349.125 bestMs=288.986`.
The structural counters moved in the intended direction: for `SlimeParser.ts`,
`ruleWrapperCalls` dropped from about `54863` to `54395`,
`ruleCoreExecutions` from about `53464` to `52996`, and
`ruleWrapperPassThroughSkips` rose from about `1311` to `1779`; for
`SlimeAstCreateUtils.ts`, `ruleWrapperCalls` dropped from about `176949` to
`174010`, `ruleCoreExecutions` from about `172935` to `169996`, and
`ruleWrapperPassThroughSkips` rose from about `2042` to `4981`.

The next retained successful-path framework change replaces the global
`HashSet<SubhutiRuleCacheKey>` loop detector with reusable active-invocation
stacks partitioned by rule name. Each rule bucket keeps the same semantic
identity as before: cache-key extra/parameters, token index, lexer mode, and
previous token name. It scans only concurrently active invocations of that same
rule, which is normally zero or one, and reuses primitive/reference arrays
instead of allocating a temporary loop key and hashing it through a global set
for every wrapper call. This is not a weaker recursion check: the focused
`SubhutiRuleInvocationLoopDetectionSmokeTestMain` proves that an identical
zero-consumption recursive invocation still fails before its body runs again,
while the same rule at the same token with a different argument remains a
distinct valid invocation. On 2026-07-11, the unchanged pre-tokenized
`SlimeAstCreateUtils.ts` recognizer benchmark moved from the clean five-round
baseline `avgMs=243.604 bestMs=234.070` to a ten-round retained result of
`avgMs=196.173 bestMs=171.178`, about a 19.5% average improvement. Grammar,
wrapper/core counts, packrat counts, and token counts stayed unchanged, which
isolates the gain to successful-path loop-detection overhead. The broader
Chevrotain target remains full GAST and immutable runtime plans; this change
removes a proven cost from rules that still need the ordinary wrapper while
that coverage expands.

The next retained architecture unit replaces recursive deep grammar recording
with a parser-class worklist self-analysis for static enhanced parsers. The
top-level rule starts one recording session; wrappers record `RULE_REF` nodes
and enqueue unseen `ruleName + parameter variant` tasks instead of recursively
executing their bodies. `OR`, `OPTION`, `MANY`, `AT_LEAST_ONE`, and terminals
become structural GAST nodes. Parameter variants merge as exact only when their
recorded bodies are identical; otherwise that rule is explicitly dynamic. The
resulting grammar and immutable runtime plan are cached by parser class,
declared-grammar revision, entry rule, and entry parameter, so later parser
instances reuse self-analysis rather than repeat it. This is one standard PEG
parser path: dynamic sections remain on ordinary PEG execution and are never
parsed by a fallback grammar.

Two graph algorithms must remain bounded for this model to scale like
Chevrotain self-analysis. Shared GAST rule analysis memoizes completed rules,
and recursive-rule detection uses one strongly connected component pass instead
of traversing the whole call graph once per rule. Callsite LL(k) analysis uses a
memoized set of token prefixes keyed by GAST node and remaining lookahead depth;
prefixes are deduplicated with a hash set and capped at 2,048 paths. Exceeding
that analysis budget marks the callsite analysis-only and does not authorize
runtime pruning. This preserves PEG order and prevents a bounded token depth
such as `k=4` from still producing unbounded intermediate path combinations.

On 2026-07-11, focused smokes covered a 64-level shared GAST DAG, an `8^4`
alternative/sequence expansion, recursive and parameterized rules, AB/AC LL(2),
and the `A` versus `A B` prefix ambiguity; all completed below one second. The
real generated TypeScript cold CST probe moved from the previous 30-second
timeout to about `1511ms` for `SlimeParser.ts` (67,200 characters) and `1953ms`
for `SlimeAstCreateUtils.ts` (174,056 characters). A same-probe worklist A/B on
the larger file measured `834.6ms` enabled versus `892.6ms` disabled with equal
token and CST counts. The matching pre-tokenized recognizer A/B measured
`268.2ms` enabled versus `340.3ms` disabled, while removing 2,033 wrapper calls,
2,031 core executions, and 500 cache-key builds through additional exact GAST
direct plans. Treat this unit as a cold self-analysis and architecture fix with
a measured warm recognizer benefit; it does not by itself remove the remaining
successful-path wrapper/core/token costs.

Static enhanced parser classes are generated source artifacts, not runtime
proxies. `SubhutiStaticEnhancedGenerator` is the standard generator for Java
parser classes: it preserves the source parser superclass, source-visible method
name and signature, `@SubhutiRule.name`, `@SubhutiRule.cache`, void versus
returned-value wrapper shape, argument order, and structural multi-argument
cache keys. Generated classes compile to ordinary `.class` files and may
explicitly enable worklist GAST self-analysis. Runtime bytecode generation,
reflection dispatch, and `SubhutiParser.create(...)` are not parser creation
paths.

Recording-mode FIRST analysis must treat `OPTION` and `MANY` as nullable. A
recorded token inside either container cannot terminate the whole alternative
as an exact FIRST sequence because the following grammar element may also start
that alternative. Recording continues after the nullable container; if the
remaining structure cannot be represented completely, the callsite becomes
analysis-only. It must never use the truncated container prefix to authorize
negative runtime pruning. `AT_LEAST_ONE` remains non-nullable.

On 2026-07-11, this correction fixed the framework-level `typeType | VOID`
case where `typeType` begins with `Many(annotation)`: primitive `INT` had
previously been excluded from FIRST and both branches were skipped. Focused
`OPTION(A) B` and `MANY(A) B` smokes now preserve `B` as a valid start. A
generated 156-rule `JavaParserStaticEnhanced` then passed 7/7 incremental Java
parser cases, including fields and methods. On a 14,643-character Java source,
the static `.class` path measured about `110.5ms` parse-only and `125.3ms`
parse-plus-AST over 30 measured rounds.

The next retained worklist/GAST correctness unit makes runtime planning partial
without making it lossy. Callsite analysis starts at LL(1) and increases `k`
only while static alternatives are still ambiguous, up to the framework bound;
it does not eagerly expand every callsite to LL(4). Every generated token path
stores all of its non-empty prefixes, so runtime lookup can try the longest
available key and then shorten it without losing a valid PEG branch. A branch
whose first token is still contextual or dynamic remains an ordered runtime
candidate beside the static token candidates. Planning-gated alternatives are
also retained until their gate is evaluated. Dynamic candidates may therefore
participate in an immutable lookahead plan, but they cannot use the direct
single-alternative `MANY` path because failed repetition still requires normal
state restoration. A raw `Runnable` prediction miss likewise means "no safe
pruning", not "the ordered PEG choice has no branches". Replacing or extending
the effective grammar invalidates parser-local and hot prediction plans before
the new runtime plan is installed.

Focused coverage proves planning-gate precedence, partial static plus dynamic
alternatives, dynamic `MANY` restoration, raw `Runnable` ordered backtracking,
LL(1)-first bounded analysis, and shorter-prefix lookup. Ten static Slime parser
smokes and five Subhuti GAST/worklist/runtime-plan smokes pass. The 40,500-
character real `OvsParser.ts` also parses through `SlimeParserStaticEnhanced`
with `fail=false` and the cursor at character 40,496; the remaining four
characters are trailing whitespace. This is one parser path: dynamic candidates
are part of the same immutable plan and ordinary PEG execution, not a fallback
parser or compatibility grammar.

An exact GAST rule must describe the complete consumable rule body, not only its
FIRST tokens. On 2026-07-11, `TemplateLiteral` had been declared as the terminal
choice `TemplateHead | NoSubstitutionTemplate`; that shape was sufficient for
lookahead but falsely authorized recognizer direct execution. The direct plan
consumed `TemplateHead` and skipped the embedded expression and template tail,
so a minimal ``function f(token: any) { return `${token}` }`` stopped before the
function in `cst(false)` mode while CST mode remained correct. The declaration
now models `TemplateHead` followed by a dynamic substitution tail, or the full
no-substitution token. This preserves FIRST/lookahead planning and prevents a
direct recognizer plan until the complete substitution grammar is structurally
known. A focused recognizer smoke and the full 40,500-character `OvsParser.ts`
benchmark pass in both modes; five measured source-mode rounds reported CST
`avgMs=226.421 bestMs=207.314` and recognizer
`avgMs=188.141 bestMs=174.988`. Treat every similar terminal-only summary as
Graph/FIRST data or as a GAST prefix with an explicit dynamic tail, never as an
exact executable GAST body.

The next retained token-mode hot-path unit removes allocation from
`LexerMode.hashCode()`. Token-cache and rule-cache identities call that method
for every mode-aware lookup; the old `Objects.hash(name)` allocated a varargs
array each time. `LexerMode` now computes the same stable hash value once in its
constructor and returns the cached integer. On the real 40,500-character
`OvsParser.ts` source-mode recognizer, the clean 20-round baseline was
`avgMs=178.215 bestMs=136.183`; two retained 20-round runs measured
`avgMs=169.197 bestMs=138.072` and `avgMs=172.271 bestMs=141.732`, about a
3.3% to 5.1% average improvement. Under the same JFR settings, average time
moved from `161.886ms` to `154.209ms`, and
`SubhutiTokenCacheLookupKey.reset` fell from 8.06% to 3.08% of execution
samples. This is a semantic mode-identity optimization, not a cache-policy
change.

The same audit found that the direct recognizer token-read path skipped the
existing pre-tokenized REGEXP negative gate. It now applies that gate before
falling back to mode-specific lexer work, matching `_getOrParseTokenEntry` and
preserving the standard regexp-versus-default token decision. The focused smoke
reports `preTokenizedRegexpNegativeHits=1` and `tokenCacheGets=0`. Seventeen
focused Subhuti/Slime smokes, including template-literal source-mode parsing,
pass after the change.

The next retained cache-key architecture unit makes previous-token identity a
declared lexer dependency instead of an unconditional cache dimension.
`SubhutiLexer` inspects token context constraints once: `onlyAfter` and
`notAfter` make tokenization depend on the previous token name; line/start-only
constraints do not. `SubhutiParserCore` includes `lastTokenName` in the token
cache key only for a lexer that declares that dependency. The lexer still
receives the real previous token on every cache miss. A focused smoke proves
both contracts: a context-free lexer reuses one entry across two previous-token
states, while an `onlyAfter` lexer retains separate entries and returns distinct
token kinds.

This removes a large accidental state dimension from the real JavaScript
grammar. `OvsParser.ts` had 18,766 token-cache keys but only 7,556 distinct
`sourceIndex + lexerMode` groups; 15,635 entries belonged to groups with two to
nine previous-token variants even though the JavaScript token table has no
`onlyAfter` or `notAfter` constraint. After the change, token-cache misses fell
from 18,768 to 7,558. Two 20-round source-mode recognizer runs measured
`avgMs=118.488 bestMs=92.047` and `avgMs=118.147 bestMs=94.908`, compared with
the retained allocation-free mode-hash baseline of `avgMs=169-172` and
`bestMs=138-142`, about a 30% average improvement. The matching 20-round CST
run measured `avgMs=130.385 bestMs=104.242`. Under the same JFR setup,
recognizer average moved from `154.209ms` to `121.117ms`. Eighteen focused
Subhuti/Slime smokes pass, and source-mode generated-file probes measured
`SlimeParser.ts` at `avgMs=251.982` and `SlimeAstCreateUtils.ts` at
`avgMs=447.665` over three rounds.

Once that semantic key collapse is proven, context-free canonical lexer modes
may use direct source-index storage. `SubhutiTokenCache` now keeps lazy arrays
for `DEFAULT_MODE`, `TEMPLATE_TAIL`, and `REGEXP` when the active token table has
no previous-token dependency. A token read becomes a mode check plus
`entries[sourceIndex]`; context-dependent token tables and custom modes retain
the structured `SubhutiTokenCacheKey` map. `clear()` resets both representations,
and `isEmpty()` preserves the direct-recognizer guard. This is the source-mode
equivalent of Chevrotain's token-array cursor without pretending that
context-sensitive/custom modes are globally linear.

On `OvsParser.ts`, two 20-round recognizer runs measured
`avgMs=115.340 bestMs=88.991` and `avgMs=108.203 bestMs=83.452`, improving on
the semantic-key Map baseline of about `118ms` average and `92-95ms` best. The
20-round CST result moved from `avgMs=130.385 bestMs=104.242` to
`avgMs=122.474 bestMs=95.049`. Under JFR, the run measured
`avgMs=97.356 bestMs=82.448`; `_getOrParseTokenEntry` and token-cache
`HashMap.getNode/hash` disappeared from the leading hot-method list, leaving
`LA(int)` as the largest parser-framework method. The context-free/contextual
cache smoke, the 18 focused Subhuti/Slime smokes, and the 1,000-round recognizer
plan probe all pass.

The next retained lookahead unit removes quadratic repeated source walks from
`LA(offset)`. A real OVS profile recorded 44,426 ordinary lookahead calls:
18,253 at offset 1, about 16,371 at offsets 2 through 15, and 14,640 above 15.
Grammar gates such as optional-chain detection and balanced member/arrow scans
ask for offsets 1, 2, 3, and so on while parser state is unchanged. The old
implementation restarted from `currentIndex` for every request, so a linear
scan generated 1.24 million token-cache reads.

For non-pretokenized, non-recovery lexers without previous-token dependence,
Subhuti now keeps one parser-state lookahead sequence keyed by source index,
line, column, and lexer mode. It grows only from the last known token and serves
already materialized offsets in O(1). A parser state change resets the sequence;
pretokenized ordinal lookup, mixed-mode lists, recovery, and contextual token
tables keep their existing paths. Runtime stats expose sequence hits, fills,
and resets. The focused smoke proves four-token incremental extension, repeated
offset hits, and reset after consumption.

On `OvsParser.ts`, token-cache reads fell from about 1,246,000 to 75,000, a 94%
reduction. Two 20-round recognizer runs measured
`avgMs=105.558 bestMs=82.314` and `avgMs=92.232 bestMs=71.248`, versus the
source-index array baseline of `avgMs=108-115` and `bestMs=83-89`. The 20-round
CST result measured `avgMs=109.887 bestMs=87.094`, down from
`avgMs=122.474 bestMs=95.049`. Under JFR, recognizer measured
`avgMs=86.717 bestMs=72.759`; `LA(int)` disappeared from the leading hot-method
list and `sourceLookaheadEntry` accounted for only 1.99% of samples. Nineteen
focused smokes and the 1,000-round recognizer reset/plan probe pass.

The next retained lexer-framework unit adopts Chevrotain's ordered first-character
candidate shape without requiring grammar-specific hints. `SubhutiCreateToken`
now distinguishes an exact `fixedValue` contract from ordinary regex value
metadata. At lexer construction, Subhuti builds immutable ASCII first-character
candidate arrays: exact fixed tokens enter only their declared first-character
bucket, while regex tokens remain in every bucket at their original relative
position. Non-ASCII input retains the full ordered token table. This preserves
keyword/identifier priority, long/short operator priority, skip tokens, lexer
modes, context constraints, and lookahead; it is one standard lexer path, not a
fallback scanner.

The JavaScript/TypeScript table has 120 definitions: 96 exact fixed tokens and
24 regex definitions. On the 40,500-character `OvsParser.ts` probe, one parse
performed 188,548 candidate checks and 151,086 pattern attempts. The retained
20-round result moved CST from `avgMs=103.994 bestMs=83.997` to
`avgMs=94.312 bestMs=69.592`, and recognizer mode from
`avgMs=82.075 bestMs=69.214` to `avgMs=70.840 bestMs=55.895`. JFR reduced
`SubhutiLexer._matchTokenWithMode` from 5.98% to 2.53% of samples and
`Matcher.reset` from 4.78% to 0.84%. Focused coverage proves
keyword/identifier conflicts, longest operators, comments, template-tail mode,
and ordered regex candidates without fixed hints.

The next retained GAST correction separates two identities that a runtime plan
must not conflate. A candidate-map key such as `A` may be only a FIRST prefix
summary for complete paths `A B` and `A C`; it is not proof that an actual
one-token alternative `A` exists. Prefix preservation now compares the complete
token sequences and their owning alternative indexes. Thus `A B | A C` becomes
LL(2), while a real `A | A B` choice still preserves both PEG-ordered candidates
for the longer input.

Parser runtime plans also retain the keys produced by exact GAST self-analysis
separately from graph/FIRST prediction keys. Only an exact-GAST key may execute
before callsite GAST analysis; graph predictions remain at the conservative
prediction boundary. This lets a declared exact `AB | AC` plan run directly
without allowing a partial graph plan to reject valid contextual syntax such as
`yield => 42`. Focused runtime-plan tests assert both identities directly.
Against the preceding retained lexer commit, a paired 20-round recognizer run
was effectively wall-clock neutral (`73.945ms` to `73.241ms`) while state saves
fell from 9,290 to 8,838 and direct planned repetitions rose from 10 to 462.

Declared graph alternations are now reusable rule facts as well as callsite
prediction declarations. `putAlternation(scope, ruleNames...)` installs an
alternation rule for `scope` when no explicit rule body exists; a later or
earlier explicit `putRule(scope, ...)` remains authoritative. Grammar owners
should reference that scope instead of maintaining a second hand-written FIRST
token list. This removes duplicated metadata and lets nested graph analysis
resolve the same canonical alternatives used by parser-class self-analysis.

The first retained use replaces `ModuleStatementListItem`'s stale terminal list
with a reference to `StatementListItem`. The duplicate list had omitted valid
`Yield` and `Await` expression starts, so a graph plan rejected `yield => {}`
before the dynamic statement branch could run. With the canonical alternation
rule, both the yield-arrow regression and the `-value` exponentiation probe pass.
On `OvsParser.ts`, the following 20-round result moved CST from `91.681ms` to
`87.319ms` and recognizer mode from `72.729ms` to `65.965ms`; recognizer state
saves fell from 8,838 to 3,210 and rule-wrapper calls from 37,624 to 32,849.

Lexer-mode normalization is parser-class planning work, not runtime `Or(...)`
work. Every immutable `SubhutiOrPrediction` now owns copied token paths, one
fully normalized lexer-mode path for each token path, and its shared lexer-mode
prefix, the value-aware-token flag derived from those paths, and ordered groups
of token-path indexes sharing the same lexer-mode path. Each group also records
whether any of its paths needs value-aware keys. Runtime prediction reads those
facts directly; it must not rebuild
`LexerMode[]`, `Collections.nCopies(...)`, or a shared mode prefix on each
callsite execution, rescan token paths for value-aware keys, or construct a
temporary mode-to-lookahead `HashMap` for mixed-mode choices. Dynamic graph
candidates remain explicit ordered candidate indexes and continue through
ordinary speculative execution.

On the same 40,500-character `OvsParser.ts` probe, a crossed 50-round A/B moved
CST from `80.703ms` to `76.492ms` and recognizer mode from `62.778ms` to
`57.901ms`. A paired JFR recognizer run moved from `66.714ms` to `62.812ms`:
`sharedLexerModePrefix` disappeared from hot methods, mixed-mode candidate
selection fell from 4.01% to 3.14%, and the prior `LexerMode[]` and
`Collections$CopiesList` allocation hotspots disappeared. The parse counters,
rule-wrapper counts, state saves, token counts, and CST output remained equal.
Moving the remaining value-aware-key scan to the same plan changed a crossed
80-round recognizer result from `59.846ms` to `59.028ms`; a paired JFR run moved
from `61.825ms` to `59.331ms`, and the prior 3.13% scan hotspot disappeared.
Precomputing mixed-mode path groups changed the next crossed 80-round result
from `57.583ms` to `55.312ms`; paired JFR moved from `58.333ms` to `57.159ms`,
the mixed-mode prediction method fell from 3.86% to 3.22%, and its runtime
`HashMap.computeIfAbsent` hotspot disappeared.
Using each group's value-aware flag to select token names or value-aware keys
changed the next crossed 80-round result from `55.813ms` to `54.747ms`; paired
JFR moved from `57.301ms` to `55.522ms`, and the prior
`currentTokenKeysForPrediction`, value-key matcher, and `String[][]` allocation
hotspots disappeared for ordinary token-name groups.

`Alternative` execution shape is also part of the runtime plan boundary.
Runnable alternatives now retain and execute their original `Runnable`
directly instead of wrapping it in a second capturing `Supplier` lambda on every
`Or(...)` call. This preserves the same `Alternative.rule/token/tokens/structure`
source APIs, GAST nodes, gates, PEG order, and null result while making the
original method-reference class the callsite identity.

On `OvsParser.ts`, a crossed 50-round result moved CST from `72.069ms` to
`66.892ms` and recognizer mode from `53.236ms` to `50.328ms`. JFR removed both
wrapper-lambda allocation classes; allocation pressure attributed to
`Alternative.rule(... Runnable)` fell from 7.76% to 2.54%.

Rule-reference grammar metadata is now materialized only when self-analysis
asks for it. Hot `Alternative.rule(...)` construction retains the rule name and
execution payload, while `RULE_REF` `SubhutiGrammarNode` and `SubhutiGastNode`
objects are created lazily by `predictionGrammarNode()` / `gastNode()`. This is
one standard grammar path: self-analysis still sees the same complete nodes,
while runtime execution no longer rebuilds metadata that the immutable plan
already owns.

On `OvsParser.ts`, a crossed 80-round recognizer result moved from `52.580ms`
to `52.349ms`; paired JFR moved from `54.620ms` to `53.545ms`. Grammar-node
allocation pressure fell from 7.16% to 2.94%. A 50-round CST pair moved from
`66.215ms` to `64.828ms` with identical CST and parser counters.

Worklist self-analysis now records stable `Or(...)` callsites beside their full
branch GAST. The parser-class runtime plan builds lookahead entries only for
callsites whose recorded branch structure is stable and whose resolved GAST is
exact. Runtime lookup uses the first executable lambda class as the occurrence
identity and verifies the recorded arity and first structural identity before
reading the immutable plan. A `Runnable Or` containing a conditional `null`
branch is explicitly dynamic and cannot receive this direct callsite plan;
partial or context-dependent GAST continues through the ordinary ordered PEG
prediction/execution semantics. This is one parser path, not an exact-plan plus
fallback-parser pair.

`getRuntimePlanReport()` exposes `gastCallsiteCount` and
`gastOrCallsitePlanCount`; `getOrPredictionStats()` exposes
`orPredictionRuntimePlanCallsiteHits`. The focused
`SubhutiGastCallsiteRuntimePlanSmokeTestMain` proves three repeated exact calls
use the parser-class plan with zero runtime key builds while a conditional-null
callsite receives zero direct-plan hits. On the 40,500-character
`OvsParser.ts`, the retained plan covered about 1,907 recognizer calls. Two
crossed 50-round pairs averaged about a 2.4% CST improvement and a 1.1%
recognizer improvement. Paired JFR reduced
`hotOrPredictionEntryMatchesAlternatives` from 3.10% to 0.98%; no runtime-plan
lookup appeared as a replacement hotspot. This is a measured architecture step,
not a claim that all dynamic callsites have reached Chevrotain-style direct
plans.

Grammar declaration order is part of the self-analysis contract. GAST rules,
alternations, recorded callsites, and graph-derived rule facts must publish
immutable insertion-ordered snapshots; unordered `Map.copyOf(...)` or
`Set.copyOf(...)` views are not valid plan inputs. Exact runtime plans identify
recorded `Or(...)` occurrences by stable grammar/callsite order, so changing
iteration order across snapshots can change plan identity or attach a plan to
the wrong occurrence even when the grammar source did not change. The focused
`SubhutiGrammarOrderSmokeTestMain` owns this invariant.

Runtime gates and token lookahead are separate plan dimensions. An explicit
GAST `GATE` consumes no token, so exact callsite analysis may compute the token
paths that follow it while the ordinary runtime `Alternative` still evaluates
`gateAllows()` before executing the selected branch. Planning gates remain
eligible runtime candidates through the standard planning-gate path. This does
not make arbitrary `DYNAMIC` nodes exact and does not authorize direct rule
execution through a gate; it only lets a structurally complete `Or(...)`
callsite reuse immutable token lookahead without dropping the predicate.

LL(k) exactness is decided at complete token paths, not every intermediate
prefix. `A B | A C` is exact at `k=2` when each complete path maps to its one
owning alternative, even though the intermediate `A` prefix is shared. `A | A
B` remains analysis-only because the complete shorter path `A` still maps to
both ordered PEG candidates. `SubhutiGastCallsiteAnalysisSmokeTestMain`,
`SubhutiGastCallsiteRuntimePlanSmokeTestMain`,
`SubhutiGrammarRecordingSmokeTestMain`, and
`SubhutiOrFirstTokenPredictionSmokeTestMain` jointly own these boundaries.

On the 40,500-character `OvsParser.ts`, admitting exact gated token lookahead
increased `gastOrCallsitePlanCount` from 38 to 39 and recognizer
`orPredictionRuntimePlanCallsiteHits` from about 1,907 to 3,536. A sequential
50-round run measured about `64.679ms` CST and `49.833ms` recognizer versus the
recent approximately `65.4ms` / `50.0ms` baseline. All 26 Subhuti parser smoke
mains passed. The result is retained as a self-analysis coverage improvement;
wrapper and packrat work remain separate later targets.

Generated static rule wrappers now carry deterministic numeric rule ids. The
generator assigns ids by grammar rule name in stable first-occurrence order and passes them to the
standard wrapper API without changing grammar source methods, rule names,
arguments, return values, or cache policy. Runtime recursion detection uses a
parser-instance `ActiveRuleInvocations[]` slot for indexed wrappers instead of
performing a rule-name hash-map lookup on every wrapper entry. Packrat cache
identity remains unchanged and is a separate optimization boundary.

Overloads with the same effective `@SubhutiRule` name share one numeric id.
This preserves the previous rule-name recursion domain; assigning ids per Java
method signature is incorrect because mutually recursive overloads would no
longer meet in the same active-invocation slot. The generator smoke owns this
same-name-overload invariant.

This uses one tiered execution model, not fallback parsing. The parser instance
that builds worklist self-analysis stays on the cold name-indexed invocation
path; later instances that reuse the immutable parser-class analysis use the
numeric slots. Handwritten/non-generated wrappers use the same semantics
through the non-indexed wrapper overload. A focused
`SubhutiIndexedRuleWrapperSmokeTestMain` proves indexed wrappers avoid the
name map and still reject same-position recursion, while the generator smoke
owns deterministic emitted ids.

On the 40,500-character `OvsParser.ts`, a B-C-C-B crossed 20-round run moved
the mean from about `71.412ms` / `53.584ms` CST/recognizer to `67.870ms` /
`52.648ms`, approximately 5.0% and 1.7% faster. A later sequential 50-round
run measured `64.599ms` CST and `48.761ms` recognizer, with all 35,474 CST and
32,852 recognizer wrapper entries using indexed slots after self-analysis was
warm. The generator smoke plus all 27 parser smoke mains passed. An `-Xint`
first-parse check measured about `10.861s` candidate versus `11.088s` baseline;
JIT cold-start samples remained noisy, which is why the first self-analysis
builder stays on the cold tier.

An indexed adaptive-memo policy was measured and removed. Replacing the
`Map<String,Integer>` / `Set<String>` learning state with rule-id arrays kept
the exact 10,110 key builds, 8,266 puts, and 22,219 adaptive skips only after
same-name overloads shared an id, but a B-C-C-B run moved from about
`56.961ms` to `57.129ms` recognizer time. The approximately 0.3% regression
did not justify another policy representation, so adaptive memoization remains
name-keyed.

Numeric ids were also tested as warm-tier packrat-key dimensions and removed.
The cache hit/put/key-build counts and cache scope remained identical, but a
B-C-C-B crossed run moved from about `57.122ms` to `59.451ms` recognizer
time, roughly 4.1% slower. `SubhutiRuleCacheKey` therefore remains keyed by
the effective grammar rule name plus its existing parameter/token/mode context.
Numeric rule ids are retained only for active-invocation slots, where the
crossed benchmark proved a material gain.

Parser-class reuse of learned adaptive low-yield names was also measured and
removed. It let later parser instances start with 57 learned rules, reducing
per-file key builds from 10,110 to 6,440 and puts from 8,266 to 4,596, but a
B-C-C-B run regressed from about `54.173ms` to `58.107ms` recognizer time,
roughly 7.3%. Adaptive low-yield learning therefore remains parser-instance
local; lower cache counters alone are not evidence of a faster parser.

Partial worklist callsites are not exact parser-class execution plans. A focused
experiment admitted every callsite with at least one safe static prefix,
increasing `gastOrCallsitePlanCount` from 38 to 113. After fixing two lossy
nullable/dynamic path cases, the real `OvsParser.ts` parsed correctly, but the
conservative dynamic candidates increased recognizer wrapper calls from 32,852
to 40,059 and state saves from 3,219 to 11,640. Keeping the richer partial path
representation without direct execution still regressed the paired benchmark
from about `65.4ms` / `50.0ms` CST/recognizer to `73.2ms` / `60.2ms`.
The experiment was therefore removed. Partial GAST remains diagnostic and may
feed the ordinary contextual prediction path; it must not be installed as an
exact immutable callsite plan until the framework can encode its dynamic paths
without losing correctness or increasing successful-path work. The focused
runtime-plan smoke asserts both static-hit and dynamic-hit partial examples
receive zero `orPredictionRuntimePlanCallsiteHits`.

Parameterized rule GAST cannot be specialized independently of callsite
identity. A rejected experiment materialized internal rules for every distinct
`cacheKeyExtra` body while runtime callsites were still keyed only by lambda
class. On the generated Slime parser this propagated variant differences from
13 source rules to 122 rules, expanded GAST rules from 267 to 866, reduced
recorded stable callsites from 118 to 61, and reduced exact callsite plans from
39 to 26. The experiment was removed. A future variant-aware design must carry
the enclosing normalized rule variant through recording, immutable callsite
plan identity, and runtime lookup as one architecture change. It may fold equal
variant bodies back to the base rule, but must not publish synthetic GAST rules
that runtime callsites cannot identify.

Adding enclosing `ruleName + cacheKeyExtra` to every runtime callsite identity
was also tested and removed. It increased recorded callsites from 118 to 410
and exact plans from 39 to 106, but did not increase real `OvsParser.ts` plan
hits. A B-C-C-B crossed run measured about `70.905ms` / `54.690ms`
CST/recognizer for the baseline and `72.733ms` / `54.480ms` for the identity
candidate, so CST regressed about 2.6% while recognizer stayed flat. Combining
that identity with synthetic variant GAST still produced 866 rules and reduced
`OvsConsumer.ts` runtime-plan hits from 77 to 55, with about `6.100ms` CST and
`4.161ms` recognizer. Both experiments were removed. The next variant design
should use a generated/static grammar occurrence id (or equivalent immutable
GAST occurrence identity), with parameters represented by explicit gates or
bounded specialization, instead of copying every callsite across all 783
observed parameter states or adding nested runtime map lookups.

The retained variant-aware GAST foundation preserves parameter identity without
installing that rejected runtime lookup. `RULE_REF` nodes may carry the
normalized target `cacheKeyExtra`; worklist self-analysis stores every
`ruleName + cacheKeyExtra` body in an immutable variant table while continuing
to publish the name-only common body for the existing runtime plan. Variant
self-analysis follows those exact references transitively, and
`getRuntimePlanReport()` exposes `gastVariantCount`, `gastExactVariantCount`,
`gastDynamicVariantCount`, and `gastVariantConsumingElementCount`. This is one
grammar model, not a fallback parser: variant metadata is self-analysis input,
while no variant may affect runtime execution until stable grammar occurrence
identity is available end to end.

On generated `SlimeParser.ts`, the retained model records 783 variants across
267 name-level rules. Of those variants, 105 are exact and 748 contain or reach
a dynamic boundary; recursive variants may be both structurally exact and
runtime-dynamic, so these categories are not complements. The existing runtime
surface remains unchanged at 118 callsites, 39 exact callsite plans, 5,758
runtime-plan hits, and 56,298 wrapper calls. This proves two things: variant
identity was previously discarded too early, and preserving it alone cannot
remove the successful-path cost. The next architecture boundary is a static
enhanced grammar generator that emits stable rule-local occurrence ids and
complete GAST for the admitted combinator/control-flow subset, so immutable
variant plans can be indexed directly instead of guessed from lambda classes or
looked up through a universal `ruleName + cacheKeyExtra` map.

Recording more GAST structure is not by itself a parser-speed result. A rejected
experiment changed `assertNoLineBreak()` during worklist recording from a
`DYNAMIC(contextual-lookahead)` stop into an explicit non-consuming `GATE` and
continued recording the remaining rule body. On generated `SlimeParser.ts`
(67,200 characters), this increased GAST rules from 267 to 272, exact rules from
61 to 63, exact callsite plans from 39 to 44, and runtime-plan callsite hits from
5,758 to 6,299. It did not reduce any wrapper, core execution, cache-key, or
direct-recognizer counter, and it added lookahead/token-cache reads. A B-C-C-B
five-round crossed run measured `134.245ms` / `139.589ms` for the baseline and
`142.967ms` / `154.725ms` for the candidate, about an 8.6% average regression.
The experiment was removed. Keep explicit GAST gates when they are required to
model a real grammar predicate, but do not promote contextual assertions merely
to raise coverage counters. A retained GAST change must either remove proven
successful-path work or produce a wall-clock win on the focused real parser;
plan count and plan-hit count alone are insufficient.

Static enhanced parser generation now publishes the deterministic rule-id table
beside the generated wrappers. The table uses the same stable first-occurrence
rule-name ordering as the numeric ids passed to `executeVoidRuleWrapper(...)`
and `executeRuleWrapper(...)`; same-name overloads still share one id. Both
`SlimeParserStaticEnhanced` and `JavaParserStaticEnhanced` expose this table
through the parser-class hook. The table has no hot-path behavior by itself. It
is the indexed identity boundary that future generated rule-local occurrence
plans must use, rather than reconstructing rule names or adding composite string
keys at runtime.

Exact GAST is proof of grammar completeness, not proof that packrat memoization
is unprofitable. A rejected variant experiment treated every exact,
non-recursive variant as recognizer pass-through. On `SlimeParser.ts` it reduced
wrapper/core executions by 4,107, but cache-key builds rose from 12,809 to
13,605, cache puts rose from 9,630 to 10,426, and a crossed baseline/candidate
run regressed from `132.889ms` to `220.108ms` in the first pair. Restricting the
plan to one-consuming-element variants and indexing it through the generated
rule-id table removed that large regression, but neither `SlimeParser.ts` nor
`SlimeAstCreateUtils.ts` executed any eligible variant: structural counters
were unchanged, while the larger file measured `299.972ms` baseline versus
`345.413ms` candidate. The runtime check was removed. Do not equate
"exact" with "skip wrapper/cache". A successful-path optimization must be a
complete executable plan, or have separate evidence that memoization and state
work are unnecessary for that exact occurrence; otherwise exact GAST remains
self-analysis metadata.

The retained static-occurrence architecture removes the runtime lambda-class
identity boundary. `SubhutiStaticEnhancedGenerator` now uses the JDK Javac Tree
API during the parser build to preserve rule variants and the complete lexical
grammar occurrence tree, including `OR`, alternatives, `OPTION`, `MANY`,
`AT_LEAST_ONE`, subrules, token consumption, gates, and actions. Every grammar
operation receives a dense source-order `rule id + variant id + occurrence id`
address. The generated parser embeds one immutable `SubhutiStaticGrammarPlan`;
the parser-class runtime plan derives executable lookahead from that model once.

The source grammar API remains unchanged. A second build step uses the standard
JDK Class-File API to rewrite compiled combinator invocations such as
`Or(Runnable...)` into `OrIndexed(Runnable[], occurrenceId)`. Generated rule
wrappers establish the active numeric rule/variant scope before invoking the
original body. Runtime indexed combinators therefore use direct array lookup;
they must fail on a missing scope, unknown occurrence, source/class mismatch, or
kind mismatch. Runtime `StackWalker`, lambda-class maps, composite string keys,
and a recording fallback are not part of this path.

The executable slice covers static `OR`, `OPTION`, `MANY`, and `AT_LEAST_ONE`.
A focused instrumented parser kept ordinary combinator source, rewrote four
bytecode callsites, selected `BRule` without executing `ARule`, skipped absent
optional/repeated bodies through precomputed token lookahead, and enforced
progress for repeated bodies. `ACTION` is non-consuming self-analysis
structure, while `GATE` remains an explicit runtime predicate; neither may be
confused with an unknown consumable rule body. Do not reintroduce lambda
guessing or per-call recording for these containers.

Complete static void rule variants may additionally replace their rule body with
the generated direct recognizer plan in `cst(false)` mode. This optimization is
admitted only when the occurrence GAST is wholly reducible to terminals,
subrules, alternations, and the supported containers. A focused pure-grammar
probe reduced the successful path to the single top-level wrapper and one direct
recognizer execution; nested subrule wrappers and cores were not entered. Any
`ACTION`, dynamic boundary, CST output, recovery mode, or debugger disables that
replacement. Source actions are observable semantics and must never be dropped
merely because their neighboring token path is statically known.

The direct executable representation is an instruction tree, not a flattened
terminal list. It preserves nested `Rule`, `Sequence`, `Choice`, `Optional`,
`Repetition`, `AtLeastOne`, and `Terminal` operations. Recognizer execution
ignores rule-enter/exit materialization, while CST execution uses the same tree
to create and attach every nested rule node and token node. The generated
wrapper continues to own the root rule boundary, so the instruction executor
starts at the root body and materializes only nested `SUBRULE` boundaries. A
focused CST probe proved the expected `Top -> BRule -> B` shape while retaining
all following container tokens, with one wrapper and one direct-plan execution.
Do not flatten subrules when compiling a CST-capable plan; token-equivalent
output with a different CST hierarchy is not parity.

`getRuntimePlanReport()` must expose this static architecture separately from
legacy Graph/worklist GAST facts: static rule count, variant count, occurrence
count, action count, gate count, dynamic count, and executable variant count.
These counters are the migration coverage gate. A higher FIRST/callsite-plan
count does not substitute for more executable static variants, and an
action/gate/dynamic-heavy rule must remain visibly non-executable until its
semantics are represented in the instruction model.

Source analysis must give lambda bodies and Java method references the same
grammar ownership. A method reference such as `this::TSAsExpressionTail` is a
static `NonTerminal` targeting that exact rule variant; it must not become an
empty alternative, a lambda-class identity, or a runtime recording request.
The arguments of a `SUBRULE` call are runtime parameter bindings, not grammar
children. Accessors such as `params.yield()` and `params.await()` therefore do
not create dynamic GAST descendants under the referenced non-terminal; any
context-dependent behavior remains an explicit gate in the referenced rule.

Parser helper methods that consume a terminal use the framework-level
`@SubhutiTerminal(tokenName=..., tokenValueArgument=...)` declaration. The
source analyzer resolves that declaration into an exact terminal or
value-terminal occurrence. A non-literal value argument stays visibly dynamic;
the analyzer must not guess it, specialize it from a sample run, or hard-code a
Slime helper name. This is Subhuti's static equivalent of an explicit
Chevrotain `CONSUME` node.

Nested parser classes are owned by their enclosing Java source file during
Javac Tree analysis, so source filtering must map `Outer.InnerParser` to
`Outer.java` without scanning unrelated project sources. The real Slime build
now generates and initializes one static plan with 301 rules, 317 variants, and
3,313 occurrences. Source-owned parser helper methods are admitted as inline
non-terminals only after an isolated source scan proves their complete body has
no action, gate, dynamic boundary, recursion, or non-parser receiver. Blindly
inlining every Java helper is invalid because it imports lexer/runtime utility
implementation into the grammar tree. The current coverage gate reports 88
inline occurrences, 154 dynamic occurrences, 263 immutable occurrence
predictions, and 36 executable variants. `SubhutiStaticGrammarPlan` owns this
self-analysis result. The Class-File instrumenter gives every source-owned
top-level `OR` a stable occurrence id; an occurrence with no safe lookahead
plan remains indexed but executes in PEG order. `OPTION`, `MANY`, and
`AT_LEAST_ONE` are indexed only when their static occurrence has an exact
executable prediction. Runtime-plan construction reads the precomputed
occurrence prediction instead of repeating callsite analysis.

Direct rule-body execution, occurrence identity, and branch pruning have
separate proof gates. A complete executable rule body does not authorize an
occurrence to prune a partial choice. The retained template-literal recognizer
regression proves this boundary: giving every container a non-exact start plan
allowed a partial prediction to skip the function declaration path and leave
the source unconsumed. Stable identity is safe for every source-owned top-level
`OR`; branch pruning is enabled only when the occurrence plan proves it safe.
This preserves ordinary PEG behavior while removing runtime callsite discovery.

This is a migration baseline, not a completion
claim: the next architecture work must structurally model token/value factories,
helpers, and remaining lambda bodies until the static plan can replace runtime
worklist analysis as the sole authoritative grammar model.

Static enhanced generation no longer has a worklist switch. The generator
accepts one standard five-argument command, emits the source-derived static
plan, and must not override `enableGastWorklistSelfAnalysis()`. Real generated
Slime parser validation requires `gastWorklistAnalysisBuilds=0`; grammar
recording may remain a framework development facility for non-generated parser
experiments, but it is not part of generated parser startup or parsing.

The generated parser currently still receives a small hand-declared exact GAST
surface alongside the generated static plan. That declared surface is static,
not runtime recording, and remains useful for callsites not yet admitted by the
source-derived exactness gate. The next consolidation step is to represent
those declarations in the generated static plan, then remove Graph/declared
GAST as separate authoritative inputs rather than retaining parallel models.

That GAST consolidation is now complete for generated parsers. When a static
plan exists, `effectiveGastGrammar()` is the plan's generated GAST, never the
hand-declared GAST or a worklist result. The real Slime parser now exposes 301
GAST rules and 317 variants from the same plan, including 88 exact rules and
100 exact variants; the previous 24-rule declared GAST is no longer an
authoritative generated-parser input. The generated static plan now also
projects a conservative prefix graph from that same GAST. Consequently
generated parsers no longer read the hand-written Graph as an authoritative
input: `graphRuleCount`, `gastRuleCount`, and `staticRuleCount` are all 301.
Prefix evidence remains explicitly weaker than an exact executable body and
may support FIRST/lookahead or recognizer analysis, but it never authorizes
direct execution by itself.

Generated parser runtime has one strict analysis boundary. Indexed occurrences
read only their immutable static occurrence plans. A generated parser callsite
that has not yet received an occurrence id executes the source PEG order and
must not start runtime Graph traversal, GAST callsite analysis, recording mode,
or prediction-cache construction. This is intentionally different from
non-generated development parsers, which may still opt into explicit
Graph/GAST self-analysis. Focused generated-parser smokes require both
`orPredictionGraphBuilds=0` and `orPredictionRecordingBuilds=0`.

The real Slime Class-File pass now writes stable ids into all 317 parser DSL
bytecode callsites, including source-owned top-level operations, operations in
ordinary Java control flow, and operations compiled into synthetic lambda
methods. Source analysis records every grammar lambda's SAM method name,
enclosing lambda occurrence, and owned grammar occurrences. The Class-File pass
follows each `LambdaMetafactory` implementation handle and maps that source
ownership tree onto the actual synthetic methods recursively. It does not guess
`lambda$...` numbering, because compiler numbering is not a grammar identity.

This mapping is strict. Missing implementation methods, SAM/order mismatches,
duplicate ownership, or source/Class-File occurrence mismatches fail the build;
they do not restore runtime recording or whole-graph analysis. Focused coverage
must prove both a root `OR` and a combinator nested inside an alternative lambda,
while a real generated Slime probe must prove that an absent nested `OPTION`
skips its body through the indexed immutable plan.

Occurrence identity and lookahead eligibility are separate architecture facts,
as they are in Chevrotain's numbered DSL occurrences and precomputed lookahead
function cache. Every real `OR`, `OPTION`, `MANY`, and `AT_LEAST_ONE` bytecode
callsite is rewritten to its indexed API even when self-analysis cannot build a
lookahead plan. An indexed occurrence without a plan executes the single source
PEG body path and must not start Graph traversal, recording, or runtime grammar
analysis. The Class-File instrumenter therefore depends only on source-owned
occurrence identity, never on prediction eligibility.

The static model distinguishes three coverage levels. The current Slime plan
contains 401 parser DSL GAST nodes after safe inline expansion, 317 real
bytecode callsites with stable numeric identity, and 188 indexed callsites with
an immutable executable lookahead plan. Inline-expanded GAST nodes do not claim
independent bytecode callsites. Runtime reports expose these as
`staticDslOccurrenceCount`, `staticIndexedDslOccurrenceCount`, and
`staticPlannedDslOccurrenceCount`; these numbers must not be replaced by one
ambiguous "prediction count".

Static occurrence lookahead is compiled after self-analysis, not interpreted
from the diagnostic `SubhutiOrPrediction` object graph on every parse. The
lexer assigns every token name a dense parser-local `tokenTypeId` in original
vocabulary order and writes that id into every lexer-produced
`SubhutiMatchToken`. Token mode sorting must not change this identity. The
runtime-plan cache includes a strict SHA-256 fingerprint of the ordered token
vocabulary, so a plan cannot be reused with incompatible token ids.

`SubhutiLookaheadPlan` is the generated/static runtime form. It has specialized
plans for ordinary LL(1), value-aware LL(1), and LL(k):

- ordinary LL(1) indexes a precomputed candidate array directly by
  `tokenTypeId`;
- value-aware LL(1) first selects the token family by id and then reads the
  precomputed value entry, preserving contextual keywords such as
  `IdentifierName("from")` without constructing a string key at runtime;
- LL(k) stores ordered token-id/value paths and their prefix candidate lists,
  then compares current token objects directly. Incomplete common prefixes must
  still select the ordered PEG candidates and produce the normal visible parse
  failure; they must not become an empty-candidate success.

Contextual token-consumer helpers must declare their real terminal identity.
`@SubhutiTerminal` supports either `tokenValueArgument` or one fixed
`tokenValue`, never both. For example `Let()` is statically
`IdentifierName("let")`, not a fictional lexer token named `Let`. The analyzer
must reject or leave dynamic missing metadata; the lookahead compiler must not
guess aliases or ignore unknown token names.

The current real Slime gate has 188 planned bytecode callsites and 188 compiled
runtime lookahead plans: 153 ordinary LL(1), 26 value-aware LL(1), and 9 LL(k),
with no mixed-mode callsite. Runtime reports expose these classes plus
`staticCompiledLookaheadCount`; that compiled count must equal
`staticPlannedDslOccurrenceCount`. Focused execution tests separately prove
source semantic actions, value-aware branch selection, complete LL(k) selection,
and incomplete-prefix failure while Graph/recording builds remain zero.

Static generated rules use one compiled invocation engine keyed by dense
`ruleId + variantId`. Runtime self-analysis assigns each variant a numeric
`invocationId`, computes conservative nullability to a fixed point, and compiles
its executable instruction tree once. Generated wrappers enter
`executeStaticRule(...)` or `executeStaticVoidRule(...)`; they do not call the
generic string wrapper, construct a rule cache key, or rebuild a plan. A root
entry initializes parser state once, installs the default-mode token-array input
once, and validates complete consumption once. Nested static rules enter the
numeric core directly. Focused real Slime parsing requires
`staticRootRuleEntries=1`, a non-zero `staticCoreRuleEntries`,
`ruleWrapperCalls=0`, and `ruleCacheKeyBuilds=0`.

Static execution mode is specialized once per root invocation, following
Chevrotain's initialization-time CST hook selection. Subhuti binds one of
`RECOGNIZER`, `CST`, `RECOVERY_RECOGNIZER`, `RECOVERY_CST`,
`DEBUG_RECOGNIZER`, or `DEBUG_CST`, then installs the corresponding rule-core,
void-body, and debug-hook strategies. Nested static rules call those bound
strategies directly; they do not repeatedly inspect the mutable
`buildCst/recovery/debug` flag combination. Runtime stats require
`staticExecutionModeBindings=1` and expose the selected mode. On the same
20-warmup/100-round `let a = 10` recognizer probe, this reduced average parse
time from `1.075ms` to `0.985ms` while retaining zero generic wrappers, cache
keys, Graph builds, and recording builds.

Static rule recursion protection uses a primitive invocation stack, not the
generic wrapper's `ActiveRuleInvocations` object table. Parallel arrays hold the
active `invocationId`, token index, argument, lexer mode, previous-token name,
and previous frame for the same invocation id. A dense head array gives direct
access to the active chain for one rule variant. Entry therefore checks only
active frames of that numeric invocation and preserves the existing rule that
same variant + same argument + same token/mode context is a loop, while a
different parameterized invocation remains legal. Exit is an index decrement
and head restoration.

The same stack owns the active static `ruleId + variantId` scope used by indexed
DSL occurrences. Static parsing no longer pushes rule names into the generic
`ArrayDeque<String>`; diagnostics resolve a name from the generated numeric rule
table only when needed. Generic non-generated wrappers retain their existing
object-based recursion path. On the same 20-warmup/100-round Slime recognizer
probe, replacing static object slots/string scope with the primitive stack
reduced average parse time from `0.985ms` to `0.879ms` and best time from
`0.503ms` to `0.460ms`. Runtime stats expose
`staticPrimitiveInvocationEntries`; generic wrapper/core/cache and runtime
Graph/recording counters remain zero.

Executable-plan FIRST analysis returns the complete terminal set, not one
representative terminal. Sequences continue through nullable prefixes, choices
union every branch, and rule references recurse through the static variant
graph. Nullability preserves Java control-flow structure: a `GATE` owns its
conditional children and is itself nullable; its children are not flattened
into an unconditional sequence. The `SemicolonASI` EOF regression owns this
boundary.

Ordered choice has an explicit epsilon contract, matching Chevrotain's
`EMPTY_ALT` model. `Alternative.empty()` is the only branch identity allowed to
succeed without consuming input. Ordinary lambdas that return successfully
without consuming input are rejected so they cannot mask a later valid branch;
`MANY` and `AT_LEAST_ONE` retain independent progress checks. Source analysis
records `Alternative.empty()` as a nullable GAST action. Grammars use this API
for intentional empty productions such as the omitted binding name in an
anonymous default-exported function or class; an unlabelled `() -> {}` is not a
valid substitute.

Lexer grammar metadata follows the same initialization-time lifecycle as the
static parser plan. `SubhutiLexerVocabulary` is the single immutable compiled
form for a stable token-definition identity sequence. It owns mode-prioritized
definitions, dense token ids, token names, the vocabulary fingerprint, ASCII
first-character candidate arrays, exact-fixed-token counts, and declared
previous-token dependence. `SubhutiLexer` is only the mutable session: it owns
per-run instrumentation and traditional tokenize state while delegating all
grammar metadata to the vocabulary plan. Constructing another parser from the
same static token definitions must reuse the vocabulary-plan identity; it must
not sort definitions, rebuild candidate arrays, or calculate SHA-256 again.
Changed definitions compile a distinct plan, and concurrent parsers share no
mutable lexer state. This is one standard initialization path, not a cached
path plus an uncached fallback.

`SubhutiLexerVocabularyLifecycleSmokeTestMain` owns plan sharing, session
counter isolation, changed-vocabulary invalidation, and concurrent-session
isolation. On the same 100-warmup/500-round `let a = 10` recognizer probe, three
pre-change averages were `0.661ms`, `0.627ms`, and `0.639ms`; after moving lexer
metadata to the immutable vocabulary plan they were `0.329ms`, `0.316ms`, and
`0.348ms`, an approximately 48.5% reduction by the mean of those runs. Best
time moved from approximately `0.35ms` to `0.14-0.15ms`. Generic wrappers,
runtime Graph/recording, and rule cache-key construction remain absent from the
static success path.

## Pipeline Probe Artifact Reuse

Five-stage parser/compiler probes are diagnostic accelerators. They should expose token -> CST -> AST -> emitted ESM -> integration boundaries without paying for the same lower layers twice.

When a probe has already produced active tokens, CST, and AST for a source, the emitted ESM stage should reuse those exact artifacts and call the active emitter directly. It should not call a second full transform that reparses the same source and reruns CST-to-AST just to get generated code.

This is not fallback behavior. The probe still uses the single active parser, CST-to-AST, and emitter path. It only avoids duplicate diagnostic work inside one probe. If the goal is specifically to compare the public full-transform API with the staged artifacts, add that as an explicit parity smoke instead of making every ordinary probe pay that cost.

## Dev Launcher And Watcher Memory

Qin launchers must not hard-code a tiny JVM heap for `qin dev` or other compiler-heavy flows. The launcher should expose `QIN_JAVA_OPTS` as the standard override surface and keep the default heap large enough for OVS/CSSTS/parser package compilation. A command-line `-Xmx128m` default is invalid for modern Qin dev because OVS batch compilation and generated parser toolchains can legitimately need far more memory.

The dev watcher must prune ignored directories while walking the tree, not after collecting every file. Directories such as `.git`, `.qin`, `@qin-mod`, `build`, `dist`, `logs`, `node_modules`, `out`, `target`, and temporary `tmp-*` directories should be skipped before descending. Traversing generated packages, caches, logs, and temp trees competes with OVS/CSSTS compilation for heap and can turn a valid focused compile into a dev-server `OutOfMemoryError`.

When diagnosing dev-server memory failures, first separate the boundaries: run the focused OVS/CSSTS/compiler probe with the same heap, then run the dev server with browser requests paused. If the focused probe passes but the dev server fails, repair the launcher, watcher, concurrency, or cache/session owning layer rather than changing application syntax or deleting caches as the fix.
