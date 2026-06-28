# Qin CSSTS Integration Model

This document defines the formal integration model for `lang=cssts` in Qin.

It answers one core question directly:

- When Qin sees `lang=cssts`, which implementation is the formal source of truth?

The answer is:

- the formal CSSTS compilation entry is the `cssts-compiler` package
- the `cssts-compiler` parser must inherit the Qin-generated TypeScript parser package, `@qin/generated-qin-parser-ts`
- the runtime helper package is `cssts-ts`
- Qin-owned alternate parser/compiler fallback implementations are not the long-term mainline

Related documents:

- `QIN_JS_COMPATIBILITY_MODEL.md`
- `QIN_NPM_COMPATIBILITY_POLICY.md`
- `QIN_DEV_SERVER_STAGE1.md`

## 1. Core Rule

`lang=cssts` must be treated as:

- npm package functionality
- compiled and orchestrated by Qin
- not redefined by a separate local fallback implementation

So the formal rule is:

- `lang=cssts` -> `cssts-compiler` + `@qin/generated-qin-parser-ts` parser base + `cssts-ts`

not:

- `lang=cssts` -> local Java/Qin fallback

## 2. Non-Goals

Qin should not keep these as formal long-term mainline behavior:

- dual-track cssts implementations
- automatic fallback from npm `cssts` to local `qin-cssts-parser`
- hidden substitution of local cssts logic when npm compatibility fails
- "best effort" compilation that silently changes the implementation source

## 3. Role Of Qin

Qin's responsibility is:

- resolve the CSSTS compiler/runtime packages declared in `qin.config.js`
- keep their parser base wired to the generated Qin/Slime TypeScript parser
- compile/package it through the Qin pipeline
- integrate it into Vue/frontend orchestration
- report unsupported features clearly
- stop immediately when the package requires unsupported Qin/JS/host behavior

Qin's responsibility is not:

- maintaining a second formal cssts language implementation forever

## 4. Failure Policy

If npm `cssts` requires:

- unsupported JS syntax
- unsupported runtime semantics
- unsupported host capability
- unsupported Qin frontend pipeline integration behavior

then Qin must:

- report a clear error
- stop immediately
- not fallback to local cssts logic
- let the user decide what to do next

## 5. Status Of Local CSSTS Code

Existing local cssts-related code may still exist in the repository as:

- experimental infrastructure
- migration scaffolding
- historical implementation
- test assets

But it should not be treated as:

- the formal Stage-1 frontend mainline
- the official implementation source for `lang=cssts`

## 6. Vue Integration Rule

For Vue SFC processing, the intended formal chain is:

1. Qin dev/build orchestration
2. Qin frontend module graph / request pipeline
3. `@vue/compiler-sfc`
4. `cssts-compiler` for `lang=cssts`, with its parser inheriting `@qin/generated-qin-parser-ts`
5. `cssts-ts` for generated runtime helpers

This keeps Vue/cssts behavior aligned with the shared Qin parser authority instead of growing a separate fallback branch.

Vite and `@vitejs/plugin-vue` are not part of the Stage-1 formal chain. Qin should compile and invoke the needed npm
packages directly through its own package/module compiler path.

## 7. Definition Of Success

This model is successful when:

1. `lang=cssts` means exactly one thing in Qin.
2. The implementation source is explicit and documented.
3. Unsupported package behavior fails fast instead of silently switching tracks.
4. Qin's frontend architecture stays ecosystem-aligned without losing control of its host/runtime model.

## 8. Current Implementation Boundary

The intended chain remains official Vue SFC parsing plus `cssts-compiler`, whose syntax base inherits the generated Qin/Slime TypeScript parser.

The current blocker for rendering `.vue lang=cssts` is not a separate local CSSTS implementation gap. The blocker is
that compiling official Vue compiler packages through the current linked-source/single-class JVM path is too large:

- `@vue/compiler-sfc` can be resolved and parsed
- the linked source is currently emitted as one large generated JVM class
- large runtime function models make classfile emission too slow/heavy

So the next architecture milestone is package compilation caching and module-level/class-level splitting, not a
return to a Qin-owned Vue or CSSTS fallback parser.
