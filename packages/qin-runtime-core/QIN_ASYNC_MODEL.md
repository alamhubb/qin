# Qin Async Model

This document defines the intended async/concurrency model for Qin as a language.

It answers one core question directly:

- Should Qin inherit JavaScript's Promise-first async programming model?

The answer is:

- No.

Qin should be synchronous by default across `shared/`, `main/`, and `app`, with async only when explicitly requested.

## 1. Core Position

Qin is:

- ESM-style in source syntax
- one language across `shared/`, `main/`, and `app`
- target-specific in runtime/backend mapping
- Java/Kotlin-like in its default execution feel

So Qin should not inherit the JavaScript assumption that network or I/O-facing
code naturally becomes Promise-shaped all the way up the stack.

Instead:

- ordinary Qin code stays synchronous by default
- async work is an explicit language feature
- concurrency is opt-in, not ambient
- async does not propagate through call chains by default

## 2. Default Language Rule

The default Qin execution model is:

- function calls are synchronous
- ordinary method calls are synchronous
- backend controller/service code is synchronous unless explicitly made async
- frontend code is also synchronous by default unless explicitly made async
- Java interop calls are synchronous unless explicitly wrapped
- a caller remains synchronous unless its own signature or return surface explicitly exposes `Task<T>` or another Qin async surface
- an implementation may use internal async work and join it before returning without making its caller async

Example:

```qin
function getUserName(id: string): string {
  const user = userService.loadUser(id)
  return user.name
}
```

This is the normative style for ordinary Qin code, especially backend business code.

## 3. Explicit Async Forms

Qin should support both of these forms:

### 3.1 `async expr`

```qin
const task = async userService.loadUser(id)
```

Meaning:

- evaluate the expression asynchronously
- produce a `Task<T>`

This is defined as syntax sugar for:

```qin
const task = async {
  return userService.loadUser(id)
}
```

### 3.2 `async { ... }`

```qin
const task = async {
  const user = userService.loadUser(id)
  const profile = userService.loadProfile(id)
  return mergeUser(user, profile)
}
```

Meaning:

- run the block asynchronously
- allow local variables, branching, and multi-step logic
- produce a `Task<T>`

## 4. Unified Async Result Type

Qin should expose one language-facing async result abstraction:

- `Task<T>`

Not:

- raw `Promise<T>` as the primary backend abstraction
- raw `CompletableFuture<T>` as the language-level abstraction

Reason:

- `Promise` would pull Qin toward JavaScript runtime expectations
- `CompletableFuture` is a Java host type, not a clean language surface

So the rule is:

- Qin surface API: `Task<T>`
- Java host implementation: adapters may internally use Java concurrency primitives

## 5. Waiting For Results

Qin should prefer explicit waiting instead of JavaScript-style `await`.

Example:

```qin
const task = async userService.loadUser(id)
const user = task.join()
return user.name
```

This keeps the model:

- explicit
- JVM-friendly
- easy to reason about for backend developers

`await` is not the primary model in this direction.

If Qin supports `await` for frontend authoring or JavaScript interop, it must be
defined as a target/interop convenience over Qin's `Task<T>` boundary. It must
not redefine the core language into JavaScript-style async contagion.

## 6. Why Qin Does Not Use Promise-First Semantics

Promise-first semantics are a poor default for Qin because they tend to:

- make async behavior contagious through call chains
- push all I/O-shaped code into Promise-returning APIs
- bias the language toward Node/browser runtime expectations
- weaken the "ordinary synchronous business method" experience that JVM developers expect

Qin wants the opposite default:

- write straightforward backend code first
- introduce concurrency only where it is valuable

The normative Qin rule is therefore:

- `async` marks the exact task boundary
- `Task<T>` marks the exact async value boundary
- ordinary callers do not become async automatically
- a sync function that blocks, joins, or adapts internal async work still has a sync surface
- Qin does not require every upstream caller to become `async` merely to consume one async implementation detail

## 7. JVM Host Strategy

Qin async behavior should be implemented through target-specific host mappings.

For the JVM target, the preferred host foundation is Java 25 concurrency facilities.

Preferred foundation:

- Virtual Threads
- Structured Concurrency
- `CompletableFuture` adapters where integration requires it

For JVM this means:

- Qin language model is not JavaScript event-loop semantics
- Qin runtime does not need Node-style microtask queues as the foundational abstraction
- Qin async execution can be efficient and natural on the JVM

## 8. Spring Direction

For Spring integration, the preferred order is:

1. synchronous `.qin` controllers and services
2. explicit `async` tasks inside business logic where useful
3. optional async controller return adaptation later when necessary

This keeps Qin aligned with ordinary Spring MVC development.

Example direction:

```qin
@GET("/api/user")
function getUser(): UserDto {
  return userService.loadUserDto()
}
```

And optionally:

```qin
@GET("/api/report")
function getReport(): ReportDto {
  const reportTask = async reportService.generate()
  return reportTask.join()
}
```

Future framework integration may allow direct adaptation from `Task<T>` to framework-native
async return forms, but that is not the default authoring model.

## 9. Design Rules

When implementing Qin async support, the rules should be:

1. Default sync remains normative.
2. `async { ... }` is the semantic core form.
3. `async expr` is syntax sugar over `async { return expr }`.
4. Both forms lower to the same IR/runtime concept.
5. `Task<T>` is the Qin-facing abstraction.
6. Java host async primitives remain implementation details or interop adapters.
7. Async does not propagate through ordinary call chains by default.
8. `await`, when supported, is not the normative contagion model.

## 10. Non-Goals

Qin async model does not aim to make these foundational:

- Promise as the default backend abstraction
- `async/await`-first programming style
- browser event loop semantics
- Node microtask semantics as the definition of correctness
- full JavaScript async parity

## 11. Definition Of Success

This async model is successful when:

1. Most Qin code reads as normal synchronous code by default.
2. Concurrency can be introduced with explicit `async` at the exact call or block where needed.
3. `async expr` and `async { ... }` share one coherent lowering/runtime path.
4. JVM mapping stays friendly to Spring/JVM developers.
5. The async model feels intentional and language-owned, not like accidental Promise emulation.
