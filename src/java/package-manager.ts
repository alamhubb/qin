import { existsSync, mkdirSync, readFileSync, writeFileSync } from "fs";
import { join } from "path";
import { loadQinConfig, getInternalConfig, loadPackageJson, savePackageJson, type QinConfig, type PackageJson, type InternalConfig } from "./config";

/**
 * Qin package dependency
 */
export interface QinDependency {
  name: string;
  version: string;
}

/**
 * Validation result for dependency format
 */
export interface DependencyValidationResult {
  valid: boolean;
  error?: string;
  dependency?: QinDependency;
}

/**
 * Parse dependency string in npm style: name@version or just name
 */
export function parseDependency(dep: string): DependencyValidationResult {
  if (!dep || typeof dep !== "string") {
    return { valid: false, error: "Dependency string is required" };
  }

  const trimmed = dep.trim();
  if (trimmed.length === 0) {
    return { valid: false, error: "Dependency string cannot be empty" };
  }

  // Support both "name@version" and "name" (latest)
  // Handle scoped packages like @types/node@18.0.0
  const atIndex = trimmed.lastIndexOf("@");
  
  let name: string;
  let version: string;

  if (atIndex > 0) {
    name = trimmed.substring(0, atIndex).trim();
    version = trimmed.substring(atIndex + 1).trim();
  } else {
    name = trimmed;
    version = "latest";
  }

  if (name.length === 0) {
    return { valid: false, error: "Package name cannot be empty" };
  }

  if (version.length === 0) {
    return { valid: false, error: "Version cannot be empty" };
  }

  return {
    valid: true,
    dependency: { name, version },
  };
}

/**
 * Qin Package Manager - manages package.json dependencies
 */
export class QinPackageManager {
  private projectRoot: string;
  private libDir: string;
  private config: InternalConfig;
  private pkg: PackageJson;

  constructor(projectRoot: string = ".") {
    this.projectRoot = projectRoot;
    this.libDir = join(projectRoot, "lib");
    this.config = getInternalConfig();
    this.pkg = this.loadOrCreatePackageJson();
  }

  async init(): Promise<void> {
    const userConfig = await loadQinConfig(this.projectRoot);
    this.config = getInternalConfig(userConfig);
  }

  private loadOrCreatePackageJson(): PackageJson {
    const existing = loadPackageJson(this.projectRoot);
    if (existing) {
      return existing;
    }
    
    // Create default package.json
    return {
      name: "unnamed",
      version: "1.0.0",
      dependencies: {},
      devDependencies: {},
    };
  }

  getConfig() {
    return this.config;
  }

  getPackageJson(): PackageJson {
    return this.pkg;
  }

  savePackageJson(): void {
    savePackageJson(this.pkg, this.projectRoot);
  }

  async add(dep: string, isDev: boolean = false): Promise<boolean> {
    const validation = parseDependency(dep);
    if (!validation.valid || !validation.dependency) {
      console.error(validation.error ?? "Invalid dependency format. Use: name@version");
      return false;
    }

    const { name, version } = validation.dependency;
    const depsKey = isDev ? "devDependencies" : "dependencies";
    
    if (!this.pkg[depsKey]) {
      this.pkg[depsKey] = {};
    }

    const existing = this.pkg[depsKey]![name];
    if (existing) {
      console.log(`✓ Updated ${name} from ${existing} to ${version}`);
    } else {
      console.log(`✓ Added ${name}@${version}`);
    }

    this.pkg[depsKey]![name] = version;
    this.savePackageJson();
    
    return true;
  }

  async install(): Promise<boolean> {
    const allDeps = {
      ...this.pkg.dependencies,
      ...this.pkg.devDependencies,
    };
    
    const deps = Object.entries(allDeps);
    if (deps.length === 0) {
      console.log("No dependencies to install");
      return true;
    }

    console.log(`Installing ${deps.length} dependencies...`);
    
    // For now, just log - actual download will be implemented with registry
    for (const [name, version] of deps) {
      console.log(`  ✓ ${name}@${version}`);
    }
    
    return true;
  }

  getClasspath(): string[] {
    if (!existsSync(this.libDir)) return [];
    const glob = new Bun.Glob("*.jar");
    const jars: string[] = [];
    for (const file of glob.scanSync({ cwd: this.libDir })) {
      jars.push(join(this.libDir, file));
    }
    return jars;
  }

  list(): void {
    console.log(`\n${this.pkg.name}@${this.pkg.version}\n`);
    
    const deps = Object.entries(this.pkg.dependencies || {});
    const devDeps = Object.entries(this.pkg.devDependencies || {});
    
    if (deps.length === 0 && devDeps.length === 0) {
      console.log("No dependencies");
      return;
    }
    
    if (deps.length > 0) {
      console.log("Dependencies:");
      for (const [name, version] of deps) {
        console.log(`  ${name}@${version}`);
      }
    }
    
    if (devDeps.length > 0) {
      console.log("\nDev Dependencies:");
      for (const [name, version] of devDeps) {
        console.log(`  ${name}@${version}`);
      }
    }
  }
}

// Re-export config types
export type { QinConfig, PackageJson, InternalConfig } from "./config";
export { loadQinConfig, getInternalConfig, loadPackageJson, savePackageJson } from "./config";
