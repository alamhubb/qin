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
is useful as a development mechanism, but the Qin-owned parser path should use a
static enhanced parser `.class` when the source grammar is known. A valid
static enhanced parser preserves the same `@SubhutiRule` wrapper semantics,
packrat keys, CST shape, AST output, and error behavior as the dynamic enhanced
class; it is not a raw-parser fallback. Parser construction must also avoid
framework-level repeated work such as recompiling token regexes that are already
matched with `Matcher.region(...).lookingAt()`. Parser hot classes must not
eagerly initialize heavyweight logging frameworks just to emit rare diagnostics;
diagnostics that only apply to non-standard raw parser construction should stay
on a lightweight path so the standard static parser can start quickly.

Decorator handling follows the canonical Qin JS compatibility rule in
`packages/qin-runtime-core/QIN_JS_COMPATIBILITY_MODEL.md`: Qin-owned decorators
are lowered at compile time into static `.class` wrappers, metadata,
initializer calls, JVM annotations, or rule tables. Runtime ByteBuddy,
reflection, or JavaScript descriptor emulation must not become the standard
fallback for a missing decorator lowerer.

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

Focused performance probes must measure both structural work avoided and real
wall-clock time. FIRST-token prediction that only skips cheap token mismatches
may be slower than ordinary PEG retry because it pays an extra lookahead cost;
common-prefix LL(2+) prediction can still be much faster when it avoids deeper
failed branches. Treat skipped-alternative counters as necessary evidence, but
not sufficient evidence, for parser speed work.

Runtime pruning must therefore be cost-gated. A recorded `Or(...)` whose
alternatives are fully analyzable but only differ by a single cheap FIRST(1)
token should keep the grammar metadata for diagnostics, but should not enter
the runtime pruning hot path. A recorded `Or(...)` with duplicated prefixes may
expand conservatively to LL(k) up to the framework limit and enable pruning only
when the recorded token sequences are complete and non-dynamic. The current
Subhuti Java prototype uses the unchanged `Alternative.of(...)` surface, records
lambda callsite identities as the prediction cache key instead of stack traces,
keeps a hot in-process prediction plan for repeated `Or` calls, disables
pruning for cheap FIRST(1)-only choices, and enables pruning for LL(2+)/LL(3+)
common-prefix choices. Analysis-only FIRST(1) decisions may record grammar
metadata and diagnostics, but repeated hot calls should skip repeated
recording/planning work and stay on the normal PEG execution path. Focused
measurements on 20,000-item probes showed FIRST(1)-distinct staying outside
runtime pruning with only about a 3% analysis overhead, while LL(2) and LL(3)
common-prefix choices improved by about 3x and reduced wall-clock time by about
66%.

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
20,000-item focused benchmark measured `FIRST1_DISTINCT` at `302.759ms` with
prediction enabled vs `294.010ms` without prediction, so it remained outside
runtime pruning with `0.97x` speed and a `3.0%` analysis cost. The same run
measured `LL2_COMMON_PREFIX` at `1251.176ms` with prediction vs `3752.675ms`
without prediction, or about `3.00x` faster and `66.7%` less wall-clock time.
`LL3_COMMON_PREFIX` measured `2925.806ms` vs `8540.298ms`, or about `2.92x`
faster and `65.7%` less wall-clock time.

The next framework step is parser-class/callsite prediction-plan reuse, closer
to Chevrotain self-analysis than per-instance probing. Subhuti keeps the
`Alternative.of(...)` grammar surface, records an analyzable plan once, stores
it in a parser-class scoped global prediction cache, and lets later parser
instances reuse that plan. Focused 5,000-item measurements on the Java runtime
showed the intended shape: `FIRST1_DISTINCT` remains analysis-only and near
parity (`25.822ms` vs `25.726ms`, `1.00x`) because pure token mismatches are
too cheap to prune; `LL2_COMMON_PREFIX` uses runtime pruning with a global plan
hit and measured `72.996ms` vs `202.965ms`, or `2.78x` faster; and
`LL3_COMMON_PREFIX` measured `168.838ms` vs `502.695ms`, or `2.98x` faster.
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

A tiny follow-up applies the same `MANY` start-prediction rule to `TSTypeName`
dotted suffixes: `Identifier ('.' Identifier)*` now checks `Dot` before entering
the repeated body. On 2026-07-09 this moved `OvsParser.ts` wrapper calls from
`81647` to `81550` and `Identifier` wrapper calls from `2051` to `1954`, while
wall-clock remained within probe noise (`cstWarmAvgMs=365.241`,
`cstBestMs=285.029`). Keep it only as a minor structural cleanup; the remaining
gap is still dominated by expression-chain wrappers, `TSType`, and CST/packrat
costs.

Framework optimization must be proven with focused parser probes before it is
trusted by OVS, CSSTS, Qin, or Java parser users. A correct performance fix must
preserve token -> CST -> AST -> emitted ESM -> integration behavior while
reducing unnecessary rule execution.

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
