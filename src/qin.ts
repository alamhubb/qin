/**
 * Qin - Java-Vite Build Tool
 * A modern Java build tool with zero XML configuration
 * 
 * Main exports for use as a library
 */

// Types
export type {
  QinConfig,
  ParsedEntry,
  ValidationResult,
  EnvironmentStatus,
  CompileResult,
  BuildResult,
  ResolveResult,
  FrontendConfig,
} from "./types";

// Core modules
export { ConfigLoader, parseEntryPath } from "./core/config-loader";
export { EnvironmentChecker } from "./core/environment";
export { 
  DependencyResolver, 
  getClasspathSeparator, 
  isWindows, 
  buildClasspath, 
  parseClasspath 
} from "./core/dependency-resolver";
export { JavaRunner } from "./core/java-runner";
export { 
  FatJarBuilder, 
  generateManifestContent, 
  parseManifestMainClass,
  type FatJarBuilderOptions 
} from "./core/fat-jar-builder";

// Commands
export { initProject } from "./commands/init";

// Plugins
export { 
  FrontendPlugin, 
  createFrontendPlugin,
  type FrontendPluginOptions 
} from "./plugins/frontend-plugin";
