// Qin - Cross-language Build System
// Main exports for use as a library

// Core types and config
export { defineConfig } from "./types";
export type { QinConfig, QinPlugin, PluginContext, ClientConfig, Repository } from "./types";

// Plugin system
export {
  PluginManager,
  definePlugin,
  type LanguageSupport,
  type CompileContext,
  type RunContext,
  type TestContext,
  type BuildContext,
  type CompileResult,
  type BuildResult,
  type TestResult,
} from "./core/plugin-system";

// Plugin detector (auto-detection)
export {
  PluginDetector,
  autoConfigurePlugins,
  describeDetection,
  type DetectionResult,
} from "./core/plugin-detector";

// Java utilities
export { JavaBuilder, type JavaBuildConfig } from "./java/builder";
export { QinPackageManager, parseDependency, type QinDependency, type DependencyValidationResult } from "./java/package-manager";
export { loadQinConfig, mergeConfig, loadPackageJson, savePackageJson, type PackageJson } from "./java/config";
export { 
  ClassFileParser, 
  getPublicMethods, 
  getPublicFields,
  parseTypeDescriptor,
  parseMethodDescriptor,
  type ParsedClass,
  type ParsedMethod,
  type ParsedField
} from "./java/classfile";
export { 
  getClasspathSeparator, 
  buildClasspath, 
  buildFullClasspath, 
  parseClasspath, 
  isWindows 
} from "./java/classpath";
export { 
  WasmBridge, 
  type WasmBridgeConfig, 
  type WasmCompileResult, 
  type WasmModuleMeta,
  type MethodInfo,
  type FieldInfo
} from "./wasm/bridge";
export { loadWasmModule, getWasmExports, clearModuleCache, isModuleCached } from "./wasm/loader";
export { 
  toI32, fromI32, toF64, fromF64, toWasmBool, fromWasmBool,
  WasmStringHelper, javaTypeToTs, JAVA_TO_TS_TYPE_MAP
} from "./wasm/types";
export { createClassProxy, type JavaClassProxy, type JavaInstanceProxy } from "./wasm/proxy";
export { javaPlugin, generateDtsFile, type JavaPluginConfig } from "./plugin";
