import { existsSync, readFileSync, writeFileSync } from "fs";
import { join } from "path";

/**
 * Qin project configuration (qin.config.ts)
 * Most settings have sensible defaults and don't need to be configured
 */
export interface QinConfig {
  // Java version (default: "17")
  javaVersion?: string;
  // Main class for `qin run` (default: "Main")
  mainClass?: string;
}

/**
 * Internal configuration with all paths (not exposed to users)
 */
export interface InternalConfig {
  javaVersion: string;
  mainClass: string;
  srcDir: string;
  entryFile: string;
  outDir: string;
  wasmOutDir: string;
  cacheDir: string;
}

/**
 * Package.json structure managed by Qin
 */
export interface PackageJson {
  name: string;
  version: string;
  description?: string;
  main?: string;
  types?: string;
  scripts?: Record<string, string>;
  dependencies?: Record<string, string>;
  devDependencies?: Record<string, string>;
}

// Default paths (internal, not configurable by users)
const DEFAULT_PATHS = {
  srcDir: "src",
  entryFile: "src/main.java",
  outDir: ".qin/classes",
  wasmOutDir: ".qin/wasm",
  cacheDir: ".qin/cache",
};

// Default user-configurable options
const DEFAULT_OPTIONS = {
  javaVersion: "17",
  mainClass: "Main",
};

/**
 * Load qin.config.ts from project root
 */
export async function loadQinConfig(projectRoot: string = "."): Promise<QinConfig> {
  const configPath = join(projectRoot, "qin.config.ts");
  
  if (existsSync(configPath)) {
    try {
      const module = await import(configPath);
      return module.default || module;
    } catch (e) {
      console.warn(`Warning: Could not load ${configPath}, using defaults`);
    }
  }
  
  return {};
}

/**
 * Get full internal configuration (merges user config with defaults)
 */
export function getInternalConfig(userConfig: QinConfig = {}): InternalConfig {
  return {
    javaVersion: userConfig.javaVersion ?? DEFAULT_OPTIONS.javaVersion,
    mainClass: userConfig.mainClass ?? DEFAULT_OPTIONS.mainClass,
    ...DEFAULT_PATHS,
  };
}

/**
 * Merge user config with defaults (legacy, for compatibility)
 */
export function mergeConfig(userConfig: QinConfig): InternalConfig {
  return getInternalConfig(userConfig);
}

/**
 * Load package.json from project root
 */
export function loadPackageJson(projectRoot: string = "."): PackageJson | null {
  const pkgPath = join(projectRoot, "package.json");
  
  if (existsSync(pkgPath)) {
    try {
      return JSON.parse(readFileSync(pkgPath, "utf-8"));
    } catch (e) {
      console.warn(`Warning: Could not parse ${pkgPath}`);
    }
  }
  
  return null;
}

/**
 * Save package.json to project root
 */
export function savePackageJson(pkg: PackageJson, projectRoot: string = "."): void {
  const pkgPath = join(projectRoot, "package.json");
  writeFileSync(pkgPath, JSON.stringify(pkg, null, 2));
}

/**
 * Initialize a new Qin project
 */
export function initProject(projectRoot: string, name: string): { config: QinConfig; pkg: PackageJson } {
  // Minimal config - most things use defaults
  const config: QinConfig = {
    mainClass: "Main",
  };
  
  const pkg: PackageJson = {
    name,
    version: "1.0.0",
    description: `A Qin managed project`,
    scripts: {
      build: "qin java compile",
      start: "qin java run",
      dev: "qin src/Main.java",
    },
    dependencies: {},
    devDependencies: {},
  };
  
  return { config, pkg };
}
