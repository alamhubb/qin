# Implementation Plan

> **注意**: 本实现计划针对**方案 2: GraalVM Node.js Runtime**。
> 
> **方案 1: Polyglot API** 已在 `.kiro/specs/graalvm-polyglot-js-runner/` 中完成实现。

## 实现状态

- **方案 1 (Polyglot API)**: ✅ 已完成 - `qin run xxx.js`
- **方案 2 (GraalVM Node.js)**: 🚧 待实现 - `qin gral xxx.js`

---

- [x] 1. Set up plugin packages and core interfaces


  - [x] 1.1 Create `packages/qin-plugin-graalvm` package structure


    - Create package.json with dependencies
    - Create tsconfig.json for TypeScript compilation
    - Create src/index.ts with type exports
    - _Requirements: 4.1, 4.2_
  - [x] 1.2 Create `packages/qin-plugin-graalvm-js` package structure


    - Create package.json with dependency on qin-plugin-graalvm
    - Create tsconfig.json for TypeScript compilation
    - Create src/index.ts with type exports
    - _Requirements: 5.1, 5.5_
  - [ ]* 1.3 Write property test for GraalVM detection
    - **Property 1: GraalVM Detection Consistency**
    - **Validates: Requirements 1.1, 1.2, 4.2**

- [x] 2. Implement GraalVM environment detection


  - [x] 2.1 Implement `detectGraalVM()` function

    - Check GRAALVM_HOME environment variable
    - Check for `gu` command in PATH
    - Parse GraalVM version from `gu --version` output
    - Return GraalVMDetectionResult with detection method
    - _Requirements: 1.1, 1.2_
  - [x] 2.2 Implement `getInstalledComponents()` function

    - Run `gu list` command to get installed components
    - Parse output to extract component names
    - Return array of component names
    - _Requirements: 4.4_
  - [x] 2.3 Implement `isComponentInstalled()` function

    - Check if component name exists in installed components list
    - Return boolean result
    - _Requirements: 4.5_
  - [ ]* 2.4 Write property test for plugin API consistency
    - **Property 3: Plugin API Consistency**
    - **Validates: Requirements 4.3, 4.4, 4.5**

- [x] 3. Implement qin-plugin-graalvm base plugin


  - [x] 3.1 Implement `graalvm()` plugin factory function

    - Accept GraalVMPluginOptions
    - Initialize detection on plugin load
    - Expose getInfo(), getGraalVMHome(), getInstalledComponents(), isComponentInstalled() methods
    - _Requirements: 4.1, 4.2, 4.3_
  - [x] 3.2 Implement error handling and installation guidance

    - Create GraalVMNotFoundError with installation instructions
    - Create ComponentNotInstalledError with `gu install` command
    - Include links to official documentation
    - _Requirements: 1.3, 7.2, 7.3_
  - [ ]* 3.3 Write unit tests for graalvm plugin
    - Test detection with mocked environment
    - Test component listing
    - Test error messages
    - _Requirements: 1.1, 1.2, 4.2_

- [x] 4. Checkpoint - Ensure all tests pass

  - Ensure all tests pass, ask the user if questions arise.

- [x] 5. Implement configuration parsing for GraalVM


  - [x] 5.1 Extend QinConfig type with graalvm configuration

    - Add graalvm field to QinConfig interface in src/types.ts
    - Support both boolean and object configuration for js
    - _Requirements: 2.1_
  - [x] 5.2 Implement configuration validation

    - Validate graalvm.home path if provided
    - Validate graalvm.js.entry file exists
    - Return specific validation errors
    - _Requirements: 2.4_
  - [ ]* 5.3 Write property test for configuration parsing
    - **Property 2: Configuration Parsing Validity**
    - **Validates: Requirements 2.1, 2.2, 2.3, 2.4**

- [x] 6. Implement qin-plugin-graalvm-js language support


  - [x] 6.1 Implement GraalVMJsLanguageSupport class

    - Set name to "graalvm-js"
    - Set extensions to [".js", ".mjs"]
    - Implement compile() method for syntax validation
    - _Requirements: 5.2, 5.3_
  - [x] 6.2 Implement JavaScript execution with GraalVM Node.js

    - Build command using GraalVM's node binary path
    - Include entry point and user arguments
    - Set up Java interop classpath when enabled
    - _Requirements: 3.1, 3.4, 5.4_
  - [ ]* 6.3 Write property test for file extension handling
    - **Property 5: File Extension Handling**
    - **Validates: Requirements 5.2, 5.3**
  - [ ]* 6.4 Write property test for execution command construction
    - **Property 4: JavaScript Execution Command Construction**
    - **Validates: Requirements 3.1, 3.4, 5.4**

- [x] 7. Implement graalvmJs() plugin factory


  - [x] 7.1 Implement `graalvmJs()` plugin factory function

    - Accept GraalVMJsPluginOptions
    - Auto-register graalvm base plugin as dependency
    - Register GraalVMJsLanguageSupport
    - _Requirements: 5.1, 5.5_
  - [x] 7.2 Implement npm dependency resolution

    - Detect node_modules directory
    - Include in module resolution path
    - _Requirements: 3.2_
  - [x] 7.3 Implement error formatting for JavaScript errors

    - Parse error output for file location
    - Format stack trace for readability
    - _Requirements: 3.3_
  - [ ]* 7.4 Write unit tests for graalvmJs plugin
    - Test plugin registration
    - Test language support
    - Test error formatting
    - _Requirements: 5.1, 5.2_

- [x] 8. Checkpoint - Ensure all tests pass

  - Ensure all tests pass, ask the user if questions arise.

- [x] 9. Implement hot reload for JavaScript


  - [x] 9.1 Implement file watcher for JavaScript files

    - Watch for changes in .js and .mjs files
    - Use debounce to prevent rapid restarts
    - _Requirements: 6.1_
  - [x] 9.2 Implement process restart with state preservation

    - Save environment variables before restart
    - Save command line arguments before restart
    - Restore state after restart
    - _Requirements: 6.3_
  - [x] 9.3 Implement --no-hot flag handling

    - Check for flag in CLI options
    - Skip watcher setup when flag is present
    - _Requirements: 6.4_
  - [ ]* 9.4 Write property test for hot reload state preservation
    - **Property 6: Hot Reload State Preservation**
    - **Validates: Requirements 6.3**

- [x] 10. Implement qin env command for GraalVM status


  - [x] 10.1 Add GraalVM status to `qin env` output

    - Display GraalVM installation path
    - Display GraalVM version
    - Display installed components list
    - _Requirements: 7.1_
  - [x] 10.2 Implement status formatting with checkmarks

    - Show green checkmark for installed components
    - Show red X for missing components
    - Show installation commands for missing components
    - _Requirements: 7.4_
  - [ ]* 10.3 Write unit tests for env command output
    - Test output format with GraalVM installed
    - Test output format without GraalVM
    - _Requirements: 7.1, 7.2_

- [x] 11. Create test project


  - [x] 11.1 Create `examples/apps/graalvm-js-test` directory structure

    - Create qin.config.ts with graalvmJs plugin
    - Create src/server/index.js with basic JavaScript code
    - _Requirements: 8.1, 8.2_
  - [x] 11.2 Implement test JavaScript file

    - Add console.log output for verification
    - Add basic function to demonstrate execution
    - _Requirements: 8.2, 8.3_
  - [ ]* 11.3 Write integration test for test project
    - Verify qin run executes successfully
    - Verify output matches expected
    - _Requirements: 8.3_

- [x] 12. Final Checkpoint - Ensure all tests pass


  - Ensure all tests pass, ask the user if questions arise.
