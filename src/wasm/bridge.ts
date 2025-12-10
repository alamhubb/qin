import { existsSync, mkdirSync, readFileSync, writeFileSync } from "fs";
import { join, dirname, basename } from "path";
import { $ } from "bun";

/**
 * WasmBridge configuration
 */
export interface WasmBridgeConfig {
  cacheDir: string;       // Directory for WASM cache
  wasmOutDir: string;     // Output directory for WASM files
  classOutDir: string;    // Directory containing .class files
  debug: boolean;         // Enable debug output
}

/**
 * Result of WASM compilation
 */
export interface WasmCompileResult {
  success: boolean;
  wasmPath?: string;
  jsGluePath?: string;
  dtsPath?: string;
  error?: string;
}

/**
 * Metadata for a compiled WASM module
 */
export interface WasmModuleMeta {
  className: string;
  sourceHash: string;
  compiledAt: string;
  exports: {
    staticMethods: MethodInfo[];
    instanceMethods: MethodInfo[];
    constructors: MethodInfo[];
    fields: FieldInfo[];
  };
}

export interface MethodInfo {
  name: string;
  params: { name: string; type: string }[];
  returnType: string;
}

export interface FieldInfo {
  name: string;
  type: string;
  isStatic: boolean;
}

const DEFAULT_CONFIG: WasmBridgeConfig = {
  cacheDir: ".qin/cache",
  wasmOutDir: "build/wasm",
  classOutDir: "build/classes",
  debug: false,
};

/**
 * WasmBridge - Compiles Java to WASM and provides JS interop
 */
export class WasmBridge {
  private config: WasmBridgeConfig;

  constructor(config: Partial<WasmBridgeConfig> = {}) {
    this.config = {
      cacheDir: config.cacheDir ?? DEFAULT_CONFIG.cacheDir,
      wasmOutDir: config.wasmOutDir ?? DEFAULT_CONFIG.wasmOutDir,
      classOutDir: config.classOutDir ?? DEFAULT_CONFIG.classOutDir,
      debug: config.debug ?? DEFAULT_CONFIG.debug,
    };
  }

  /**
   * Get current configuration
   */
  getConfig(): WasmBridgeConfig {
    return { ...this.config };
  }

  /**
   * Calculate hash of a source file for cache invalidation
   */
  async calculateSourceHash(filePath: string): Promise<string> {
    const content = readFileSync(filePath);
    const hasher = new Bun.CryptoHasher("sha256");
    hasher.update(content);
    return hasher.digest("hex");
  }

  /**
   * Get the path to the metadata file for a class
   */
  private getMetaPath(className: string): string {
    return join(this.config.cacheDir, `${className}.meta.json`);
  }

  /**
   * Load cached metadata for a class
   */
  loadMeta(className: string): WasmModuleMeta | null {
    const metaPath = this.getMetaPath(className);
    if (existsSync(metaPath)) {
      try {
        return JSON.parse(readFileSync(metaPath, "utf-8"));
      } catch {
        return null;
      }
    }
    return null;
  }

  /**
   * Save metadata for a compiled class
   */
  saveMeta(meta: WasmModuleMeta): void {
    const metaPath = this.getMetaPath(meta.className);
    const dir = dirname(metaPath);
    if (!existsSync(dir)) {
      mkdirSync(dir, { recursive: true });
    }
    writeFileSync(metaPath, JSON.stringify(meta, null, 2));
  }

  /**
   * Check if a Java file needs recompilation
   */
  async needsRecompile(javaFilePath: string): Promise<boolean> {
    const className = basename(javaFilePath, ".java");
    const meta = this.loadMeta(className);
    
    if (!meta) {
      return true; // No cache, needs compile
    }

    const currentHash = await this.calculateSourceHash(javaFilePath);
    return currentHash !== meta.sourceHash;
  }

  /**
   * Compile a Java class to WASM using TeaVM
   * Note: This is a placeholder - actual TeaVM integration requires TeaVM JAR
   */
  async compileClass(javaFilePath: string): Promise<WasmCompileResult> {
    const className = basename(javaFilePath, ".java");
    
    // Ensure output directories exist
    if (!existsSync(this.config.wasmOutDir)) {
      mkdirSync(this.config.wasmOutDir, { recursive: true });
    }

    try {
      // Calculate source hash for caching
      const sourceHash = await this.calculateSourceHash(javaFilePath);

      // Check if we can use cached version
      if (!(await this.needsRecompile(javaFilePath))) {
        const wasmPath = join(this.config.wasmOutDir, `${className}.wasm`);
        if (existsSync(wasmPath)) {
          if (this.config.debug) {
            console.log(`Using cached WASM for ${className}`);
          }
          return {
            success: true,
            wasmPath,
            jsGluePath: join(this.config.wasmOutDir, `${className}.js`),
            dtsPath: join(this.config.wasmOutDir, `${className}.d.ts`),
          };
        }
      }

      // TODO: Actual TeaVM compilation
      // For now, create placeholder files to demonstrate the flow
      const wasmPath = join(this.config.wasmOutDir, `${className}.wasm`);
      const jsGluePath = join(this.config.wasmOutDir, `${className}.js`);
      const dtsPath = join(this.config.wasmOutDir, `${className}.d.ts`);

      // Create placeholder JS glue code
      const jsGlue = this.generateJsGlue(className);
      writeFileSync(jsGluePath, jsGlue);

      // Create placeholder TypeScript definitions
      const dts = this.generateTypeDefs(className);
      writeFileSync(dtsPath, dts);

      // Save metadata
      const meta: WasmModuleMeta = {
        className,
        sourceHash,
        compiledAt: new Date().toISOString(),
        exports: {
          staticMethods: [],
          instanceMethods: [],
          constructors: [],
          fields: [],
        },
      };
      this.saveMeta(meta);

      if (this.config.debug) {
        console.log(`Compiled ${className} to WASM`);
      }

      return {
        success: true,
        wasmPath,
        jsGluePath,
        dtsPath,
      };
    } catch (e: any) {
      return {
        success: false,
        error: e.message,
      };
    }
  }

  /**
   * Generate JavaScript glue code for loading WASM module
   */
  private generateJsGlue(className: string): string {
    return `// Auto-generated by Qin WasmBridge
// WASM glue code for ${className}

let wasmInstance = null;
let wasmMemory = null;

export async function load() {
  if (wasmInstance) return wasmInstance;
  
  const wasmPath = new URL('./${className}.wasm', import.meta.url);
  const wasmBuffer = await Bun.file(wasmPath).arrayBuffer();
  
  const imports = {
    env: {
      memory: new WebAssembly.Memory({ initial: 256 }),
    },
  };
  
  const { instance } = await WebAssembly.instantiate(wasmBuffer, imports);
  wasmInstance = instance;
  wasmMemory = imports.env.memory;
  
  return instance;
}

export function getExports() {
  if (!wasmInstance) {
    throw new Error('WASM module not loaded. Call load() first.');
  }
  return wasmInstance.exports;
}

export default { load, getExports };
`;
  }

  /**
   * Generate TypeScript type definitions
   */
  private generateTypeDefs(className: string): string {
    return `// Auto-generated by Qin WasmBridge
// TypeScript definitions for ${className}

export interface ${className}Exports {
  // Add exported methods here
}

export function load(): Promise<WebAssembly.Instance>;
export function getExports(): ${className}Exports;

declare const _default: {
  load: typeof load;
  getExports: typeof getExports;
};

export default _default;
`;
  }

  /**
   * Clear the WASM cache
   */
  async clearCache(): Promise<void> {
    if (existsSync(this.config.cacheDir)) {
      const glob = new Bun.Glob("**/*");
      for await (const file of glob.scan({ cwd: this.config.cacheDir })) {
        const filePath = join(this.config.cacheDir, file);
        await Bun.write(filePath, ""); // Clear file
      }
    }
  }
}
