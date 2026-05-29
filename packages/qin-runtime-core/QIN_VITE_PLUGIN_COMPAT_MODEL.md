# Qin Vite Plugin Compatibility Model

This document defines how Qin should approach compatibility with Vite plugins, especially `@vitejs/plugin-vue`.

It answers one core question directly:

- Should Qin try to support `@vitejs/plugin-vue`, and if so, how without redefining Qin as Vite/Node?

The answer is:

- yes, Qin should support a minimal Vite-compatible adapter layer where it brings strong practical value
- no, Qin should not adopt Vite or Node as its platform definition

Related documents:

- `QIN_DEV_SERVER_STAGE1.md`
- `QIN_JS_COMPATIBILITY_MODEL.md`
- `QIN_NPM_COMPATIBILITY_POLICY.md`
- `QIN_HOST_CAPABILITY_MODEL.md`
- `QIN_JS_ON_JVM_FEASIBILITY.md`

## 1. Core Position

Qin should learn from Vite's frontend orchestration model, but not inherit Vite's platform identity.

That means:

- Qin should keep its own single-process dev/build host
- Qin should keep its own module graph and package policy
- Qin may adapt selected Vite plugin contracts where that is cheaper and more future-proof than re-creating ecosystem logic

So the correct direction is:

- Qin-compatible Vite plugin adaptation

not:

- turning Qin into another Vite runtime

## 2. Why `@vitejs/plugin-vue` Matters

`@vitejs/plugin-vue` is the most strategically useful first target because it represents the official Vue SFC integration path.

Practical value:

- closer alignment with official Vue behavior
- less Qin-owned Vue compiler logic to maintain
- easier future compatibility with Vue ecosystem expectations
- a more credible path for `.vue` support under `qin dev`

So Qin should prefer:

- official `@vue/compiler-sfc`
- plus a minimal adapter path for `@vitejs/plugin-vue`

over:

- a large Qin-owned Vue parser/compiler stack as the main long-term path

## 3. What Qin Should Borrow From Vite

Qin should borrow the orchestration principles, not the whole host model.

### 3.1 Good Borrowed Ideas

- `index.html` as a source entry
- on-demand dev transforms
- descriptor-driven SFC compilation
- query-based submodule splitting for `.vue`
- module-graph-centered frontend serving
- transform pipeline as a first-class abstraction

### 3.2 What Not To Borrow As Platform Law

- Node as the normative host
- CommonJS compatibility as a baseline
- full Vite plugin ecosystem compatibility
- Vite HMR API as a Qin language standard
- Vite dev-server assumptions as Qin architecture

## 4. Qin Adapter Layer

Qin should introduce a minimal Vite-compatible adapter layer between:

- `qin-dev-server` / Qin frontend orchestration
- and selected Vite plugins

This adapter layer is not the core platform.
It is an integration boundary.

Recommended name direction:

- `qin-vite-compat`
- `qin-vite-plugin-adapter`
- or equivalent internal naming

## 5. Minimum Plugin Contract

Qin should initially support only the subset of Vite plugin behavior needed by `@vitejs/plugin-vue`.

Likely first-wave contract surface:

- plugin registration
- `resolveId`
- `load`
- `transform`
- module id / query normalization for `.vue` subparts
- dev/build mode distinction where necessary

Possible future additions:

- selected hot-update hooks
- selected virtual-module conventions

Not a Stage-1 requirement:

- full plugin container parity
- full Rollup/Vite hook matrix
- broad third-party plugin compatibility claims

## 6. Error Policy

Qin must keep the same fail-fast rule here.

If `@vitejs/plugin-vue` or any adapted plugin requires:

- unsupported JS syntax/runtime behavior
- unsupported Node host capability
- unsupported Vite container behavior
- unsupported async/runtime model

then Qin should:

- report a clear error
- stop immediately
- not silently fallback
- not auto-downgrade behavior

User decides whether to:

- add support
- patch the package
- choose a different version
- or stop using that package

## 7. Recommended Architecture

The preferred frontend chain is:

1. `qin-dev-server`
2. Qin module graph / resolver / package policy
3. Qin Vite-compatible adapter layer
4. `@vitejs/plugin-vue`
5. `@vue/compiler-sfc`
6. Qin module assembly / browser output serving

This means:

- Vue parsing/descriptor work comes from official Vue packages
- Qin remains the host/orchestrator
- Vite compatibility is a targeted integration layer, not the core product identity

## 8. Stage-1 Scope

Stage-1 should mean:

- prioritize `@vitejs/plugin-vue`
- do not promise generic Vite plugin compatibility
- do not promise Vite parity for all hooks
- do not block Qin frontend progress on full HMR parity

In simpler terms:

- support the plugin we need
- only the contract surface we need
- only the host behavior we can justify

## 9. Definition Of Success

This model is successful when:

1. Qin can run a real Vue frontend path without making Node/Vite the platform definition.
2. `@vitejs/plugin-vue` becomes usable through a narrow, deliberate adapter layer.
3. Unsupported host or runtime requirements fail clearly.
4. Qin keeps ownership of dev server, module graph, and fullstack runtime architecture.
