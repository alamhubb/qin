// Qin - Cross-language Build System
// Main exports for use as a library

export { JavaBuilder, type JavaBuildConfig } from "./java/builder";
export { QinPackageManager, parseDependency, type QinDependency, type DependencyValidationResult } from "./java/package-manager";
export { loadQinConfig, mergeConfig, loadPackageJson, savePackageJson, type QinConfig, type PackageJson } from "./java/config";
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
