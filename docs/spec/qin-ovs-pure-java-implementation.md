# Qin/OVS 纯 Java 编译链

## 目标

OVS 与 CSSTS 的标准 JVM 编译链从解析到转换、样式产物和源码生成均执行 Java 实现。它直接消费 Java `OvsParser` / `CssTsParser` 生成的 `SubhutiCst`，复用 Java Slime CST-to-AST 与 AST 生成器，并在 Java 层实现 OVS/CSSTS 语法转换和 Qin 集成。

标准 JVM 路径不运行 OVS/CSSTS TypeScript compiler module，不把 CST 编码为 Qin JavaScript 函数可读取的 Map，不调用 `InterpretedFunction` 执行转换器函数，也不启动 GraalJS/Node。浏览器端继续接收标准 ESM、CSS 和 atom 模块。

Node OVS compiler 是语义和输出对照。两边功能保持一致，分别使用本语言实现。

## 全链路验收

完成定义须同时满足以下条件：

1. Qin 正常单文件与批量 OVS 入口执行 Java parser、Java CST-to-AST、Java OVS/CSSTS 转换、Java style/atom 产物收集以及 Java SlimeGenerator。
2. 完整转换路径不调用 Qin JS package runner、Qin JavaScript 解释器或 Node/GraalJS。
3. 每个已登记 OVS/CSSTS 语法构造有 Java focused test，并与 Node 基准比较 token/CST/AST/ESM/CSS/atom 语义。允许坐标或具体实现结构有语言表示差异，但用户可观察功能和输出契约相同。
4. 验证普通 JS/TS、OVS render、props/spread、view 声明、NoRender、嵌套函数/块/控制流、导入导出、CSSTS declaration/merge/atom、反应式视图和错误输入。
5. 默认 Qin dev/build 入口通过真实 OVS/CSSTS 项目回归。校验输出 ESM、CSS、atom 内容、source locations/source maps、缓存身份与失效。
6. 同一文件、同一 token/CST 输入、零预热，对照 Java 和 Node 各转换阶段性能。最终报告同时公布功能验收和阶段耗时。

## 工作阶段

| 阶段 | 权重 | 验收产物 | 状态 |
|---|---:|---|---|
| Java OVS CST-to-AST 基础 | 15% | 直接消费 SubhutiCst，复用 Java Slime visitor，转换普通语句、render、props、嵌套控制流 | 进行中。Java visitor 覆盖基础 render、props、view、NoRender、动态 child reactive、side effect、if/else、循环、switch 和 try/catch/finally lowering；新增 nested render/control-flow regression。OVS catalog verifier 已检查 HTML render lowering、命名/default exports 和 implicit component wrapper，尚缺完整 Node output parity |
| OVS 语法闭包 | 25% | render 表达式/语句、component/view、导入导出、props 方法/spread、NoRender、函数体规则和响应式 lowering | 进行中。render、spread 基础、props 方法、view 基本形态、NoRender block、动态 child reactive、side-effect、if/else、循环、switch 与 try/catch/finally reactive lowering 已开始；完整函数/reactive 语义仍未实现 |
| CSSTS Java 转换 | 20% | CSS declaration、merge、class/style metadata、atom 收集与生成 | 进行中。OVS 与独立 `.cssts` 标准入口走 Java parser/visitor/generator；动态 TS atom、默认及用户配置的 pseudo atom、classGroup、logical/conditional atom 和 CSS spread 已生成 CSS/atom。20 项上游 `.cssts` 样例全部通过 Java 编译；精确 Node 输出对照和完整 style metadata 行为仍待验收 |
| Java ESM/style emitter 集成 | 15% | SlimeGenerator、runtime imports、CSS 与 atom 产物及位置映射 | 进行中。OVS 与 `.cssts` 均调用 Java SlimeGenerator；样式 imports、动态 atom、配置 classPrefix/pseudo/classGroup 的 CSS 与 atom 模块已验证。source map/customProperties 尚未实现 |
| Qin 单文件/批量标准入口切换 | 10% | QinOvsCompiler 单件和批处理直接走 Java pipeline，缓存指纹覆盖 Java compiler inputs | 进行中。单文件与批量标准入口已调用 `QinOvsJavaCompiler`，移除 `QinOvsCompiler` 内 JS package runner/wrapper；缓存身份含 Java parser/runtime 与 Java OVS compiler 指纹。项目自定义 JS transform 插件暂未迁移 |
| 全量行为与错误对照 | 10% | 登记 OVS/CSSTS corpus、负例和真实 balance-monitoring project 回归 | 进行中。balance-monitoring 全量构建通过并生成 Java OVS/CSSTS 输出；完整 Node parity corpus、错误诊断和 source-map 对照尚未开始 |
| 冷启动性能验收 | 5% | 新 JVM 首次转换，各阶段对比 Node，结果可复现 | 进行中。Java 端已能在新 JVM 进程用 fresh marker 完成真实 9 个 OVS 模块转换，Node 同输入的阶段对照仍未建立 |

权重之和为 100%。只按上表验收项更新完成度，不按代码行数或局部 smoke 通过率更新。

## 当前证据

- `qin/packages/qin-parser/src/java/com/qin/parser/OvsParser.java` 已是 Java OVS grammar，覆盖 OVS render、view 和 NoRender CST 规则。
- `slime/java-slime/slime-parser/.../SlimeCstToAstUtils.java` 提供类型化 Java Slime AST 构造入口。
- `slime/java-slime/slime-generator/.../SlimeGenerator.java` 提供 Java AST 到 ESM 的生成器。
- `QinOvsCompiler.java` 的单文件与批量标准 OVS 路径现已直接调用 `QinOvsJavaCompiler`，不再把 CST 编码成 JS Map，也不再调用 Qin JS package runner。批量入口逐项执行 Java 编译并复用现有内存/磁盘缓存。
- `QinOvsCompilerNoFallbackSmokeTestMain` 对 `QinOvsCompiler` 与 `QinOvsJavaCompiler` 做源码级约束，禁止重新引入 Qin JS package runner、`InterpretedFunction`、GraalJS 或 `generated-runtime-adapter`。该测试同时执行一次 Java OVS 编译并检查 Java 入口生成的 ESM。
- 使用 `QinOvsCompilerBatchProfileProbeMain` 在新 JVM 进程、`--fresh-marker` 下对 `balance-monitoring/app` 的 9 个 OVS 模块做真实 Java 转换，记录 `java OVS transform` 约 1077 ms，整批约 1154 ms。第二次命中 9 个磁盘缓存约 77 ms。这是 Java 侧证据，不能代替尚未完成的 Node 同输入对照，也不能把缓存命中时间当作转换性能。
- Java OVS 编译器现在在 `qin.profile` 开启时单独记录 `parse source`、`cst to ast`、`generate esm` 和产物阶段。一次新 JVM 采样中首个模块解析约 329 ms，随后 8 个模块解析为 3–40 ms，CST→AST 为 1–12 ms，ESM 生成为 0–2 ms。首个解析值包含 JVM 类加载/初始化，阶段计时因此保留为诊断证据，不能直接当作算法稳态耗时。
- `QinOvsCstToAst.java` 目前有 Java 标准 visitor 委托、基础 OVS render/property、顶层 runtime import/component wrapper、嵌套 if 和 view lowering。view focused AST 与 runtime ESM smoke 覆盖 destructured/default/rest 参数、NoRender 语句与显式返回 render；完整 Node view corpus 语义仍待对照。
- Java visitor 现已覆盖 `css { ... }` lowering，并按模块与命名导入检查后补齐 `ovsjs` / CSSTS runtime imports，避免重复声明。parser smoke 验证已有完整 runtime imports 时不再重复注入。
- Java `CsstsAtomNames` 承载 CSSTS CSS 属性映射、命名颜色与默认 pseudo/classGroup 配置，识别动态 atom 并保持非 atom 变量引用。parser smoke 覆盖 `displayFlex`、`colorRed`、普通变量、`hover` 与 `click`。
- `QinOvsJavaCompiler` 由 Qin OVS 单文件和批量入口直接调用；`QinCsstsCompiler` 也切到 `QinCsstsJavaCompiler`，不再调用 TS runner、TS 包目录 fingerprint 或 JS CST→AST adapter。两者复用 Java OVS/CSS visitor 与 SlimeGenerator。配置 classPrefix、模块 imports、数值 atom、默认伪类和默认 classGroup smoke 已通过。自定义配置 atom、source map 尚未实现。Node `cssts-compiler` 源码目前只在配置类型中声明 `customProperties`，实现源码没有读取它；因此它暂不属于两端现存功能差异。
- Java visitor 在 view 函数体中将 `NoRenderBlock` 降低为标准 `BlockStatement`，保留其普通语句，并继续 lowering 随后的 OVS render。在 render body 的 `#{...}` 中，直接 render 及嵌套 `if`、`for-of`、`switch`、`try/catch` 分支的 OVS render 会进入当前 `children`，普通表达式仍保留为逻辑语句。parser AST smoke、runtime ESM smoke 和真实项目 build 均通过。
- Java OVS/CSSTS classPrefix、pseudoClassConfig、classGroup 现在从 Qin Java parser 的配置 AST 静态读取，不再用正则扫描 `qin.config.js` 文本。优先限定到 `export default` 内的插件 `cssts` 对象，并保留裸对象、顶层 cssts 及旧顶层 classPrefix 配置。配置读取缓存以完整配置源码为键，且 OVS/CSSTS 与前端模块 transform fingerprint 覆盖新的 reader，确保注释/字符串/其他插件同名属性不会误读、旧磁盘缓存不会污染结果。回归验证插件范围、注释、字符串、伪类/group 配置以及配置变更失效均通过。
- Java `CssTsParser` 的 CSS atom 项现使用标准 Slime `AssignmentExpression` 解析，并支持 `...spread`。Java OVS/CSSSTS CST-to-AST 对逻辑、条件、一元表达式递归替换已登记 atom 为 `csstsAtom` 引用，同时保留普通变量；spread 编译成 Java Slime `SpreadElement`。CSSTS catalog verifier 的 20 项检查已包括 merge/atomModule 关联、逻辑与条件表达式保留、spread、命名/默认导出和 async/await，20 项通过。runtime smoke 另验证动态逻辑 atom、三元 atom、spread 和 atom module 收集。样例 `20-complex-expression` 中 `bgDark`/`bgLight` 不属于当前 Java atom 词典，按普通变量引用输出，条件表达式结构保持。
- Java render lowering 可处理同时包含渲染表达式和普通语句的复杂 body：生成 `defineOvsComponent` IIFE、局部 `children`、顺序执行 NoRender block，并按源顺序 `children.push` 输出内容。非字面量 child 使用 `defineReactiveExpression(() => expression)`，字面量直接保留；赋值、更新和一元表达式作为执行语句保留。focused AST regressions 与 runtime ESM smoke 均通过。复杂控制流的精确 reactive lowering 仍未覆盖。
- render body 内 `if/else` 现在由 Java 代码生成 `defineReactiveExpression(() => { const children = []; if (...) { ...children.push(renderCall) } return children; })` 并追加到父 children。parser AST 与 Java ESM smoke 均验证两个分支保留 render push 和动态响应式控制流。复杂分支内变量作用域语义仍未完整验证。
- render body 内 `while`、`do/while`、`for`、`for-in`、`for-of` 由 Java Slime visitor 构造标准 loop AST，再将其 body 中 OVS render lower 到局部 children push，整个循环计算由 `defineReactiveExpression` 包裹。switch case/default 及 try/catch/finally 同样由 Java Slime AST visitor 构造控制节点，并递归 lower 各分支里的 render。`OvsParserJavaSmokeTestMain` 验证循环、switch、try 的 AST 分支保留 children push；`QinOvsJavaCompilerSmokeTestMain` 验证 Java ESM 输出。
- `python -X utf8 build-chevrotain-parser.py` 与 `qin.bat build` 均通过。Qin 标准入口的 OVS compiler smoke、render/if、object props、classPrefix、箭头函数体、生产批量输出及 Java toolchain fingerprint smoke 均通过。
- `QinOvsCompilerConfigPluginSmokeTestMain` 仍失败，因为它要求对任意文本执行 `qin.config.js` 中自定义 JavaScript Vite `transform` hook；该 hook 尚无 Java 插件 API。标准 OVS/CSSTS 转换不执行此 hook。
- `balance-monitoring` 全量 `qin build` 已通过并生成 `.cssts` ESM、`balance-*` CSS、默认 pseudo/classGroup 样式和共享 atom 模块，`.cssts` 的 import/default export 保留。parser build、OVS/CSSTS Java compiler smoke 均通过。
- `QinOvsJavaCatalogProbeMain` 对 OVS 仓库 `tests/cases` 的 64 个示例执行 Java 编译，63 个生成非空 ESM，1 个按需求文档规定因 `div({ class: ... })` 对象 props 语法而预期拒绝，0 个意外失败。这个结构性目录检查不等于 Node 完整输出语义对照。
- OVS catalog verifier 现在还检查 source 中登记 HTML 标签的 `$OvsHtmlTag` lowering、显式 named/default exports、未显式导出的顶层组件包装，以及 `45-loop-component.ovs` 中 nested `<ul>`、for-of 与 `ListItem` 调用的边界。该样例暴露 nested `<ul>` 中 loop 被误提升至外层 `<div>`，Java visitor 现优先在控制流前界限内消费 nested render；focused AST regression 与 64 项 catalog 检查通过。
- Node 输出对照仍未可执行。Node 24 直接加载生成 `java-sdk-js` 中带 TypeScript 语法的 `.js` 失败；临时仅测试 loader 处理该语法后，又遇到生成 parser 包 exports 未公开 `@qin/generated-qin-parser-ts/com/slime/ast/SourceLocation` 子路径。没有修改生成包或生产解析路径来绕过这两个问题。
- 当前 converter 仍缺 OVS 完整 view 函数体语义对照、复杂控制流的完整 reactive 语义、CSSTS source map、项目自定义 JavaScript transform hook 的 Java 插件实现、完整 Node 输出逐例对照与 Java/Node 阶段性能对照。Node 端未消费的 `customProperties` 配置类型不作为迁移缺口。因此不可宣称全目标完成。

## 代码约束

- 输入和输出保持 `SubhutiCst` 与类型化 Slime AST，使用 Java getter、字段、visitor 和 collections。
- OVS/CSSTS 语法转换写在 Java visitor/compiler 中，不能用 TypeScript adapter、Map 语法节点或 Qin interpreter 充当 Java 实现。
- 复用现有 Java Slime transformer/generator；只在 OVS/CSSTS 语义所属层添加 Java 专用行为。
- 不对失败语法提供第二条 TS fallback。
- 每个转换阶段先复用上游产生的 CST/AST，再以 focused tests、项目集成和冷态性能扩大验证。
