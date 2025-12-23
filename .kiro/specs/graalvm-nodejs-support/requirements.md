# Requirements Document

## Introduction

本功能为 Qin 构建工具添加 GraalVM 多语言运行时支持。通过两个插件实现：
1. `qin-plugin-graalvm` - GraalVM 基础插件，提供环境检测、版本管理和核心 Polyglot 能力
2. `qin-plugin-graalvm-js` - GraalVM JavaScript 插件，提供 Node.js 运行时支持

同时创建一个测试项目 `examples/apps/graalvm-js-test` 来验证插件功能。

## Glossary

- **Qin**: 一个现代化的 Java 构建工具，支持零 XML 配置
- **GraalVM**: Oracle 开发的高性能多语言虚拟机，支持 Java、JavaScript、Python 等多种语言
- **GraalVM Node.js Runtime**: GraalVM 提供的 Node.js 兼容运行时，通过 `gu install nodejs` 安装
- **Polyglot API**: GraalVM 提供的多语言互操作 API，允许不同语言代码在同一进程中相互调用
- **qin-plugin-graalvm**: GraalVM 基础插件，提供环境检测和核心功能
- **qin-plugin-graalvm-js**: GraalVM JavaScript 语言支持插件
- **Truffle**: GraalVM 的语言实现框架，提供高性能的语言互操作
- **gu**: GraalVM Updater，用于安装和管理 GraalVM 组件的命令行工具

## Requirements

### Requirement 1

**User Story:** As a developer, I want Qin to detect and use GraalVM Node.js runtime, so that I can run JavaScript code with GraalVM's performance benefits.

#### Acceptance Criteria

1. WHEN Qin starts THEN the system SHALL detect whether GraalVM is installed by checking for the `gu` command or `GRAALVM_HOME` environment variable
2. WHEN GraalVM is detected THEN the system SHALL verify that the Node.js component is installed by checking for `node` command in GraalVM's bin directory
3. WHEN GraalVM Node.js is not installed THEN the system SHALL provide clear instructions for installing it via `gu install nodejs`
4. WHEN the user runs `qin env` THEN the system SHALL display GraalVM detection status including version and installed components

### Requirement 2

**User Story:** As a developer, I want to configure GraalVM JavaScript support in my qin.config.ts, so that I can enable polyglot features for my project.

#### Acceptance Criteria

1. WHEN a user adds `graalvm: { js: true }` to qin.config.ts THEN the system SHALL enable GraalVM JavaScript support for the project
2. WHEN GraalVM JavaScript is enabled THEN the system SHALL use GraalVM's Node.js runtime instead of system Node.js for JavaScript execution
3. WHEN the configuration specifies `graalvm: { js: { entry: "src/server/index.js" } }` THEN the system SHALL use the specified file as the JavaScript entry point
4. WHEN the configuration is invalid THEN the system SHALL report specific validation errors with guidance on correct configuration format

### Requirement 3

**User Story:** As a developer, I want to run JavaScript files using GraalVM Node.js through Qin, so that I can leverage GraalVM's polyglot capabilities.

#### Acceptance Criteria

1. WHEN a user runs `qin run` with GraalVM JS enabled THEN the system SHALL execute the JavaScript entry point using GraalVM's Node.js runtime
2. WHEN the JavaScript file imports npm packages THEN the system SHALL resolve dependencies from node_modules directory
3. WHEN the JavaScript execution fails THEN the system SHALL display formatted error messages with file location and stack trace
4. WHEN the user passes arguments via `qin run -- arg1 arg2` THEN the system SHALL forward those arguments to the JavaScript program

### Requirement 4

**User Story:** As a developer, I want a qin-plugin-graalvm base plugin, so that I can detect and manage GraalVM environment for my project.

#### Acceptance Criteria

1. WHEN a user imports and uses `graalvm()` plugin THEN the system SHALL register GraalVM environment detection capabilities
2. WHEN the plugin initializes THEN the system SHALL detect GraalVM installation path, version, and available components
3. WHEN the plugin provides `getGraalVMHome()` method THEN the system SHALL return the GraalVM installation directory path
4. WHEN the plugin provides `getInstalledComponents()` method THEN the system SHALL return a list of installed GraalVM components including nodejs, python, ruby
5. WHEN the plugin provides `isComponentInstalled(name)` method THEN the system SHALL return true only when the specified component exists

### Requirement 5

**User Story:** As a developer, I want a qin-plugin-graalvm-js plugin, so that I can add GraalVM JavaScript support to my project in a modular way.

#### Acceptance Criteria

1. WHEN a user imports and uses `graalvmJs()` plugin THEN the system SHALL register GraalVM JavaScript as a supported language
2. WHEN the plugin is registered THEN the system SHALL handle `.js` and `.mjs` file extensions for compilation and execution
3. WHEN the plugin's `compile` method is called THEN the system SHALL validate JavaScript syntax using GraalVM's parser
4. WHEN the plugin's `run` method is called THEN the system SHALL execute JavaScript using GraalVM Node.js with proper classpath for Java interop
5. WHEN the graalvm base plugin is not registered THEN the graalvmJs plugin SHALL automatically register it as a dependency

### Requirement 6

**User Story:** As a developer, I want hot reload support for JavaScript files in development mode, so that I can see changes immediately without manual restarts.

#### Acceptance Criteria

1. WHEN `qin dev` is running with GraalVM JS enabled THEN the system SHALL watch for changes in JavaScript source files
2. WHEN a JavaScript file changes THEN the system SHALL restart the GraalVM Node.js process within 2 seconds
3. WHEN hot reload triggers THEN the system SHALL preserve environment variables and command line arguments
4. WHEN the user disables hot reload via `--no-hot` flag THEN the system SHALL run without file watching

### Requirement 7

**User Story:** As a developer, I want to see clear status information about GraalVM setup, so that I can troubleshoot configuration issues.

#### Acceptance Criteria

1. WHEN a user runs `qin env` THEN the system SHALL display GraalVM installation path, version, and installed language components
2. WHEN GraalVM is not found THEN the system SHALL provide installation instructions with links to official documentation
3. WHEN GraalVM is found but Node.js component is missing THEN the system SHALL display the exact command to install it: `gu install nodejs`
4. WHEN all components are properly configured THEN the system SHALL display a success status with green checkmarks

### Requirement 8

**User Story:** As a developer, I want a test project to verify GraalVM JavaScript plugin functionality, so that I can validate the implementation works correctly.

#### Acceptance Criteria

1. WHEN the test project is created at `examples/apps/graalvm-js-test` THEN the project SHALL contain a valid qin.config.ts with graalvmJs plugin configured
2. WHEN the test project contains `src/server/index.js` THEN the file SHALL demonstrate basic JavaScript execution with console output
3. WHEN running `qin run` in the test project THEN the system SHALL execute the JavaScript file using GraalVM Node.js and display output
4. WHEN the test project is used for development THEN the project SHALL serve as documentation example for other developers

