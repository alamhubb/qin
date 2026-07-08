# Qin Package Manifest Model

This document defines the role of `qin.config.js` in the Qin architecture.

It is the normative project-manifest layer that sits between:

- the Qin language model
- the workspace/package graph
- dependency acquisition
- runtime/build/dev orchestration

## 1. Core Definition

`qin.config.js` is the canonical Qin project manifest.

It is not only a config file.
It is also:

- package manifest
- dependency declaration surface
- workspace root descriptor
- module-root coordination surface
- runtime/build entry configuration surface

In short:

- source files define Qin code
- `qin.config.js` defines the project/package that owns that code

## 2. What The Manifest Owns

Current manifest responsibilities are:

- package identity
  - `name`
  - `version`
  - `description`
- entry selection
  - `entry`
- dependency declaration
  - `dependencies`
  - `devDependencies`
  - `repositories`
- workspace shape
  - `packages`
- target/runtime/build config
  - `java`
  - `graalvm`
  - `frontend`
  - `output`
  - `port`

Future package-resolution responsibilities may include target conditions similar to Node package conditional exports:

- frontend/browser entry selection
- backend/JVM entry selection
- portable/shared entry selection
- package-specific compatibility declarations

This belongs in the manifest/resolver layer. It must not loosen the Qin language rule that `shared/` MVP source is `.qin` only and must stay inside the JS/JVM portable subset.

The Node-style lesson to copy is target-aware entry selection. The lesson not to copy is using conditional package entries as evidence that arbitrary `.js` or `.ts` source is portable Qin `shared/` code. Qin packages may eventually declare `app`, `main`, and `shared` entries, but `shared` entries still have to be Qin-portable source or Qin-approved generated output.

For project-local source, this gives Qin a deliberately stricter rule than many Node/browser dual packages:

- `src/shared/` remains `.qin` only in the MVP.
- `qin.config.js` may later choose different target entries for a package or workspace, similar to Node conditional exports.
- The selected shared/portable entry is a package boundary decision, not permission to put ordinary JS/TS in project `shared/`.
- JS/TS that needs bundler conditions, host shims, or platform replacement belongs in `app`, `main`, or a manifest-selected package entry.

This means `qin.config.js` is where package-management policy and module-system policy begin to meet.

## 3. Entry Model

`entry` is a project entry declaration, not a language-definition statement.

Current accepted entry kinds are:

- Qin source entry:
  - `.qin`
  - `.js`
  - `.mjs`
  - `.ts`
- Java host entry:
  - `.java`

Important boundary:

- `.java` may be used as a backend host/interop entry
- `.java` is not a Qin language source suffix
- `.ts/.js/.mjs/.qin` are Qin-managed source inputs

So `entry` can point at either:

- a Qin-managed source root
- or a Java host bootstrap

depending on the project mode.

## 4. Dependency Model

`qin.config.js` is also the unified dependency declaration surface.

Current shape:

- npm-style packages
  - key: npm package name
  - value: npm version or tag
- Maven-style packages
  - key: Maven/Qin coordinate
  - value: version

Examples:

- `"mitt": "3.0.1"`
- `"@vue/compiler-sfc": "^3.5.0"`
- `"org.jsoup:jsoup": "1.18.1"`
- `"com.qin:qin-runtime-core": "0.1.0"`

Important product-direction example:

- `@vue/compiler-sfc` is the intended long-term `.vue` compilation dependency
- Qin should compile/load/execute it through Qin's own package and module pipeline
- Qin should not depend on a Node subprocess as the normative Vue path

Current operational split:

- `qin install` acquires and records dependencies
- `qin sync` resolves backend/JVM classpath dependencies
- Qin module resolution consumes project/package context from the manifest

Long-term direction:

- Qin should compile and link `.ts/.js/.qin` dependencies directly through its own pipeline
- package installation is not the same thing as runtime compatibility
- module ingestion should progressively move from "download only" to "Qin compile graph input"

Target-facing clarification:

- compatible npm packages should be able to enter Qin target compilation
- backend-compatible packages should be able to participate in JVM `.class` target output
- frontend-compatible packages should be able to participate in JS target output
- package support is compatibility-based, not "all npm packages by default"

## 5. Workspace Model

`packages` defines workspace package discovery.

This gives `qin.config.js` a second role beyond a single package:

- it can describe one package
- or it can describe a workspace root containing many Qin packages

That makes it the root descriptor for:

- monorepo package discovery
- local package linking
- dependency alias resolution
- future workspace-level module graph coordination
- future target-condition entry selection for packages that provide separate frontend, backend, and portable surfaces

## 6. Relationship To The Language Model

`qin.config.js` does not replace the Qin language model.

The language model still defines:

- valid source syntax
- `shared/main/app` zoning
- import policy
- target legality
- async/sync semantics

The manifest defines project-level ownership and orchestration:

- which package this code belongs to
- which entry is the project bootstrap
- which dependencies and repositories are declared
- which workspace packages participate
- which runtime/build options apply

So the split is:

- language rules: `QIN_LANGUAGE_TARGET_MODEL.md`
- project/package rules: `QIN_PACKAGE_MANIFEST_MODEL.md`

## 7. Long-Term Direction

The long-term Qin architecture should converge on:

- one manifest for package identity
- one manifest for dependency declaration
- one manifest for workspace/module-root discovery
- one manifest for `dev/build/run/deploy` coordination

This is important because Qin is not only a compiler.
It is intended to become:

- a language
- a package system
- a runtime/toolchain
- a fullstack application platform

`qin.config.js` is the project boundary where those concerns meet.

