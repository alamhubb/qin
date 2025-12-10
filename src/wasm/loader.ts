import { existsSync, readFileSync } from "fs";

/**
 * WASM module instance cache
 */
const moduleCache = new Map<string, WebAssembly.Instance>();

/**
 * Load a WASM module from file
 */
export async function loadWasmModule(wasmPath: string): Promise<WebAssembly.Instance> {
  // Check cache first
  if (moduleCache.has(wasmPath)) {
    return moduleCache.get(wasmPath)!;
  }

  if (!existsSync(wasmPath)) {
    throw new Error(`WASM file not found: ${wasmPath}`);
  }

  const wasmBuffer = readFileSync(wasmPath);
  
  const imports = {
    env: {
      memory: new WebAssembly.Memory({ initial: 256, maximum: 512 }),
      // Standard imports for TeaVM generated WASM
      abort: () => { throw new Error("WASM abort called"); },
    },
  };

  const { instance } = await WebAssembly.instantiate(wasmBuffer, imports);
  
  // Cache the instance
  moduleCache.set(wasmPath, instance);
  
  return instance;
}

/**
 * Get exports from a loaded WASM module
 */
export function getWasmExports(instance: WebAssembly.Instance): WebAssembly.Exports {
  return instance.exports;
}

/**
 * Clear the module cache
 */
export function clearModuleCache(): void {
  moduleCache.clear();
}

/**
 * Check if a module is cached
 */
export function isModuleCached(wasmPath: string): boolean {
  return moduleCache.has(wasmPath);
}

/**
 * Get all cached module paths
 */
export function getCachedModules(): string[] {
  return Array.from(moduleCache.keys());
}
