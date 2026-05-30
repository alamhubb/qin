# Qin Dev Server Stage 1 Design

This document defines the Stage-1 design for Qin's single-process dev/fullstack server.

## Goal

Stage 1 goal:

- `qin dev` starts one Qin-owned dev server
- the server runs backend and frontend in one process
- the server exposes one port
- `main/` hosts backend `.qin`
- `app/` hosts frontend HTML/Qin/JS/CSS
- browser access and API access are served by the same Qin runtime host

This is the current implementation target for HTML/JS fullstack, with Vue support flowing through the same server path via the official Vue compiler stack.

## Positioning

Qin owns its frontend dev/build lifecycle. Vite can be studied as prior art, but it is not a dependency, bridge, fallback, or compatibility runtime.

Stage 1 means:

- Qin does not run or adapt Vite as part of the Stage-1 platform
- Qin does not depend on Node runtime semantics for its core server
- Qin does not need HMR graph sophistication first
- Qin does need the part of the model that is strategically valuable:
  - single-process dev server
  - frontend module serving
  - backend API serving
  - browser auto reload
  - build/dev path alignment
- Vue `.vue` SFC parsing and module serving, owned by Qin orchestration but backed by the official Vue compiler

## Runtime Roles

### `qin-cli`

Responsibilities:

- user-facing `run` / `dev` / `build`
- project layout detection
- dependency/classpath orchestration
- runtime entry dispatch

### `QinDevServerMain`

Responsibilities:

- canonical Stage-1 dev/fullstack server entry
- stable named entry for `qin dev`
- future home for clearer server-specific bootstrapping

Current status:

- delegates to `QinFullstackMain`
- exists to stabilize the runtime surface and naming

### `QinFullstackMain`

Responsibilities in current transition state:

- build orchestration
- backend/frontend source discovery
- dev rebuild loop trigger
- compatibility fullstack entry

Future direction:

- continue shrinking toward build/bootstrap orchestration
- stop owning raw HTTP serving details

### `QinDevServer`

Responsibilities:

- single-process HTTP server
- API endpoints
- static asset serving
- frontend bootstrap/module endpoints
- dev auto-reload endpoints

This is the Qin-owned server host for Stage 1.

## Current Request Model

Current server routes:

- `/`
  - serves HTML/static assets from `app/` or configured static root
- `/api/health`
  - server health endpoint
- `/api/result`
  - invokes current generated backend run method
- `/app.js`
  - frontend bootstrap module in dev mode
- `/@qin-mod/...`
  - transformed frontend ESM modules in dev mode
- `/@qin/dev-client.js`
  - browser auto-reload client in dev mode
- `/@qin/version`
  - dev polling version endpoint

## Frontend Model In Stage 1

Stage 1 frontend model is intentionally simple:

- official frontend directory is `app/`
- frontend starts from `index.html`
- frontend module entry is currently `app/main.qin`, `app/main.js`, or equivalent ESM-style file
- static HTML is first-class
- CSS is first-class
- browser JS modules are first-class
- Qin frontend modules are now accepted for Stage-1 fullstack examples
- Vue frontends should flow through the official Vue SFC compiler (`@vue/compiler-sfc`) under Qin orchestration
- Vue and frontend module processing must run through Qin's native frontend pipeline

Not yet in Stage 1 baseline:

- full HMR graph
- advanced third-party plugin-container parity
- full Vue template codegen/runtime parity

## Why HTML First

Before deeper Vue support, Qin should stabilize the simpler fullstack path:

- `app/index.html`
- `app/main.qin`
- `main/main.qin`
- `shared/shared.qin`
- one port
- one process

This gives Qin:

- a usable fullstack baseline
- easier smoke tests
- clearer server boundaries
- lower architectural risk before adding SFC transforms

## Build And Dev Alignment

Stage 1 should keep dev and build conceptually aligned:

- backend source discovery should be shared
- frontend source discovery should be shared
- import/module policy should be shared
- frontend module transforms should be shared as much as practical

Difference:

- build emits production assets
- dev serves frontend modules on demand and injects browser reload client

## Near-Term Refactor Direction

Next refactor sequence:

1. keep `QinDevServerMain` as canonical entry
2. keep moving HTTP/server concerns from `QinFullstackMain` into `QinDevServer`
3. introduce a clearer frontend request pipeline:
   - resolve
   - load
   - transform
4. keep build/rebuild orchestration in `QinFullstackMain` for now
5. integrate official Vue SFC compilation into the Qin frontend request/build pipeline
6. keep lifecycle concepts Qin-owned, with no Vite bridge or fallback path

Important transition note:

- local Qin-owned Vue code should stay limited to orchestration, descriptor assembly, import rewriting, and virtual module serving
- Qin must not maintain a second primary Vue parser/compiler as a fallback path
- the intended steady-state path is `@vue/compiler-sfc` executed under Qin's own package/module/compiler system
- `lang=cssts` is handled through npm `cssts-compiler` and `cssts-ts`, not a local Qin-only CSSTS branch

## Non-Goals For This Stage

- Vite dependency, bridge, fallback, or compatibility runtime
- Node-compatible dev server semantics
- complete Vue ecosystem support
- advanced HMR invalidation graph
- multi-process frontend/backend model as the long-term Qin default
- making a Qin-native Vue parser the primary Stage-1 path

## Success Criteria

Stage 1 is considered successful when:

- `qin dev` reliably starts one Qin dev server
- one port serves both backend and frontend
- `main/main.qin` backend works
- `app/index.html + app/main.qin` frontend works
- `app/index.html + app/main.ts/.js` frontend works
- `.vue` modules can be compiled under Qin orchestration via the official Vue compiler path
- `shared/shared.qin` can be shared by frontend and backend
- browser reload works after source changes
- `qin build` produces usable backend/frontend outputs
- the implementation surface is clearly Qin-owned, not hidden inside accidental compatibility classes
