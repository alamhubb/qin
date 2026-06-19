# CST to AST 转换器架构

本目录包含将 CST (具体语法树) 转换为 AST (抽象语法树) 的实现。

## 核心设计原则

### 1. 单一职责 & 调用链设计

每个模块负责一类节点的转换，形成清晰的调用链：

```
SlimeCstToAstUtil (分发器)
    ↓
各领域转换器 (expressions/, statements/, typescript/, ...)
    ↓
SlimeAstCreateUtils (工厂方法)
```

### 2. SlimeCstToAstUtil 只做分发

`SlimeCstToAstUtils.ts` 是**纯分发器**，不包含任何转换逻辑：
- 根据 CST 节点类型分发到对应的转换器
- 作为各转换器之间的协调中心
- 避免循环依赖

### 3. 逻辑归一，避免重复

相同的逻辑只在**一处**实现：

| 功能 | 唯一实现位置 |
|------|-------------|
| 二元表达式链 | `SlimeBinaryExpressionCstToAst.createBinaryExpressionChain` |
| 后缀操作处理 | `SlimePostfixExpressionCstToAst.processPostfixOperations` |
| 函数解析核心 | `SlimeFunctionDeclarationCstToAst.parseFunctionFromCst` |
| MemberExpression 创建 | `SlimeAstCreateUtils.createMemberExpression` / `createComputedMemberExpression` |

## 目录结构

```
cstToAst/
├── README.md                 # 本文件
├── index.ts                  # 导出入口
├── expressions/              # 表达式转换
│   ├── SlimeExpressionCstToAst.ts       # 表达式入口分发
│   ├── SlimeBinaryExpressionCstToAst.ts # 二元/逻辑表达式
│   ├── SlimeUnaryExpressionCstToAst.ts  # 一元表达式
│   ├── SlimeMemberCallCstToAst.ts       # 成员访问/调用
│   ├── SlimeCallExpressionCstToAst.ts   # 函数调用
│   └── SlimePostfixExpressionCstToAst.ts# 后缀操作统一处理器
├── statements/               # 语句转换
├── function/                 # 函数声明/表达式
│   ├── SlimeFunctionDeclarationCstToAst.ts
│   └── SlimeFunctionExpressionCstToAst.ts
├── class/                    # 类相关
├── module/                   # 模块 import/export
├── identifier/               # 标识符
├── components/               # 公共组件（参数解析等）
└── typescript/               # TypeScript 类型系统
    ├── SlimeTSTypeAnnotationCstToAst.ts  # 类型注解
    ├── SlimeTSPrimaryTypeCstToAst.ts     # 基础类型
    ├── SlimeTSCompositeTypeCstToAst.ts   # 复合类型
    ├── SlimeTSFunctionTypeCstToAst.ts    # 函数类型
    ├── SlimeTSDeclarationCstToAst.ts     # TS 声明
    ├── SlimeTSExpressionCstToAst.ts      # TS 表达式
    └── ...
```

## 设计模式

### 后缀操作统一处理

所有后缀操作（`.prop`, `[expr]`, `()`, `!`, `as T`, `satisfies T`）通过统一入口处理：

```typescript
SlimePostfixExpressionCstToAst.processPostfixOperations(base, children, startIdx, loc)
```

这确保了：
- TypeScript 扩展（`as`/`satisfies`/`!`）只需在一处实现
- 新的后缀操作只需在 `processSinglePostfix` 中添加

### 二元表达式链式处理

所有二元表达式（算术、比较、逻辑等）使用统一的链式构建：

```typescript
SlimeBinaryExpressionCstToAst.createBinaryExpressionChain(cst, 'BinaryExpression' | 'LogicalExpression')
```

### 函数解析核心

函数声明和表达式共享相同的解析逻辑：

```typescript
parseFunctionFromCst(cst) → { id, typeParameters, params, returnType, body, ... }
buildFunctionDeclaration(parsed) → AST
buildFunctionExpression(parsed) → AST
```

## 扩展指南

### 添加新的表达式类型

1. 在对应的转换器中添加方法
2. 在 `SlimeCstToAstUtil` 中添加分发
3. 如果需要新的 AST 节点，在 `SlimeAstCreateUtils` 中添加工厂方法

### 添加新的 TypeScript 类型

1. 在 `typescript/` 目录的对应文件中添加转换方法
2. 在 `SlimeCstToAstUtil` 中添加分发
3. 确保 `createTSTypeAst` 的分发逻辑包含新类型

## 全局可注册模式

`SlimeCstToAstUtil` 支持子类扩展和动态替换，允许自定义转换逻辑。

### 为什么需要这个设计

1. **扩展性** - 子类可以重写任意转换方法，实现自定义逻辑
2. **内部调用也生效** - 注册后，内部各转换器通过 `SlimeCstToAstUtils.xxx()` 的调用也会路由到子类方法
3. **零侵入** - 不需要修改现有调用代码

### 实现方式

使用 ES6 Proxy 拦截属性访问，动态转发到当前注册的实例：

```typescript
let _util: SlimeCstToAst = new SlimeCstToAst()

export function registerSlimeCstToAstUtil(instance: SlimeCstToAst) {
    _util = instance
}

const slimeCstToAstUtil = new Proxy({} as SlimeCstToAst, {
    get(_, prop) {
        const val = (_util as any)[prop]
        return typeof val === 'function' ? val.bind(_util) : val
    }
})

export default slimeCstToAstUtil
```

### 使用方法

```typescript
import { SlimeCstToAst, registerSlimeCstToAstUtil } from 'slime-parser'

// 1. 继承并重写需要的方法
class MyExtendedCstToAst extends SlimeCstToAst {
    constructor() {
        super()
        // 关键：在构造函数中注册当前实例
        registerSlimeCstToAstUtil(this)
    }

    createPrimaryExpressionAst(cst: SubhutiCst): SlimeExpression {
        // 自定义逻辑
        const first = cst.children?.[0]
        if (first?.name === 'CustomExpression') {
            return this.handleCustomExpression(first)
        }
        return super.createPrimaryExpressionAst(cst)
    }
}

// 2. 创建实例时自动注册
const converter = new MyExtendedCstToAst()
const ast = converter.toProgram(cst)
```

### 已使用此机制的项目

| 项目 | 扩展类 | 用途 |
|------|--------|------|
| cssts | `CssTsCstToAst` | 处理 `css { }` 表达式 |
| ovs | `OvsCstToAst` | 处理 OVS DSL 语法 |

## 注意事项

- **永远不要**在 `SlimeCstToAstUtil` 中写转换逻辑
- 相同的逻辑只在**一处**实现，其他地方调用
- 新增方法时检查是否可以复用现有逻辑
- 保持调用链清晰：分发器 → 转换器 → 工厂方法
