# GraalVM Polyglot JavaScript 测试项目

这个项目演示如何使用 Qin 运行 JavaScript 文件，通过 GraalVM Polyglot API 执行。

## 前置条件

- 安装 GraalVM (https://www.graalvm.org/downloads/)
- 设置 `GRAALVM_HOME` 环境变量
- 将 `$GRAALVM_HOME/bin` 添加到 PATH

## 运行示例

### 基本 JavaScript 执行

```bash
# 使用 run 命令
qin run src/hello.js

# 使用简写形式
qin src/hello.js

# 传递参数
qin run src/hello.js -- arg1 arg2
```

### Java 互操作

```bash
qin run src/java-interop.js
```

## 功能演示

### hello.js

- 基本 console.log 输出
- ES6+ 语法（箭头函数、解构、展开运算符、模板字符串）
- 命令行参数访问

### java-interop.js

- 使用 `Java.type()` 获取 Java 类
- 创建和使用 Java 对象（ArrayList, HashMap, StringBuilder）
- 调用 Java 静态方法（System, Math）

## 执行流程

```
qin run xxx.js
    ↓
Qin CLI (TypeScript/Bun)
    ↓
启动 Java 进程 (GraalVM)
    ↓
JsRunner.java (桥接器)
    ↓
Polyglot Context + GraalJS
    ↓
执行 JavaScript 文件
```
