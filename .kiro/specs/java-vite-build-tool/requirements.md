# Requirements Document

## Introduction

Qin 是一个基于 Bun + Coursier + JDK 技术栈的现代化 Java 构建工具，定位为 "Java 的 Vite"。核心目标是：
1. 零 XML 配置 - 使用 TypeScript 配置文件 (qin.config.ts) 替代繁琐的 pom.xml
2. 极速启动 - 利用 Bun 的高性能和 Coursier 的快速依赖解析
3. 简化开发体验 - 一键编译运行，脚本化 Java 开发
4. Fat Jar 打包 - 内置 Uber Jar 打包能力，替代 Maven Shade Plugin

系统允许开发者通过简洁的命令行接口编译、运行 Java 程序，管理 Maven 依赖，并生成可独立运行的 Fat Jar。

## Glossary

- **Qin**: 本 Java 构建工具的项目代号，取自"秦"，寓意统一和简化
- **qin.config.ts**: TypeScript 格式的项目配置文件，替代 Maven 的 pom.xml
- **Coursier (cs)**: 高性能的 Scala/Java 依赖解析器，用于从 Maven 仓库获取依赖
- **Fat Jar (Uber Jar)**: 包含所有依赖的单一可执行 JAR 文件
- **Classpath**: Java 运行时查找类文件和 JAR 文件的路径列表
- **Dependency Coordinate**: Maven 依赖的唯一标识，格式为 groupId:artifactId:version
- **Maven Central**: 默认的 Maven 依赖仓库
- **Entry Point**: Java 程序的入口文件，包含 main 方法的类
- **Manifest**: JAR 文件中的 META-INF/MANIFEST.MF，定义主类等元信息
- **Signature Files**: JAR 包中的签名文件 (*.SF, *.DSA, *.RSA)，打包 Fat Jar 时需要清理

## Requirements

### Requirement 1

**User Story:** As a Java developer, I want to initialize a new Qin project with a single command, so that I can quickly start developing without manual setup.

#### Acceptance Criteria

1. WHEN a user runs `qin init` THEN the system SHALL create a qin.config.ts file with default configuration
2. WHEN a user runs `qin init` THEN the system SHALL create a src directory with a template Main.java file
3. WHEN a user runs `qin init` in a directory that already has qin.config.ts THEN the system SHALL display a warning and skip file creation
4. WHEN generating the template Main.java THEN the system SHALL include a working "Hello World" example

### Requirement 2

**User Story:** As a Java developer, I want to define my project configuration in TypeScript, so that I can enjoy type safety and IDE support.

#### Acceptance Criteria

1. WHEN loading configuration THEN the system SHALL dynamically import qin.config.ts from the current working directory
2. WHEN qin.config.ts does not exist THEN the system SHALL display an error message suggesting to run `qin init`
3. WHEN qin.config.ts exports a QinConfig object THEN the system SHALL parse the entry point and dependencies
4. WHEN serializing the configuration THEN the system SHALL write valid TypeScript to qin.config.ts
5. WHEN deserializing the configuration THEN the system SHALL parse the TypeScript from qin.config.ts and restore the project state

### Requirement 3

**User Story:** As a Java developer, I want the system to check for required tools, so that I get clear guidance when dependencies are missing.

#### Acceptance Criteria

1. WHEN the system starts THEN the system SHALL verify that Coursier (cs) is installed and accessible
2. WHEN the system starts THEN the system SHALL verify that javac is installed and accessible
3. IF Coursier is not installed THEN the system SHALL display installation instructions for the user's platform
4. IF javac is not installed THEN the system SHALL display JDK installation instructions

### Requirement 4

**User Story:** As a Java developer, I want to resolve Maven dependencies using Coursier, so that I can use third-party libraries without manual JAR management.

#### Acceptance Criteria

1. WHEN dependencies are specified in qin.config.ts THEN the system SHALL call Coursier to resolve the complete dependency tree
2. WHEN Coursier resolves dependencies THEN the system SHALL return a classpath string containing all JAR paths
3. IF a dependency coordinate is invalid THEN the system SHALL display an error message with the problematic dependency
4. IF network timeout occurs during resolution THEN the system SHALL display a timeout error and suggest retry

### Requirement 5

**User Story:** As a Java developer, I want to run my Java program with a single command, so that I can quickly iterate during development.

#### Acceptance Criteria

1. WHEN a user runs `qin run` THEN the system SHALL parse the entry point from qin.config.ts
2. WHEN parsing the entry point THEN the system SHALL extract the source directory and class name from the file path
3. WHEN running the program THEN the system SHALL compile the source files with the resolved classpath
4. WHEN compilation succeeds THEN the system SHALL execute the main class with the resolved classpath
5. WHEN the Java program produces output THEN the system SHALL display the output in the terminal
6. IF compilation fails THEN the system SHALL display the compiler error message and skip execution

### Requirement 6

**User Story:** As a Java developer, I want to build a Fat Jar containing all dependencies, so that I can distribute my application as a single executable file.

#### Acceptance Criteria

1. WHEN a user runs `qin build` THEN the system SHALL create a .qin/temp directory for build artifacts
2. WHEN building THEN the system SHALL extract all dependency JARs into the temp directory
3. WHEN extracting JARs THEN the system SHALL remove signature files (*.SF, *.DSA, *.RSA) from META-INF to prevent SecurityException
4. WHEN building THEN the system SHALL compile user source code and copy class files to the temp directory
5. WHEN building THEN the system SHALL generate a MANIFEST.MF with the correct Main-Class entry
6. WHEN building THEN the system SHALL package all files into a single JAR in the dist directory
7. WHEN build completes THEN the system SHALL optionally clean up the temp directory based on debug mode setting

### Requirement 7

**User Story:** As a Java developer, I want clear visual feedback during build operations, so that I can understand what the tool is doing.

#### Acceptance Criteria

1. WHEN performing operations THEN the system SHALL display colored output to distinguish different phases
2. WHEN resolving dependencies THEN the system SHALL display progress information
3. WHEN compiling THEN the system SHALL display the compilation status
4. WHEN packaging THEN the system SHALL display the packaging progress
5. WHEN an operation completes THEN the system SHALL display a success or failure summary

### Requirement 8

**User Story:** As a Java developer, I want the system to handle cross-platform differences, so that the build works on Windows, macOS, and Linux.

#### Acceptance Criteria

1. WHEN constructing classpath strings on Windows THEN the system SHALL use semicolon (;) as the separator
2. WHEN constructing classpath strings on Unix-like systems THEN the system SHALL use colon (:) as the separator
3. WHEN resolving file paths THEN the system SHALL use platform-appropriate path separators

