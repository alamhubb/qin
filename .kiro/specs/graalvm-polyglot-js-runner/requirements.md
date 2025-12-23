# Requirements Document

## Introduction

本功能为 Qin 构建工具添加基于 GraalVM Polyglot API 的 JavaScript 执行支持。与现有的 GraalVM Node.js 运行时方案不同，本方案通过 Java 桥接器（JsRunner.java）使用 GraalVM 的 Polyglot API 直接执行 JavaScript 代码，实现更紧密的 Java-JavaScript 互操作。

用户可以通过以下方式运行 JavaScript 文件：
- `qin run xxx.js` - 显式运行命令
- `qin xxx.js` - 简化形式

执行流程：
```
qin run (Bun/TypeScript CLI)
    ↓
启动 Java 进程 (GraalVM)
    ↓
Java 桥接器 (JsRunner.java)
    ↓
Polyglot API → GraalJS 引擎
    ↓
执行 JavaScript 文件
```

## Glossary

- **Qin**: 一个现代化的 Java 构建工具，支持零 XML 配置
- **GraalVM**: Oracle 开发的高性能多语言虚拟机，支持 Java、JavaScript、Python 等多种语言
- **Polyglot API**: GraalVM 提供的多语言互操作 API（org.graalvm.polyglot），允许在 Java 中嵌入和执行其他语言代码
- **GraalJS**: GraalVM 的 JavaScript 引擎实现，通过 Polyglot API 提供 JavaScript 执行能力
- **JsRunner**: Java 桥接器类，负责初始化 Polyglot Context 并执行 JavaScript 文件
- **Context**: Polyglot API 中的执行上下文，用于加载和执行多语言代码
- **Source**: Polyglot API 中表示源代码的对象，可以从文件或字符串创建

## Requirements

### Requirement 1

**User Story:** As a developer, I want to run JavaScript files directly using `qin run xxx.js` or `qin xxx.js`, so that I can execute JavaScript code with GraalVM's Polyglot API.

#### Acceptance Criteria

1. WHEN a user runs `qin run path/to/file.js` THEN the system SHALL execute the JavaScript file using GraalVM Polyglot API
2. WHEN a user runs `qin path/to/file.js` THEN the system SHALL recognize the .js extension and execute the file using GraalVM Polyglot API
3. WHEN the JavaScript file path is relative THEN the system SHALL resolve it relative to the current working directory
4. WHEN the JavaScript file does not exist THEN the system SHALL display a clear error message with the attempted file path

### Requirement 2

**User Story:** As a developer, I want the system to use a Java bridge (JsRunner.java) to execute JavaScript, so that I can leverage tight Java-JavaScript interoperability.

#### Acceptance Criteria

1. WHEN JavaScript execution is requested THEN the system SHALL start a Java process with GraalVM
2. WHEN the Java process starts THEN the system SHALL load and execute the JsRunner class
3. WHEN JsRunner initializes THEN the system SHALL create a Polyglot Context with JavaScript language enabled
4. WHEN JsRunner receives a JavaScript file path THEN the system SHALL load the file as a Polyglot Source and evaluate it
5. WHEN JsRunner executes JavaScript THEN the system SHALL capture and forward console output to the terminal

### Requirement 3

**User Story:** As a developer, I want to pass command line arguments to my JavaScript program, so that I can customize its behavior at runtime.

#### Acceptance Criteria

1. WHEN a user runs `qin run file.js -- arg1 arg2` THEN the system SHALL pass arg1 and arg2 to the JavaScript program
2. WHEN arguments are passed THEN the system SHALL make them available via a global `args` array in JavaScript
3. WHEN no arguments are provided THEN the system SHALL provide an empty args array

### Requirement 4

**User Story:** As a developer, I want clear error messages when JavaScript execution fails, so that I can quickly identify and fix issues.

#### Acceptance Criteria

1. WHEN JavaScript syntax error occurs THEN the system SHALL display the error message with file name, line number, and column
2. WHEN JavaScript runtime error occurs THEN the system SHALL display the error message with stack trace
3. WHEN GraalVM is not available THEN the system SHALL display installation instructions
4. WHEN the JavaScript file cannot be read THEN the system SHALL display a file access error with the path

### Requirement 5

**User Story:** As a developer, I want the JsRunner to support ES modules and modern JavaScript features, so that I can use contemporary JavaScript syntax.

#### Acceptance Criteria

1. WHEN JavaScript code uses ES6+ syntax THEN the system SHALL execute it without errors
2. WHEN JavaScript code uses `import` statements for local modules THEN the system SHALL resolve and load the imported modules
3. WHEN JavaScript code uses `export` statements THEN the system SHALL handle module exports correctly
4. WHEN the Polyglot Context is created THEN the system SHALL enable ECMAScript module support

### Requirement 6

**User Story:** As a developer, I want to access Java classes from my JavaScript code, so that I can leverage existing Java libraries.

#### Acceptance Criteria

1. WHEN JavaScript code uses `Java.type('className')` THEN the system SHALL return the corresponding Java class
2. WHEN JavaScript code creates Java objects THEN the system SHALL instantiate them correctly
3. WHEN JavaScript code calls Java methods THEN the system SHALL invoke them and return results
4. WHEN the Polyglot Context is created THEN the system SHALL enable host class access for Java interop

### Requirement 7

**User Story:** As a developer, I want the JsRunner to be bundled with Qin, so that I can use it without additional setup.

#### Acceptance Criteria

1. WHEN Qin is installed THEN the system SHALL include the compiled JsRunner.class in the distribution
2. WHEN JavaScript execution is requested THEN the system SHALL locate JsRunner from Qin's internal resources
3. WHEN JsRunner is loaded THEN the system SHALL include necessary GraalVM Polyglot dependencies in the classpath

