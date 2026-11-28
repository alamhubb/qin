# Qin/OVS 纯 Java 编译链

## 目标

OVS 与 CSSTS 的标准 JVM 编译链从解析到转换、样式产物和源码生成均执行 Java 实现。它直接消费 Java `OvsParser` / `CssTsParser` 生成的 `SubhutiCst`，复用 Java Slime CST-to-AST 与 AST 生成器，并在 Java 层实现 OVS/CSSTS 语法转换和 Qin 集成。

标准 JVM 路径不运行 OVS/CSSTS TypeScript compiler module，不把 CST 编码为 Qin JavaScript 函数可读取的 Map，不调用 `InterpretedFunction` 执行转换器函数，也不启动 GraalJS/Node。浏览器端继续接收标准 ESM、CSS 和 atom 模块。

Node OVS compiler 是语义和输出对照。两边功能保持一致，分别使用本语言实现。

## 2026-11-28 标准入口复验

Java 标准链路为 `OvsParser / CssTsParser → SubhutiCst → QinOvsCstToAst → 类型化 Slime AST → SlimeGenerator → ESM / CSS / atom`。仅保留 JDK 的 PATH 时，未命中缓存的 OVS 单文件、批量 OVS 和 CSSTS 编译均通过。

本轮修复了两个转换错误。受 `try` 保护的复杂子渲染块在离开该块前完成同步构造，属性求值抛出的异常能进入源码中的 `catch`。默认导出按其直接表达式或声明节点转换，避免递归选中表达式内部的局部声明。Slime 提交 `19f5c7dd` 已推送，新增默认导出回归验证箭头、对象、函数和类，旧转换代码在箭头用例失败。

正式 Java 包 `chevrotain-zor0o8bj` 的 Parser 构建、Java OVS compiler、Java 标准入口约束、OVS/CSSTS 缓存指纹、前端源码映射和热刷新回归通过。命名 `view` 和默认导出的 Java/Node 产物在实际 Vue 中均渲染 `caught`，执行顺序为 `throw → caught:props → finally`。63 份 OVS 的首次挂载和三次父级更新一致，20 份 CSSTS 的运行值、CSS 和 atom 一致。`balance-monitoring` 标准 build 和关闭余额采集的本地 dev 通过，dev 返回内联源码映射及一致的重复响应。

证据目录为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/complex-try-formal`。关键报告为 `try-props-vue.log`、`ovs-report.json`、`cssts/report.json`、`balance-build.log` 和 `balance-dev.log`。

交错运行 Java 与 Node，每组 5 个新进程，零预热。完整编译从源码读入内存后开始，Node 包加载包含在完整时间内，操作系统启动进程另计。同 CST 转换的 Node JSON 导入另计，Java 窗口包含 visitor 初始化与默认样式配置。各组产物哈希稳定。下表为毫秒中位数，原始数据为同级 `complex-try-cold/samples.csv`。

| 文件 | Java 完整编译 | Node 完整编译 | Java 同 CST→AST | Node 同 CST→AST |
|---|---:|---:|---:|---:|
| BalancePanel.ovs | 337.20 | 5580.95 | 39.41 | 22.00 |
| CardHeading.ovs | 367.85 | 1120.16 | 42.92 | 37.00 |

纯 Java 路径消除了此前转换器的解释执行成本。本轮同 CST 数据仍显示 Java 转换比 Node 多约 17.41 ms 和 5.92 ms。类加载、首次执行、树遍历和节点分配继续计入各自阶段。已有 25 份 OVS 的所选响应式调用计数存在结构差异，登记样例的 Vue 挂载与更新以及本轮异常顺序对照通过。

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
| Java OVS CST-to-AST 基础 | 15% | 直接消费 SubhutiCst，复用 Java Slime visitor，转换普通语句、render、props、嵌套控制流 | 登记回归通过。Java visitor 直接构造类型化 Slime AST，Parser 与 compiler focused tests、63 份 OVS 编译及独立 Node Vue 行为对照通过 |
| OVS 语法闭包 | 25% | render 表达式/语句、component/view、导入导出、props 方法/spread、NoRender、函数体规则和响应式 lowering | 登记回归通过。命名/default exports、复杂子块、控制流、事件、props、响应式更新及异常顺序有定向运行验证。保留所选响应式调用计数差异的结构报告 |
| CSSTS Java 转换 | 20% | CSS declaration、merge、class/style metadata、atom 收集与生成 | 登记回归通过。20 份独立 Node 对照的运行值、CSS 和 atom 一致。配置 pseudo、classGroup、classPrefix、组合与 spread 的 Java 回归通过 |
| Java ESM/style emitter 集成 | 15% | SlimeGenerator、runtime imports、CSS 与 atom 产物及位置映射 | 登记回归通过。Java SlimeGenerator 发射 ESM，Java 收集 CSS/atom。源码映射回归与实际 dev 返回的内联源码映射通过 |
| Qin 单文件/批量标准入口切换 | 10% | QinOvsCompiler 单件和批处理直接走 Java pipeline，缓存指纹覆盖 Java compiler inputs | 登记回归通过。仅保留 JDK 的 PATH 时，未缓存的标准 OVS 单文件、批量和 CSSTS 编译通过。工具链、配置与源码变化的缓存失效回归通过 |
| 全量行为与错误对照 | 10% | 登记 OVS/CSSTS corpus、负例和真实 balance-monitoring project 回归 | 登记回归通过。63 份 OVS 的首次挂载和三次更新、20 份 CSSTS 的运行值及产物、登记负例与 focused diagnostics 通过。balance-monitoring 标准 build、关闭采集的本地 dev、源码映射与热刷新通过 |
| 冷启动性能验收 | 5% | 新 JVM 首次转换，各阶段对比 Node，结果可复现 | 完成 5 次新进程、零预热的完整编译和同 CST 转换对照。BalancePanel 为 Java/Node 337.20/5580.95 ms，同 CST→AST 为 39.41/22.00 ms。原始数据和计时边界见上节 |

权重之和为 100%。只按上表验收项更新完成度，不按代码行数或局部 smoke 通过率更新。

## 当前证据

- `qin/packages/qin-parser/src/java/com/qin/parser/OvsParser.java` 已是 Java OVS grammar，覆盖 OVS render、view 和 NoRender CST 规则。
- `slime/java-slime/slime-parser/.../SlimeCstToAstUtils.java` 提供类型化 Java Slime AST 构造入口。
- `slime/java-slime/slime-generator/.../SlimeGenerator.java` 提供 Java AST 到 ESM 的生成器。
- `QinOvsCompiler.java` 的单文件与批量标准 OVS 路径现已直接调用 `QinOvsJavaCompiler`，不再把 CST 编码成 JS Map，也不再调用 Qin JS package runner。批量入口逐项执行 Java 编译并复用现有内存/磁盘缓存。
- `QinOvsCompilerNoFallbackSmokeTestMain` 对 `QinOvsCompiler` 与 `QinOvsJavaCompiler` 做源码级约束，禁止重新引入 Qin JS package runner、`InterpretedFunction`、GraalJS 或 `generated-runtime-adapter`。该测试同时执行一次 Java OVS 编译并检查 Java 入口生成的 ESM。
- 使用 `QinOvsCompilerBatchProfileProbeMain` 在新 JVM 进程、`--fresh-marker` 下对 `balance-monitoring/app` 的 9 个 OVS 模块做真实 Java 转换，记录 `java OVS transform` 约 1077 ms，整批约 1154 ms。第二次命中 9 个磁盘缓存约 77 ms。这是 Java 侧证据，不能代替尚未完成的 Node 同输入对照，也不能把缓存命中时间当作转换性能。
- Java OVS 编译器现在在 `qin.profile` 开启时单独记录 `parse source`、`cst to ast`、`generate esm` 和产物阶段。一次新 JVM 采样中首个模块解析约 329 ms，随后 8 个模块解析为 3–40 ms，CST→AST 为 1–12 ms，ESM 生成为 0–2 ms。首个解析值包含 JVM 类加载/初始化，阶段计时因此保留为诊断证据，不能直接当作算法稳态耗时。
- 同一批 `balance-monitoring` 文件的 CST→AST 阶段对照已经可运行。Node 使用现有 Java CST JSON，visitor 注册移到循环外，Java 使用相同源文件的原生 parser/CST 与 Java visitor。三轮稳态样本的 visitor/转换阶段约为 `CardHeading` Java 1.0–1.2 ms、Node 214 ms，`BalanceRow` Java 3.6 ms、Node 460 ms，`BalanceTable` Java 1.0 ms、Node 873 ms，`SummaryGrid` Java 1.2 ms、Node 13.5 ms，`BalancePanel` Java 0.95 ms、Node 6.3 ms。Node 还额外花费约 17–65 ms 做 AST normalization。该对照的 Node 输入是 Java CST 的 JSON 导入，Java 侧重新生成等价 CST，阶段归因用于定位适配开销，完整字节级 CST 身份对照仍需单独验收。
- 已发布的 Node OVS 0.2.2 诊断快照能生成 `BalancePanel.ovs` 的 ESM，该快照在另四个文件的 `export` 处停止。当前 OVS 源码的 Node 诊断构建在词汇表初始化时遇到 SDK 缺少 `>>>` 的错误，因此四个失败不能归到当前 Node 语法。旁路输入现成的 Java CST 后，当前 Node visitor 可以运行，但生成器仍会误把 Java 风格访问方法当成字段值，例如把 `raw()` 的方法体写进 import。完整输出对照仍待验收。
- Java 生成的 63 个有效 OVS 与 20 个 CSSTS 样例 ESM 均通过 Node 24 `vm.SourceTextModule` 语法检查。用相同的模拟组件和应用状态加载 `BalancePanel.ovs` 的 Java 构建产物与已发布 Node 诊断快照的输出，四组 loading/error 状态下的组件调用、props 和刷新回调结果一致。它覆盖这个页面的组件工厂行为，浏览器渲染与全量语义对照另待验证。
- `QinOvsCstToAst.java` 目前有 Java 标准 visitor 委托、基础 OVS render/property、顶层 runtime import/component wrapper、嵌套 if 和 view lowering。view focused AST 与 runtime ESM smoke 覆盖 destructured/default/rest 参数、NoRender 语句与显式返回 render；完整 Node view corpus 语义仍待对照。
- Java visitor 现已覆盖 `css { ... }` lowering，并按模块与命名导入检查后补齐 `ovsjs` / CSSTS runtime imports，避免重复声明。parser smoke 验证已有完整 runtime imports 时不再重复注入。
- Java `CsstsAtomNames` 承载 CSSTS CSS 属性映射、命名颜色与默认 pseudo/classGroup 配置，识别动态 atom 并保持非 atom 变量引用。parser smoke 覆盖 `displayFlex`、`colorRed`、普通变量、`hover` 与 `click`。
- `QinOvsJavaCompiler` 由 Qin OVS 单文件和批量入口直接调用；`QinCsstsCompiler` 也切到 `QinCsstsJavaCompiler`。两者复用 Java OVS/CSS visitor 与 SlimeGenerator。配置 classPrefix、模块 imports、数值 atom、默认伪类和默认 classGroup smoke 已通过。Node `cssts-compiler` 的 `customProperties` 目前仅在配置类型中出现。
- Java OVS/CSSTS 编译缓存随 ESM 保存偏移。前端服务在改写 import、包装 OVS 组件、添加运行时导入和开发热更新前缀时同步调整偏移，最终输出内联 Source Map v3。测试用 Node 的标准 SourceMap API 验证显式和隐式默认导出、命名导出、CSSTS atom、CRLF、UTF-16 列号、缓存读取和热更新后的精确位置。`balance-monitoring` 的 12 个 OVS/CSSTS 构建模块均带有映射，内联源码与实际文件一致。
- Java visitor 在 view 函数体中将 `NoRenderBlock` 降低为标准 `BlockStatement`，保留其普通语句，并继续 lowering 随后的 OVS render。在 render body 的 `#{...}` 中，直接 render 及嵌套 `if`、`for-of`、`switch`、`try/catch` 分支的 OVS render 会进入当前 `children`，普通表达式仍保留为逻辑语句。parser AST smoke、runtime ESM smoke 和真实项目 build 均通过。
- Java OVS/CSSTS classPrefix、pseudoClassConfig、classGroup 现在从 Qin Java parser 的配置 AST 静态读取，不再用正则扫描 `qin.config.js` 文本。优先限定到 `export default` 内的插件 `cssts` 对象，并保留裸对象、顶层 cssts 及旧顶层 classPrefix 配置。配置读取缓存以完整配置源码为键，且 OVS/CSSTS 与前端模块 transform fingerprint 覆盖新的 reader，确保注释/字符串/其他插件同名属性不会误读、旧磁盘缓存不会污染结果。回归验证插件范围、注释、字符串、伪类/group 配置以及配置变更失效均通过。
- Java OVS 单文件与批量编译从同一份配置文本生成缓存键、CSS 和 atom 模块。诊断在批量扫描缓存之后更新配置，修复前产生了新前缀并写入旧配置的缓存键，修复后两个模块均使用原快照。下一批采用新前缀，恢复原配置后加载磁盘缓存也保留原前缀。
- Java `CssTsParser` 的 CSS atom 项现使用标准 Slime `AssignmentExpression` 解析，并支持 `...spread`。Java OVS/CSSSTS CST-to-AST 对逻辑、条件、一元表达式递归替换已登记 atom 为 `csstsAtom` 引用，同时保留普通变量；spread 编译成 Java Slime `SpreadElement`。CSSTS catalog verifier 的 20 项检查已包括 merge/atomModule 关联、逻辑与条件表达式保留、spread、命名/默认导出和 async/await，20 项通过。runtime smoke 另验证动态逻辑 atom、三元 atom、spread 和 atom module 收集。样例 `20-complex-expression` 中 `bgDark`/`bgLight` 不属于当前 Java atom 词典，按普通变量引用输出，条件表达式结构保持。
- Java render lowering 可处理同时包含渲染表达式和普通语句的复杂 body：生成 `defineOvsComponent` IIFE、局部 `children`、顺序执行 NoRender block，并按源顺序 `children.push` 输出内容。非字面量 child 使用 `defineReactiveExpression(() => expression)`，字面量直接保留；赋值、更新和一元表达式作为执行语句保留。focused AST regressions 与 runtime ESM smoke 均通过。复杂控制流的精确 reactive lowering 仍未覆盖。
- render body 内 `if/else` 现在由 Java 代码生成 `defineReactiveExpression(() => { const children = []; if (...) { ...children.push(renderCall) } return children; })` 并追加到父 children。parser AST 与 Java ESM smoke 均验证两个分支保留 render push 和动态响应式控制流。复杂分支内变量作用域语义仍未完整验证。
- `#{...}` 与控制流嵌套时，Java visitor 以当前语句及其文法包装节点为边界。`if` 分支里的 NoRender 语句留在原分支，已有响应式回调内的控制流共用作用域，保留 `continue` 与循环的关系。Java compiler smoke 覆盖这两个回归，生成 ESM 在 Node 中用模拟渲染函数执行后验证了真假分支、执行顺序和跳过循环项。
- Java Slime 在分派表达式时把扩展语法节点交给 Qin visitor。此前在父表达式中递归查找 OVS/CSSTS 节点会丢失三元分支、数组元素、调用参数、对象属性和局部变量绑定。新的 compiler smoke 覆盖这些组合，生成 ESM 也在 Node 中用模拟组件和样式模块执行了两组条件。Qin parser 构建把共享 Slime visitor 与 Qin visitor 一起编译，避免消费公共 JAR 里的旧 visitor。
- render body 内 `while`、`do/while`、`for`、`for-in`、`for-of` 由 Java Slime visitor 构造标准 loop AST，再将其 body 中 OVS render lower 到局部 children push，整个循环计算由 `defineReactiveExpression` 包裹。switch case/default 及 try/catch/finally 同样由 Java Slime AST visitor 构造控制节点，并递归 lower 各分支里的 render。`OvsParserJavaSmokeTestMain` 验证循环、switch、try 的 AST 分支保留 children push；`QinOvsJavaCompilerSmokeTestMain` 验证 Java ESM 输出。
- `python -X utf8 build-chevrotain-parser.py` 与 `qin.bat build` 均通过。Qin 标准入口的 OVS compiler smoke、render/if、object props、classPrefix、箭头函数体、生产批量输出及 Java toolchain fingerprint smoke 均通过。
- `QinOvsCompilerConfigPluginSmokeTestMain` 现在验证配置里存在自定义 JavaScript Vite `transform` hook 时，标准 OVS 编译仍使用 Java parser/compiler，并且不执行该 hook。任意 JavaScript 插件扩展仍需另行设计 Java 插件 API。
- `balance-monitoring` 全量 `qin build` 已通过并生成 `.cssts` ESM、`balance-*` CSS、默认 pseudo/classGroup 样式和共享 atom 模块，`.cssts` 的 import/default export 保留。parser build、OVS/CSSTS Java compiler smoke 均通过。
- `QinOvsJavaCatalogProbeMain` 对 OVS 仓库 `tests/cases` 的 64 个示例执行 Java 编译，63 个生成非空 ESM，1 个按需求文档规定因 `div({ class: ... })` 对象 props 语法而预期拒绝，0 个意外失败。这个结构性目录检查不等于 Node 完整输出语义对照。
- OVS catalog verifier 现在还检查 source 中登记 HTML 标签的 `$OvsHtmlTag` lowering、显式 named/default exports、未显式导出的顶层组件包装，以及 `45-loop-component.ovs` 中 nested `<ul>`、for-of 与 `ListItem` 调用的边界。该样例暴露 nested `<ul>` 中 loop 被误提升至外层 `<div>`，Java visitor 现优先在控制流前界限内消费 nested render；focused AST regression 与 64 项 catalog 检查通过。
- Node 输出对照仍未可执行。Node 24 直接加载生成 `java-sdk-js` 中带 TypeScript 语法的 `.js` 失败；临时仅测试 loader 处理该语法后，又遇到生成 parser 包 exports 未公开 `@qin/generated-qin-parser-ts/com/slime/ast/SourceLocation` 子路径。没有修改生成包或生产解析路径来绕过这两个问题。
- 当前 converter 仍待验证 OVS 完整 view 函数体和复杂控制流的 reactive 语义。项目自定义 JavaScript transform hook 的 Java 插件实现、完整 Node 输出逐例对照及 Java/Node 阶段性能对照仍待完成。

## 2026-11-28 OVS 冷启动归因

从源码读入内存后开始计时。每份源码及每种配置启动独立 JDK 25 进程，测第一次解析。使用当前 Qin 生成的 Java Chevrotain 语法和随包发布的前瞻表。阶段探针先通过公开 `OvsParser.Program(MODULE)` 测量，再直接调用同一生成 Parser 拆分；两个入口生成的 ESM 哈希相同。

| 文件 | Token 词汇表 | 生成类初始化和前瞻表加载 | 生成 Parser 构造 | 分词 | 匹配规则和构建 CST | 语法校验 |
|---|---:|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | 10 ms | 67 ms | 375 ms | 9 ms | 29 ms | 2 ms |
| `CardHeading.ovs` | 10 ms | 66 ms | 379 ms | 9 ms | 29 ms | 15 ms |

上表为每个阶段分别取三次中位数。构造阶段的独立带追踪样本显示，`performSelfAnalysis` 约 292 ms，其中 396 条规则的录制约 210 ms、规则引用解析约 4 ms、基于构建期前瞻路径装配运行时函数约 73 ms。语法录制会执行生成规则的录制分支，重建 Chevrotain GAST。运行时仍要装配前瞻函数，因为构建产物保存的是前瞻路径。

### 构建期录制原型

在独立输出目录，将构建期录制所得的 GAST 保存为 57,138 字节数据。原型加载后保留规则引用解析和原有前瞻装配。使用同一份诊断字节码切换录制与加载，比较五个新 JVM 的中位数。

| 文件 | JVM 模式 | 录制时初始化 | 加载时初始化 | 录制时首次解析总计 | 加载时首次解析总计 |
|---|---|---:|---:|---:|---:|
| `BalancePanel.ovs` | 分层编译 | 585 ms | 294 ms | 638 ms | 390 ms |
| `CardHeading.ovs` | 分层编译 | 462 ms | 266 ms | 518 ms | 382 ms |
| `BalancePanel.ovs` | `-Xint` | 1536 ms | 999 ms | 1622 ms | 1202 ms |
| `CardHeading.ovs` | `-Xint` | 1516 ms | 973 ms | 1616 ms | 1230 ms |

构造后第一次 `Program()` 耗时也发生变化。分层编译下，对应两份文件分别从 49 ms 增至 96 ms、从 56 ms 增至 116 ms。关闭 JIT 后仍有同方向差异。`BalancePanel.ovs` 的独立 `-Xint -Xlog:class+load=info` 样本显示，运行时录制在构造阶段加载了 1665 个生成 Parser 的 lambda 类，之后的首次 `Program()` 新增 0 个。加载构建期 GAST 时，构造和首次 `Program()` 分别新增 397 和 263 个。这些计数证明部分首次链接工作移到了实际解析，无法据此换算各项耗时。采用包含构造的首次解析总时间衡量净收益。

### 正式 Qin 构建

`build-chevrotain-parser.py` 在捕获前瞻表时同时写入 GAST。三个生成 Parser 从各自的 `.gast.bin` 加载录制结果，再调用 Chevrotain 的规则引用解析和前瞻装配。构建清单覆盖编译输入、生成类、GAST、前瞻表和 JAR；缓存校验通过八项检查。

正式产物复测每文件每版本五个新 JVM，首次解析从源码进入 Parser 到 CST 完成，包含初始化及语法校验。新旧版本均从各自的构建 classes 目录加载。新 JAR 也单独跑了冷启动及语料检查。

| 文件 | 旧构建首次解析 | 新构建首次解析 |
|---|---:|---:|
| `BalancePanel.ovs` | 504 ms | 351 ms |
| `CardHeading.ovs` | 529 ms | 393 ms |

新 JAR 与旧产物在九个业务模块及 64 个登记 OVS 文件上的 CST 结构、位置、Token 信息、结果状态和诊断哈希均相同。两个计时样例的 ESM 哈希相同。Qin Parser 的四个构建 smoke、OVS 目录（63 个编译通过、1 个预期拒绝）、20 个 CSSTS 样例和 `balance-monitoring` 的 `qin.bat build` 均通过。独立 Node 对照仍待验证。原始输出位于工作区 `tmp/ovs-parser-cold-5b5852c4ddbd473790b4d3e076620d34`。

### Node Chevrotain 解析对照

对两个业务文件各执行 5 次新 JVM 和 5 次新 Node 进程，零预热。以源码读入内存为起点，到首次 CST 返回为止。Node 使用 `Chevrotain-test` 当前 OVS Parser，构造时已分词，基准直接调用 `Program()`，避免重复分词。Node 模块加载包含 `tsx` 的装载成本。

| 文件 | Java 初始化 | Java Program（分词、匹配、CST 和校验） | Java 首次解析 | Node 模块加载 | Node 构造及分词 | Node Program | Node 首次解析 |
|---|---:|---:|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | 252 ms | 77 ms | 327 ms | 39 ms | 664 ms | 2.39 ms | 705 ms |
| `CardHeading.ovs` | 244 ms | 102 ms | 346 ms | 38 ms | 662 ms | 3.01 ms | 703 ms |

两条路线均消费完整文件。Java 分项记录包括显式 EOF，排除它后，两份文件的 128 和 121 个源码 token 的文本及 UTF-16 起止偏移逐个相同。类型命名分别有 43 和 38 处差异，包括 `IdentifierName` 与 `Identifier`、`IdentifierName` 与上下文关键字 `From`。因此还需对齐 CST 和语法校验边界，才能归因 `Program()` 的差距。Node 此处运行时执行 `performSelfAnalysis()`，Java 已在构建期录制并预计算前瞻路径。原始样本及 token 导出位于 `tmp/node-ovs-recheck-07b0fc92ab3348438842b52d8e6edb9d`。

Node 完整 OVS 转换使用另一条源码路径。排查修复了 Java SDK 的位运算、数字正则标志、matcher reset、集合遍历及 Stream 回调调用方式，并对齐 OVS 参数访问器与生成 Parser。旧诊断包的 `CardHeading.ovs` 仍在 CST 转换阶段依赖 `__qin_cst_name_of__` 等宿主接口。使用重新生成的 Java 语言 parser 后，诊断构建通过了 v20/v23 元数据检查。Java→JS 编译器现支持 `if (!(value instanceof T v)) throw ...` 后续语句及 `while (value instanceof T v)` 循环体中使用模式变量。仅生成 JavaScript 时，变量声明输出 `let v = null`。Node 执行回归覆盖匹配、不匹配、异常和循环；既有 TS shadow smoke 通过。重新生成整个 Node 对照包已越过语义与 lowering 阶段，当前 JS 后端在处理 `SubhutiDecisionProgram` 依赖的 `java.lang.Thread` 时失败。

### BalancePanel 首次生成 ESM

源码读入内存后开始计时。每次在新进程中运行，Java 调用 `QinOvsJavaCompiler.compile`，Node 动态加载发布版 `ovs-compiler` 0.2.2 的本地诊断 bundle 并执行 `vitePluginOvsTransform`。两边均执行实际转换并返回 ESM。首次五次样本的中位数如下。

| 对象 | Java 当前版本 | Node 发布版 0.2.2 |
|---|---:|---:|
| 首次生成 ESM，总计 | 473 ms | 36.7 ms |
| Java 解析（含初始化） | 431 ms | — |
| Java CST→AST | 27 ms | — |
| Java ESM 生成 | 3 ms | — |
| Node 模块加载 | — | 13.7 ms |
| Node transform | — | 23.0 ms |

重新执行四组页面状态的行为对照，组件调用、props 与刷新回调相同。这组结果只覆盖 `BalancePanel.ovs` 与 0.2.2，不能作为当前 Node 源码与 Java 的全量或同语法边界对照。Java 独立冷态解析探针分五个 JVM 运行，Parser 构造、生成类初始化与表加载、规则匹配及 CST 构建、分词、语法校验的中位数分别为 217、76、90、10、3 ms。独立探针与全链路采样的初始化顺序不同，两组数据分开解读。样本见 `tmp/node-ovs-recheck-07b0fc92ab3348438842b52d8e6edb9d/cold-published-balance-panel.txt` 与 `current-balance-panel-split.txt`。

### 规则注册与前瞻装配

带追踪的五轮冷态诊断显示，旧生成器为 OVS 的 396 条规则各生成一个注册 lambda。两份输入的注册阶段分别为 85.5 与 103.4 ms，读取 GAST 为 10.9 与 12.5 ms，解析规则引用为 3 与 4 ms，装配前瞻函数为 80 与 81 ms。用同一个 lambda 表达式捕获规则编号，由生成的 `switch` 调用具体规则后，规则注册降到 5.9 与 6.9 ms。关闭 JIT 后，注册也从 164.9 与 174.5 ms 降到 12.1 与 12.5 ms。实验保持规则编号、规则声明与语法数据相同。正式生成器仅在使用构建期录制语法的产物中采用编号分派。

关闭追踪后，交替运行新旧正式 JAR，每个版本、每份文件各启动七个 JVM，零预热。计时包含 Parser 初始化到首次返回 CST。

| 文件 | 初始化，旧 → 新 | 初始化后 `Program()`，旧 → 新 | 首次解析，旧 → 新 |
|---|---:|---:|---:|
| `BalancePanel.ovs` | 345.6 → 243.5 ms | 113.7 → 136.1 ms | 459.3 → 377.3 ms |
| `CardHeading.ovs` | 355.3 → 247.7 ms | 153.4 → 160.9 ms | 508.6 → 419.3 ms |

两份文件的 ESM 哈希一致。73 个业务及登记 OVS 文件的 CST、位置、Token、诊断和结果状态指纹一致。正式 Qin Parser 构建四项 smoke 通过。OVS 目录 63 个通过、1 个预期拒绝，CSSTS 目录 20 个通过。`balance-monitoring` 的 `qin.bat build` 通过并产出服务类与前端资源。样本见 `tmp/ovs-constructor-fdfbf51f04f84f47a2089b7f95c63cf6/packaged-cold.txt`。前瞻函数装配仍是构造阶段的主要成本之一，后续优化需验证 token 分类、动态 gate 与相同 CST/诊断。

前瞻装配探针对 485 个决策分别累计状态扫描与函数构建耗时。新 JVM 各五轮，默认模式下两份 OVS 业务文件的状态扫描中位数为 30.4、31.2 ms，函数构建为 26.5、29.4 ms。`-Xint` 下状态扫描为 205.8、206.8 ms，函数构建为 77.8、76.4 ms。状态扫描主要遍历预计算路径，去重 token 类型并收集实际 token 索引和类别，以区分可复用的函数。

直接按前瞻表装配的诊断原型绕开了运行时 GAST 决策遍历。关闭追踪后每组九个新 JVM，`BalancePanel.ovs` 首次解析从 352.9 降至 347.2 ms，`CardHeading.ovs` 从 411.1 降至 382.0 ms。73 份 OVS 的 CST 和诊断一致。原型尚未建立替代运行时 GAST 与前瞻表逐决策匹配的校验，正式构建保留现有装配。减少缓存键分配的另一原型在 `CardHeading.ovs` 默认 JVM 采样中从 353.2 增至 428.9 ms，已撤回。诊断样本分别位于 `tmp/ovs-lookahead-d51208ecc95a4b99beb638a165a468d4` 的 `strategy-profile.txt`、`direct-untraced-cold.txt` 和 `cache-key-cold.txt`。

标准入口回归从临时项目执行 OVS 单文件和批量编译，验证 render ESM 与映射，执行 CSSTS 编译验证 ESM、CSS、atom 模块及无效输入诊断。两项回归在独立 JVM、无 Node 的 PATH 中通过，类加载日志中未出现 Qin package runner、Qin 解释函数或 Graal polyglot。样本为同一诊断目录的 `Ovs-no-node-classes.log` 和 `Cssts-no-node-classes.log`。

### 前瞻表数据表示实验

当前 OVS 表包含 66,962 条 token 路径、485 个决策和 218,541 个路径引用。第一项原型把每个决策按首次出现顺序去重后的源 token ID 写入表，运行时仍读取实际词汇表的索引和类别。第二项只在加载时用 `int[]` 保存路径引用，沿用现有文件格式。两项原型在 73 个 OVS 文件上均得到相同 CST 与诊断，共同使用上一版正式 JAR 为基线。默认模式每组九个新 JVM，`-Xint` 每组五个。

| 原型 | `BalancePanel.ovs` 默认 / `-Xint` | `CardHeading.ovs` 默认 / `-Xint` |
|---|---:|---:|
| 预存每个决策的源 token ID | 320 → 312 / 1082 → 875 ms | 341 → 330 / 1090 → 953 ms |
| 路径引用按 `int[]` 存储 | 316 → 302 / 1159 → 1051 ms | 338 → 344 / 1176 → 1154 ms |

两项原型均保留在诊断目录。前者默认模式的总收益约 2%–3%，后者在 `CardHeading.ovs` 上默认模式变慢。样本见 `tmp/ovs-lookahead-types-67b753bdd7dc4948b4644ffa313b25f9` 下的 `cold.txt` 和 `primitive-cold.txt`。

### 嵌套渲染控制流

复现了两个 Java OVS visitor 缺陷。渲染循环中的 `try` 被套入第二个响应式回调，`continue` 脱离了目标循环，产物无法通过 JavaScript 语法检查。循环中的 `switch` 经过普通 Slime visitor 后丢失渲染语义。现由 Java visitor 在同一个响应式回调中生成嵌套循环、`switch` 和 `try`，分支中的 render 使用当前 `children`。

既有 Java 编译 smoke 加入 `for` → `try` → `switch` 的回归，旧产物触发断言，修复后通过。分别执行 Java 产物与 Node 发布版 `ovs-compiler` 0.2.2 产物，用相同的组件及响应式 getter 测试替身验证两边都渲染 `[1, 3]`，`finally` 均记录 `[1, 2, 3]`。完整 Node 源码版仍待验证。

复测 Java 编译的 63 个 OVS 和 20 个 CSSTS 产物，全部通过 Node ESM 语法检查，另有一个 OVS 输入按约定拒绝。用 Node 发布版 0.2.2 编译同一批 64 个 OVS 文件时，60 个产物通过 ESM 语法检查，4 个产物分别在 `while`、`for`、`try`、`for` 处被拒绝。目录结果保存在诊断目录的 `node-published-catalog.jsonl`。正式 Qin Parser JAR 构建、`balance-monitoring` 全量构建、Qin 前端源码映射、缓存与热刷新 smoke 均通过。

### Vue 响应式更新

正式 `ovsjs` runtime 的 `defineOvsComponent` 在渲染时调用工厂，工厂返回 VNode 或其他可渲染值。Java 隐式 `view` 原先多生成了一层箭头函数，Vue 将函数转成了文本。Java visitor 现在直接返回末尾表达式，与显式 `return` 一致。AST 回归在旧 visitor 上失败，在新 JAR 上通过。

使用实际 Vue `createRenderer` 与 `ovsjs` runtime 挂载 Java 编译产物，在内存宿主中验证 `view` 随 `ref` 从 `first` 更新到 `second`，嵌套循环与 `try`/`switch` 从 `13` 更新到 `45`，两轮 `finally` 分别记录 `[1, 2, 3]` 和 `[2, 4, 5]`。正式 Java Parser 构建、63 个 OVS 样例加一个预期拒绝、20 个 CSSTS 样例、源码映射 smoke 和 `balance-monitoring` 构建均通过。Node 发布版 0.2.2 在这份 `view` 样例的 `view` token 处停止。样本位于 `tmp/ovs-vue-reactivity-d105bee1f07741df9a8ccc631cbb4713`。

### 渲染临时变量与源码绑定

Java visitor 原先固定生成 `const children = []`，遮蔽了源码里的同名绑定。例子中的 `if (children.length)` 因此读取空的内部数组，挂载后页面为空。现在 visitor 从源 CST 识别可能冲突的名字，包含 Unicode 转义拼写，并在有冲突时选取未使用的内部名称。正式 JAR 产物挂载到 Vue 与 OVS runtime 后渲染出 `source` 和 `reserved` 两段文本。回归同时覆盖源码已占用 `__qinOvsChildren` 的情况。正式 JAR、OVS/CSSTS 样例、源码映射及 `balance-monitoring` 构建均通过。样本位于 `tmp/ovs-render-bindings-fedd502c856b4cecae380f1969e81971`。

### OVS 与 CSSTS 运行时导入别名

Java visitor 曾把从 `ovsjs`、`cssts-ts` 或 `virtual:csstsAtom` 导入的别名视为已绑定的原名称，导致 DSL 生成的引用在运行时找不到变量。现在只有本地名、导出名同时匹配的值导入才会抑制生成该绑定。已有别名与业务引用原样保留。旧 JAR 的样例执行报 `$OvsHtmlTag` 未定义。新 JAR 产物连同实际 OVS、CSSTS runtime 执行成功，验证了四个源码别名、生成的 `div`、样式类和响应式子项。正式 Parser 构建、OVS 与 CSSTS 样例、源码映射和 `balance-monitoring` 构建均通过。样本位于 `tmp/ovs-runtime-alias-33d75ecca1bd4353bb1feb7aa1b90d55`。

### 独立 CSSTS 语法与 CSS 顺序

`.cssts` 标准入口改为直接运行 `CssTsParser`。旧入口调用 `OvsParser`，会把只属于 OVS 的 `export default div { ... }` 当作独立 CSSTS 编译成功。新增回归验证这类语法与不完整的 `css { ... }` 都返回 Java 语法诊断。共享 Java visitor 继续生成 ESM、CSS 和 atom，OVS 中仍通过自己的 Java 语法处理 `css { ... }`。

逐字节对照还复现了默认 CSS 规则的不稳定顺序。旧默认伪类、伪类内部声明和 class group 使用 `Map.of`，同一份源码在 5 个独立 JVM 中产生 4 种结果。现在使用保留插入顺序的只读 Map，对齐 Node 源码默认配置的声明顺序，5 个新 JVM 产生相同结果。固定 CSS 顺序后，20 个 CSSTS 样例更换 Parser 前后的 ESM、CSS、atom 模块和源码映射哈希均相同。记录位于 `tmp/cssts-java-boundary-1563ad62f3994b159d2344e006c9e458`。

使用正式 Parser JAR，交替运行新旧 CSSTS 编译器 class，每组七个独立 JVM。从源码读入内存开始，不预热也不查转换缓存。只编译 `tokens.cssts` 时 421 → 417 ms。先编译 `BalancePanel.ovs` 再编译同一份 CSSTS 时，总计 486 → 604 ms，第二个 CSSTS 阶段 26 → 206 ms。两份语法现在各自初始化生成的 Java Parser，混合项目首次编译会付出第二份语法的初始化成本。原始数据在该目录的 `cold.txt`。正式 Parser 构建、业务项目 `balance-monitoring` 构建、源码映射和缓存指纹测试通过。

### Node 源码版的宿主依赖

重跑当前 Java→TS 工具，发现 390 个源码文件并完成 920 个类的 lowering，输出阶段仍在 `SubhutiDecisionProgram` 的 `java.lang.Thread` 处停止。现有生成包中还复现了 SDK `Stream.allMatch` 对 JS 函数调用 `predicate.test` 的错误。SDK 源码的 `anyMatch`、`allMatch` 和 `noneMatch` 现接受普通函数和 Java predicate 对象，Node 回归覆盖空流与匹配结果。

OVS/CSSTS 源码转换层改用 CST 对象自己的访问器和已有的 AST、集合适配函数，移除对八个 Qin CST/AST/collection 全局函数的直接调用。既有生成器诊断包中，Node 从 `CardHeading.ovs` 读出了部分 CST，真实生成节点与 AST 访问器的定向检查通过。该检查直接解析源码，无需注入 Qin 全局函数或导入 Java CST。完整转换仍失败，`CardHeading.ovs` 生成 `import {} from ;`，`BalancePanel.ovs` 在旧生成的 `isIntegralVariant` 处读到尚未初始化的局部变量。诊断目录为 `tmp/ovs-node-source-73313ea975ad4b9799edb5f8b342411b`。

进一步检查发现，上述 `CardHeading.ovs` 的 CST 只有两个 import，游标仍停在 `export` 后面的 `const`。Node OVS 的 `ExportDeclaration` 现在把 `const` 和上下文关键字 `let` 交给 `LexicalDeclaration`，`var` 仍交给 `VariableStatement`。源码转换入口检查 Parser 失败标志及 EOF。测试同时覆盖尾随 `}` 和已消费到 EOF 的不完整 `export`。生成的 Slime `Literal.raw()` 已在 JS AST 适配器中转换为 emitter 所需字符串。Node 执行生成的 ESM，验证了十六进制数和字符串导出。

用真实文件继续定位时，Java `Supplier` 接收到 TypeScript `StatementList` 的 `undefined`，触发生成的 `RuleExecutionResult/2` 构造失败。JS SDK 的 `Supplier.get()` 现在将无返回值的结果转成 `null`，导出箭头函数的函数体回归通过。这些定向回归使用诊断 bundle，依赖对旧生成代码的静态调用和模式变量遮蔽的临时修正。`CardHeading.ovs` 现在走到第 120 个 token 附近的 `RBrace` 并给出失败诊断。`BalancePanel.ovs` 消费全部 128 个 token 后返回 6 个模块项，但 ESM 仍出现 `import {} from ;`。标准 `tsx tests/test-generated-parser-chain.ts` 在加载旧生成 SDK 时失败，源文件是 `core/runtime.js` 中的 TypeScript `as any`。生成全新对照包、验证真实项目完整输出及冷态对照仍待完成。

SDK 的生成步骤现在使用 Node 的 TypeScript 类型擦除，将当前 24 个源模块输出为可直接加载的 `.js`，保留现有 ESM 导入路径。此步骤属于 Java→JS/TS 包生成。正常 JVM OVS/CSSTS 编译直接使用 Java Parser 与 visitor。`QinJavaSdkJsPackageSmokeTestMain` 通过真实包入口加载含类型标注、断言及相对模块导入的 SDK 样例。新生成的 24 个模块均通过 `node --check`，Node 直接加载主入口和 `tooling` 入口，并验证了 `Supplier.get()` 的返回值。

生成的 Parser 清单现在导出 OVS/CSSTS 使用的 Slime AST 模块，CST 类型和转换器沿用已发布的命名入口。OVS 通过 `cssts-compiler` 公共入口加载 `CssTsParser`。CSSTS 标准构建通过，两个编译器的标准 Node 测试均越过 SDK 语法和模块路径错误，目前在既有 Parser 包的 `RuleVariantKey` 构造处触发 `this.normalizeVariantKey is not a function`。全新 Parser 重建完成 390 个源文件、920 个类的 lowering 后，在发射阶段报告 `java.lang.Thread` 仍未映射。该宿主差异、真实项目转换和同口径冷态性能仍待验收。

前瞻程序每消耗 4096 个 token 调用 `SubhutiPredictionCancellation`。JVM 实现查询当前线程的中断标志并保持原有取消诊断。Java→JS/TS 源码索引对名为 `java` 的源目录优先选取相邻 `java-js` 目录中对应的同名源码；此处的同步 JavaScript 实现没有线程中断标志。JVM 的 5000-token 定向回归验证了有中断时抛出原诊断、保留中断标志，以及清除中断后的正常选择。生成 Parser 的回归还检查取消类的源码来源及生成内容。

Java→TS 已发射 391 个模块并完成 921 个类的 lowering，Node 可执行生成的取消模块。用作者文法类在同一 JVM 做静态审计时，审计用的 Qin Parser 会返回空 CST。把同一批产物交给正式 Java Parser 独立运行原审计，共通过 1,640 个契约调用，旧分类计数为 0。正式 Java Parser 重建通过，`balance-monitoring` 的标准 `qin.bat build` 也再次产出服务器类和前端资源。JVM 源文件由公开构建的 Git 清单发现，新取消类需加入该清单。

Java 源码语义分析现按正在编译的继承链查找嵌套类型，并缓存每个类型所有者的完整作用域。此前把正式 Parser 放在 classpath 前面时，源码继承的 `StatementParams` 和 `StaticGrammarRuntime` 会丢失。用同一进程的正式 JVM Parser 重新执行 Java→TS 生成，391 个模块的生成、原静态审计及包清单写出均通过。回归覆盖源码继承链与类路径中同名已编译类的差异，以及跨类型调用时返回继承嵌套类型的推断。

Java→TS 发射的 `RuleVariantKey` 曾把外围类型的静态方法写成 `this.normalizeVariantKey`。语义分析与 IR 降低现在共享已解析的静态成员所有者，Node 回归验证嵌套类能调用外围静态方法并读取外围静态字段。修复后的 391 个模块通过原静态审计及包生成，Node 直接构造生成的 `RuleVariantKey` 并验证数字、字符串两种变体。使用同一生成包进行 OVS 源码测试时，Node 初始化首先拒绝 Java 模板字符串 token 使用的占有量词 `++`。该问题影响四种模板 token。修复后仍需重跑真实 OVS/CSSTS 文件及冷态性能对照。

模板的正文、普通及私有标识符的尾部和正则字面量扫描使用目标平台的词法常量。JVM 保留原占有量词。Node 模板以单个普通字符为单位重复，避免在未闭合模板中反复划分普通字符段。标识符尾部没有后续约束，正则字面量中的重复项不能耗掉关闭定界符，因此 Node 使用 ECMAScript 的贪婪量词。JVM 改动前后的 121 条正则源码及 flags 全量相同，Java 与生成 Node 词法器对 17 个样例的 token 类型、UTF-16 起止、位置和文本一致，包含 10 万字符的模板与标识符，以及未闭合长模板的拒绝。拒绝的结构化坐标一致，旧生成 Node 异常的 `message` 仍为空。证据目录为 `tmp/qin-node-sdk-package-be06a74aa7fa4432adccff492f4f8c3e/lexical-bfb387b1450f43619f5abc5a16218d47`。

词法初始化通过后，`SlimeJavascriptParser` 初始化语法表时取到空的 `SourceType.MODULE`。生成器原先先执行各类的静态字段初始化，再创建模块内的 enum 常量。当前只含默认常量的 enum 在这些静态初始化前构造常量。Node 回归验证同模块静态字段通过方法读取后声明的嵌套 enum，旧结果为 `false`，修复后读取正确身份。

Node 初始化接着在 TS 函数声明语法的 `seq(body.toArray(Node[]::new))` 处把整个数组放进一个节点。Java→TS 降低现在从构造表达式推断 `var` 的类类型，并在标准集合的 `toArray` 与已确认的静态可变参数签名相遇时展开数组。定向回归覆盖 `var` 局部集合、静态导入、普通数组参数，以及名字相同但返回单个元素的用户方法。Node 直接加载新生成的 `SlimeTSRootDeclarationStaticGrammar`，验证函数声明语法的十个有序子节点。正式 Java Parser 重新打包通过，包中 121 条词法定义与旧包完全一致。

新生成的 Qin Parser 模块已可直接在 Node 中完成初始化。解析 `const answer = 42;` 时发现 ESM 未导入生成代码引用的 `SlimeUnaryExpressionParser`。ESM 生成现在从正在编译的 IR 继承链收集祖先模块。原实现使用 JVM 上已加载的同名类的继承链，正式 Java Parser 和作者文法的类层级不同。标准测试增加了这一实际导入的断言，最新生成的 Node Parser 返回 `Program` 并消费全部 18 个 UTF-16 代码单元。`balance-monitoring` 的 Java 标准构建再次通过。

最新 392 个生成模块通过原静态审计。以实际导出清单绑定工作区依赖、不改写生成模块的 Node bundle 通过 `test-complete-program.ts`。Node 能运行样例生成的 ESM。CST 适配保留生成节点 `getChildren()` 的 Java 集合契约，AST 适配保留可空 `typeParameters`、`returnType` 和 `optional` 的值。新增 AST 断言覆盖 `() => Math.abs(7)` 的表达式体和空类型参数。

同一包从源码完整解析 `BalancePanel.ovs` 的 128 个 token，进入 ESM 生成后失败。生成代码将 Java `List.of(args)` 发射为 `__QinJavaUtilList.of(args)`，该 SDK 的 rest 参数将来源数组保留成一个参数项，生成器收到嵌套数组。普通带参调用也能独立复现。`CardHeading.ovs` 仍在第 120 个 token `RBrace` 拒绝。现有最小复现是 `return div(class = "test") { "yes" }`，对应无属性的 render 测试在此前诊断包中能消费完整文件。两份业务文件尚未产生有效 Node 基准产物，冷启动性能对照继续待验收。最新证据目录为 `tmp/qin-node-sdk-package-be06a74aa7fa4432adccff492f4f8c3e/typed-array-fefb0da7a826499492d2ce072e41538b`。

### 业务文件冷态复验

Java→TS lowering 现在在确认 `List.of` 接收引用类型可变参数数组时发射展开参数。定向测试覆盖引用类型数组、显式元素类型的数组项和基本类型数组项。重新生成的 392 个模块通过静态审计，普通调用的 CST-to-AST 回归确认只有一个实际参数节点。

Node OVS 在渲染表达式后仅遇到合法后缀的起始 token 时才进入 CallExpressionSuffix。原先复用的 gate 只检查 `<` 的歧义，因此也会在 `}` 上请求调用后缀。标识符转换也新增了对 `IdentifierReference → Identifier → IdentifierName` 的读取。定向 OVS 回归通过，`BalancePanel.ovs` 和 `CardHeading.ovs` 分别消费了全部 128、121 个源码 token 并完成代码生成。两份 Node 产物仍有 `import {} from ;`。对应的 CST 缺少 Java visitor 消费的 `ImportClause`、`FromClause/ModuleSpecifier` 和 `ImportSpecifier` 节点，导入绑定下的 `Identifier` 也缺少终端。Node 完整转换仍待语义与性能验收。

Java 的 OVS 和 CSSTS 路由 smoke 均通过。两份业务文件的 Java ESM 通过 `node --check`。每份文件使用五个独立 JDK 25 进程，源码读入后调用标准 Java OVS 转换，无预热和转换缓存。下表为各阶段中位数，单位为毫秒。

| 文件 | Parser 初始化、分词、CST 与语法校验 | CST→AST | ESM 生成 | 首次编译合计 |
|---|---:|---:|---:|---:|
| `BalancePanel.ovs` | 494 | 52 | 6 | 568.5 |
| `CardHeading.ovs` | 540 | 33 | 4 | 596.2 |

另用三个新 JVM 拆分同一生成 Parser。两份文件的 Parser 构造分别约 158、167 ms，生成类和前瞻表初始化约 86、90 ms，实际规则匹配和 CST 构建约 125、132 ms，分词约 10、13 ms。直接探针和公开 Parser 入口生成的 ESM 哈希一致。样本位于 `tmp/qin-node-sdk-package-be06a74aa7fa4432adccff492f4f8c3e/list-factory-b7ae875d7b844b2a81da76ace284e729`。

### 构建期前瞻 trie

2026-11-28 07:55（UTC+8）。原生成路径通过独立的方法引用判断无类别匹配器，身份比较导致 trie 分支未命中。强制在运行时构建 trie 后，总时间分别从 451 增至 520 ms、457 增至 507 ms。正式 Qin 构建现在对 Qin、CSS TS 和 OVS 的固定词汇表在构建期生成 trie，合并具有同样后续匹配的节点，运行时只加载表和绑定 token ID。OVS 的表从 1,776,129 字节降至 97,127 字节。词汇表的 token 索引在每个 JVM 中绑定，谓词继续按语法顺序在当次调用时执行。

旧、新正式 JAR 按交替顺序各运行五个独立 JDK 25 进程，从源码进入 Parser 到首次返回 CST，无预热或转换缓存。耗时为各阶段中位数，单位为毫秒。

| 文件 | 初始化，旧 → 新 | 初始化后 Program，旧 → 新 | 首次解析，旧 → 新 |
|---|---:|---:|---:|
| `BalancePanel.ovs` | 301.2 → 142.2 | 151.2 → 137.4 | 452.4 → 281.6 |
| `CardHeading.ovs` | 287.9 → 124.8 | 195.4 → 174.4 | 483.3 → 300.4 |

两份 ESM 哈希一致，73 份 OVS 的 CST、位置及诊断指纹与原正式 JAR 一致。打包的四项 Parser 回归、Java OVS/CSSTS 无回退回归及 `balance-monitoring` 的 `qin.bat build` 均通过。损坏文件头、截断、附加数据三项拒绝测试通过。原始样本保存于 `tmp/ovs-stable-matcher-5848692655c2489d81f6c30af25ae5d4`。语料语义和 Node 完整性能对照的验收状态仍在原阶段。

### 两份业务文件的 Java／Node 冷态完整转换

2026-11-28 08:07（UTC+8）。Node 对照中，Slime 的模块和属性静态分派曾直接执行子规则的 body，绕过子规则创建 CST 节点的入口。将相关调用改回已声明的规则方法后，导入子句、导入项、模块名、对象字面量和属性名节点恢复。Java→TS 重新生成的 392 个模块通过静态审计。Node 对照还修复了 Java record 字段访问、SDK 的 `String.valueOf`，以及把带后缀的成员表达式或条件表达式缩成单叶子的错误。定向回归实际加载生成的模块，检查导入别名、对象属性、成员访问和条件表达式。

正式 Java JAR 的四项 Parser 回归通过，73 份 OVS 的 CST、位置和诊断指纹与改动前相同，`balance-monitoring` 标准构建通过。对两份业务文件，Java 和 Node 的 ESM 均通过语法检查。用相同的组件替身执行，`BalancePanel.ovs` 的四组加载／错误状态和 `CardHeading.ovs` 的四组配置状态输出一致。

每份文件每种实现各运行五个新进程，交替顺序，从源码读入内存开始，无预热、无转换缓存。Java 使用正式 JDK 25 JAR，Node 24 用当前 OVS 源码和按正式清单组装的 Java→TS Parser。Java 解析列包含初始化和语法校验，Node 解析列包含构造、分词和 CST 规范化。Node 的单独 ESM 校验在计时之外。各列为中位数，单位毫秒。

| 文件 | 路径 | 模块加载 | 解析与 CST | CST→AST | ESM 生成 | 完整转换 |
|---|---|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | Java | 计入各阶段 | 317 | 45 | 5 | 386.5 |
| `BalancePanel.ovs` | Node 源码 | 189.4 | 4,010 | 184 | 3 | 4,382.8 |
| `CardHeading.ovs` | Java | 计入各阶段 | 391 | 34 | 5 | 447.1 |
| `CardHeading.ovs` | Node 源码 | 211.6 | 756 | 420 | 4 | 1,406.1 |

另用三个新 Node 进程分别计时构造和 `parseCompleteProgram`，中位数为 `BalancePanel` 13.5／3,879.2 ms、`CardHeading` 13.9／758.9 ms，分别消费 128、121 个 token。主要差距出现在当前源码对照的语法执行阶段，之后再计 CST 规范化。该测量覆盖当前 OVS 编译链，没有将仅解析的独立 Node Chevrotain 用例与完整编译混合。两条语法执行路径和 CST 边界仍有差异，待以同一 CST 输入进一步对齐转换阶段，并扩大到完整 OVS／CSSTS 语料及错误对照。原始数据和组件执行脚本在 `tmp/qin-node-sdk-package-be06a74aa7fa4432adccff492f4f8c3e/property-cst-ea6e8a2c1ef94fbfb4eff5af748947e4`。

### 同 CST 对照与 OVS 计算属性

2026-11-28 08:15（UTC+8）。Java OVS 语法已接受计算属性，旧 visitor 却把 `[label] = "hello"` 生成成 `[: "hello"`。现在从 `ComputedPropertyName` 取出内部表达式并标记 computed。简写方法也保留 method 标记。旧正式 JAR 复现回归失败，新 JAR 的回归及打包通过。在 Node 的模块测试替身中执行 Java 产物，计算属性解析为 `title`，简写方法返回预期结果且没有普通构造函数的 `prototype`。Java OVS catalog 63 通过、1 预期拒绝，CSSTS 20 项通过，`balance-monitoring` 标准构建通过。

在 Node 对照中，`NoRenderBlock` 原本在已提前返回的 `Statement` 分支之后处理，导致样例 13 遇到直接节点时失败。让直接节点在入口生成 block 后，定向回归和样例 13 的 ESM 语法检查通过。

从正式 Java Parser 导出两份业务文件的 CST、token 和位置。Java 使用该 Parser 产生的原生树，Node 从导出的树还原。每个实现每份文件各五个新进程，首次调用 CST→AST；两边都排除 Parser、模块加载、磁盘读取和 JSON 解码。Node 的 CST 还原另计。生成的两份 ESM 各通过四组组件替身执行。中位数如下，单位为毫秒。

| 文件 | Java CST→AST | Node CST 还原 | Node CST→AST | Java／Node ESM |
|---|---:|---:|---:|---:|
| `BalancePanel.ovs` | 48.0 | 8 | 24 | 5.9／2 |
| `CardHeading.ovs` | 33.7 | 9 | 424 | 6.6／4 |

`CardHeading` 的 Node CPU 采样中，字段读取、子树遍历和属性重定义函数分别约 189、128、78 ms self time。已转换的节点每次再进入 `normalizeGeneratedAst` 都会重扫子树。直接返回已转换节点后，另做五轮新进程交替 A/B，`BalancePanel` 的 Node CST→AST 从 25 降至 20 ms，`CardHeading` 从 401 降至 32 ms。两份生成 ESM 的字节哈希均保持一致。用独立进程运行 64 份 OVS 和 20 份 CSSTS 样例，改动前后每例状态和成功产物的 ESM 哈希一致。当前 Node OVS 54 份通过语法检查、6 份转换失败、3 份输出语法错误、1 份在 12.5 秒内未完成。Node CSSTS 14 份通过语法检查、6 份转换失败。原始数据位于 `tmp/qin-node-sdk-package-be06a74aa7fa4432adccff492f4f8c3e/property-cst-ea6e8a2c1ef94fbfb4eff5af748947e4`。

CSSTS 定向对照发现，声明 `const colorRed = existing` 后在 `css { colorRed }` 中引用，当前 Java 和 Node 源码版都输出了 `csstsAtom.colorRed`，与 CSSTS 的作用域规则相冲突。Java 对 `css { (colorRed) }` 则保留成普通标识符，缺少对括号表达式内部 atom 的识别。两项都是待验收的语义差异。

### Java CSSTS 绑定与普通语句回归

2026-11-28。Java 转换器在遇到第一个 `css {}` 时建立作用域索引，根据导入、变量声明、解构、参数和循环等实际值绑定解析同名 atom。括号中的引用按同样的规则处理。普通函数的块按语句列表分派给 Slime visitor，保留嵌套声明和循环；渲染体内的循环由既有反应式转换入口处理。

正式 Parser JAR 通过四项构建回归、OVS/CSSTS 编译入口无回退检查、源码映射回归、63 个 OVS 正例和 1 个预期拒绝，以及 20 个 CSSTS 样例。生成 ESM 的执行检查覆盖导入值、局部遮蔽、括号 atom、空循环、两次迭代和嵌套函数调用。`balance-monitoring` 的 `qin.bat build` 通过，产出 JVM 服务类和前端资源。

每份业务文件按轮换顺序在旧 JAR、新 JAR 和 Node OVS 源码对照上各运行五个新进程。计时从读取源码之后开始，执行首次转换，无预热或转换缓存。Java 旧、新 JAR 输出的 ESM 哈希一致。表中为阶段中位数，单位毫秒。Node 模块加载计入总耗时，Java 对应的首次类加载分布在各阶段。

| 文件 | 路径 | 解析与 CST | CST→AST | ESM 生成 | 首次完整转换 |
|---|---|---:|---:|---:|---:|
| `BalancePanel.ovs` | Java 旧 JAR | 314 | 38 | 4 | 381.4 |
| `BalancePanel.ovs` | Java 新 JAR | 287 | 35 | 3 | 346.6 |
| `BalancePanel.ovs` | Node OVS 源码 | 4261 | 213 | 4 | 4688.8 |
| `CardHeading.ovs` | Java 旧 JAR | 371 | 32 | 5 | 433.6 |
| `CardHeading.ovs` | Java 新 JAR | 367 | 31 | 5 | 428.2 |
| `CardHeading.ovs` | Node OVS 源码 | 733 | 30 | 4 | 1020.6 |

Java 首次转换的最大耗时仍在解析阶段，该阶段包含 Parser 初始化、分词、匹配、CST 构造和语法校验。Java／Node 表中测的是当前两个完整编译入口，Node 使用源码版 OVS Parser，不能据此量化语言本身的差异。样本见此前诊断目录下的 `block-visitor-trial/cold-final.csv`。

### 类型导入与 ESM

Java Slime 文法将 `type as colorRed` 识别为导出名为 `type` 的值导入，并区分 `type Shade as colorBlue`、`type as` 与 `type as as`。AST 转换从实际包含 token 的 `ImportDeclaration` 读取 `type`，保留供分析使用的类型标记。OVS/CSSTS 的文件 AST 在生成 ESM 前删除类型导入及混合导入中的类型项，保留默认和值导入、显式空导入和副作用导入。

正式 JAR 的语法和 AST 回归、OVS/CSSTS 编译回归、无回退和源码映射检查通过。生成的 CSSTS ESM 只链接提供值导出的模拟模块，执行时确认两个显式副作用导入仍生效，类型别名仍按 CSS atom 规则解析。重新生成的 63 个 OVS 和 20 个 CSSTS ESM 与修复前逐文件字节一致，并通过 ESM 语法检查。1 个预期拒绝的诊断也相同。`balance-monitoring` 标准 `qin.bat build` 通过。

Node 源码版 CSSTS 对照仍有独立差异。`CssAtomList` 只消费标识符，三个含展开、条件或箭头函数的现有样例只解析部分输入并得到空产物，最后才报告缺少 token 映射。

### 类型导出与前端模块

Java AST 记录 `export type *` 和 `export type * as NS` 的类型标记，Slime 的 TypeScript 源码生成保留该标记。OVS/CSSTS 的运行时模块在生成 ESM 前删除纯类型导出、混合导出中的类型项和模块级接口／类型别名。普通重导出和显式 `export {} from` 保留。只有类型导出的 OVS 渲染模块仍可生成隐式默认组件。运行时链接检查会拒绝经由类型星号或类型命名空间导入值。

正式 JAR 的 Parser、OVS/CSSTS 编译、无回退、源码映射和 ESM 链接回归通过。测试执行 Java 生成的 ESM，验证星号、命名空间及值重导出，验证 `export {} from` 的副作用，以及类型模块未被加载。默认导入的 OVS/CSSTS 测试项目通过 Qin 的开发和生产前端入口，`balance-monitoring` 标准 `qin.bat build` 通过。63 个 OVS 和 20 个 CSSTS 样例的 ESM 与前一版字节一致，1 个预期拒绝的诊断一致。

当时的定向探针还发现，函数签名和函数体内的类型声明会进入 ESM。后续修复见 JavaScript 生成小节。

### 前端链接使用 Java 编译产物

Qin 前端使用 Java AST 收集生产和开发实际发出的 OVS／CSSTS 模块绑定。CSSTS 在服务创建时检查，OVS 在按需或批量编译完成时检查。经过星号转发的 OVS 绑定会等到对应模块编译后验证。回归覆盖命名导入、解构导出、值重导出、移除导出后的热更新、缺失默认导出，以及无效 OVS 文件只在请求该文件时失败。

改动暴露出 `balance-monitoring` 之前的导出不匹配。主模块默认导入 `AdminDashboard.ovs`，该文件原本仅导出一个命名类。仪表盘现在导出具有 `render` 方法的 Vue 组件，同时提供命名和默认导出。标准 `qin.bat build` 通过。测试加载实际生产入口的整棵 ESM 图，并调用组件渲染，确认生成外层仪表盘和 `balance-panel` 插槽。

独立 JDK 25 进程中，两个 OVS 加一个 CSSTS 的小项目各执行五次冷态创建和生产输出。中位数为修改前 628 ms、修改后 730 ms，创建服务为 506 → 566 ms、写出阶段为 120 → 169 ms。此测试包含新增的 ESM 绑定提取。产物在同一个服务实例中复用，额外成本来自解析生成模块和验证链接，下一步是在 Java 编译时直接保留绑定信息，减少对生成代码的再次解析。样本见诊断目录下的 `compiled-frontend-semantics/cold.csv`。

### 编译时保留模块绑定

OVS／CSSTS 的 Java 编译器直接从用于生成 ESM 的 AST 提取模块绑定。编译缓存保存相对模块名和源位置，前端按当前模块图解析路径。回归在两个不同目录复用同一份 CSSTS 转换结果，第二个目录仍能发现它自己的依赖缺少导出。OVS 挂载使用生成器给出的默认导出和顶层表达式偏移，字符串中的 `export default` 不会影响挂载，带 CSS 的 OVS 也不会重复导入 `cssts`。前端无需再解析生成的 ESM。

正式 Parser JAR、无回退、按需 OVS、源码映射、链接和 `balance-monitoring` 标准构建通过，实际仪表盘入口完成 ESM 链接和 Vue VNode 渲染。63 个 OVS 和 20 个 CSSTS 样例的原始 ESM 产物与前版逐字节一致，预期拒绝诊断一致。

同一小项目、同一 JVM 参数重新交替测试，旧版和新版各五个新进程，使用不同源码标记使编译缓存失效。完整时间中位数为 665.5 → 554.1 ms，约少 111 ms（17%）。服务创建 504.5 → 436.7 ms，生产输出 161.7 → 115.8 ms。对照起点是上一版已有链接检查的正式包。样本见 `native-module-bindings/cold.csv`。

### JavaScript 生成时擦除 TypeScript 类型

OVS 和 CSSTS Java 编译器选择 Slime 的 JavaScript 输出模式。Java visitor 保留类型 AST 供模块分析和 TypeScript 源码生成使用。生成 ESM 时省去泛型、参数／返回类型、可选参数标记、`this` 接收者声明、重载签名和无运行时声明，保留函数实现的实参顺序、解构、默认值和运行时表达式。类的接口实现、访问级别和 `abstract`／`override` 标记在 ESM 中擦除。类型断言的运行时表达式保留分组。

旧正式包对带泛型签名的 fixture 输出了 JavaScript 无法解析的代码。新包执行了对应 ESM，验证条件 CSS atom、解构和 rest 参数、默认参数、`this` 接收者、重载实现、泛型调用和 OVS 类渲染。源码映射回归核对了擦除后运行时 token 的原始行列。正式 JAR 和 `balance-monitoring` 标准构建通过，63 份 OVS 和 20 份 CSSTS 样例的原始 ESM 逐字节一致，预期拒绝诊断一致。

两个 OVS 加一个 CSSTS 的小项目分两轮交替测试，旧版和新版各十个新 JVM 进程，转换和链接缓存均未命中。完整时间中位数为 540 → 561 ms，其中三模块 ESM 生成阶段合计为 3 → 4 ms。第二轮的完整时间中位数为 541 → 543 ms，两轮差异说明整链时延还受其他阶段的波动影响。样本见 `native-type-erasure/cold-combined.csv`。

此前复现的构造器参数属性、枚举和尖括号断言问题已分别在下文处理。

### 构造器参数属性

Java visitor 保留构造器参数的访问修饰符、`readonly`、可选标记和默认值。JavaScript 输出模式从这些参数生成实例字段。基类在执行原构造器语句前赋值，派生类在实际执行的 `super()` 后赋值，使用原构造器参数绑定。TypeScript 输出模式保留参数属性声明。

正式 JAR 的定向测试覆盖默认值、可选属性、派生构造器两条 `super()` 分支、参数遮蔽、同名辅助变量及源码位置。实际执行 Java 生成的 ESM，与等价的 TypeScript 参考实现比较了四组调用的字段值和属性描述符。63 个 OVS 和 20 个 CSSTS 样例的 ESM 与前版逐字节一致，预期拒绝诊断一致。`balance-monitoring` 标准构建和生产仪表盘链接、渲染通过。

同一小项目交替各测五个新 JVM 进程，完整冷态中位数为 547 → 564 ms，生产输出阶段为 115.5 → 115.1 ms。该样例未使用参数属性，衡量普通输入上的变化。样本见 `native-parameter-properties/cold.csv`。独立探针中，`public override` 组合仍被现有文法拒绝。

### 枚举的 JavaScript 生成

Java 生成器将枚举输出为 ESM 可执行的运行时对象，支持数值自动编号和反向映射、字符串常量、同枚举成员引用、运行时初始化表达式以及局部同名枚举。合并的枚举声明共用一个对象和导出绑定。环境声明仅保留在类型 AST 中。按独立模块编译的方式保留 `const enum` 对象。

执行正式 JAR 发出的 ESM，与 TypeScript 独立模块参考产物对比了导出、枚举属性描述符、局部遮蔽和初始化调用次数。正式 Parser 构建、源码映射、无回退检查、`balance-monitoring` 标准构建和生产仪表盘渲染通过。63 个 OVS 和 20 个 CSSTS 样例产物逐字节一致，1 个预期拒绝的诊断一致。本轮覆盖了同枚举的常量引用和常见计算表达式，跨枚举常量传播及复杂嵌套表达式作用域仍需单独验收。

### 尖括号类型断言

旧正式 JAR 把 `<number>value` 选为泛型箭头函数，在分号处要求 `=>`。Java Chevrotain 现在检查尖括号后的参数和箭头，保留带默认类型参数和返回类型的泛型箭头函数。Qin 的构建直接编译当前语言前瞻源码，并把它纳入缓存指纹。Java AST 保存断言类型、操作数和源码范围，JavaScript 生成器只输出带分组的运行时操作数。CSSTS 继续在断言内部识别 CSS atom。

正式构建 `chevrotain-7ptj7mrf` 通过四项 Parser 回归。OVS／CSSTS 编译、无回退和源码映射检查通过。测试执行 Java 生成的 ESM，确认断言对运行时值无影响，保留一元运算及括号优先级，且操作数函数只调用一次。63 个 OVS 和 20 个 CSSTS 样例的 ESM 产物逐文件字节一致，1 个预期拒绝的诊断一致。`balance-monitoring` 标准构建成功，生成的仪表盘模块图可以链接并渲染 Vue VNode。

同一文件交替运行新旧 JAR，两轮各五个新 JVM 进程，无预热或转换缓存。第一轮完整转换中位数，`BalancePanel.ovs` 为 345.3 → 337.7 ms，`CardHeading.ovs` 为 366.7 → 426.0 ms。第二轮分别为 336.3 → 332.6 ms、367.4 → 363.7 ms。`CardHeading.ovs` 的第一轮差异在第二轮未复现。第二轮新 JAR 的解析与 CST 为 265 和 302 ms，CST→AST 为 36 和 28 ms，ESM 生成为 4 和 3 ms。两轮产物哈希一致。样本位于此前诊断目录下的 `native-angle-assertions/cold-first.csv` 和 `native-angle-assertions/cold.csv`。

### 冷态解析的阶段归因

2026-11-28 09:24（UTC+8）。使用正式 `chevrotain-7ptj7mrf` JAR 与 `Chevrotain-test` 的 Node Chevrotain OVS Parser，对三份原始业务文件随机顺序各运行七个新进程。计时从读完同一份源码开始，无预热。Java 正式 JAR 测构造和 Program 总时间，额外用只插入阶段计时的临时 class 覆盖分词、输入适配、匹配和校验。Node 模块载入另计，构造包含语法自分析及一次空输入分词，随后加载实际文件。两边各文件可见 token 数一致，CST 结构和语法校验范围有差异。

| 文件 | Java 构造（正式 JAR） | Java 首次 Program（正式 JAR） | Java 分词 | Java 输入适配 | Java 匹配与 CST | Java 语法校验 | Node 模块加载 | Node 构造 | Node 加载输入 | Node 匹配与 CST |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | 96.19 | 112.61 | 7.92 | 2.16 | 93.79 | 3.04 | 50.17 | 798.49 | 0.75 | 2.75 |
| `CardHeading.ovs` | 93.16 | 140.37 | 9.18 | 2.31 | 114.93 | 16.86 | 47.15 | 769.87 | 0.83 | 3.50 |
| `BalanceRow.ovs` | 96.59 | 191.43 | 12.43 | 2.70 | 152.98 | 22.19 | 51.00 | 847.59 | 1.33 | 10.51 |

表中为毫秒中位数。逐进程总时间的中位数为 Java 209.39、234.32、289.70 ms，Node 845.44、821.32、908.89 ms。临时 class 覆盖改变了 JVM 读取生成类的方式，其构造耗时中位数为 163–171 ms，故保留正式 JAR 构造值作为总时间基线。探针中加载与安装预录语法分别约 7 和 23 ms。空值合并校验另占 0.29–1.12 ms，结果与节点计数约占 1.43–1.80 ms。各列独立取中位数。

启用内部采样后，Java 三份文件的 token 与规则节点构造合计约 6.6、6.7、13.8 ms。Java CST 分别生成 552、591、1,999 个规则节点，Node 分别为 320、427、1,329。独立的新进程类加载日志显示，首次 `Program` 加载 350、491、589 个类，其中由生成 Parser 的 lambda 站点产生的类分别为 263、361、451 个，同一进程第二次 Program 窗口内三个用例均为零。关闭 JIT 后，用新的每文件 Parser 对象、同一个共享后端重跑相同输入，匹配与 CST 第一次／第二次为 185.1／27.4、234.8／32.0、365.9／119.2 ms。这确认首次开销在无 JIT 时依然存在。类数和关闭 JIT 的前后差值不能换算成 lambda 链接的独立耗时。

待验证的优化是让生成器用共享的 Java 控制流执行分支，减少每个选择点、可选项和重复项在首次执行时创建的回调站点。验证应从单文件和同一份生成语法开始，逐一核对 GATE 求值和动作执行顺序、CST、错误诊断和恢复行为，再比较新进程的实际 Program 耗时。此次归因未修改生产代码，验收覆盖率保持原状态。样本及探针位于 `qin/build/chevrotain-runtime-budget-probe/cold-attribution-20261128`。

### 生成 Parser 复用分支回调

2026-11-28 09:30（UTC+8）。Qin 的预录语法生成路径为选择分支、门控、可选项和重复项生成共享 Java 回调与按规则编号分派。回调捕获当前规则的 CST frame、参数及录制状态，继续调用原有 Chevrotain 判定与诊断逻辑。带有序空分支的可选项保留返回值，用于判断是否执行空分支的谓词。

正式 JAR `chevrotain-nitctnj5` 的 Parser 回归、OVS／CSSTS 编译、无回退、源码映射检查通过。新旧 JAR 对 96 个文件的有序 CST 字段、解析结果和诊断摘要逐项一致，其中包含一个预期拒绝。63 个 OVS 和 20 个 CSSTS 文件的 ESM 与上一版逐字节一致。`balance-monitoring` 标准构建和仪表盘 ESM 链接、Vue VNode 渲染通过。专门的恢复及语义动作对照尚未执行。

每份文件的新旧正式 JAR 各在七个新 JVM 中执行首次解析，从读完源码开始计时。下表为中位数，单位毫秒。

| 文件 | 首次 Program 前 → 后 | 构造与首次 Program 前 → 后 | 首次 Program 加载类数前 → 后 |
|---|---:|---:|---:|
| `BalancePanel.ovs` | 110.54 → 45.09 | 213.39 → 148.14 | 350 → 62 |
| `CardHeading.ovs` | 147.55 → 61.78 | 241.65 → 166.91 | 491 → 104 |
| `BalanceRow.ovs` | 211.51 → 96.72 | 315.44 → 200.43 | 589 → 111 |

三个用例首次 Program 窗口内由生成 Parser 本身加载的 lambda 类从 263、361、451 降为零。样本及工具位于 `qin/build/chevrotain-runtime-budget-probe/indexed-callbacks`。完整 Node 行为、错误和阶段验收仍在进行。

### 预录语法回归与 CSSTS 类型断言

2026-11-28。将现有参数与动作、错误恢复、可空分支测试的夹具分别生成成捕获版和预录版，使用捕获版生成的前瞻表与语法记录运行预录版断言。共 29 个用例、49 项检查通过，覆盖参数传递、动作提交顺序、失败时不提交动作、单 token 插入和删除、错误后复用、空分支门控及重复分支。复现脚本和结果位于 `qin/build/chevrotain-runtime-budget-probe/indexed-callbacks/verify-recorded.py` 和 `recorded-proofs.log`。

Java CSSTS visitor 现在在 `value as Type` 内也按原作用域转换样式 atom，与 `<Type>value` 一致。未绑定的 atom 进入生成的 CSS 和 atom 模块，局部同名值继续按普通变量引用。正式构建 `chevrotain-tptpx0ye` 通过。执行生成的 ESM 验证了三项 atom 的值，并核对输出的 CSS。63 个 OVS 和 20 个 CSSTS 样例 ESM 与上一版逐字节一致，预期拒绝诊断一致。余额监控标准构建与仪表盘 ESM 链接、渲染通过。完整 Node 行为和错误诊断对照仍待验收。

### 构造器参数属性的 override 修饰符

2026-11-28 09:44（UTC+8）。旧正式 JAR 拒绝 `constructor(public override readonly id: string)`。Java Slime 文法现在识别带访问级别的 `override` 和单独的 `override`，并根据后续 token 区分修饰符与参数名。TypeScript 输出保留修饰符，ESM 按既有参数属性逻辑在 `super()` 后初始化实例字段。

正式构建 `chevrotain-w2j5r1h1`、OVS／CSSTS 编译和源码映射回归通过。对带访问级别和单独 `override` 的两种写法，分别执行 Java 生成的 ESM 并与 TypeScript 对比三组调用的实例属性值和描述符。普通参数名 `readonly` 及带访问级别的属性名 `override` 也包含在对照中。96 份语料的 CST 指纹、解析状态和诊断一致。63 份 OVS、20 份 CSSTS 的 ESM 与上一版逐字节一致，预期拒绝诊断一致。余额监控标准构建和仪表盘渲染通过。完整 TypeScript 类型检查和 Node OVS／CSSTS 行为对照仍有待验收。

### 跨枚举常量与词法遮蔽

2026-11-28 09:50（UTC+8）。Java 生成器现在能读取同一文件中已经生成的可见枚举成员，支持点访问和字符串键访问。字符串常量保留正向映射，从外部枚举常量计算出的数值可以继续自动编号。编译时尊重块、函数参数和局部声明的遮蔽，遮蔽后的初始值在运行时读取。

正式构建 `chevrotain-or2q29t0` 和 CSSTS 枚举回归通过。执行生成的 ESM，对比 TypeScript 5.9.3 的完整 Program 编译结果，三个模块枚举和一个读取外部枚举的函数输出相同的属性描述符。另有六种遮蔽情况通过显式运行时断言，覆盖参数、块级声明、函数内提升的 var、catch、for-of 和 switch。该版本的 TypeScript 编译器在这些用例中仍折叠到外层同名枚举，因此这六项记录为有意保留运行时词法绑定的差异。探针位于 `qin/build/chevrotain-runtime-budget-probe/indexed-callbacks/verify-cross-enum-runtime.mjs`。

63 份 OVS 和 20 份 CSSTS 的 ESM 与上一版逐字节一致，预期拒绝诊断一致。`balance-monitoring` 标准构建以及仪表盘 ESM 链接和渲染通过。本轮常量传播范围为同一文件此前生成的枚举，前向引用和跨模块引用仍待验证。

### 枚举引用已声明的 const

2026-11-28 09:59（UTC+8）。旧版把 `const step = 6; enum Example { Start = step, Next }` 的 `Next` 生成成 `undefined`。Java 生成器现在在当前词法作用域记录可静态求出的数值和字符串 `const`。枚举初始化可以读取这些值，支持连续常量声明、字符串拼接和自动编号。函数调用及同名参数仍由生成的 JavaScript 在运行时求值。

正式构建 `chevrotain-1u385p5n` 和 CSSTS 定向回归通过。用 TypeScript 完整 Program 发射的结果对照执行，枚举属性描述符、自动编号、反向映射及参数遮蔽一致。63 份 OVS、20 份 CSSTS 样例 ESM 和预期拒绝诊断均与上一版逐字节一致。`balance-monitoring` 标准构建以及仪表盘 ESM 链接和渲染通过。探针位于 `qin/build/chevrotain-runtime-budget-probe/indexed-callbacks/verify-const-enum.mjs`。前向引用、跨模块常量解析及完整 Node 行为对照继续待验收。

### 命名伪类样式变量

2026-11-28。Java CST visitor 为 `const btn$$hover = css { colorBlue }` 一类声明加入基类 atom。生成器根据默认及项目配置输出对应选择器，多个伪类使用同一基类，重复选择器合并。普通 `cssts.merge` 调用保留原语义。

正式构建 `chevrotain-hqf1qcvn` 和六项编译、无回退及源码映射检查通过。使用真实 CSSTS 浏览器运行库执行生成的 OVS 模块，核对渲染元素收到基类和颜色 class，以及默认和配置的伪类选择器。63 份 OVS、20 份 CSSTS 的 ESM 对照仅有 `17-pseudo-variable` 发生预期变化，预期拒绝诊断一致。`balance-monitoring` 标准构建和仪表盘 ESM 链接、渲染通过。执行探针位于 `qin/build/chevrotain-runtime-budget-probe/indexed-callbacks/verify-pseudo-variant.mjs`。

用该正式 JAR 对两份业务文件各运行五次独立 JVM，源码读入内存后执行首次完整转换，无预热和转换缓存。`BalancePanel.ovs` 的首次转换中位数为 271.9 ms，其中解析与 CST 为 211 ms，CST→AST 为 34 ms，ESM 生成 4 ms，最终整理 12 ms。`CardHeading.ovs` 对应为 279.1、220、28、5、15 ms。分阶段取中位数，另有计时窗口外的首次调用开销。原始数据见 `qin/build/chevrotain-runtime-budget-probe/indexed-callbacks/pseudo-java-cold.log`。本轮测量使用 Java 正式入口，未重跑 Node，也未做修复前后性能 A/B。

### 正式 JAR 的冷态解析细分

2026-11-28。`chevrotain-hqf1qcvn` 对三份业务文件分别运行七个新 JVM。源码读入内存后直接执行首次解析，禁用转换缓存与预热。正式 JAR 的构造和总耗时独立测量。各内部阶段使用相同源码生成的诊断 class 或已有 CST 计时器采样，避免把插桩的构造开销计入正式包的时间。下表各列为中位数，单位毫秒。

| 文件 | 正式包构造 | 正式包解析总计 | 诊断分词 | 会话绑定 | 规则执行含 CST | 其中 CST 节点构建 | 上下文语法校验 |
|---|---:|---:|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | 91.9 | 133.3 | 8.7 | 2.3 | 27.1 | 6.5 | 2.6 |
| `CardHeading.ovs` | 95.8 | 146.7 | 8.9 | 2.2 | 29.2 | 6.7 | 16.7 |
| `BalanceRow.ovs` | 97.2 | 187.0 | 12.4 | 2.7 | 49.6 | 11.7 | 24.0 |

CST 节点构建使用单独开启计数器的进程，不能从另一组的规则执行时间直接扣减。构造仍是三份文件中最大的单项。启动探针中，读取预录 GAST 约 7 ms，安装约 24 ms。该探针把生成类移出 JAR，构造比正式包增加约 75 ms，因此这两项仅反映插桩组内部耗时。仅延迟父类默认词表的原型在相同覆盖 class 的 A/B 中使构造从 102–105 ms 增到 172–176 ms，未采用。

独立 `Chevrotain-test` Node OVS 对照分别消费相同的 128、121、418 个 token，首次构造约 765、796、830 ms，规则执行含其 CST 约 3.5、3.9、6.9 ms。两份语法的 CST 规则节点数分别为 Java 552／591／1999 和 Node 320／427／1329，上下文校验也不同。这组对照展示现有执行路径的差异。原始样本、输入哈希、JAR 哈希和诊断脚本位于 `qin/build/chevrotain-runtime-budget-probe/cold-current-20261128`。

### Parser 初始化与前瞻装配

2026-11-28。在正式 JAR 的副本中放入计时类，测得构造 96–100 ms，与同期正式包的 94–98 ms 接近。按三份业务文件的阶段中位数取范围，前瞻表加载 17.8–19.4 ms，Parser 父类构造及配置 9.5–9.6 ms，生成规则注册 5.3–6.0 ms，预录 GAST 读取 5.9–6.2 ms，安装 22.3–22.8 ms，外围词表及 JVM 初始化等未细分阶段约 34–36 ms。受类加载顺序影响的旧插桩样本保留作对照。

对 97,223 字节的 OVS 前瞻表再插桩，打开资源及读入约 0.9–1.1 ms，内存中的解码与验证一起用 16.8–18.2 ms。以 Java NIO 批量读取整数的原型在三个输入均未降低首次解析时间，因此未采用。原始样本在 `cold-current-20261128/initialization` 与 `cold-current-20261128/packed`。

正式构建 `chevrotain-szc013f3` 为关闭追踪的预录语法直接装配 Java 前瞻函数，沿用同一 GAST 遍历、策略、决策键与前瞻表校验。显式启用追踪仍输出装配阶段、规则和决策名称。对三份业务文件交替采集新旧正式 JAR 各 11 个新 JVM，从读入源码后开始计时，无预热。各列为中位数，单位毫秒。

| 文件 | 构造前 → 后 | 首次完整解析前 → 后 |
|---|---:|---:|
| `BalancePanel.ovs` | 95.15 → 88.42 | 138.28 → 129.47 |
| `CardHeading.ovs` | 92.96 → 90.22 | 144.53 → 145.27 |
| `BalanceRow.ovs` | 92.08 → 89.13 | 178.93 → 175.74 |

正式 Parser 回归、预录语法参数和语义动作、恢复、可空分支的 29 个用例／49 项检查通过。96 份输入的有序 CST 与诊断指纹一致。63 份 OVS 与 20 份 CSSTS 的 ESM 和一项预期拒绝逐字节一致。`balance-monitoring` 的标准构建、仪表盘 ESM 链接与渲染通过。原始冷启动样本在 `cold-current-20261128/untraced-final.csv`。完整的 Node 功能和诊断对照仍待验收。

### CSSTS 生成引用与源码绑定同名

2026-11-28。`export const cssts = ...` 与旧 Java 编译器自动生成的 `cssts` 导入重名，生成模块在 ESM 语法检查阶段被拒绝。函数参数也能遮蔽 `csstsAtom`。Java visitor 现在复用已有的 CSSTS 作用域遍历挑选未占用的运行时导入名，普通 atom 和命名伪类都引用同一组导入。OVS 前端挂载按生成的模块绑定识别对 `cssts-ts` 的值导入，包括别名。

第一轮构建发现，按所有源码声明统一改名会重复导入已有的正确运行库绑定。现在通过 Slime 既有 import AST 区分模块、导出名、本地名以及类型标记，复用正确导入，并保留同名业务导入。正式构建 `chevrotain-agesscek`、五项 OVS／CSSTS 编译及源码映射回归通过。用实际浏览器 CSSTS 运行库执行生成 ESM，验证命名导出、局部参数、默认导入和命名空间导入保留，且生成的 class 正常生效。63 份 OVS、20 份 CSSTS 的 ESM 和一项预期拒绝与前一版逐字节一致。`balance-monitoring` 标准构建与仪表盘渲染通过。

同一源码在保存的 Node 对照包中也能复现 `cssts` 重复声明。该 Node 对照包重跑登记语料，OVS 54 份产物通过 ESM 语法检查，6 份转换失败、3 份生成语法错误、1 份在既有诊断时限内未完成。CSSTS 14 份通过语法检查、6 份转换失败。其中五份报告缺少 token 映射，一份类字段样例报告 AST 类型未定义。原始逐项结果见 `qin/build/chevrotain-runtime-budget-probe/cssts-runtime-bindings`。

### OVS 渲染运行库绑定

2026-11-28。`$OvsHtmlTag` 和 `defineOvsComponent` 与业务顶层声明重名时，旧版会输出无法加载的 ESM。局部参数 `defineReactiveExpression` 也会遮蔽生成的调用。OVS 和 CSSTS 现在共用 Java 源码作用域索引。生成的 HTML 标签、组件和响应式辅助函数使用各自的未占用名称，已有的正确运行库导入继续复用。定向测试覆盖源码已经占用首选别名的情况，并通过开发、生产前端的 ESM 语法与源码映射验证。

正式包 `chevrotain-fts523vr` 通过 Parser、OVS／CSSTS 转换和源码映射回归。用实际 Vue 和 OVS runtime 执行产物，业务导出和局部参数值保持原样，`view` 读取的 `ref` 从 `first` 更新到 `second`。独立尝试由父级重新传入 props 时，测试文本停留在初值，此项仍待确认运行库调用契约和归因。63 份 OVS、20 份 CSSTS 的 ESM 及一项预期拒绝逐字节一致。`balance-monitoring` 标准构建、生产入口链接和仪表盘渲染通过。探针见 `qin/build/chevrotain-runtime-budget-probe/ovs-runtime-bindings`。

### 父组件更新 OVS view 的属性

2026-11-28。手写的 `defineOvsComponent` 和纯 Java 编译出的 `view Display(props)` 均复现父级传入新属性后文字停留在旧值。两者共用的浏览器运行库在 Vue `setup` 中把 attrs 和 props 复制为普通对象，后续渲染沿用旧对象。现在每次调用组件渲染函数时合并当前 attrs 和 props，继续由现有 getter 读取默认插槽。

实际 Vue 渲染对照中，旧包的两种组件都显示旧属性 `first` 和新插槽 `B`，修复后两者均显示 `secondB`，删除属性后显示 `absentB`。运行库回归 `ovs/ovs-runtime/tests/updated-props.mjs` 还覆盖删除后重新添加属性。ESM/CJS 构建和 `balance-monitoring` 标准构建通过，仪表盘的实际生产运行库模块包含修复，主入口可链接并渲染。Java 编译器和浏览器代码生成路径与前一轮相同。

### CSSTS 样式组合展开

2026-11-28。登记样例 `09-spread-in-css` 使用 `css { ...base, fontBold }`，其中 `base` 为普通类对象。旧 Java visitor 直接生成参数展开，浏览器执行时要求对象可迭代。本次将值作为一个参数交给现有 `cssts.merge`，由其按顺序处理对象和嵌套数组，保留最后一个同 CSS 属性的类。

正式包 `chevrotain-xybx795j` 通过 Parser 回归。旧包产物复现运行时异常，新包产物用真实 CSSTS 运行库验证对象与嵌套数组、同属性覆盖和展开表达式只求值一次。83 个 ESM 中仅展开样例的产物改变，预期拒绝诊断一致。`balance-monitoring` 标准构建与仪表盘渲染通过。执行探针在 `qin/build/chevrotain-runtime-budget-probe/cssts-class-spread`。现有 Node 编译器对照仍在展开样例报告缺少 token 映射。

### CSSTS 声明语法的边界

2026-11-28。旧脚手架示例含有 `css colorRed` 和 `css buttonBase = { colorRed }`。CSSTS 架构文档规定样式使用 `css { ... }` 表达式。旧 Java 文法却匹配了裸名称，并把它转换为空的 `cssts.merge()`，导致源文件看似编译成功却没有样式。Java 文法现在要求 `css` 后接 `{` 才进入样式表达式。回归也检查了 `css` 作为普通 JavaScript 标识符的行为。

正式包 `chevrotain-ykq4xw2h` 通过 Parser 和 CSSTS 标准入口回归。83 份登记样例的 ESM 与一项预期拒绝逐字节一致，`balance-monitoring` 标准构建和生产仪表盘渲染通过。

### CSSTS 嵌套数组中的原子样式

2026-11-28。旧 Java visitor 转换顶层样式表达式时会跳过数组元素，使 `css { [colorRed, [colorGreen]] }` 生成未绑定变量。现在递归访问数组及数组展开项，保留空位、源码位置和词法作用域。局部同名变量仍读取业务值。

正式包 `chevrotain-kuk0vqq8` 通过 Parser 和 CSSTS 回归。用真实 CSSTS 运行库执行旧、新产物，确认旧产物遇到未定义的 `colorRed`，新产物保留局部颜色变量，并按顺序合并嵌套数组与展开项。83 份登记样例的 ESM 与一项预期拒绝逐字节一致。`balance-monitoring` 标准构建和生产仪表盘渲染通过。复现脚本位于 `qin/build/chevrotain-runtime-budget-probe/cssts-nested-atoms/verify.mjs`。

### CSSTS 函数调用参数中的原子样式

2026-11-28。旧 Java 转换器直接保留样式块中的函数调用，`css { service.choose?.(colorRed) }` 会在执行时访问未绑定的 `colorRed`。现在访问调用参数中的原子样式，保留业务同名变量、调用目标、可选调用标记和源码位置。

正式包 `chevrotain-mzws4w0i` 通过 Parser 和 CSSTS 回归。用实际 CSSTS 运行库执行旧、新产物，确认旧产物报未定义变量，新产物保留方法接收者、可选链短路和单次调用。83 份登记样例的 ESM 和一项预期拒绝逐字节一致。`balance-monitoring` 标准构建及生产仪表盘渲染通过。复现脚本位于 `qin/build/chevrotain-runtime-budget-probe/cssts-call-atoms/verify.mjs`。

### Node 对照中的 CSSTS 表达式与类字段

2026-11-28。Node 对照的 `CssAtomList` 原来每项只消费标识符，展开、条件等合法样式项会中途停止，最后表现为 token 映射失败。现在通过生成 Parser 的赋值表达式规则读取样式项，转换器处理逻辑和三元表达式中的原子样式。类字段输出的问题来自生成 AST 的访问器未转换为值，对照适配补齐了 `superClass`、字段类型注解、访问级别、静态和只读标记。

使用工作区源码和已有的生成 Node Parser 构建对照包，20 份登记 CSSTS 样例均通过 ESM 语法检查，原对照为 14 份。使用同一个 CSSTS 运行库执行 Java 与 Node 的展开、条件、三元和类字段四份产物，样式对象、atom 表及所核对的 CSS 规则一致。OVS 对照仍为 54 份通过、6 份转换失败、3 份输出语法错误、1 份超时。逐例结果和执行脚本位于 `qin/build/chevrotain-runtime-budget-probe/cssts-node-expressions`。本轮未修改 Java 正式包，尚未验证全部语料的运行行为、诊断及同口径性能。

### Node OVS 对照中的 `let` 与循环

Node OVS lexer 将上下文关键字 `let` 标为 `IdentifierName`。语句入口通过现有绑定形态和同一行约束识别声明，保留换行后的独立表达式。Node OVS visitor 现在展开 `IterationStatement`，把其中的具体循环交给现有转换逻辑。Node AST adapter 读取普通 `for` 的 `update` 节点和 `ForOfStatement` 的布尔 `await`。此前 `while` 会在生成 ESM 时丢失，普通 `for…of` 则在展开循环后暴露出把访问器函数当作真值的错误。

执行 `test-complete-program.ts` 生成的 ESM，确认 `while` 的结果由 10 恢复到 12，普通 `for`、`for…of` 和 `for await…of` 合计结果为 25，且只生成一个 `for await`。Node OVS 64 份登记样例中 58 份通过 ESM 语法检查，另有模板字符串和 `super` 共 3 份语法错误、数组解构和 `try/catch` 共 2 份转换失败，以及 1 份非标准 OVS props 输入超时。Node CSSTS 的 20 份语法检查均通过。样本在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`。这些结果覆盖普通循环执行和登记样例的语法检查，图形渲染循环的输出和完整行为对照仍待验证。

### 业务文件首次编译复测

使用 Java 正式入口和修复循环后的 Node 源码版对照，每份文件每种实现各运行五个新进程，交替顺序。计时从源码读入内存之后开始，零预热、零转换缓存。Java 的类加载计入首次编译，Node 的模块加载单独列出并计入合计。中位数单位为毫秒。

| 文件 | 实现 | 解析及 CST | CST→AST | ESM 生成 | Node 加载 | 首次编译合计 |
|---|---|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | Java | 248 | 43 | 5 | — | 324.5 |
| `BalancePanel.ovs` | Node | 4953 | 220 | 3 | 217 | 5394.7 |
| `CardHeading.ovs` | Java | 267 | 38 | 6 | — | 350.7 |
| `CardHeading.ovs` | Node | 757 | 40 | 4 | 256 | 1051.7 |

全部 20 份产物通过 ESM 语法检查。单份源码在同一实现的各次输出哈希一致。使用相同的组件和状态替身执行产物，`BalancePanel` 四种状态的组件输出一致，`CardHeading` 四种配置状态的组件输出一致。两条路径的 Parser 和 CST 结构有差别，以上为现有编译入口对照。原始数据见 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/business-cold.csv`。

### 模板字符串、类继承、数组解构和异常处理

Node 对照补齐了 `TemplateLiteral.quasis`、`TemplateElement.value` 和 `try/catch/finally` 的 AST 字段，Slime 的 JS emitter 保留数组解构空位。OVS 转换保留类声明和方法体，JS emitter 在 `return` 与其表达式之间禁止按源码映射插入换行，避免自动分号插入改变返回值。Java Slime 模板字面量转换按 CRLF、单独 CR 和 LF 规范化，覆盖了原先生成给 Node 的 `String.replace` 只替换首个匹配的问题。

Java 正式 Parser 构建 `chevrotain-i49ewgqk` 通过。执行正式 Java 构建和 Node 对照的 ESM，确认继承方法、模板插值、带空位及 rest 的解构、catch/finally 和多行 CRLF 模板值一致。Node 现有对照包覆盖 63 份 OVS 和 20 份 CSSTS 的 ESM 语法检查，`combined/01-export-class.ovs` 仍超时，Java 将它作为非法 OVS props 拒绝。输入和产物在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`。

Node 验证复用已生成对照包，并接入这次由 Java 源码生成的模板转换模块。完整 Java→TS 重新生成还需处理 Java 源解析器不接受 `case null, default`，以及验证探针继续生成时 JS backend 拒绝 `java.lang.Object` 构造。两处阻塞均出现在 Node 对照包生成路径。完整运行行为、诊断与同 CST 性能对照仍待验收。

### 正式 Java 构建与同 CST 冷态复测

使用 `chevrotain-i49ewgqk` 正式包重新检查标准入口。63 份 OVS 和 20 份 CSSTS 产物及一份预期拒绝诊断，与上一正式包保存的 84 份文件逐字节一致。OVS 单文件与批量 Java 入口、CSSTS Java 入口和源码映射回归通过。`balance-monitoring` 标准构建通过，生产前端模块完成链接，仪表盘渲染出的 Vue VNode 包含 `balance-panel` 插槽。

CSSTS 展开样例的检查原先仍要求生成 `...base`。已按 `cssts.merge` 的对象和嵌套数组契约检查参数及其顺序，并使用实际浏览器运行库复验对象、数组、同属性覆盖与单次求值。Java CSSTS 目录 20 份通过。

Node 的 `StatementList` 在子语句失败且游标没有前进时继续循环，导致非法对象 props 样例超时。现在在失败时退出，完整程序入口以 `SyntaxError` 拒绝该样例。63 份正例生成的 ESM 哈希与修复前一致。Java 和 Node 均拒绝非法 `{`，Java 报期望 `RParen` 及源码位置，Node 报 `LBrace` 及 token 索引。

从当前正式 Java Parser 导出同一 CST 和 token 流，Java 读取原生树，Node 从导出数据还原。两份业务文件分别在七个新 JVM 和七个新 Node 进程中执行首次转换，零预热。下表是毫秒中位数。Parser、模块加载、文件读取和 JSON 解码均在表内阶段之外。Node 的 CST 还原单独计时。两份 ESM 分别用四种业务状态执行，Java、Node 组件输出一致。

| 文件 | Java CST→AST | Node CST 还原 | Node CST→AST | Java ESM 生成 | Node ESM 生成 |
|---|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | 37.7 | 7 | 17 | 3.8 | 2 |
| `CardHeading.ovs` | 40.9 | 6 | 31 | 4.1 | 3 |

原始样本在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/shared-current.csv`。此次覆盖两个业务文件的同 CST 转换，完整语料运行行为、诊断和对照包重新生成仍待验收。

### OVS 渲染循环行为对照

Node 对照原先通过通用 Slime visitor 转换循环体，生成的 JavaScript 保留循环，却把 `p { item }` 降成不产生子节点的 `item;`。现在循环体使用 OVS 的语句转换，并在同一个响应式回调内处理嵌套控制流，使 `continue` 与 `break` 仍作用于所在循环。对照适配也识别了正式 Java CST 中的 `BreakableStatement` 包装节点和跳转语句的 `label` 字段。

同一份源文件分别由正式 Java、Node 自己的 Parser，以及消费同一份 Java CST 的 Node 转换入口生成 ESM。使用同一个实际 OVS 运行库和 Vue renderer 挂载三份产物，均只渲染文本为 `2` 和 `3` 的两个段落。完整程序回归通过，Node 登记语料为 63 份 ESM 语法通过和一份预期语法拒绝。复现输入和运行脚本位于 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`。该运行对照覆盖渲染 `for…of` 及其跳转，其他语法和错误仍需逐项对照。

### OVS 渲染分支和异常控制流对照

Node 对照的 `switch` AST 将 `cases` 和各分支的 `consequent` 保留为 Java 集合，导致 ESM 生成失败。现在转换为 JavaScript 数组，并在 `switch` 的各分支及 `try/catch/finally` 块中复用 OVS 语句转换。处于同一渲染循环的分支共用响应式回调，保留 `continue`、`break` 及 `finally` 的执行顺序。

同一份 OVS 源码由正式 Java、Node 独立解析以及 Node 消费 Java CST 三条路径生成 ESM。用实际 OVS 运行库和 Vue renderer 执行时，三个产物均按顺序渲染 `1`、`case`、`2`、`catch`、`3`。Node 完整程序回归通过，63 份 OVS 正例的 ESM 哈希与上轮一致，一份非法 props 按预期拒绝，20 份 CSSTS 通过 ESM 语法检查。复现材料在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`。

### Java OVS 渲染控制流的词法作用域

正式 Java 包复现了渲染循环中的 `ReferenceError`。CST 的 `Statement` 和 `StatementListItem` 包装层被转换为额外的 JavaScript 块，导致 `let label = ...` 与后续的赋值、渲染处于不同作用域。Java visitor 现在展开语法包装层，保留源码中显式的块，让循环、条件、switch 分支及 try/catch/finally 共用这一逻辑。新增 AST 回归检查循环体内的声明、显式嵌套块和后续赋值层级，在旧包失败，在新包通过。

正式构建 `chevrotain-qifn_zmx` 通过。用实际 OVS 运行库和 Vue 执行新 Java 包与 Node 对照生成的 ESM，两边均按顺序渲染 `21`、`31`。此前 `continue`、`break` 和嵌套 `switch/try/finally` 回归再次通过。63 个 OVS 正例、1 个预期拒绝、20 个 CSSTS 样例，以及 Java 入口、源码映射和 `balance-monitoring` 构建与仪表盘渲染通过。9 个受影响的 OVS 产物均通过 ESM 语法检查，其余 75 份产物与上一版逐字节一致。复现材料在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`。

### Java OVS 带标签的渲染循环

旧 Java 包用通用 Slime visitor 处理带标签的循环，保留了 `continue outer` 和 `break outer`，却丢掉了循环内的 OVS 渲染。Java visitor 现在保留标签，并用当前渲染上下文转换其语句体。Node 对照也在同一个响应式回调内处理标签与循环体，保留跳转目标。

正式构建 `chevrotain-yyfdn134` 通过。新增 Java 回归在旧包失败、新包通过。用实际 OVS 运行库及 Vue 执行 Java 与 Node 产物，两边均只渲染 `2`、`3`。Java 的 `#{}` 变体也只渲染这两个值。Java 的 63 份 OVS、20 份 CSSTS 及一份预期拒绝与上一版逐字节一致。Node 完整程序回归与 OVS 目录复验通过。复现材料在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`。

Node 消费同一份正式 Java CST 生成的产物也只渲染 `2`、`3`。新版包通过 OVS 与 CSSTS 的 Java 入口测试、源码映射测试，以及 `balance-monitoring` 的标准构建。构建生成的前端模块完成链接，仪表盘通过 Vue 渲染检查。构建日志为复现目录中的 `label-formal-balance.log`。

### OVS 全语料的同 CST 转换

从正式 Java Parser 导出全部 63 份 OVS 正例的 CST 和 token 流，交给 Node 对照转换。原来有 61 份通过 ESM 语法检查，比较运算符和空值合并两份样例触发了 Node 顶层语句收集器的八条上限。修复后遍历 CST 时按源码顺序追加到同一个语句列表，63 份全部通过。完整程序回归还执行了分组 CST 生成的 ESM，确认十次写入保留了顺序和值。

对原来失败的两份样例，用同一 OVS 运行库和 Vue 执行正式 Java 与 Node 消费 Java CST 的产物，两边分别渲染四个段落，文本输出一致。比较运算符样例的布尔子节点在 Vue 中表现为空文本，此项只验证显示结果。报告位于 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/shared-catalog/report.json`，修复前的结果在同目录的 `before.json`。全语料语法检查之外的完整运行行为与诊断仍待验收。

### OVS 全语料的首次挂载对照

用同一个实际 OVS 运行库和 Vue renderer 执行正式 Java 包 `chevrotain-yyfdn134` 与 Node 消费同一 Java CST 生成的产物。63 份正例的导出名称、首次挂载元素层级、文本与 `console.log` 调用全部一致，差异和执行错误均为零。`13-norender-block` 的外部 `getData` 由两边共用的固定返回值替身提供，报告记录调用。此轮全语料测试覆盖首次挂载，props、事件和响应式更新仍待逐项对照。复现脚本和报告在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`，文件名为 `render-catalog.mjs` 和 `render-catalog-final.json`。

Node 完整程序回归额外执行了前置与后置自增／自减，以及对象展开和属性覆盖，分别断言表达式返回值与最终对象。独立 Node Parser 重新编译目录，63 个 OVS 正例和 20 个 CSSTS 样例通过 ESM 语法检查，另一个 OVS 样例在对象 props 的 `LBrace` 处按预期拒绝。结果见同目录 `independent-ovs-final.jsonl` 和 `independent-cssts-final.jsonl`。本轮 Node 构建使用已有生成 Parser/SDK，发布包重新生成仍待验收。

### 正式 Java Parser 与最新 Node 源码的首次转换

从 `chevrotain-yyfdn134` 导出两份业务文件的 CST 和 token，Java 使用同一 Parser 产生的原生树。每份文件、每个实现各运行七个新进程，交替顺序，零预热、零转换缓存。计时排除 Parser、模块载入、文件读取和 JSON 解码。Node 还原 CST 单独计时。下表为毫秒中位数。

| 文件 | Java CST→AST | Node CST→AST | Node CST 还原 | Java ESM 生成 | Node ESM 生成 |
|---|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | 35.43 | 19 | 7 | 4.17 | 2 |
| `CardHeading.ovs` | 31.85 | 25 | 7 | 3.70 | 4 |

每个文件在同一实现的七次输出哈希一致。两份文件各用四种业务状态执行 Java 和 Node 产物，组件输出一致。用业务项目依赖重新执行 OVS、CSSTS 的 Java 标准入口 smoke，均通过。原始采样在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/cold-yyfdn134-node-final/samples.csv`。

### 命名 view 的 props、插槽和点击事件

Java 与 Node 的 `view` 声明入口现在检查标识符前的换行。行末的普通 `view` 表达式后面可以另起一行读取 `props.label`。旧正式 Java 包在该位置进入组件声明并拒绝输入。新包 `chevrotain-_jxw2egc` 通过新增 Parser smoke、OVS 与 CSSTS Java 标准入口及 `balance-monitoring` 标准构建。63 份 OVS、20 份 CSSTS 的登记产物和一份预期拒绝诊断与前一正式包逐字节一致。

Node 对照原先漏掉普通 `view` 声明的分派，并将命名导出的 `exported` 访问器传给 JavaScript 生成器。修复后，`export { Counter as NamedCounter }` 和普通命名转导出均通过执行测试。Node 的 `view` 工厂直接返回最后一个表达式。已处于该工厂内的复杂 render 使用立即调用函数，避免每次父组件重渲染都定义一个新的 Vue 组件。

同一实际 OVS 运行库和 Vue renderer 执行了正式 Java、Node 独立解析与 Node 消费 Java CST 的三份产物。三条路径均通过按钮身份、标题、属性删除和重新添加、插槽更新、点击事件参数以及由事件更新的 `ref` 文本断言。复现材料为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let` 中的 `InteractiveProps.ovs`、`verify-interactive.mjs` 和 `interactive-vue-verification.log`。

### 独立 Node OVS 的首次挂载对照

将 OVS 目录的 63 份正例分别从 Node 自己的 CST 和 Java CST 生成 ESM，再用实际 Vue 比较首次挂载的元素、文本、宿主 props 和日志。记录函数属性是否存在，事件调用和更新行为由上面的定向测试覆盖。

独立 Node Parser 产生的 `IfStatementBody → Statement → BlockStatement → Block` 内可以直接包含 `Statement`。旧 visitor 递归查找第一个 `StatementList`，误取嵌套标签的子列表，丢失标签及其层级。改用已有的控制流语句体转换，保留真实块边界，8 个样例恢复。最终同 CST 为 63 份一致，独立 Node 为 61 份一致、2 份差异、零执行错误。独立 Node CST 中，`BindingProperty` 的 `PropertyName : SingleNameBinding` 被当成简写，`ArrayLiteral` 的直属 `SpreadElement` 被跳过。报告为同目录 `interactive-render-catalog/shared-formal-final.json` 和 `interactive-render-catalog/independent-formal-final.json`。独立 Node 的 20 份 CSSTS 样例均通过 ESM 语法检查，产物运行语义仍待完整对照。

### 对象解构、数组展开与 CSSTS 运行产物对照

独立 Node 解析的 `BindingProperty` 可以直接包含 `PropertyName : SingleNameBinding`，`ArrayLiteral` 可以直接包含 `SpreadElement`。共用的 Java Slime visitor 现在保留属性原名和绑定别名，并读取直属数组展开项。正式 Java 包 `chevrotain-mpjd6cc0` 通过 Parser 构建、OVS 与 CSSTS Java 标准入口、`balance-monitoring` 构建。与上一包相比，63 份 OVS、20 份 CSSTS 的 ESM 及一份预期拒绝逐字节一致。对照包重新生成两个 Java visitor 模块后，63 份独立 Node OVS 产物均通过 Vue 首次挂载对照，节点、文本、宿主 props 和日志一致。完整程序回归执行了对象解构别名和数组展开。

CSSTS 对照补齐了 `cursorPointer`、已登记的默认伪类和类组合，以及变量绑定入口中的 `$$` 变体。只有直接来自 `css { ... }` 的初始化表达式会加基类 atom。重复伪类选择器只输出一次。用同一个实际 CSSTS runtime 执行两边的 20 份登记产物，所有样例的运行值、显式导出、atom 表、CSS 选择器及其声明一致。四份样例引用的外部样式名称使用固定替身，异步样例以替身记录 `fetch('/api/style')`。CSS 比较保留各规则内声明顺序，选择器按名称排序。

报告和复现脚本在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`，文件为 `flat-cst-independent-report.json`、`flat-cst-cssts-runtime/report.json` 和 `compare-cssts-catalog.mjs`。Node 对照仍使用已有 SDK 与 Parser 基线，附加从 Java 源重新生成的两个转换模块。完整 Java→TS 生成在处理另一个模块时仍报 `JS backend does not support Java interop owner yet: java.lang.Object`，日志为 `flat-cst-generated.log`。

### 全新生成的 Node 对照包与业务文件冷态复测

当前 Slime 的箭头函数前瞻改用显式空值判断，保留原 token 分流行为，并避开 Qin Java 源码解析器暂不支持的组合标签 `case null, default`。Java→TS 后端现在可生成无参 `new Object()`，每次生成独立对象。定向 Node 探针验证了同一对象的相等性与稳定哈希，以及不同对象的引用区分。Java 模块生成失败时的诊断补充源文件路径。

从工作区运行生成探针，使用项目 Java SDK，输出 394 个 TypeScript 模块并通过静态审计。以这批全部重新生成的 Parser 模块运行 Node 对照，63 份 OVS 正例在 Vue 的首次挂载一致，20 份 CSSTS 的运行值、atom 和 CSS 规则一致。正式 Java Parser `chevrotain-y13kw5zn` 通过构建，84 份登记产物及诊断与上一版逐字节一致，`balance-monitoring` 标准构建通过。对照包在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/fresh-workspace-node`。

使用新对照包复测非法对象 props 的登记用例。两边均在同一个 `{` 拒绝输入，Java 给出第 25 行第 16 列及期望的 `RParen`，Node 给出第 87 个 token 的零基索引及 `LBrace`。此证据覆盖该登记负例。

两份业务输入每种方案各跑五个新进程，无预热及转换缓存。完整编译从源码读入内存后开始，Node 的模块加载计入总耗时，Java 首次加载分布在各阶段。表中各列独立取中位数，单位为毫秒。

| 文件 | 实现 | 解析及 CST | CST→AST | ESM 生成 | Node 模块加载 | 完整编译 |
|---|---|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | Java | 226 | 42 | 5 | — | 297.97 |
| `BalancePanel.ovs` | Node | 3618 | 175 | 3 | 205.17 | 4010.87 |
| `CardHeading.ovs` | Java | 250 | 36 | 7 | — | 319.72 |
| `CardHeading.ovs` | Node | 646 | 28 | 3 | 190.54 | 854.72 |

使用相同的 Java 原生 CST 与 token 再测转换时，`BalancePanel.ovs` 的 Java／Node CST→AST 为 37.43／20 ms，`CardHeading.ovs` 为 34.48／31 ms。Node CST 还原另为 7／6 ms，两边计时均排除进程启动、文件读取、模块加载和解析，Java 使用原生树。两份文件各在四组业务状态下执行同 CST 转换的产物，组件输出一致。原始数据为 `fresh-package-cold/samples.csv`，复现脚本为 `measure-fresh-package.py`。这些冷态数据覆盖两份业务文件。

### OVS 与 CSSTS 的错误位置

Java Chevrotain 的合成 EOF token 用 `-1` 表示无源码偏移。公共 Parser 入口现在用源码末尾计算 EOF 诊断的偏移、行和列。Java CSSTS 入口的 CRLF 负例断言第 2 行第 12 列。Node CSSTS 入口在 CST 转换前检查解析状态和剩余 token，语法失败直接抛出带 token 及源码偏移的 `SyntaxError`。Node OVS 也使用同样的位置格式。

正式 Java Parser `chevrotain-6j5m69_8` 和 `balance-monitoring` 标准构建通过，84 份登记产物及原有诊断逐字节一致。使用 17 份负例和一份空文件复验，Java 与 Node 的接受或拒绝结果全部一致。15 份负例的源码偏移一致，其中两个未闭合字符串在两边均报告词法错误。孤立 `@` 的两份输入仍有一字符差异，Node 指向该 token，Java 指向尝试解析装饰器后的 EOF。Node 完整程序回归和 20 份 CSSTS 运行产物对照通过。逐例结果为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/negative-diagnostics/summary.json`。

### CST 换行标记与语法构造范围

Java Chevrotain 已将 `StableTokenInput.Token.lineTerminatorBefore()` 用于语法判断，但构建 token CST 时漏传给 `SubhutiSourceLocation`。修复后重新导出 63 份 OVS 与 20 份 CSSTS 正例。与独立 Node 解析对照，83 份的 token 类型、文本、起始偏移、起始行列及 token 换行标记一致，OVS/CSSTS 专用 CST 规则的名称和源码范围也一致。范围按源码偏移比较，结束列遵循各实现的坐标约定。

AST 计数对照中，20 份 CSSTS 的选定调用及 atom 引用计数一致。63 份 OVS 的 HTML 标签调用计数一致，`defineOvsComponent` 在 63 份存在计数差异，`defineReactiveExpression` 在 54 份存在计数差异。首次挂载对照与定向命名 `view` 交互测试已覆盖前述行为，其他包装差异仍需按响应式更新、副作用、事件和组件身份验证。这一对照的 OVS AST 由 Node 独立解析生成。

正式 Parser `chevrotain-i_2nwxi8`、源码映射 smoke 和 `balance-monitoring` 标准构建通过，84 份登记产物及拒绝诊断与上一版逐字节一致。逐例报告为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/structure-comparison` 中的 `ovs-report.json` 与 `cssts-report.json`。

### 父级更新时的 Vue 实例复用

用同一 Vue renderer 挂载 63 份 OVS 样例的 67 个导出，随后通过响应式父组件添加、修改、删除一个 prop。每步比较宿主节点类型、文本、props、日志及相同路径下的节点引用。原始实现中 54 份样例的节点复用行为不同。旧浏览器运行库每次调用 `defineReactiveExpression` 都创建新的 Vue 组件类型，Java 产物会在 58 份样例中替换节点。共用运行库改为复用组件类型，通过 prop 更新 getter。单独更新运行库后，Java 产物仍有 11 份样例替换节点，定位到复杂渲染块生成的即时 `defineOvsComponent` 调用。

Java 的复杂渲染块复用 `defineReactiveExpression`，生成的 getter 仍负责局部变量、副作用和子节点列表。Node 对照也为延迟求值的渲染块发射相同的运行库调用。修正两侧后，63 份样例逐步比较全部一致，父级传入的探测属性变化未替换任何已观察到的宿主节点。

定向测试另使用接受 props 的命名 `view`，验证复杂块内的 NoRender 日志只在对应输入更新时追加，点击事件保留参数，改变内部 ref 后更新文字，同一按钮保持引用相等。Java 与独立 Node 产物均通过。运行库的 `tests/updated-props.mjs` 验证有状态子组件只挂载一次，旧包失败，新包通过。逐例报告和复现脚本位于 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`，文件为 `reactive-ovs-catalog-node-and-java.json`、`reactive-ovs-catalog.mjs`、`StableInteractive.ovs` 和 `verify-interactive.mjs`。

正式 Java Parser `chevrotain-fvydteau` 通过构建和 OVS 转换回归。重新导出的产物在 63 份登记样例上通过相同的 Vue 更新对照，报告为 `reactive-ovs-catalog-formal.json`。相比旧包，11 份 OVS 的包装发生预期变化，20 份 CSSTS 产物和登记负例诊断保持字节一致。`balance-monitoring` 的标准 `qin.bat build` 与本机 `qin.bat dev` 均通过。在关闭余额采集的 dev 进程中，从 `/app.js` 跟随实际导入到 `BalancePanel.ovs`，确认 ESM、内联源码映射、修正后的浏览器运行库及重复请求内容。Java 标准入口、源码映射及热刷新 smoke 均通过。

### OVS 计算属性、展开与事件方法

新增组合探针 `ConstructProps.ovs` 同时使用 `...defaults`、`[keys[0]] = value`、布尔简写和点击方法。正式 Java 已生成正确的计算属性。修复前 Node 对照在第一个 `[` 拒绝输入，其属性分派只检查第二个 token，AST 属性构造也固定标记为非计算属性。现在 Node 对照识别完整方括号属性名后的 `=` 或 `(`，并保留 AST 的 computed 标记。

实际 Vue 渲染验证 Java 和 Node 的 `id` 来自展开，`title` 被后续计算属性覆盖，`disabled` 保留 `false`，点击处理器收到事件参数 `42`。Java 编译器 smoke、Node 完整程序回归及 63 份样例的父级更新对照均通过。探针、Node 回归日志及逐例报告位于 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`。

### Vue 实例复用修正后的业务文件冷测

使用正式 Java Parser `chevrotain-fvydteau` 和复用 Vue 实例修正后的 Node 对照。每份文件、每种模式各启动五个独立进程，共 40 次，零预热与转换缓存。完整编译从源码读入内存后计时。各列独立取毫秒中位数。

| 文件 | 实现 | 解析及 CST | CST→AST | ESM 生成 | Node 模块加载 | 完整编译 | 同 CST→AST | 同 CST 的 ESM 生成 | Node CST 还原 |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | Java | 232 | 38 | 4 | — | 327.86 | 31.11 | 3.48 | — |
| `BalancePanel.ovs` | Node | 3778 | 180 | 3 | 186.43 | 4127.93 | 17 | 3 | 7 |
| `CardHeading.ovs` | Java | 237 | 35 | 6 | — | 338.41 | 29.72 | 3.38 | — |
| `CardHeading.ovs` | Node | 652 | 30 | 4 | 197.25 | 893.77 | 25 | 3 | 6 |

同 CST 模式复用 Java 原生 CST 及 token。Node 的 CST 还原单列，转换计时排除进程、文件读取、模块加载与解析。相同实现和模式的五次 ESM 哈希一致。原始记录为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/stable-complex-cold/samples.csv`。本轮性能先于后续 Node 计算属性修复采样。

### 隐式组件的顶层渲染顺序

`TopLevelRenderOrder.ovs` 将两个根级按钮与变量声明、赋值交错。旧 Java 和 Node 转换都把赋值表达式收集成渲染输出。Vue 挂载旧 Java 产物时显示 `beforesecondafter`。旧 Node 产物还延迟求值第一个按钮的 props，两个按钮的标题都成了 `second`。

Java 和 Node 的隐式组件转换按源码顺序处理声明、表达式和普通语句。赋值等副作用表达式保留为语句。如果渲染表达式后面还有普通语句，先求值前段渲染结果，再向局部数组追加后续根节点。语句后的连续尾部渲染仍可直接返回。Node 的直接根渲染在渲染体只追加子节点时立即创建根 VNode，子节点自己的响应式求值继续由运行库负责。

正式 Java Parser `chevrotain-0y37erqi` 和 Node 对照生成的模块通过实际 Vue 测试，两个按钮标题依次为 `first`、`second`，文本为 `beforeafter`。Java 编译器回归、Node 完整程序回归和 `balance-monitoring` 标准构建均通过。正式包重新导出的 63 份 OVS 正例、20 份 CSSTS 正例及 1 份预期拒绝诊断与上一版逐字节一致。63 份 OVS 正例的 Java／Node 父级更新对照全部通过。复现输入是 `TopLevelRenderOrder.ovs`，逐例报告是 `top-level-formal-catalog.json`，均位于 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`。

### 项目配置下的 CSSTS 与 OVS 样式产物

用相同的 `classPrefix`、伪类样式覆盖和两级 `classGroup` 编译独立 `.cssts` 与 `.ovs` 内嵌 `css {}`。原 Node 对照丢弃传入配置，且 OVS 入口从另一个 visitor 读取 atom。现在 Node 使用传入配置，识别伪类和组合 atom，并从实际执行转换的 visitor 收集名称。重置编译会话时释放对共享集合的引用，已由调用方累计的 atom 得以保留。

正式 Java 和 Node 都生成 `probe-` 前缀、覆盖后的 `:hover`、嵌套组合规则和对应 atom 模块。独立 CSSTS 的导出值和 `$$hover` 基类一致。实际 Vue SSR 中，两边生成的按钮都带 `probe-display_flex`、`probe-hover` 与 `probe-interactive`。连续编译两个 OVS 模块及一个 CSSTS 模块，共享集合保留全部五个 atom。组合回归、Java CSSTS smoke、63 份 OVS 的 Vue 更新对照及 20 份默认 CSSTS 的运行产物对照通过。

逐例产物在 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let`，报告为 `configured-cssts-report.json`、`configured-ovs-report.json`、`configured-cssts-default-catalog/report.json` 及 `configured-style-ovs-catalog.json`。

### Node 对照中的 `getClass` 枚举行为

使用同一 Vue SSR 探针复现 `Invalid prop … getClass`。Node 对照加载的 Java SDK 以赋值方式在 `Object.prototype` 上安装 Java 反射方法 `getClass`，因此 Vue 使用 `for…in` 枚举组件 props 配置时也会读到这个继承属性。Java SDK 现在通过 `Object.defineProperty` 安装不可枚举的 `getClass`，保留调用行为和原有可写、可配置属性。

使用正式 SDK 打包入口重新生成 Node 对照依赖后，Vue SSR 不再出现该警告，Java 与 Node 的 CSS、atom 和按钮渲染结果仍然一致。生成 SDK 的反射及枚举检查、Node OVS 完整程序回归、Java OVS 单文件与批量入口及 CSSTS 无回退 smoke 均通过。复验日志为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/ssr-warning-fixed.log`。

### JVM 编译入口与当前冷态对照

将探针进程的 PATH 设为仅包含 JDK 可执行文件目录。公共 `QinOvsCompiler` 单文件和批量入口，以及公共 `QinCsstsCompiler` 入口，使用带随机标记的新源码执行实际编译。OVS 批量日志显示两个模块均未命中内存或磁盘缓存。验证了 OVS ESM 和源码映射、CSSTS CSS 和 atom 模块。日志为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/java-only-path.log`。

正式 Java Parser `chevrotain-0y37erqi` 与重新打包的 Node 对照执行 40 次首次编译或首次转换，每组 5 个新进程，零预热。完整编译从源码读入内存后开始，Node 包加载计入完整耗时。同 CST 模式比较首次 CST→AST，Node 的 CST 还原单独计时。各列为独立的毫秒中位数。

| 文件 | 实现 | 解析及 CST | CST→AST | ESM 生成 | Node 包加载 | 完整编译 | 同 CST→AST | 同 CST 的 ESM 生成 | Node CST 还原 |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| `BalancePanel.ovs` | Java | 201 | 34 | 4 | — | 286.03 | 31.44 | 3.78 | — |
| `BalancePanel.ovs` | Node | 3593 | 154 | 2 | 170.79 | 3922.75 | 20 | 3 | 6 |
| `CardHeading.ovs` | Java | 220 | 35 | 6 | — | 311.70 | 29.67 | 3.35 | — |
| `CardHeading.ovs` | Node | 633 | 28 | 3 | 171.20 | 834.05 | 27 | 3 | 8 |

每组的 ESM 哈希一致。采样文件为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/nonenumerable-sdk-cold/samples.csv`。

### 长 props 和循环头的前瞻

Node OVS 的渲染分派曾在第 256 个 token 前停止，普通 `for` 与 `for…in/of` 的分派曾在第 160 个 token 前停止。原对照在两个合法长样例上均返回错误分派。正式 Java 已能生成两份样例的 ESM。Node 现在在匹配分隔符、循环头的顶层分号或到达 EOF 时按语法停止，保留已有分派缓存。未闭合的 props 和循环头探针均返回失败。

用实际 Vue 挂载 Java 和 Node 的长 props 产物，96 个计算属性和值及渲染文本全部一致。96 项解构 `for…of` 的产物在两侧均执行并返回 `95`，分别验证了从调用方传入数组和使用数组字面量。数组字面量的 Node 编译仍明显慢于传参版本，本轮未细分耗时。Node 完整程序回归和 63 份 OVS 样例的 Vue 更新对照通过。报告为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/unbounded-lookahead-ovs-catalog.json`。

### 循环头中的上下文标识符 `of`

旧 Java 和 Node 都会把 `for (let of = 0; of < 2; of++) { … }` 中的绑定名 `of` 当作分隔符。Java 将判断集中在 Slime 循环语法中，让循环头的顶层分号优先选择普通 `for`，并识别成员名和模板的边界。Node 对照判断分隔符前的绑定或表达式，确认分隔符后立即交回普通解析规则，以便模板词法模式在消费右侧表达式时切换。

正式 Java 包 `chevrotain-3sh7ncwb` 通过打包。组合用例覆盖 `let of`、`obj.of`、`obj.in`、`const of of`、`for…in`、`for await…of` 和模板右侧表达式，两端产物执行都得到 `20`。另外检查了 Java 的嵌套模板、包含分号和括号的正则，以及除法。`for (const x of /[;)]/.exec(")")) {}` 在改动前后的 Java 包中都失败，词法阶段把 `of` 后的 `/` 读成除号。这个词法边界仍待修复。

63 份 OVS 正例的首次 Vue 挂载和三次更新结果一致。与上一 Java 包相比，63 份 OVS 产物、1 份预期拒绝诊断和 20 份 CSSTS ESM 逐字节一致。`balance-monitoring` 标准构建、前端源码映射检查、仅含 JDK 的 PATH 下的 OVS 和 CSSTS 编译检查均通过。报告为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/contextual-loop-ovs-report.json`。

使用新包重测，每组 5 个新进程且零预热。完整编译从源码读入内存后开始。Node 同 CST 模式将 CST 还原独立计时。单位为毫秒，表中为中位数。

| 文件 | Java 完整编译 | Node 完整编译 | Java 同 CST→AST | Node 同 CST→AST |
|---|---:|---:|---:|---:|
| `BalancePanel.ovs` | 347.31 | 4820.98 | 82.31 | 23.00 |
| `CardHeading.ovs` | 319.46 | 1004.04 | 85.43 | 26.00 |

本轮另外交错运行新旧 Java 包，使用相同 classpath 布局，每包每文件各 5 次。`BalancePanel` 的旧包／新包同 CST→AST 中位数为 91.24／85.65 ms，`CardHeading` 为 86.23／82.25 ms。这使跨轮增长无法归因于循环修复。当前同 CST 探针将 Java visitor 初始化、CSSTS 选项读取和 AST 遍历计在同一窗口，仍需分别量化。采样文件为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/contextual-loop-cold/samples.csv`。

### 首次 CST→AST 中的默认 CSSTS 选项

分开计时后，`BalancePanel` 和 `CardHeading` 的默认选项准备分别为 57.80 和 57.97 ms，AST 遍历分别为 13.52 和 21.15 ms。空配置原先仍进入静态配置读取器。`QinCsstsJavaCompiler.parseOptions` 现在直接复用不可变的默认选项。对照使用同一 Parser 包，每组交错启动 5 个新 JVM，零预热。

| 文件 | 默认选项准备，修改前 → 修改后 | Java 首次转换窗口，修改前 → 修改后 |
|---|---:|---:|
| `BalancePanel.ovs` | 57.80 → 4.39 ms | 85.36 → 35.43 ms |
| `CardHeading.ovs` | 57.97 → 4.49 ms | 81.60 → 31.26 ms |

以单独编译的 runtime-core 类验证本次修改。默认 CSSTS 编译、OVS 自定义样式选项及配置变更失效 smoke 通过，登记的 84 份产物及诊断逐字节一致。包含实际 `qin.config.js` 的项目采用自定义配置路径，本次空配置的计时收益不能用于估计这类项目的总构建时间。采样文件为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/contextual-loop-cold/java-options-paired.csv`。

### 项目配置与批量 OVS 编译

Qin 的项目入口现在一次读取并解释 `classPrefix`、`pseudoClassConfig` 和 `classGroup`，将解析结果传给该批次各个 OVS 文件。项目入口缺省前缀仍为 `cmp-`，独立 CSSTS 缺省前缀为 `cssts_`。测试使用每轮新源码，验证两个文件的批量结果与单文件编译一致，修改配置后两个文件均重新编译并采用新前缀。

用 `balance-monitoring/app` 的九个 OVS 文件交错运行改动前后各 5 个新 JVM。每次使用新的源码注释确保九个文件均未命中缓存。整批中位数从 738 ms 到 707 ms，样本范围分别为 612–882 ms 与 672–763 ms。改动后，独立批入口首次准备项目样式配置的中位数为 255 ms，随后九个文件的转换和缓存写入中位数为 322 ms。旧版转换计时同时包含配置准备，因此两个转换窗口不能直接相减。样本为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/contextual-loop-cold/configured-batch-paired.csv`。

运行时标准 `qin.bat compile --no-sync` 编译 600 个 Java 文件。随后 `balance-monitoring` 标准 `qin.bat build --profile` 通过，九个 OVS 文件均重新转换。这次标准构建中的 OVS 批次为 171 ms。完整构建此前已解析配置，计时起点与独立批入口不同。单文件、批量、自定义选项、缓存失效、CSSTS 和源码映射测试通过。登记的 84 份产物和诊断逐字节一致。

另用 5 个新 JVM 从内存里的 `balance-monitoring/qin.config.js` 开始拆分独立配置读取。首次构建配置 AST 的中位数为 324.21 ms，之后复用该 AST 提取样式选项为 8.29 ms。样本为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/contextual-loop-cold/config-parse-split.csv`。这组 JVM 使用与标准构建不同的启动参数和初始化顺序，结果与标准构建分开记录。

### 循环右侧的正则字面量

旧包把 `for…of` 和 `for await…of` 分隔符后的斜杠读成除号。正式 Java Parser `chevrotain-by8n181b` 在稳定分词阶段识别循环头的首个顶层分隔符，继续用 JDK 正则模式匹配字面量。测试覆盖直接正则、解构、绑定名 `of`、`obj.of` 和右侧变量 `of` 后的连续除法，以及普通循环头中的一元表达式和除法。生成的 ESM 与 Node 直接执行原始 ECMAScript 用例均输出 `)a2`。

正式打包、词法回归（148 项断言）、仅含 JDK 的编译检查和 `balance-monitoring` 标准构建通过。63 份 OVS 样例的 Vue 挂载及更新对照一致，84 份 OVS/CSSTS 产物及诊断与修复前逐字节一致。样例报告为 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let/contextual-loop-cold/regex-loop-final-ovs-report.json`。独立 Node OVS 编译器在新增用例的第一个正则处仍读到 `Slash` 并拒绝输入，日志为同目录的 `regex-loop-independent-node.log`。

修复前的包和最终包交错各跑 5 次新 JVM，零预热。解析及 CST 中位数在 `BalancePanel` 为 195 → 202 ms，在 `CardHeading` 为 210 → 211 ms。完整编译分别为 309.63 → 310.58 ms 和 323.08 → 325.89 ms。两轮样本范围有重叠，产物哈希一致。样本为同目录的 `regex-java-final-cold-paired.csv`。

### 独立 Node 对照中的正则和变量声明

Node OVS 解析器在语法规则消费 `of` 分隔符后，对右侧首个斜杠启用正则词法模式。生成器现在按正则 AST 的原始文本发射字面量。共享 Java AST visitor 按顶层逗号读取扁平 CST 中的各个变量声明，保留每一项的绑定与初始值。Node 对照从修订后的 Java 源码重新生成了 394 个 TypeScript 模块。

Java 与独立 Node OVS 编译的正则、除法和多变量声明组合用例均输出 `)a2`。Java 正式 Parser `chevrotain-8u_v960k` 与 `balance-monitoring` 标准构建通过。63 份 OVS 样例在 Vue 中的首次挂载及三次更新全部一致，20 份 CSSTS 的运行值、atom 与 CSS 规则全部一致，84 份 Java 产物和诊断与上一版逐字节一致。报告位于 `qin/build/chevrotain-runtime-budget-probe/ovs-contextual-let` 下的 `contextual-loop-cold/regex-parity-ovs-report.json` 与 `regex-parity-cssts/report.json`。

加入以等号开头的正则 `/=/.exec("=")` 后，Node 完整程序回归通过。两个编译器独立生成的扩展用例 ESM 都输出 `)a=2`。标准 dev 入口实际返回 `BalancePanel.ovs` 的 ESM 和内联源码映射，带原始源码，重复请求内容一致。dev 验证关闭了余额采集。

重跑 83 份正例的结构对照，token 名称、文本、起点、换行标记及 OVS/CSSTS 专用 CST 范围全部一致。所选 AST 调用计数在 20 份 CSSTS 和 37 份 OVS 上一致，剩余 26 份 OVS 的差异均为 `defineReactiveExpression` 次数。报告为 `regex-parity-structure/ovs-report.json` 和 `regex-parity-structure/cssts-report.json`。Vue 挂载和三次更新对照还不足以证明这些包装在所有副作用、事件和更新顺序下都等价。

### 命名 view 的副作用顺序

用实际 Vue 挂载含外层 `#{ … }`、子渲染块局部变量和点击事件的命名 `view`。旧 Node 对照在 view 工厂内立即执行复杂子渲染块，因此按钮点击改变子块读取的 ref 时，外层副作用也多执行一次。Java 把复杂块放在独立的响应式组件中。Node 对照移除命名 view 的立即执行特例后，两端在首次挂载、点击、父 props 更新、同一 tick 内同时改变 props 和 ref 时，文字、计数、完整副作用顺序和按钮引用均一致。

新增定向测试验证外层工厂只执行一次，子渲染 getter 可独立重新执行。Node 完整程序回归通过。用修订后的独立 Node 编译器重新编译 63 份样例，实际 Vue 首次挂载及三次父 props 更新全部一致。样例报告和定向运行日志分别为 `contextual-loop-cold/reactive-effect-catalog-report.json` 与 `contextual-loop-cold/reactive-effect-order.log`。

### 控制流内的标签属性与异常

同一份条件渲染用例中，按钮属性读取 `count.value`，分支本身记录 `#{ … }` 副作用。点击修改计数后，Java 会重新执行该分支并计算按钮属性，旧 Node 对照只更新按钮。另一个用例在 `try` 内的按钮属性计算中抛出异常，旧 Node 对照先执行 `finally`，随后让异常逃到 Vue 的错误处理器。

Node 对照现在让控制流回调直接执行仅追加子节点的标签构造，并保持标签子表达式各自已有的 getter。实际 Vue 测试中，点击、父 props 更新和同一 tick 的组合更新，两端副作用顺序和按钮引用一致。抛错用例在两端均渲染 `caught`，依次记录 `throw`、`caught:props`、`finally`。新增完整程序回归验证异常边界。记录在 `contextual-loop-cold/branch-props-before.log`、`branch-props-after.log` 和 `try-props-vue.log`。

重编的 63 份 OVS 产物通过 Vue 首次挂载和三次父级更新对照。83 份 OVS/CSSTS 的 token 和扩展 CST 范围继续一致。所选 AST 调用计数为 38/63 份 OVS 和 20/20 份 CSSTS 一致。仍需逐项验证其他响应式包装和包含局部逻辑的复杂子渲染块。报告为 `contextual-loop-cold/control-flow-props-vue-report.json` 与 `control-flow-props-structure/ovs-report.json`。

### 同 CST 冷测中的 JVM 类查找

用正式 Java 包 `chevrotain-8u_v960k` 拆开同 CST 探针的样式选项阶段。交错启动每组 5 个新 JVM，保持类文件和源码相同，只把现有 runtime-core classes 放到探针 classpath 前面。`QinCsstsJavaCompiler` 的首次查找、加载与链接合计由 `BalancePanel` 的 47.50 ms 降到 0.60 ms，`CardHeading` 由 42.43 ms 降到 0.57 ms。CSS atom 字典初始化约 3 ms。两组实际 AST 遍历分别约 14 ms 和 22–24 ms。探针原顺序把 JVM 查找依赖的时间计进了转换窗口。样本为 `control-flow-props-cold/java-classpath-order.csv`。

用调整后的 classpath 与本轮独立 Node 编译器再交错运行 40 个新进程，每组 5 次，零预热。完整编译从源码读入内存后开始，Node 完整耗时包含包加载。Node 同 CST 的反序列化另计，Java 同 CST 的计时包含 visitor 初始化及默认样式选项准备。各组产物哈希一致。下表为毫秒中位数，样本为 `control-flow-props-cold-runtime-first/samples.csv`。

| 文件 | Java 完整编译 | Node 完整编译 | Java 同 CST→AST | Node 同 CST→AST |
|---|---:|---:|---:|---:|
| `BalancePanel.ovs` | 316.90 | 4235.71 | 39.81 | 23.00 |
| `CardHeading.ovs` | 411.31 | 1013.82 | 30.37 | 32.00 |

## 代码约束


- 输入和输出保持 `SubhutiCst` 与类型化 Slime AST，使用 Java getter、字段、visitor 和 collections。
- OVS/CSSTS 语法转换写在 Java visitor/compiler 中，不能用 TypeScript adapter、Map 语法节点或 Qin interpreter 充当 Java 实现。
- 复用现有 Java Slime transformer/generator；只在 OVS/CSSTS 语义所属层添加 Java 专用行为。
- 不对失败语法提供第二条 TS fallback。
- 每个转换阶段先复用上游产生的 CST/AST，再以 focused tests、项目集成和冷态性能扩大验证。
