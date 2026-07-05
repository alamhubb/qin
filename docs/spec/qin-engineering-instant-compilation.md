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
- Kotlin/Gradle-style incremental compilation: classpath and source changes
  should invalidate only affected work, not the entire graph.
- Vite-style dev ergonomics: cold start should precompute heavy dependencies,
  and hot updates should touch only changed application modules.
- esbuild-style persistent rebuild context: long-lived dev processes should
  keep parser/compiler/module graph state warm instead of rebuilding from
  scratch on every request.

These are design influences, not permission to copy another tool's semantics.
Qin must keep its own language and target-zone boundaries.

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
- `frontend transform disk cache hit`;
- `package materialization fresh`;
- `dependency fingerprint changed`;
- `hot module cache invalidated`.

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
