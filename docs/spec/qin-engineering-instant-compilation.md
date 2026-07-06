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
- module-class cache: stores JVM module-class bytecode keyed by stable source
  identity, dependency fingerprint, compiler version, and target options;
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

### Active Dependency Fingerprints

The Qin runtime npm host must fingerprint the active dependency closure for the
current wrapper/module graph, not every directory that happens to remain under
`.qin/runtime/npm-host/node_modules`.

This follows the Vite module-graph lesson: dev-server work should be scoped to
the modules that can affect the requested transform. Stale or inactive runtime
packages must not be part of every request's cache key, because that turns one
old large dependency into global latency.

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

### Fast Path Is Not Fallback

A fast path is correct only when it preserves the owning semantic model. For
example, a generated Java `HashMap` fast path must honor Java `hashCode` and
`equals`; otherwise it is a bug, not an optimization.

Do not add degraded parser/runtime/compiler shortcuts that silently accept
wrong behavior to make startup look faster. Performance work must preserve
strict semantics.

### Incremental Compilation Contract

Incremental compilation should be graph-based:

- changing one app OVS/Qin module should not recompile generated parser and
  runtime packages;
- changing a package manifest or package stamp should invalidate imports from
  that package only;
- changing `qin.config.js` should invalidate transforms that read that config;
- changing generated parser artifacts should invalidate parser consumers;
- changing backend runtime code should invalidate affected JVM class outputs.

When Qin cannot prove the affected set, it may rebuild conservatively, but that
case should be observable in logs and covered by an explicit test before it is
accepted as the permanent design.

### Dev Server Shape

The Qin dev server should be one orchestrator for frontend and backend work:

- keep a persistent runtime/compiler service for the current project;
- pre-materialize heavy compiler dependencies once per dependency fingerprint;
- precompile stable tool packages such as generated parser, OVS, CSSTS, and
  Vue compiler shims into reusable module classes;
- compile changed app/main/shared modules on demand;
- serve cached frontend transform output until a relevant source/config input
  changes;
- restart or reload only the affected backend/frontend slice when possible.

The end-user expectation is "first run may warm caches; subsequent runs should
be fast without manual cleanup."

Dev-server startup should not synchronously compile every OVS module before the
HTTP listener is available. Like Vite, Qin should start the server after the
minimal backend/frontend graph is ready, then transform OVS modules on demand
as the browser requests them. Optional warming is acceptable only when it does
not block the listener and uses the same standard transform/cache path.

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

## Pipeline Probe Artifact Reuse

Five-stage parser/compiler probes are diagnostic accelerators. They should expose token -> CST -> AST -> emitted ESM -> integration boundaries without paying for the same lower layers twice.

When a probe has already produced active tokens, CST, and AST for a source, the emitted ESM stage should reuse those exact artifacts and call the active emitter directly. It should not call a second full transform that reparses the same source and reruns CST-to-AST just to get generated code.

This is not fallback behavior. The probe still uses the single active parser, CST-to-AST, and emitter path. It only avoids duplicate diagnostic work inside one probe. If the goal is specifically to compare the public full-transform API with the staged artifacts, add that as an explicit parity smoke instead of making every ordinary probe pay that cost.
