# Qin Product Positioning

This document defines Qin as a product, not only as a parser/backend stack.

For the user-facing application abstraction layer that should sit above target zoning and host integration, also see:

- `QIN_APP_MODEL.md`
- `QIN_MISSION_AND_VALUE.md`

It answers four questions:

- What is Qin for?
- What should users think they are building?
- What is the gap between current Qin and final Qin?
- Which current architectural choices still reflect legacy layering instead of the final product shape?

This document should be read together with `QIN_MISSION_AND_VALUE.md`, which explains why Qin is strategically worth building and what would make it not worth building.

## 1. Product Definition

Qin should be positioned as:

- an AI-native fullstack application language
- a unified application model, not a frontend/backend split-first tool
- a language/runtime/toolchain that lets users describe products, data, pages, actions, and deployment in one system
- a multi-suffix compiler platform that accepts `.ts`, `.js`, and `.qin` as Qin source inputs
- a package-manifest-driven platform where `qin.config.js` defines package identity, dependencies, workspace shape, and runtime/build entry

The intended user experience is:

- users describe an application in Qin
- AI generates or edits Qin code
- `qin dev` runs the app locally
- `qin deploy` deploys the app
- users can keep existing `.ts`, `.js`, and `.qin` source files in one Qin workspace
- users manage package/workspace/runtime entry through one `qin.config.js` manifest
- Java code may still coexist as host/interoperability code, but not as a Qin source suffix
- users do not need to manually design controller/service/frontend/backend boundaries first

The most accurate short definition is:

- Qin is an AI-native fullstack application language with ESM-style syntax and target-specific backends.

Important value clarification:

- Qin is not strategically valuable just because its syntax is close to JavaScript
- Qin is only valuable if that syntax helps unlock a better JVM-centered fullstack and AI-native application workflow

## 2. What Users Should Think About

Final-form Qin users should primarily think in terms of:

- models
- actions
- queries
- pages
- forms
- auth
- jobs
- deploy

They should not be forced to think first in terms of:

- frontend vs backend
- controller vs service vs DTO
- REST route plumbing
- serialization wiring
- deployment topology

Those are platform expansion concerns and should be handled by Qin tooling, runtime, and target backends wherever possible.

## 3. Current Qin vs Final Qin

### 3.1 What Qin already is

Today Qin already has important foundations:

- one language model across `shared/`, `main/`, and `app`
- ESM-style source syntax
- JVM as primary backend/runtime kernel
- JS emission as a real target
- explicit async, sync by default
- working `.qin + Spring Boot` demo path
- parser/lowering/backend separation that is increasingly Qin-owned
- mixed-source workspace direction across `.ts`, `.js`, and `.qin`

### 3.2 What Qin is still missing

Qin is not yet in its final product form because users still need to think too much about host layering.

Current user-facing shape is still close to:

- Qin source files
- plus Java/Spring host shell
- plus explicit backend/controller framing
- plus target-aware manual structure

Final Qin should instead feel like:

- one application model
- one project command surface
- one deployment story
- target expansion mostly hidden behind the compiler/runtime/toolchain

This is also why Qin cannot stop at being merely a JS-like JVM language.
If that were the end state, its product value would be too small relative to the cost of creating a new language.

## 3.4 Three-Stage Product Path

Qin should move in stages, but from the first stage onward it should already be positioned as a fullstack language.

### Stage 1: Fullstack Language Bootstrap

User-facing definition:

- Qin is already a fullstack language

Practical shape:

- one language across `shared/`, `main/`, and `app`
- backend emitted to JVM
- frontend emitted to JS
- `qin.config.js` acts as the canonical manifest for package identity, dependency declaration, workspace discovery, and entry selection
- frontend framework integration is orchestrated by Qin, while mature ecosystem compilers such as the official Vue SFC compiler may be reused
- framework-host bridges still visible where needed

This stage accepts:

- Java/Spring host shells
- explicit `.qin + Spring Boot` demos
- target-aware project structure

because the goal is to first make the fullstack loop real and reliable.

Current execution priority:

- finish Stage 1 first
- make Qin work well as a normal fullstack language before prioritizing higher-level Qin-owned app abstractions

### Stage 2: Qin-Owned Application Layer

User-facing definition:

- Qin is a fullstack application language

Practical shape:

- begin reducing direct controller/service/frontend/backend mental overhead
- add Qin-owned application concepts
- keep platform targets underneath, but stop exposing them as the main authoring model

This stage should shift users from:

- framework-first authoring

to:

- application-first authoring

### Stage 3: AI-Native App Platform

User-facing definition:

- Qin is an AI-native application language and deployment platform

Practical shape:

- AI writes application-level Qin code
- `qin dev` and `qin deploy` become the default operational model
- platform/runtime expansion happens mostly behind the scenes

This is the final desired product form.

### 3.3 Final-form Qin

The final desired user experience is:

1. user describes app intent in Qin
2. AI writes or updates Qin code
3. Qin expands app intent into:
   - backend handlers
   - page delivery
   - data contracts
   - runtime wiring
   - deployment artifacts
4. user runs:
   - `qin dev`
   - `qin deploy`

In that final form, `java:` interop, Spring, JS backend details, and target zoning remain real architecture, but become mostly platform internals rather than the primary user mental model.

## 4. Architectural Tensions Today

The current architecture is strong at the backend pipeline layer, but several tensions remain.

### 4.1 Product model vs implementation model

Current documentation often still describes Qin as:

- a JVM language
- a JS-syntax language
- a fullstack language with `shared/main/app`

That is true, but still implementation-first.

The final product model should be:

- application-first
- AI-first
- deployment-first

So the current architecture is ahead of the product wording in some places and behind it in others.

### 4.2 User model vs zone model

`shared/main/app` is a good target architecture boundary.
It is not a good primary end-user story.

Conflict:

- architecture needs zoning
- product should not force users to think in zoning first

Adjustment needed:

- keep zoning as compiler/runtime policy
- avoid making zoning the primary user-level concept for application authoring

### 4.3 Language model vs framework-host model

Current `.qin + Spring Boot` works, but still exposes:

- host `Main.java`
- Spring annotation interop directly in user-facing code
- controller/service framing as the main app authoring style

That is an excellent bridge stage, but it is not the final Qin shape.

Adjustment needed:

- keep Spring-host integration
- progressively add Qin-owned app abstractions above it
- reduce the need for end users to author Spring-shaped code directly

### 4.4 Fullstack goal vs current demo shape

Today the fullstack story is technically possible, but the demo story is still backend-demo-first.

Adjustment needed:

- define a first-class Qin app model
- make fullstack generation and serving a default workflow
- converge local dev and deploy commands around application units, not host fragments

## 5. Recommended Design Direction

### 5.1 Keep the current technical base

Keep:

- `qin-parser`
- `qin-lang-frontend-adapter`
- backend-neutral IR
- `qin-lang-backend-jvm`
- `qin-lang-backend-js`
- `shared/main/app` zoning rules

These are good foundations and align with the final architecture.

### 5.2 Add a Qin-owned application layer

Add a higher-level Qin application model above raw host-framework authoring.

Likely first-class concepts:

- `model`
- `query`
- `action`
- `page`
- `form`
- `auth`
- `job`
- `deploy`

These should become the preferred user-facing language/product layer.

The intended shape of that layer is specified in `QIN_APP_MODEL.md`.

### 5.3 Reframe interop as escape hatch

`java:` and direct Spring interop should remain important, but be positioned as:

- platform escape hatch
- advanced integration surface
- framework integration layer

not as the primary long-term authoring model for common app development.

## 6. Near-Term Product Gaps

Before Qin reaches the final product shape, the biggest gaps are:

1. missing Qin-owned app abstraction layer above framework-shaped code
2. missing first-class deploy story
3. missing 鈥渙ne command鈥?app lifecycle polish
4. missing clearer separation between:
   - user model
   - target model
   - host integration model
5. missing a stronger proof that Qin reduces real fullstack complexity rather than only changing syntax

This means the immediate strategy is:

- keep shipping Stage 1 as a real fullstack language
- while intentionally designing toward Stage 2 and Stage 3

## 7. Definition Of Product Success

Qin succeeds in its intended final form when:

1. users think 鈥淚 am building an app,鈥?not 鈥淚 am wiring frontend and backend.鈥?2. AI can generate useful Qin application code without hand-assembling infrastructure layers.
3. `qin dev` and `qin deploy` are the default operational story.
4. target-specific backends remain powerful but mostly invisible for common workflows.
5. Spring/JVM and JS/web remain implementation targets, not the primary mental model.

