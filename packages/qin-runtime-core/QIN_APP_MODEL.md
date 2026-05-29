# Qin App Model

This document defines the intended user-facing application model for Qin.

It answers one core question directly:

- If Qin is a fullstack language, what should users think they are authoring?

The answer is:

- users should think first in terms of applications
- target zoning, host frameworks, and backend/frontend expansion are real architecture, but not the primary product mental model

## 1. Core Position

Qin should be presented as:

- an AI-native fullstack application language
- an application-first authoring model
- one language that can expand into JVM backend and JS frontend targets

Qin should not require common users to begin with:

- controller vs service vs DTO layering
- explicit REST wiring
- frontend vs backend topology planning
- Spring bootstrap concerns
- target zoning as the first design step

Those concerns remain important implementation details, but Qin should progressively hide them behind language, tooling, runtime, and deployment workflows.

## 2. Primary User Concepts

Final-form Qin users should primarily think in:

- `model`
- `query`
- `action`
- `page`
- `form`
- `auth`
- `job`
- `deploy`

These are the preferred product-level concepts because they describe application intent rather than framework structure.

## 3. Relationship To `shared/`, `main/`, and `app`

`shared/`, `main/`, and `app` remain normative architecture.

They are important because they define:

- portability boundaries
- target legality
- backend/frontend capability mapping
- compiler and runtime policy

But they should increasingly behave as implementation zoning, not as the first thing end users must model mentally.

In other words:

- `shared/main/app` is how Qin organizes target expansion
- application concepts are how users should think about what they are building

## 4. Relationship To Spring, `java:`, and Host Frameworks

Spring and `java:` interop remain important.

They should be treated as:

- backend ecosystem integration
- platform capability access
- advanced escape hatches
- migration bridges during Stage 1

They should not remain the preferred long-term authoring surface for ordinary Qin app development.

So the direction is:

- keep `.qin + Spring Boot` working and improving
- keep `java:` import semantics first-class where JVM integration is needed
- add Qin-owned application abstractions above raw framework-shaped authoring

## 5. Stage Model

### Stage 1: Fullstack Language Bootstrap

User-facing product story:

- Qin is already a fullstack language

Implementation reality:

- one language across `shared/`, `main/`, and `app`
- JVM backend and JS frontend backends are visible
- Spring host shells and explicit zoning may still appear

This stage is allowed to be bridge-shaped as long as the product direction is already fullstack.

Current implementation policy:

- Stage 1 completeness comes before Stage 2 abstraction growth
- Qin should first become a solid ordinary fullstack language
- application-first abstractions should not outrun compiler/runtime/fullstack baseline stability

### Stage 2: Qin-Owned Application Layer

User-facing product story:

- Qin becomes application-first instead of framework-first

Implementation direction:

- add first-class application concepts
- reduce direct controller/service/frontend/backend mental overhead
- keep target zoning underneath as compiler/runtime policy

### Stage 3: AI-Native App Platform

User-facing product story:

- Qin is an AI-native app language and app platform

Implementation direction:

- AI primarily edits application-level Qin code
- `qin dev` becomes the default fullstack local loop
- `qin deploy` becomes the default deployment story
- runtime and target expansion are mostly hidden behind Qin tooling

## 6. Current Gap

Current Qin is not yet at the final application-first form because it still exposes too much implementation layering:

- `.qin + Spring Boot` still visibly uses host bootstrap code
- target zoning still appears directly in authoring structure
- framework-shaped code is still more mature than Qin-owned app abstractions
- deploy story is not yet first-class enough

So the main gap is not that Qin lacks a language foundation.
The main gap is that Qin still needs a stronger Qin-owned application layer above the current technical base.

## 7. Design Rules

When designing new Qin features, prefer this decision order:

1. Is this primarily an application concept?
2. If yes, should Qin expose it directly as a first-class app-level abstraction?
3. If not app-level, is it a general language/runtime feature?
4. If not general, is it a target-specific capability that should stay behind zoning and backend mapping?
5. If target-specific, can it remain an escape hatch rather than become the main authoring model?

This keeps Qin aligned with the intended product shape.

## 8. Definition Of Success

This model is successful when:

1. Users think "I am building an app" before they think "I am wiring frontend and backend".
2. AI can generate meaningful Qin application code without reconstructing infrastructure layers by hand.
3. `shared/main/app` remains strong technical architecture without dominating the user mental model.
4. Spring/JVM and JS/web targets remain powerful, but mostly sit underneath Qin-owned application abstractions.
5. `qin dev` and `qin deploy` feel like the natural way to run and ship Qin apps.
