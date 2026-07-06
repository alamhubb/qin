# Qin JVM JS Subset

Qin 的 JVM 目标面向强类型、静态分析和稳定 `.class` 输出。这里的 JS/TS 支持不是完整浏览器 JavaScript，也不是 Node.js 运行时兼容层，而是可被 Qin 分析、降低到 Qin IR，并可靠映射到 JVM 的语言子集。

## Scope

本规范适用于 Qin 在 JVM 内编译或执行的 JS/TS 代码：

- Qin 编译器和运行时加载的本地 TS/JS 包
- `java:` interop 相关的 TS/JS 代码
- Slime、OVS、CSSTS、Subhuti 等在 Qin JVM 中运行的 parser/compiler 工具链
- 未来要输出为 JVM `.class` 的 TS/JS 代码

本规范不限制浏览器里的前端运行时代码。比如 Vue 运行时在浏览器 JS 引擎中执行，浏览器支持 `Proxy`，这和 Qin JVM 子集是两个边界。

## Core Rules

### ESM-first, `.class`-feasible subset

Qin should prefer ESM-style source syntax, but JVM-targeted support is bounded
by clean `.class` feasibility. A JS/TS/ESM feature belongs in the Qin JVM
subset only when it can be represented in Qin AST/IR and emitted to predictable
JVM classes without a broad dynamic-emulation layer.

This means:

- unsupported features are rejected because they do not have an elegant `.class`
  lowering path, not because Qin is arbitrarily less JavaScript-like
- `app/` and `main/` share the same Qin/ESM core rule: admitted syntax must have
  an elegant `.class` lowering path
- frontend/browser code may use frontend input surfaces and browser host
  capabilities, but those do not broaden Qin core semantics
- MVP shared Qin code should be `.qin` only and must stay inside the
  JS/JVM portable subset
- simple pure ESM npm packages are candidate supported inputs only when
  their syntax and runtime shape fit the relevant target zone

### Node/browser package boundary

Node/browser dual packages should inform Qin's package resolver, not Qin's
project `shared/` source rules. Node-style `"exports"` and conditional entries
choose different files for different consumers such as `import`, `require`,
`node`, or browser-oriented bundlers. They do not prove that one ordinary
JavaScript or TypeScript source file is portable across Node, browser, and JVM.

For Qin, copy the entry-selection idea:

- package manifests may expose target-specific entries
- `app/` may select browser/frontend JS, TS, OVS, Vue, or Qin entries
- `main/` may select JVM-compatible Qin, JS, TS, or Java entries
- `shared/` project code remains `.qin` only in the MVP

If a reusable package needs target-specific implementations, keep those
implementations behind manifest-selected package entries. Do not place ordinary
`.js` or `.ts` directly under project `shared/` just because Node packages can
publish multiple conditional entry points.

For Qin projects, the clean MVP rule is stricter than a typical Node package:

- project-local `shared/` is portable source, so it should contain `.qin` only
- target-specific JS/TS implementations belong in `app/`, `main/`, or a package
  entry selected by the resolver
- package-level conditions may choose entries such as `browser`, `jvm`, `qin`,
  `default`, or future Qin-defined target names
- a package entry is shared-safe only when the selected entry itself follows
  Qin portable source rules; having both Node and browser entries is not enough
- `app/` may host frontend JS/TS because it is a frontend surface, but that does
  not make ordinary JS/TS valid in `shared/`

Qin JVM JS 子集遵循以下规则：

- 普通代码应保持强类型、静态可分析。
- 运行时行为必须能由 Qin AST/IR 明确表达。
- JVM 输出应优先使用明确字段、明确方法、明确接口和明确类结构。
- Java interop 必须调用真实 Java API，不做隐藏 JavaBean 映射。
- 不为了兼容动态 JS 语义而弱化普通强类型代码。
- 如果某个 JS 语义无法稳定映射到 JVM，应显式拒绝，而不是半支持。

## Unsupported Dynamic Features

以下能力不属于 Qin JVM 默认子集：

- `eval`
- `new Function(...)`
- `with`
- JS `Proxy`
- `Reflect`
- CommonJS `require`
- `arguments`
- `Object.defineProperty`
- 修改内建 prototype，例如 `Array.prototype.x = ...`
- `WeakMap`
- `WeakSet`
- `WeakRef`
- generator function
- top-level `await`
- `import.meta`
- `Symbol`
- `Intl`

这些能力不是永久不能实现，而是不进入默认强类型 JVM 子集。只有当 Qin 有明确 IR、类型边界和 runtime SDK 语义时，才能作为独立能力重新评估。

## Proxy Policy

Qin JVM 子集不支持 JS `Proxy`。

原因不是 Java 完全不能做代理，而是 Java 代理和 JS `Proxy` 语义不同：

- Java `java.lang.reflect.Proxy` 只能代理接口方法调用。
- 字节码代理或子类代理不能拦截普通字段读取、字段写入、`final` 方法、`static` 成员和构造器语义。
- JS `Proxy` 拦截的是动态对象协议，包括 `get`、`set`、`has`、`deleteProperty`、`ownKeys`、`getPrototypeOf`、`apply`、`construct` 等。

如果 Qin 要完整支持 JS `Proxy`，普通属性访问就必须统一 lowering 成动态对象协议，例如 `QinObject.get(obj, "x")`。这会削弱静态类型、降低 `.class` 结构的可预测性，并影响 Java interop 边界。

因此：

- `new Proxy(...)` 在 Qin JVM 目标中应编译失败。
- 不应通过 `java.lang.reflect.Proxy` 模拟 JS `Proxy`。
- 不应把字段缺失、getter 猜测或 JavaBean 映射包装成 Proxy 兼容行为。

## Preferred Alternatives

需要代理、AOP 或扩展点时，应使用强类型方案：

- 明确 interface 代理
- 编译期生成 wrapper
- 装饰器或 annotation lowering
- 明确 typed FFI 边界
- Qin-owned runtime SDK 对象，但只在显式 dynamic 边界使用

示例：

```ts
interface UserService {
  findUser(id: string): User
}

const service = Qin.proxy<UserService>({
  findUser(id) {
    return repository.find(id)
  },
})
```

这种代理有明确类型边界，可以映射到 Java interface proxy 或生成 class。它不是 JS `Proxy` 兼容层。

## Object Semantics

普通对象访问应尽量保持静态：

```ts
user.name
user.name = "Qin"
service.findUser(id)
```

当类型明确时，Qin 可以将这些操作 lowering 到字段访问、方法调用或 Qin runtime SDK 调用。

以下 JS 动态语义不能用近似语义替代：

- `delete obj.x` 不能简单等价为 `obj.x = null`，因为 JS 中删除属性会改变 `"x" in obj` 和 `Object.keys(obj)` 的结果。
- `"x" in obj` 不等价于 Java class 字段反射，它还涉及动态属性、原型链和 Proxy trap。
- `Object.keys(obj)` 不等价于 Java 字段列表，它表示 JS 自有、可枚举、字符串 key 的运行时属性集合。

如果代码需要这些语义，Qin 应提供明确 runtime SDK 能力；没有 SDK 语义时应失败。

## Browser Boundary

Qin dev server 可以服务前端项目，但浏览器代码不等于 Qin JVM 子集。

例如 Vue 3 依赖浏览器 `Proxy` 实现响应式系统。只要 Vue runtime 在浏览器里运行，就不受 Qin JVM `Proxy` 禁用规则影响。

如果未来要把 Vue runtime 本身编译到 JVM `.class`，那它必须遵守本规范；当前默认规则下不支持这种目标。

## Java Interop Boundary

从 TS/JS 调用 Java 对象时，必须调用真实 Java API：

```ts
const children = cst.getChildren()
const name = cst.getName()
const value = token.getTokenValue()
```

禁止隐藏映射：

```ts
cst.children      // not mapped to getChildren()
cst.name          // not mapped to getName()
token.tokenValue  // not mapped to getTokenValue()
```

缺失成员必须清晰失败，不能用 property alias、JavaBean 猜测或 Proxy fallback 补齐。
## ESM Acceptance Standard

Qin frontend and OVS/CSSTS compiler output must be valid standard ECMAScript module syntax. The acceptance target is code a browser module parser and a normal JavaScript parser can read directly:

- Top-level imports and exports are module items.
- Exported declarations use forms such as `export const SummaryGrid = () => { ... }`, `export function load() { ... }`, or `export default value`.
- Named export lists use forms such as `export { SummaryGrid }` or `export { SummaryGrid as default }`.
- Object literals and call arguments preserve standard JavaScript syntax, for example `StatCard({ label: "Root URLs", value: String(count), description: "Healthy probes" })`.

Generated output such as `export {type=Const, value=const}` is leaked AST/token state, not ESM. Dropped object properties or malformed export declarations are compiler/runtime defects and must be fixed in the owning parser, CST-to-AST, normalization, or emitter layer.

When generated Slime/Qin/OVS behavior differs from expectations, compare against the legacy handwritten TypeScript `slime-parser` as a reference for ESM grammar and CST-to-AST behavior. The handwritten parser is a diagnostic oracle only; production fixes belong in the active generated parser/runtime path.

Do not implement a second parser or AST extraction path that activates when the generated result is empty. The standard path is the active generated Java/TypeScript parser, CST-to-AST bridge, OVS compiler, normalizer, and emitter. If that path loses object properties or emits malformed ESM, repair that path and prove it with a focused smoke.

## OVS Syntax Pointer

OVS grammar details belong in the owning OVS parser document:
`D:\project\qkyproject\qinall\ovsjs\ovs\ovs-compiler\docs\OvsParser需求文档.md`.
This JVM subset document should not duplicate the full OVS grammar. The only
cross-boundary reminder is that Qin/OVS parser, CST-to-AST, lowerer, emitter,
and runtime fixes must validate against that canonical grammar instead of
introducing alternate accepted forms.
