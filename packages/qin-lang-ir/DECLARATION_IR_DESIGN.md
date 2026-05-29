# Qin Declaration IR Design

This document defines the first formal declaration-oriented IR model for Qin.

Its purpose is to move Qin from a script-first compiler into a language that can
compile JVM classes with Java/Kotlin-like framework interoperability.

This IR is for Qin as an independent JVM language.

It is not intended to preserve Node platform semantics or to model Node-specific
runtime contracts.

## 1. Why This Exists

Today Qin IR is dominated by script/runtime concerns:

- expressions
- top-level execution steps
- Java constructor/method interop calls
- JS-style runtime built-ins

That is enough for runtime demos, but it is not enough for a language that wants
to behave like Kotlin on the JVM.

To reach that target, Qin must represent declarations explicitly:

- classes
- methods
- parameters
- annotations
- type references

Without these nodes, framework integrations must continue reading raw parser AST
or using framework-specific bridge compilers.

That is acceptable temporarily, but not as a final architecture.

## 2. Design Goals

The declaration IR should:

- be parser-neutral
- be framework-neutral
- be backend-oriented enough to support JVM lowering
- be high-level enough to remain reusable for analysis, validation, and tooling

It should not:

- encode Spring-specific semantics directly
- depend on Slime AST node types
- depend on Class-File API classes
- depend on Node runtime concepts or npm/package-manager behavior

## 3. First-Phase IR Scope

The first declaration IR milestone should model:

- one top-level class
- zero or more methods
- zero or more fields
- zero or more method parameters
- zero or more annotations on class/method/parameter
- simple type references

This first phase does not yet need to solve:

- generics
- interfaces
- inheritance modeling beyond simple optional superclass
- field lowering
- overload resolution complexity
- annotation target validation
- full constructor/accessor synthesis policy

## 4. Proposed Core Nodes

### 4.1 Program-Level Extension

`QinIrProgram` should eventually be extended to hold declaration nodes in addition
to script/runtime nodes.

Likely direction:

- `List<QinIrClassDeclaration> classDeclarations`

This allows Qin to support both:

- script-like modules
- declaration-oriented backend modules

Here, "script-like" still means Qin modules, not Node programs.

### 4.2 Class Declaration

Proposed node:

- `QinIrClassDeclaration`

Suggested core fields:

- `String packageName`
- `String simpleName`
- `QinIrTypeRef superType`
- `List<QinIrAnnotation> annotations`
- `List<QinIrMethodDeclaration> methods`

Later extensions may include:

- interfaces
- constructors
- modifiers

### 4.2.1 Field Declaration

Proposed node:

- `QinIrFieldDeclaration`

Suggested core fields:

- `String name`
- `QinIrTypeRef type`
- `List<QinIrAnnotation> annotations`
- `QinIrExpression initializer`

Current field lowering scope is:

- field name
- field type
- field annotations
- literal field initializers

Current field initializer subset is intentionally narrow:

- string literal
- boolean literal
- number literal
- `null`

It still does not yet try to model:

- synthetic accessor policy
- non-literal initializer evaluation semantics
- explicit constructor declarations / overload policy

Current backend follow-up:

- field IR now feeds JVM field descriptor emission
- field IR now feeds no-arg constructor default field initialization
- first-phase backend also synthesizes basic field getters/setters as runtime shape support
- first-phase backend now also synthesizes an all-fields constructor with parameter name metadata

### 4.3 Method Declaration

Proposed node:

- `QinIrMethodDeclaration`

Suggested core fields:

- `String name`
- `QinIrTypeRef returnType`
- `List<QinIrParameter> parameters`
- `List<QinIrAnnotation> annotations`
- method body representation

For the first phase, method body may stay intentionally small:

- string literal return
- simple expression return
- receiver-based property access return
- receiver-based instance method call return

### 4.3.1 Declaration Expression Support

The declaration subset now also needs a small receiver-based expression model so
that class bodies can express normal JVM business code patterns such as:

```ts
return payload.name
return this.service.message()
```

Suggested supporting nodes:

- `QinIrThisExpression`
- `QinIrPropertyAccessExpression`
- `QinIrInstanceMethodCallExpression`

These nodes stay framework-neutral.

They do not encode Spring semantics directly.

They only encode the minimum instance-style expression structure that the JVM
backend needs in order to emit normal object-oriented `.class` behavior.

### 4.4 Parameter

Proposed node:

- `QinIrParameter`

Suggested fields:

- `String name`
- `QinIrTypeRef type`
- `List<QinIrAnnotation> annotations`

This node is required for Spring MVC binding, reflection parity, and method
descriptor lowering.

### 4.5 Annotation

Proposed node:

- `QinIrAnnotation`

Suggested fields:

- `String ownerBinaryName`
- `List<QinIrAnnotationArgument> arguments`

Important rule:

- frontend should resolve annotation references through shared `java:` import semantics
- IR should store the resolved Java binary name, not parser-local alias text

Also:

- import lowering should follow Qin's language rules
- it should not inherit Node-specific resolution or module-condition semantics

That keeps JVM lowering simple and keeps alias handling in one place.

### 4.6 Annotation Argument

Proposed node:

- `QinIrAnnotationArgument`

Suggested fields:

- `String name`
- `QinIrExpression value`

First-phase support can stay intentionally narrow:

- string literal
- number literal
- boolean literal
- array of literals

### 4.7 Type Reference

Proposed node:

- `QinIrTypeRef`

Suggested first-phase shape:

- `String binaryName`
- `QinIrTypeKind kind`

Where first-phase kinds may include:

- `VOID`
- `BOOLEAN`
- `INT`
- `DOUBLE`
- `STRING`
- `CLASS`

This should be enough to support:

- `String`
- primitive/basic JVM-friendly types
- Spring MVC controller signatures
- `java:`-imported JVM reference parameter types such as `Payload`

## 5. Frontend Lowering Rules

The frontend adapter should be responsible for:

- collecting `java:` imports
- resolving local decorator aliases
- resolving `java:`-imported type aliases used in parameter type annotations
- lowering decorators into `QinIrAnnotation`
- lowering class declarations into `QinIrClassDeclaration`
- lowering field declarations into `QinIrFieldDeclaration`
- lowering method signatures into `QinIrMethodDeclaration`
- lowering parameters into `QinIrParameter`
- lowering declaration method return expressions into receiver-based IR when needed

The frontend should not:

- emit Spring-specific nodes
- decide framework conventions

Example:

```ts
import { RestController as RC, GetMapping as GET } from "java:org.springframework.web.bind.annotation"

@RC
class HelloController {
  @GET("/api/hello")
  hello() {
    return "hello"
  }
}
```

Should lower conceptually into:

- class annotation: `org.springframework.web.bind.annotation.RestController`
- method annotation: `org.springframework.web.bind.annotation.GetMapping`
- annotation argument: `value = ["/api/hello"]`

## 6. JVM Lowering Responsibilities

Once declaration IR exists, JVM lowering should own:

- classfile version policy
- class identity
- method descriptor generation
- field descriptor generation
- runtime-visible annotation emission
- parameter annotation emission
- parameter type descriptor emission for both primitive-like and reference JVM types
- receiver-based property/method invocation emission for the supported declaration subset

This lowering must be generic.

It should not care whether an annotation belongs to:

- Spring MVC
- Jakarta Validation
- Jackson
- a user-defined Java annotation

## 7. Migration Strategy

The migration should be incremental.

### Step 1

Add declaration IR node types without removing current script IR.

### Step 2

Teach frontend adapter to lower a minimal class/controller subset into the new IR.

### Step 3

Teach JVM backend to emit classes from declaration IR.

### Step 4

Retain `QinSpringControllerCompiler` only as:

- migration fallback
- parity oracle
- regression comparison tool

### Step 5

Move Spring sample over to the shared declaration lowering path.

## 8. Definition of Done For Phase 1

Phase 1 declaration IR is successful when:

1. Qin can represent a controller class in shared IR
2. Decorator aliases are resolved before JVM emission
3. JVM backend can emit runtime-visible class and method annotations from shared IR
4. Spring-specific compilation no longer needs to inspect raw Slime AST directly for the happy path
5. declaration methods can express normal instance-style return paths such as `payload.name` and `this.service.message()`
