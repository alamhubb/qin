# Implementation Plan

- [x] 1. Implement Command Parser
  - [x] 1.1 Create `src/core/js-command-parser.ts` with core functions
    - Implement `isJavaScriptFile()` to check .js and .mjs extensions
    - Implement `resolveFilePath()` for relative to absolute path conversion
    - Implement `parseJsCommand()` to parse CLI arguments
    - _Requirements: 1.1, 1.2, 1.3_
  - [x] 1.2 Write property test for JavaScript file recognition
    - **Property 1: JavaScript File Recognition**
    - **Validates: Requirements 1.1, 1.2**
  - [x] 1.3 Write property test for path resolution
    - **Property 2: Path Resolution Consistency**
    - **Validates: Requirements 1.3**

- [x] 2. Implement JsRunner Java Bridge
  - [x] 2.1 Create `src/java/JsRunner.java` with Polyglot execution
    - Implement `main()` entry point
    - Implement `createContext()` with JS language and ES modules enabled
    - Implement `createSource()` to load JavaScript files
    - Implement `setupArgs()` to set global args array
    - Implement `handleError()` for error formatting
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 5.4, 6.4_
  - [x] 2.2 Write property test for ES module support
    - **Property 7: ES Module Support**
    - **Validates: Requirements 5.1, 5.2, 5.3**
  - [x] 2.3 Write property test for Java interop
    - **Property 8: Java Interop Functionality**
    - **Validates: Requirements 6.1, 6.2, 6.3**

- [x] 3. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [x] 4. Implement JavaScript Executor
  - [x] 4.1 Create `src/core/js-executor.ts` with execution logic
    - Implement `getJsRunnerClasspath()` to locate JsRunner and dependencies
    - Implement `buildJavaCommand()` to construct Java execution command
    - Implement `executeJavaScript()` to spawn Java process and capture output
    - _Requirements: 2.1, 2.2, 3.1, 7.2, 7.3_
  - [x] 4.2 Write property test for Java command construction
    - **Property 4: Java Command Construction**
    - **Validates: Requirements 2.1, 2.2, 2.4, 2.5**
  - [x] 4.3 Write property test for argument forwarding
    - **Property 5: Argument Forwarding**
    - **Validates: Requirements 3.1, 3.2**
  - [x] 4.4 Write property test for classpath construction
    - **Property 9: Classpath Construction**
    - **Validates: Requirements 7.2, 7.3**

- [x] 5. Implement Error Formatter
  - [x] 5.1 Create `src/core/js-error-formatter.ts` with error handling
    - Implement `parseJsError()` to extract error info from stderr
    - Implement `formatJsError()` to produce user-friendly output
    - Handle syntax errors, runtime errors, file errors, and GraalVM errors
    - _Requirements: 4.1, 4.2, 4.3, 4.4_
  - [x] 5.2 Write property test for error path inclusion
    - **Property 3: Error Message Path Inclusion**
    - **Validates: Requirements 1.4, 4.4**
  - [x] 5.3 Write property test for error location formatting
    - **Property 6: Error Location Formatting**
    - **Validates: Requirements 4.1, 4.2**

- [x] 6. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [x] 7. Integrate with Qin CLI
  - [x] 7.1 Update `src/cli.ts` to handle JavaScript execution commands
    - Add detection for `qin run xxx.js` command
    - Add detection for `qin xxx.js` shorthand command
    - Route JavaScript files to JS executor
    - _Requirements: 1.1, 1.2_
  - [x] 7.2 Add argument parsing for `--` separator
    - Parse arguments after `--` as script arguments
    - Pass script arguments to executor
    - _Requirements: 3.1_
  - [x] 7.3 Write unit tests for CLI integration
    - Test command routing for .js files
    - Test argument parsing with `--` separator
    - _Requirements: 1.1, 1.2, 3.1_

- [x] 8. Create Test Example Project
  - [x] 8.1 Create `examples/apps/polyglot-js-test` directory
    - Create basic JavaScript file demonstrating execution
    - Create JavaScript file demonstrating Java interop
    - _Requirements: 2.5, 6.1_
  - [x] 8.2 Write integration test for example project
    - Test `qin run` executes JavaScript correctly
    - Test console output is captured
    - _Requirements: 2.5_

- [x] 9. Final Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.
