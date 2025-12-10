# Requirements Document

## Introduction

本文档定义了一个基于 Bun 运行时的跨语言构建和包管理系统（代号：Qin）的需求规范。该系统的核心目标是：
1. 实现 Java 语言的构建和包管理功能
2. 通过 WebAssembly (WASM) 中间层实现跨语言互操作，允许 JavaScript/TypeScript 调用 Java 方法

系统允许开发者通过统一的命令行接口编译、运行 Java 程序，管理 Maven 依赖，并生成可供其他语言调用的 WASM 模块。

## Glossary

- **Qin**: 本跨语言构建和包管理系统的项目代号
- **JavaBuilder**: 负责编译和运行 Java 源代码的组件
- **JavaPackageManager**: 负责管理 Java 依赖（从 Maven 仓库下载 JAR 文件）的组件
- **WasmBridge**: 负责将 Java 代码编译为 WASM 并提供跨语言调用接口的组件
- **qin.java.json**: Java 项目的配置文件，包含项目元数据和依赖列表
- **Maven Central**: 默认的 Maven 依赖仓库 (https://repo1.maven.org/maven2)
- **Classpath**: Java 运行时查找类文件和 JAR 文件的路径列表
- **Dependency Coordinate**: Maven 依赖的唯一标识，格式为 groupId:artifactId:version
- **WebAssembly (WASM)**: 一种可移植的二进制指令格式，可在多种环境中运行
- **TeaVM**: 将 Java 字节码编译为 JavaScript 或 WebAssembly 的 AOT 编译器
- **Exported Function**: Java 类中的 public 方法，自动可从其他语言调用
- **WASM Module**: 编译后的 WebAssembly 二进制文件 (.wasm)

## Requirements

### Requirement 1

**User Story:** As a developer, I want to compile Java source files using Bun, so that I can build Java projects without relying on traditional build tools like Maven or Gradle.

#### Acceptance Criteria

1. WHEN a user invokes the compile command THEN the JavaBuilder SHALL locate all `.java` files in the configured source directory and compile them to the output directory
2. WHEN the output directory does not exist THEN the JavaBuilder SHALL create the directory before compilation
3. WHEN compilation succeeds THEN the JavaBuilder SHALL display the count of compiled files
4. WHEN compilation fails THEN the JavaBuilder SHALL display the compiler error message and return a failure status
5. IF no Java files exist in the source directory THEN the JavaBuilder SHALL display a "No Java files found" message and return a failure status

### Requirement 2

**User Story:** As a developer, I want to run compiled Java programs through Bun, so that I can execute my Java applications with a single command.

#### Acceptance Criteria

1. WHEN a user invokes the run command with a main class THEN the JavaBuilder SHALL execute the specified class using the Java runtime
2. WHEN a user invokes the run command without specifying a main class THEN the JavaBuilder SHALL use the main class from the project configuration
3. IF no main class is specified and no default exists in configuration THEN the JavaBuilder SHALL display an error message indicating no main class is available
4. WHEN running a Java program THEN the JavaBuilder SHALL include the output directory and all dependency JARs in the classpath
5. WHEN the Java program execution fails THEN the JavaBuilder SHALL display the runtime error message

### Requirement 3

**User Story:** As a developer, I want to compile and run Java programs in a single command, so that I can quickly iterate during development.

#### Acceptance Criteria

1. WHEN a user invokes the compile-and-run command THEN the JavaBuilder SHALL first compile all source files and then execute the main class
2. IF compilation fails during compile-and-run THEN the JavaBuilder SHALL skip the run phase and display the compilation error
3. WHEN compile-and-run succeeds THEN the JavaBuilder SHALL display both compilation success and program output

### Requirement 4

**User Story:** As a developer, I want to add Maven dependencies to my project, so that I can use third-party libraries in my Java code.

#### Acceptance Criteria

1. WHEN a user adds a dependency using the format `groupId:artifactId:version` THEN the JavaPackageManager SHALL record the dependency in qin.java.json
2. WHEN a dependency with the same groupId and artifactId already exists THEN the JavaPackageManager SHALL update the version instead of creating a duplicate
3. WHEN a dependency is added THEN the JavaPackageManager SHALL download the JAR file from Maven Central to the lib directory
4. IF the dependency format is invalid THEN the JavaPackageManager SHALL display an error message specifying the correct format
5. WHEN serializing the project configuration THEN the JavaPackageManager SHALL write valid JSON to qin.java.json
6. WHEN deserializing the project configuration THEN the JavaPackageManager SHALL parse the JSON from qin.java.json and restore the project state

### Requirement 5

**User Story:** As a developer, I want to install all project dependencies, so that I can restore a project's dependencies after cloning or updating.

#### Acceptance Criteria

1. WHEN a user invokes the install command THEN the JavaPackageManager SHALL download all dependencies listed in qin.java.json
2. WHEN a dependency JAR already exists in the lib directory THEN the JavaPackageManager SHALL skip downloading and display a cached status
3. WHEN downloading a dependency THEN the JavaPackageManager SHALL display download progress for each JAR
4. IF a dependency download fails THEN the JavaPackageManager SHALL display an error message with the failure reason and continue with remaining dependencies

### Requirement 6

**User Story:** As a developer, I want to list all project dependencies, so that I can review what libraries my project uses.

#### Acceptance Criteria

1. WHEN a user invokes the list command THEN the JavaPackageManager SHALL display the project name and version
2. WHEN dependencies exist THEN the JavaPackageManager SHALL display each dependency in the format `groupId:artifactId:version`
3. WHEN no dependencies exist THEN the JavaPackageManager SHALL display a "No dependencies" message

### Requirement 7

**User Story:** As a developer, I want a unified CLI interface, so that I can access all build and package management features through simple commands.

#### Acceptance Criteria

1. WHEN a user runs `bun run qin java compile` THEN the system SHALL compile all Java source files
2. WHEN a user runs `bun run qin java run <MainClass>` THEN the system SHALL run the specified Java class
3. WHEN a user runs `bun run qin java add <dependency>` THEN the system SHALL add the dependency to the project
4. WHEN a user runs `bun run qin java install` THEN the system SHALL install all project dependencies
5. WHEN a user runs `bun run qin java list` THEN the system SHALL list all project dependencies
6. WHEN a user runs an unknown command THEN the system SHALL display available commands and usage information

### Requirement 8

**User Story:** As a developer, I want the system to handle cross-platform path separators correctly, so that the build system works on Windows, macOS, and Linux.

#### Acceptance Criteria

1. WHEN constructing classpath strings on Windows THEN the system SHALL use semicolon (;) as the separator
2. WHEN constructing classpath strings on Unix-like systems THEN the system SHALL use colon (:) as the separator
3. WHEN resolving file paths THEN the system SHALL use platform-appropriate path separators

### Requirement 9

**User Story:** As a developer, I want to compile Java code to WebAssembly, so that I can call Java methods from JavaScript/TypeScript.

#### Acceptance Criteria

1. WHEN a user invokes the wasm-build command THEN the WasmBridge SHALL compile Java classes to a WASM module using TeaVM
2. WHEN compiling to WASM THEN the WasmBridge SHALL generate a .wasm file in the configured output directory
3. WHEN compiling to WASM THEN the WasmBridge SHALL generate a JavaScript glue file that provides the module loading interface
4. IF TeaVM compilation fails THEN the WasmBridge SHALL display the error message and return a failure status
5. WHEN a Java class has public methods THEN the WasmBridge SHALL automatically include all public methods in the WASM module's exported functions

### Requirement 10

**User Story:** As a developer, I want to call Java methods from JavaScript/TypeScript, so that I can reuse Java logic in my Bun/Node applications.

#### Acceptance Criteria

1. WHEN loading a WASM module THEN the WasmBridge SHALL return an object containing all exported Java methods
2. WHEN calling an exported Java method from JavaScript THEN the WasmBridge SHALL handle type conversion between JavaScript and Java types
3. WHEN a Java method returns a value THEN the WasmBridge SHALL convert the return value to the appropriate JavaScript type
4. WHEN a Java method throws an exception THEN the WasmBridge SHALL convert the exception to a JavaScript Error
5. WHEN passing primitive types (number, string, boolean) THEN the WasmBridge SHALL automatically convert them to Java equivalents

### Requirement 11

**User Story:** As a developer, I want all public Java methods to be automatically callable from JavaScript, so that cross-language usage feels like native Java usage without extra annotations.

#### Acceptance Criteria

1. WHEN a Java class has public methods THEN the WasmBridge SHALL automatically include all public methods in the WASM exports
2. WHEN a Java method is private or protected THEN the WasmBridge SHALL exclude it from WASM exports
3. WHEN generating TypeScript bindings THEN the WasmBridge SHALL create type definitions (.d.ts) that mirror the Java class structure
4. WHEN an exported method has unsupported parameter types THEN the WasmBridge SHALL display a warning during compilation
5. WHEN a Java class has public fields THEN the WasmBridge SHALL expose them as properties on the JavaScript object

### Requirement 12

**User Story:** As a developer, I want to import and call Java classes directly like native JavaScript modules, so that cross-language calls feel completely natural.

#### Acceptance Criteria

1. WHEN importing a Java class THEN the system SHALL allow direct import syntax like `import { hello } from "./hello.java"`
2. WHEN calling a Java static method THEN the system SHALL allow direct invocation like `const result = hello.main(["arg1"])`
3. WHEN instantiating a Java class THEN the system SHALL allow constructor syntax like `const obj = new MyClass()`
4. WHEN calling instance methods THEN the system SHALL allow dot notation like `obj.methodName(args)`
5. WHEN the Java module fails to load THEN the system SHALL throw a descriptive error with the failure reason

### Requirement 13

**User Story:** As a developer, I want Bun to automatically handle Java file imports, so that I can use Java classes without manual compilation steps.

#### Acceptance Criteria

1. WHEN a TypeScript file imports a .java file THEN the system SHALL automatically compile the Java file to WASM
2. WHEN the Java source has not changed since last compilation THEN the system SHALL use the cached WASM module
3. WHEN the Java source has changed THEN the system SHALL recompile before loading
4. WHEN multiple TypeScript files import the same Java class THEN the system SHALL share a single WASM instance
